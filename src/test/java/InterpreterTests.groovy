import Interpreter.MyInterpreter
import Lexer.MyLexer
import Lexer.Token.MyToken
import Lexer.Token.MyTokenType
import Parser.MyParser
import spock.lang.Specification;

class InterpreterTests extends Specification{
    def "expression test"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "{" +
                "int v;" +

                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.compound_statement())
        interpreter.results()

        HashMap<MyToken, Integer> expected = new HashMap<MyToken, Integer>();
        expected.put(new MyToken(MyTokenType.ID, "v", 0, 0), 0);


        expect:
        interpreter.results() == expected


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
        writer.write("1 && 0 || 1 && 0 || 90 == 90"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.condition())

        expect:
        interpreter.getOut() == 1;
    }
    def "program with if true without else statement"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "{" +
                "int v = 10;" +
                "int x = 10;" +
                "if(x == 10) {" +
                "v = 1 ;" +
                "};" +
                "x = x  + 1;" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.compound_statement())

        HashMap<MyToken, Integer> expected = new HashMap<MyToken, Integer>();
        expected.put(new MyToken(MyTokenType.ID, "v", 0, 0), 1);
        expected.put(new MyToken(MyTokenType.ID, "x", 0, 0), 11);

        expect:
        interpreter.results() == expected
    }

    def "program with if false without else statement"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "{" +
                "int v = 10;" +
                "int x = 10;" +
                "if(x != 10) {" +
                        "v = 1 ;" +
                    "};" +
                "x = x  + 1;" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.compound_statement())

        HashMap<MyToken, Integer> expected = new HashMap<MyToken, Integer>();
        expected.put(new MyToken(MyTokenType.ID, "v", 0, 0), 10);
        expected.put(new MyToken(MyTokenType.ID, "x", 0, 0), 11);

        expect:
        interpreter.results() == expected
    }
    def "program with if true with else statement"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "{" +
                "int v = 10;" +
                "int x = 10;" +
                "if(x == 10) {" +
                "v = 1 ;" +
                "} else {" +
                "v = 2" +
                "};" +
                "x = x  + 1;" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.compound_statement())

        HashMap<MyToken, Integer> expected = new HashMap<MyToken, Integer>();
        expected.put(new MyToken(MyTokenType.ID, "v", 0, 0), 1);
        expected.put(new MyToken(MyTokenType.ID, "x", 0, 0), 11);

        expect:
        interpreter.results() == expected
    }

    def "program with if false with else statement"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "{" +
                "int v = 10;" +
                "int x = 10;" +
                "if(x != 10) {" +
                "v = 1 ;" +
                "} else {" +
                "v = 2" +
                "};" +
                "x = x  + 1;" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.compound_statement())

        HashMap<MyToken, Integer> expected = new HashMap<MyToken, Integer>();
        expected.put(new MyToken(MyTokenType.ID, "v", 0, 0), 2);
        expected.put(new MyToken(MyTokenType.ID, "x", 0, 0), 11);

        expect:
        interpreter.results() == expected
    }

    def "while test"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "{" +
                "int v = 0;" +
                "while(v < 10){" +
                "v = v + 1;" +
                "}" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.compound_statement())

        HashMap<MyToken, Integer> expected = new HashMap<MyToken, Integer>();
        expected.put(new MyToken(MyTokenType.ID, "v", 0, 0), 10);

        expect:
        interpreter.results() == expected
    }
    def "while with 0 iterations test"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "{" +
                "int v = 0;" +
                "while(v > 10){" +
                "v = v + 1;" +
                "};" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.compound_statement())

        HashMap<MyToken, Integer> expected = new HashMap<MyToken, Integer>();
        expected.put(new MyToken(MyTokenType.ID, "v", 0, 0), 0);

        expect:
        interpreter.results() == expected
    }

    def "running main function"(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "function main(){" +
                "int v = 0;" +
                "while(v < 10){" +
                "v = v + 1;" +
                "};" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.program())

        HashMap<MyToken, Integer> expected = new HashMap<MyToken, Integer>();
        expected.put(new MyToken(MyTokenType.ID, "v", 0, 0), 10);

        expect:
        interpreter.results() == expected
    }

    def "multiple functions declarations "(){
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "function main(){" +
                "int v = 0;" +
                "while(v < 10){" +
                "v = v + 1;" +
                "};" +
                "}"+
                "function test(int a, int b, int c){" +
                "int k = 0;" +
                "while(k < 10){" +
                "k = k + 1;" +
                "};" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MyInterpreter interpreter = new MyInterpreter(parser.program())

        HashMap<MyToken, Integer> expected = new HashMap<MyToken, Integer>();
        expected.put(new MyToken(MyTokenType.ID, "v", 0, 0), 10);

        expect:
        interpreter.results() == expected
    }
}

