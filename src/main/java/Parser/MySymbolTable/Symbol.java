package Parser.MySymbolTable;

import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;

public class Symbol {
    public MyToken name;
    public MyTokenType type;

    public Symbol(MyToken name) {
        this.name = name;
    }

    public Symbol(MyToken name, MyTokenType type) {
        this.name = name;
        this.type = type;
    }
}
