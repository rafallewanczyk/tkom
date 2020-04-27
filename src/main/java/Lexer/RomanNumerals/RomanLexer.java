package Lexer.RomanNumerals;

public class RomanLexer {


    public int parse(String str) {

        int result = 0;
        boolean thausends = false, hundreds = false, tens = false, ones = false;
        char current = '\0';
        StringBuilder numeral = new StringBuilder();
        for (int i = 0; i <= str.length(); i++) {
            if (i != str.length()){
                current = str.charAt(i);
                numeral.append(current);
            } else {
                numeral.append('$');
            }

            try {
                RomanNumeralsLookup.THAUSENDS.valueOf(numeral.toString());
                thausends = true;
            } catch (IllegalArgumentException e) {
                result += thausends ? RomanNumeralsLookup.THAUSENDS.valueOf(numeral.substring(0, numeral.length() - 1).toString()).getValue() : 0;
                if (thausends)
                    numeral = numeral.delete(0, numeral.length() - 1);
                thausends = false;
            }

            try {
                RomanNumeralsLookup.HUNDREDS.valueOf(numeral.toString());
                hundreds = true;
            } catch (IllegalArgumentException e) {
                result += hundreds ? RomanNumeralsLookup.HUNDREDS.valueOf(numeral.substring(0, numeral.length() - 1).toString()).getValue() : 0;
                if (hundreds)
                    numeral = numeral.delete(0, numeral.length() - 1);
                hundreds = false;
            }

            try {
                RomanNumeralsLookup.TENS.valueOf(numeral.toString());
                tens = true;
            } catch (IllegalArgumentException e) {
                result += tens ? RomanNumeralsLookup.TENS.valueOf(numeral.substring(0, numeral.length() - 1).toString()).getValue() : 0;
                if (tens)
                    numeral = numeral.delete(0, numeral.length() - 1);
                tens = false;
            }

            try {
                RomanNumeralsLookup.ONES.valueOf(numeral.toString());
                ones = true;
            } catch (IllegalArgumentException e) {
                result += ones ? RomanNumeralsLookup.ONES.valueOf(numeral.substring(0, numeral.length() - 1).toString()).getValue() : 0;
                if (ones)
                    numeral = numeral.delete(0, numeral.length() - 1);
                ones = false;
            }
            if (!thausends && !hundreds && !tens && !ones && !numeral.toString().equals("$"))
                return -1;
        }
        return result;
    }


}
