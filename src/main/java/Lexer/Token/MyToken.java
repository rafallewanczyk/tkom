package Lexer.Token;

public class MyToken {
    MyTokenType type;
    String value;
    int x, y;

    public MyToken(MyTokenType type, String value, int x, int y) {
        this.type = type;
        this.value = value;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + type.toString() + ")" + value + "(" + x + "," + y + ")";
    }
}
