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
        lexer.nextToken() == new MyToken(MyTokenType.NUMBER, "0", 3, 0);
        lexer.nextToken() == new MyToken(MyTokenType.NUMBER, "123", 4, 0);
        lexer.nextToken() == new MyToken(MyTokenType.UNKNOWN, "123.", 5, 0);
        lexer.nextToken() == new MyToken(MyTokenType.NUMBER, "1.3", 6, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ID, "a123", 7, 0);
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
        FileWriter writer = new FileWriter("test.txt");
        writer.write("MCDXI; IX IXI");
        writer.close();

        MyLexer lexer = new MyLexer("test.txt");

        expect:
        lexer.nextToken() == new MyToken(MyTokenType.ROMMAN, "1411", 0, 0);
        lexer.nextToken() == new MyToken(MyTokenType.SEMICOLLON, ";", 1, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ROMMAN, "9", 2, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ID, "IXI", 3, 0);


    }

    def "test for y"(){
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write("var zmienna = VI;\nzmienna = zmienna + MMCDX;");
        writer.close();

        MyLexer lexer = new MyLexer("test.txt");
        expect:
        lexer.nextToken() == new MyToken(MyTokenType.VAR, "var", 0, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ID, "zmienna", 1, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ASSIGNMENT_OP, "=", 2, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ROMMAN, "6", 3, 0);
        lexer.nextToken() == new MyToken(MyTokenType.SEMICOLLON, ";", 4, 0);
        lexer.nextToken() == new MyToken(MyTokenType.ID, "zmienna", 5, 1);
        lexer.nextToken() == new MyToken(MyTokenType.ASSIGNMENT_OP, "=", 6, 1);
        lexer.nextToken() == new MyToken(MyTokenType.ID, "zmienna", 7, 1);
        lexer.nextToken() == new MyToken(MyTokenType.ADDITIVE_OP, "+", 8, 1);
        lexer.nextToken() == new MyToken(MyTokenType.ROMMAN, "2410", 9, 1);
        lexer.nextToken() == new MyToken(MyTokenType.SEMICOLLON, ";", 10, 1);
    }

    def "empty input"(){
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.close();

        MyLexer lexer = new MyLexer("test.txt");
        expect:
        lexer.nextToken() == new MyToken(MyTokenType.EOF, "EOF", 0, 0);
        lexer.nextToken() == new MyToken(MyTokenType.EOF, "EOF", 0, 0);
    }


}
