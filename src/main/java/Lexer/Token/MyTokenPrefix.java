package Lexer.Token;

public class MyTokenPrefix {
    public static boolean isLetter(char c) {
        return Character.isLetter(c);
    }

    public static boolean isLowerCaseLetter(char c){
        return (Character.isLetter(c) && Character.isLowerCase(c));
    }

    public static boolean isNumber(char c) {
        return Character.isDigit(c) ;
    }

    public static boolean isEOF(char c) {
        return c == '\u001a';
    }

    public static boolean isWhite(char c){
        return c == ' ' ||  c == '\r' || c == '\n';
    }

    public static boolean isNotWhiteAndSpecial(char c){
        return isNotWhite(c) && !isSpecial(c);
    }
    public static boolean isNotWhite(char c){
        return c != ' ' && c != '\r' && c != '\n';
    }

    public static boolean isRoman(char c){
//        I	V	X	L	C	D	M
        return c == 'I' ||
                c == 'V' ||
                c == 'X' ||
                c == 'L' ||
                c == 'C' ||
                c == 'D' ||
                c == 'M' ;
    }

    public static boolean isUnknown(char c){
        return !isRoman(c) && Character.isUpperCase(c);
    }

    public static boolean isSpecial(char c) {
        return  c == '!' ||
                c == '+' ||
                c == '-' ||
                c == '*' ||
                c == '%' ||
                c == '{' ||
                c == '}' ||
                c == '(' ||
                c == ')' ||
                c == '.' ||
                c == ';' ||
                c == ',' ||
                c == '\"'||
                c == '=' ||
                c == '|' ||
                c == '&' ||
                c == '<' ||
                c == '/' ||
                c == '>';
    }

}