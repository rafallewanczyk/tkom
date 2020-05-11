import java.io.FileNotFoundException;

import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;
import Parser.MySymbolTable.*;

import static Lexer.Token.MyTokenType.INT;


public class Main {

    public static void main(String [] args) throws FileNotFoundException {
//        MyLexer lexer = new MyLexer("C:\\Users\\rafal\\IdeaProjects\\TKOM\\example.rom");
//
//        MyParser parser = new MyParser(lexer);
//        parser.parse();
        MySymbolTable table = new MySymbolTable();
        Symbol inttype = new BuiltInTypeSymbol(new MyToken(INT, "int", 0,0 ));
        table.defineSymbol(inttype);
        Symbol var_x_symbol = new VarSymbol(new MyToken(MyTokenType.ID, "x", 0, 0),new MyToken(INT, "int"));
        table.defineSymbol(var_x_symbol);
        System.out.println("szukam " + table.lookupSymbol(new MyToken(MyTokenType.ID, "y")));
        table.print();
    }
}
