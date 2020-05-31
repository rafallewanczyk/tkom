import Interpreter.MyInterpreter;
import Lexer.MyLexer;
import Parser.MyParser;
import Parser.MySymbolTable.MySymbolTableBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        StringBuilder code = new StringBuilder();
        if (args.length != 2) {
            System.out.println("incorrect number of arguments\nexpected 2 arguments log true/false, file to run");
        }
        boolean log = false;

        try {
            log = Boolean.parseBoolean(args[0]);
            File file = new File(args[1]);

            FileInputStream fis = new FileInputStream(file);
            int r = 0;
            while ((r = fis.read()) != -1) {
                code.append((char) r);
            }

            MyLexer lexer = new MyLexer(args[1]);
            MyParser parser = new MyParser(lexer);
            MySymbolTableBuilder table = new MySymbolTableBuilder(parser.parse(), log);
            MyInterpreter interpreter = new MyInterpreter(table.getRoot(), log);
            System.out.println(interpreter.getOutput());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
