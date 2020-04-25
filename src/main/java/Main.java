import java.io.FileNotFoundException;
import Lexer.*;

public class Main {

    public static void main(String [] args) throws FileNotFoundException {
        MyLexer lexer = new MyLexer("C:\\Users\\rafal\\IdeaProjects\\TKOM\\src\\example.rom");
        lexer.run();
    }
}
