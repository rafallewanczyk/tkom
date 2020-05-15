import Interpreter.MyInterpreter
import Lexer.MyLexer
import Lexer.RomanNumerals.RomanLexer
import Lexer.Token.MyToken
import Lexer.Token.MyTokenType
import Parser.MyParser
import Parser.MySymbolTable.MySymbolTableBuilder
import Parser.MySymbolTable.SemanticException
import spock.lang.Specification;

class ParserTests extends Specification{

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

        when:
        MySymbolTableBuilder symbolTable = new MySymbolTableBuilder(parser.compound_statement());

        then:
        thrown SemanticException

    }



    def "redeclared variable"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "{" +
                "int v = 10;" +
                "real v = 10;" +
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

        when:
        MySymbolTableBuilder symbolTable = new MySymbolTableBuilder(parser.compound_statement());

        then:
        thrown SemanticException

    }

}
