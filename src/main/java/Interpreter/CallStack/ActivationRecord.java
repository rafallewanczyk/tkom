package Interpreter.CallStack;

import Lexer.Token.MyToken;

import java.util.HashMap;

public class ActivationRecord {
    MyToken name;
    ActivationType type;
    int nestingLevel;
    HashMap<MyToken, Integer> members = new HashMap<>();

    public ActivationRecord(MyToken name, ActivationType type, int nestingLevel) {
        this.name = name;
        this.type = type;
        this.nestingLevel = nestingLevel;
    }

    public void setMembers(HashMap<MyToken, Integer> members) {
        this.members = members;
    }

    public int getItem(MyToken key) {
        return members.get(key);
    }

    public void pushItem(MyToken key, int value) {
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

    public HashMap<MyToken, Integer> getMembers() {
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
