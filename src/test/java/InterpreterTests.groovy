import Interpreter.MyInterpreter
import Lexer.MyLexer
import Parser.MyParser
import spock.lang.Specification;

class InterpreterTests extends Specification{
    def "expression test"(){
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write(" 8 + 4 * (8--9)");
        writer.close();

        MyLexer lexer = new MyLexer("test.txt");
        MyParser parser = new MyParser(lexer)
        MyInterpreter intepreter = new MyInterpreter(parser.expression());


    }
}
