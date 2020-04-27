import Lexer.MyLexer
import Lexer.RomanNumerals.RomanLexer
import Lexer.Token.MyToken
import Lexer.Token.MyTokenType
import spock.lang.Specification;

public class LexerTests extends Specification {
    def "test single and double equals"() {
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write("===|=");
        writer.close();

        MyLexer lexer = new MyLexer("test.txt");

        expect:
        lexer.nextToken() == new MyToken(MyTokenType.EQUAL_OP, "==", 0, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ASSIGNMENT_OP, "=", 1, 0);
        lexer.nextToken() == new MyToken(MyTokenType.UNKNOWN, "|", 2, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ASSIGNMENT_OP, "=", 3, 0);

    }

    def "number test"() {
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write("1234 12.84 0 0123 123. 1.3a123");
        writer.close();

        MyLexer lexer = new MyLexer("test.txt");

        expect:
        lexer.nextToken() == new MyToken(MyTokenType.NUMBER, "1234", 0, 0);
        lexer.nextToken() == new MyToken(MyTokenType.NUMBER, "12.84", 1, 0);
        lexer.nextToken() == new MyToken(MyTokenType.NUMBER, "0", 2, 0);
        lexer.nextToken() == new MyToken(MyTokenType.UNKNOWN, "0123", 3, 0);
        lexer.nextToken() == new MyToken(MyTokenType.UNKNOWN, "123.", 4, 0);
        lexer.nextToken() == new MyToken(MyTokenType.NUMBER, "1.3", 5, 0);
    }

    def "number with mulitple dots"(){
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write("123.456.123");
        writer.close();

        MyLexer lexer = new MyLexer("test.txt");

        expect:
        lexer.nextToken() == new MyToken(MyTokenType.NUMBER, "123.456", 0, 0);
        lexer.nextToken() == new MyToken(MyTokenType.UNKNOWN, ".", 1, 0);
        lexer.nextToken() == new MyToken(MyTokenType.NUMBER, "123", 2, 0);

    }

    def "id test"() {
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write("nazwaZmiennej zmienna_Znumerem12 zmienna+zmienna");
        writer.close();

        MyLexer lexer = new MyLexer("test.txt");

        expect:
        lexer.nextToken() == new MyToken(MyTokenType.ID, "nazwaZmiennej", 0, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ID, "zmienna_Znumerem12", 1, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ID, "zmienna", 2, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ADDITIVE_OP, "+", 3, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ID, "zmienna", 4, 0);


    }

    def "id with keywords"() {
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write("if zmienna return 123");
        writer.close();

        MyLexer lexer = new MyLexer("test.txt");

        expect:
        lexer.nextToken() == new MyToken(MyTokenType.IF, "if", 0, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ID, "zmienna", 1, 0);
        lexer.nextToken() == new MyToken(MyTokenType.RETURN, "return", 2, 0);
        lexer.nextToken() == new MyToken(MyTokenType.NUMBER, "123", 3, 0);


    }

    def "roman numeral test"() {
        given:
        RomanLexer lexer = new RomanLexer();
        expect:
        lexer.parse("XVI") == 16
        lexer.parse("IXI") == -1
        lexer.parse("MMMCMXCIX") == 3999
        lexer.parse("XXa") == -1


    }
}
