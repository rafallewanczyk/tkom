package Lexer.Token;

import java.util.Objects;

public class MyToken {
    public MyTokenType type;
    public String value;
    public int x, y;

    public MyToken(MyTokenType type, String value, int x, int y) {
        this.type = type;
        this.value = value;
        this.x = x;
        this.y = y;
    }

    public MyToken(MyTokenType type, String value) {
        this.type = type;
        this.value = value;
        this.x = 0;
        this.y =0;
    }

    public MyTokenType getType() {
        return type;
    }

    public boolean isEmpty(){
       return (type == MyTokenType.EMPTY);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setType(MyTokenType type){
        this.type = type;
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
        return type == myToken.type &&
                value.equals(myToken.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}
