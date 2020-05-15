package Parser.MySymbolTable;

import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;

import java.util.HashMap;

public class MySymbolTable {
    HashMap<MyToken, Symbol> symbols = new HashMap<>();
    MyToken scopeName;
    int scopeLevel;

    public MySymbolTable(){
        defineSymbol(new BuiltInTypeSymbol(new MyToken(MyTokenType.INT, "int", 0, 0)));
        defineSymbol(new BuiltInTypeSymbol(new MyToken(MyTokenType.REAL, "real", 0, 0)));
        defineSymbol(new BuiltInTypeSymbol(new MyToken(MyTokenType.ROMMAN, "rom", 0, 0)));
    }

    public void defineSymbol(Symbol symbol){
        symbols.put(symbol.name, symbol);

    }

    public Symbol lookupSymbol(MyToken name){
        Symbol symbol = symbols.get(name);
        return symbol;
    }

    public void print() {
        symbols.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });

    }
}
