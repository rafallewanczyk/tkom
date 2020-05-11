import Interpreter.MyInterpreter
import Lexer.MyLexer
import Lexer.RomanNumerals.RomanLexer
import Lexer.Token.MyToken
import Lexer.Token.MyTokenType
import Parser.MyParser
import Parser.MySymbolTable.MySymbolTableBuilder
import spock.lang.Specification;

class ParserTests extends Specification{
    def "first program"(){
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write("" +
                "{" +
                "a = 8;" +
                "b = 8 - 5;" +
                "c = a + (b -10);" +
                "}"
        );

        writer.close();

        MyLexer lexer = new MyLexer("test.txt");
        MyParser parser = new MyParser(lexer);

        parser.parse();
    }
    def "parse expression"(){
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write(" 8 + 4 * (8-9)");
        writer.close();

        MyLexer lexer = new MyLexer("test.txt");
        MyParser parser = new MyParser(lexer)

//        parser.parse();
        System.out.println(parser.visit(parser.expression()))
    }

    def "unknown variable"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "{" +
                "int v = 10;" +
                "int x = 10;" +
                "b = 10;" +
                "if(g != 10) {" +
                "v = 1 ;" +
                "};" +
                "x = x  + 1;" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MySymbolTableBuilder symbolTable = new MySymbolTableBuilder(parser.compound_statement());

    }
    def "running main function"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "function main(){" +
                "int v = 0;" +
                "while(vi < 10){" +
                "v = t + 1;" +
                "};" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MySymbolTableBuilder symbolTable = new MySymbolTableBuilder(parser.program());


    }
}
