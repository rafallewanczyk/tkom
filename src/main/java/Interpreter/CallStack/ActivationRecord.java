package Interpreter.CallStack;

import Lexer.Token.MyToken;
import Parser.AST;

import java.util.HashMap;

public class ActivationRecord {
    MyToken name;
    ActivationType type;
    int nestingLevel;
    HashMap<MyToken, AST> members = new HashMap<>();

    public ActivationRecord(MyToken name, ActivationType type, int nestingLevel) {
        this.name = name;
        this.type = type;
        this.nestingLevel = nestingLevel;
    }

    public void setMembers(HashMap<MyToken,AST> members) {
        this.members = members;
    }

    public AST getItem(MyToken key) {
        AST ret = members.get(key);
        return ret;
    }

    public void pushItem(MyToken key, AST value) {
        members.put(key, value);
    }

    public MyToken getName() {
        return name;
    }

    public ActivationType getType() {
        return type;
    }

    public int getNestingLevel() {
        return nestingLevel;
    }

    public HashMap<MyToken, AST> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("name: " + name + " type: " + type + " nestingLevel: " + nestingLevel + "\n");
        members.entrySet().forEach(entry -> {
            s.append(entry.getKey() + " " + entry.getValue());
        });
        return s.toString();
    }
}
