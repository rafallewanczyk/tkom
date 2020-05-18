package Parser.MySymbolTable;

import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;

public class Symbol {
    public MyToken name;
    public MyToken type;
    public int scopeLevel;

    public Symbol(MyToken name) {
        this.name = name;
    }

    public Symbol(MyToken name, MyToken type) {
        this.name = name;
        this.type = type;
        this.scopeLevel = 0;
    }
}
