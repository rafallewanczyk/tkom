package Parser.MySymbolTable;

import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;

import java.util.HashMap;

public class MyScopedSymbolTable {
    HashMap<MyToken, Symbol> symbols = new HashMap<>();
    MyToken scopeName;
    int scopeLevel;
    MyScopedSymbolTable enclosingScope;

    public MyScopedSymbolTable(MyToken scopeName, int scopeLevel, MyScopedSymbolTable enclosingScope) {
        this.scopeName = scopeName;
        this.scopeLevel = scopeLevel;
        this.enclosingScope = enclosingScope;

    }

    public void addBuiltins() {
        defineSymbol(new BuiltInTypeSymbol(new MyToken(MyTokenType.INT, "int", 0, 0)));
        defineSymbol(new BuiltInTypeSymbol(new MyToken(MyTokenType.REAL, "real", 0, 0)));
        defineSymbol(new BuiltInTypeSymbol(new MyToken(MyTokenType.ROMMAN, "rom", 0, 0)));
    }

    public HashMap<MyToken, Symbol> getSymbols() {
        return symbols;
    }

    public MyToken getScopeName() {
        return scopeName;
    }

    public int getScopeLevel() {
        return scopeLevel;
    }

    public void defineSymbol(Symbol symbol) {
        symbols.put(symbol.name, symbol);
        symbol.scopeLevel = scopeLevel;

    }

    public Symbol lookupSymbol(MyToken name) {
        Symbol symbol = symbols.get(name);

        if (symbol != null) {
            return symbol;
        }

        if (enclosingScope != null) {
            return enclosingScope.lookupSymbol(name);
        }
        return null;
    }

    public MyScopedSymbolTable getEnclosingScope() {
        return enclosingScope;
    }

    public void print() {
        System.out.println("SCOPE (SCOPED SYMBOL TABLE");
        System.out.println("==========================");
        System.out.println("name: " + scopeName.getValue());
        System.out.println("level: " + scopeLevel);
        try {
            System.out.println("enclosing: " + enclosingScope.getScopeName());
        } catch (NullPointerException e) {
            System.out.println("enclosing: null");
        }
        System.out.println("content:");
        symbols.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
        System.out.println("-------------------------------");

    }
}
