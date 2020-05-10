package Parser.MySymbolTable;

import Lexer.Token.MyToken;

import java.util.HashMap;

public class SymbolTable {
    HashMap<MyToken, Symbol> symbols = new HashMap<>();

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
