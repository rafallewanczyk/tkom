package Lexer.Token;

public class RomanToken extends MyToken {
    String romanValue;
    public RomanToken(String value, String romanValue, int x, int y){
       super(MyTokenType.ROMAN_NUMBER, value, x, y);
       this.romanValue = romanValue;
    }

    public RomanToken(String value, String romanValue){
        super(MyTokenType.ROMAN_NUMBER, value);
        this.romanValue = romanValue;
    }

}
