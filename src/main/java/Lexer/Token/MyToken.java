package Lexer.Token;

import java.util.Objects;

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

    public MyTokenType getType() {
        return type;
    }

    public boolean isEmpty(){
       return (type == MyTokenType.EMPTY);
    }

    public void setEmpty(){
        type = MyTokenType.EMPTY;
    }

    public void setCordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String getValue(){
        return value;
    }

    @Override
    public String toString() {
        return "(" + type.toString() + ")" + value /*+ "(" + x + "," + y + ")"*/;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyToken myToken = (MyToken) o;
        return x == myToken.x &&
                y == myToken.y &&
                type == myToken.type &&
                value.equals(myToken.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value, x, y);
    }
}
