package Parser.MySymbolTable;

import Lexer.Token.MyToken;
import Parser.AST;
import Parser.AST_node;

import java.util.ArrayList;

public class FunctionSymbol extends Symbol {
    ArrayList<Symbol> params = new ArrayList<>();
    AST body;
    public FunctionSymbol(MyToken name, MyToken type) {
        super(name, type);
    }

    @Override
    public String toString() {
        return "<" + this.getClass().getSimpleName() + " " +  name.toString() + " : "  + type.toString() + ">";
    }

    public AST getBody() {
        return body;
    }

    public void setBody(AST body) {
        this.body = body;
    }

    public ArrayList<Symbol> getParams() {
        return params;
    }

    public void addParameter(Symbol p){
        params.add(p);
    }
}
