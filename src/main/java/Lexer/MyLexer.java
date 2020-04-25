package Lexer;


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
    private MyTokenType type;
    private StringBuilder token;
    private int x = 0, y = 0;
    private List<MyToken> tokens = new ArrayList<MyToken>();

    public MyLexer(String path) throws FileNotFoundException {
        scanner = new MyScanner(path);
    }

    public MyToken nextToken(){
        character = scanner.getNextSymbol();

        if(character == ' '){
            return nextToken();
        }
        else if(character == '\n' || character == '\r'){
            y++;
            return nextToken();
        }
        else if(character == ','){
            return new MyToken(MyTokenType.COMMA, ",", x++, y);
        }
        else if(character == ';'){
            return new MyToken(MyTokenType.SEMICOLLON, ";", x++, y);
        }
        else if(character == '('){
            return new MyToken(MyTokenType.LEFT_PARENTESIS, "(", x++, y);
        }
        else if(character == ')'){
            return new MyToken(MyTokenType.RIGHT_PARENTESIS, ")", x++, y);
        }
        else if(character == '{'){
            return new MyToken(MyTokenType.LEFT_BRACE, "{", x++, y);
        }
        else if(character == '}'){
            return new MyToken(MyTokenType.RIGHT_BRACE, "}", x++, y);
        }
        else if(character == '!'){
            return new MyToken(MyTokenType.UNARY_OP, "!", x++, y);
        }
        else if(character == '+'){
            return new MyToken(MyTokenType.ADDITIVE_OP, "+", x++, y);
        }
        else if(character == '-'){
            return new MyToken(MyTokenType.ADDITIVE_OP, "-", x++, y);
        }
        else if(character == '*'){
            return new MyToken(MyTokenType.MULTIPLICATIVE_OP, "*", x++, y);
        }
    }




    private StringBuilder readWord(Function<Character, Boolean> condition) {
        StringBuilder word = new StringBuilder();
        do {
            word.append(character);
            character = scanner.getNextSymbol();
        } while (condition.apply(character));
        return word;
    }

    private MyTokenType findSpecialType() {
        switch (token.toString()) {
            case "//":
                return MyTokenType.COMMENT;
            case "=":
                return MyTokenType.ASSIGNMENT_OP;
            case "||":
                return MyTokenType.OR_OP;
            case "&&":
                return MyTokenType.AND_OP;
            case "==":
                return MyTokenType.EQUAL_OP;
            case "<":
            case ">":
            case ">=":
            case "<=":
                return MyTokenType.REALTION_OP;
            case "/":
                return MyTokenType.MULTIPLICATIVE_OP;
            case "\"":
                return MyTokenType.QUOTE;
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
        return MyTokenType.UNKNOWN;
    }


}