package Parser.MySymbolTable;

import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;

public class VarSymbol extends Symbol {
    public VarSymbol(MyToken name, MyTokenType type){
        super(name, type);
    }

    @Override
    public String toString() {
       return "<" + name.toString() + " : "  + type.toString() + ">";
    }
}
