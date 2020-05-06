import Interpreter.MyInterpreter
import Lexer.MyLexer
import Parser.MyParser
import spock.lang.Specification;

class InterpreterTests extends Specification{
    def "expression test"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "{" +
                "int v;" +
                "int a = 8;" +
                "int b = 8-5;" +
                "int c = a + (b-9);" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.program())
        interpreter.results()




    }
}
