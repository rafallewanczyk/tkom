import java.io.FileNotFoundException;
import Lexer.*;
import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;

public class Main {

    public static void main(String [] args) throws FileNotFoundException {
        MyLexer lexer = new MyLexer("C:\\Users\\rafal\\IdeaProjects\\TKOM\\example.rom");

        MyToken token; 
        do{
            token = lexer.nextToken();
            System.out.println(token);
        }while(token.getType() != MyTokenType.EOF);
    }
}
