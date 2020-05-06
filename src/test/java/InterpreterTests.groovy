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
                "int c = a *2-5+(8-7*9) + (b-9);" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.program())
        interpreter.results()




    }
    def "or condition test"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("1 || 0"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.condition())

        expect:
        interpreter.getOut() == 1;
    }
    def "and condition test"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("1 && 0"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.condition())

        expect:
        interpreter.getOut() == 0;
    }
    def "mixed condition test"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("1 && 0 || 1 && 0"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.condition())

        expect:
        interpreter.getOut() == 0;
    }
}
