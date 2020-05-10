package Parser.MySymbolTable;

import Lexer.Token.MyToken;

public class BuiltInTypeSymbol extends Symbol {
    public BuiltInTypeSymbol(MyToken symbol){
        super(symbol);
    }

    @Override
    public String toString(){
        return name.toString();
    }
}
