package Lexer.RomanNumerals;

import Lexer.Scanner.MyScanner;
import Lexer.Token.MyToken;
import Lexer.Token.MyTokenPrefix;
import Lexer.Token.MyTokenType;

import java.util.Arrays;
import java.util.function.Function;

public class RomanLexer {


    StringBuilder numeral, total;
    public char current;

    public MyToken readRoman(MyScanner scanner, char character, Function<Character, Boolean> condition) {
        numeral = new StringBuilder();
        int result = 0;
        boolean thausends = false, hundreds = false, tens = false, ones = false;
        current = character;
        total = new StringBuilder();

        numeral.append(current);
        total.append(current);
        result += checkMagnitude(RomanNumeralsLookup.THAUSENDS.class, scanner, condition);
        result += checkMagnitude(RomanNumeralsLookup.HUNDREDS.class, scanner, condition);
        result += checkMagnitude(RomanNumeralsLookup.TENS.class, scanner, condition);
        result += checkMagnitude(RomanNumeralsLookup.ONES.class, scanner, condition);

        if(MyTokenPrefix.isNotWhiteAndSpecial(current)){
            current = scanner.getNextSymbol();
            while(MyTokenPrefix.isNotWhiteAndSpecial(current)){
                total.append(current);
                current = scanner.getNextSymbol();
            }

            return new MyToken(MyTokenType.ID, total.toString(), 0, 0);
        }

        return new MyToken(MyTokenType.ROMMAN, Integer.toString(result), 0, 0);
    }

    public <T extends Enum<T>> int checkMagnitude(Class<T> magnitude, MyScanner scanner, Function<Character, Boolean> condition) {
        int result = 0;
        boolean repeat = false;


        do {

            try {
                result = ((get) (Enum.valueOf(magnitude, numeral.toString()))).getValue();
                repeat = true;
            } catch (IllegalArgumentException e) {
                if (repeat)
                    numeral = numeral.delete(0, numeral.length() - 1);
                break;
            }
            current = scanner.getNextSymbol();
            numeral.append(current);
            total.append(current);


        } while (true);
        return result;
    }


}
