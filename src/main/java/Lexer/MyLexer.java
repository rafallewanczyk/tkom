package Lexer;


import Lexer.RomanNumerals.RomanLexer;
import Lexer.RomanNumerals.RomanNumeralsLookup;
import Lexer.Scanner.MyScanner;
import Lexer.Token.MyToken;
import Lexer.Token.MyTokenPrefix;
import Lexer.Token.MyTokenType;

import java.awt.image.LookupOp;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class MyLexer {

    private MyScanner scanner;
    private char character;
    private char character_buffer = '\0';
    private MyTokenType type;
    private StringBuilder token;
    private int x = 0, y = 0;
    private RomanLexer romanLexer = new RomanLexer();
    private boolean block = false;

    public MyLexer(String path) throws FileNotFoundException {
        scanner = new MyScanner(path);
    }

    public MyToken nextToken() {

        //Found EOF, don't read any more tokens
        if(block){
            return new MyToken(MyTokenType.EOF, "EOF", x, y);
        }


        if (character_buffer != '\0') {
            character = character_buffer;
            character_buffer = '\0';
        } else {
            character = scanner.getNextSymbol();
        }

        //EOF
        if (MyTokenPrefix.isEOF(character)) {
            block = true;
            return new MyToken(MyTokenType.EOF, "EOF", x, y);
        }

        //Single character tokens
        if (character == ' ') {
            return nextToken();
        } else if (character == '\n' || character == '\r') {
            y++;
            return nextToken();
        } else if (character == ',') {
            return new MyToken(MyTokenType.COMMA, ",", x++, y);
        } else if (character == ';') {
            return new MyToken(MyTokenType.SEMICOLLON, ";", x++, y);
        } else if (character == '(') {
            return new MyToken(MyTokenType.LEFT_PARENTESIS, "(", x++, y);
        } else if (character == ')') {
            return new MyToken(MyTokenType.RIGHT_PARENTESIS, ")", x++, y);
        } else if (character == '{') {
            return new MyToken(MyTokenType.LEFT_BRACE, "{", x++, y);
        } else if (character == '}') {
            return new MyToken(MyTokenType.RIGHT_BRACE, "}", x++, y);
        } else if (character == '!') {
            return new MyToken(MyTokenType.UNARY_OP, "!", x++, y);
        } else if (character == '+' || character == '-') {
            return new MyToken(MyTokenType.ADDITIVE_OP, Character.toString(character), x++, y);
        } else if (character == '*' || character == '%') {
            return new MyToken(MyTokenType.MULTIPLICATIVE_OP, Character.toString(character), x++, y);
        } else if (character == '\"') {
            return new MyToken(MyTokenType.QUOTE, "\"", x++, y);
        } else if (character == '*') {
            return new MyToken(MyTokenType.MULTIPLICATIVE_OP, "*", x++, y);
        }

        //Double or single character token
        else if (character == '=') {
            character_buffer = scanner.getNextSymbol();
            if (character_buffer == '=') {
                character_buffer = '\0';
                return new MyToken(MyTokenType.EQUAL_OP, "==", x++, y);
            }
            return new MyToken(MyTokenType.ASSIGNMENT_OP, "=", x++, y);

        } else if (character == '/') {
            character_buffer = scanner.getNextSymbol();
            if (character_buffer == '/') {
                character_buffer = '\0';
                return new MyToken(MyTokenType.COMMENT, "//", x++, y);
            }
            return new MyToken(MyTokenType.MULTIPLICATIVE_OP, "/", x++, y);
        } else if (character == '>' || character == '<') {
            character_buffer = scanner.getNextSymbol();
            if (character_buffer == '=') {
                character_buffer = '\0';
                return new MyToken(MyTokenType.REALTION_OP, Character.toString(character) + Character.toString(character_buffer), x++, y);
            }
            return new MyToken(MyTokenType.REALTION_OP, Character.toString(character), x++, y);
        } else if (character == '&') {
            character_buffer = scanner.getNextSymbol();
            if (character_buffer == '&') {
                character_buffer = '\0';
                return new MyToken(MyTokenType.AND_OP, "&&", x++, y);
            }
            return new MyToken(MyTokenType.UNKNOWN, Character.toString(character), x++, y);
        } else if (character == '|') {
            character_buffer = scanner.getNextSymbol();
            if (character_buffer == '|') {
                character_buffer = '\0';
                return new MyToken(MyTokenType.OR_OP, "||", x++, y);
            }
            return new MyToken(MyTokenType.UNKNOWN, Character.toString(character), x++, y);
        }
        //Numbers
        StringBuilder number = new StringBuilder();
        StringBuilder afterDot = new StringBuilder();

        if (character == '0') {
            type = MyTokenType.NUMBER;
            number.append(character);
            character = scanner.getNextSymbol();
            if (character == '.') {
                afterDot.append(readWord(MyTokenPrefix::isNumber));
                if (afterDot.toString().equals(".")) {
                    type = MyTokenType.UNKNOWN;
                }
            } else {
                character_buffer = character;
                return new MyToken(type, number.toString(), x++, y);
            }
            number.append(afterDot);
            return new MyToken(type, number.toString(), x++, y);
        } else if (MyTokenPrefix.isNumber(character)) {
            type = MyTokenType.NUMBER;
            number.append(readWord(MyTokenPrefix::isNumber));
            if (character == '.') {
                afterDot.append(readWord(MyTokenPrefix::isNumber));
                if (afterDot.toString().equals(".")) {
                    type = MyTokenType.UNKNOWN;
                }
            }
            number.append(afterDot);
            return new MyToken(type, number.toString(), x++, y);

        }
        //Roman numerals
        else if (MyTokenPrefix.isRoman(character)) {

            MyToken ret = romanLexer.readRoman(scanner, character, MyTokenPrefix::isRoman);
            ret.setCordinates(x++, y);
            character_buffer = romanLexer.current;
            return ret;
        }


        //ID
        else if (MyTokenPrefix.isLetter(character)) {
            StringBuilder name = new StringBuilder();
            name = readWord(MyTokenPrefix::isNotWhiteAndSpecial);
            return new MyToken(findSpecialType(name), name.toString(), x++, y);
        }

        return new MyToken(MyTokenType.UNKNOWN, Character.toString(character), x++, y);
    }


    private StringBuilder readWord(Function<Character, Boolean> condition) {
        StringBuilder word = new StringBuilder();
        do {
            word.append(character);
            character = scanner.getNextSymbol();
        } while (condition.apply(character) && !MyTokenPrefix.isEOF(character));
        character_buffer = character;
        return word;
    }

    private MyTokenType findSpecialType(StringBuilder name) {
        switch (name.toString()) {
            case "if":
                return MyTokenType.IF;
            case "while":
                return MyTokenType.LOOP;
            case "function":
                return MyTokenType.FUNCTION;
            case "var":
                return MyTokenType.VAR;
            case "else":
                return MyTokenType.ELSE;
            case "return":
                return MyTokenType.RETURN;
        }
        return MyTokenType.ID;
    }


}