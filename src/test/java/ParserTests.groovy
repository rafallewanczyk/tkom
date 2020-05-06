import Lexer.MyLexer
import Lexer.RomanNumerals.RomanLexer
import Lexer.Token.MyToken
import Lexer.Token.MyTokenType
import Parser.MyParser
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
}
