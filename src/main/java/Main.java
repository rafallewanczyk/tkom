import java.io.FileNotFoundException;
import Lexer.*;
import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;
import Parser.MyParser;

public class Main {

    public static void main(String [] args) throws FileNotFoundException {
        MyLexer lexer = new MyLexer("C:\\Users\\rafal\\IdeaProjects\\TKOM\\example.rom");

        MyParser parser = new MyParser(lexer);
        parser.parse();
    }
}
