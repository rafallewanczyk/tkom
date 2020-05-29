import Interpreter.MyInterpreter
import Lexer.MyLexer
import Lexer.Token.MyToken
import Lexer.Token.MyTokenType
import Parser.MyParser
import Parser.MySymbolTable.MySymbolTableBuilder
import Parser.MySymbolTable.SemanticException
import spock.lang.Specification;

class InterpreterTests extends Specification {


    def "expression with integers"() {
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write("""
               int main(){
                    int v = 50 - 90 * 10; 
               } 
        """)
        writer.close();
        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MySymbolTableBuilder table = new MySymbolTableBuilder(parser.program());
        MyInterpreter interpreter = new MyInterpreter(table.getRoot());
    }

    def "expression with real"() {
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write("""
               int main(){
                    real v = (50.0 - 90.0 * 10.0)/3.0;
               } 
        """)
        writer.close();
        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MySymbolTableBuilder table = new MySymbolTableBuilder(parser.program());
        MyInterpreter interpreter = new MyInterpreter(table.getRoot());
    }

    def "expression with var"() {
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write("""
               int main(){
                    real v = (50.0 - 90.0 * 10.0)/3.0;
                    real x = v - 5.6;
               } 
        """)
        writer.close();
        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MySymbolTableBuilder table = new MySymbolTableBuilder(parser.program());
        MyInterpreter interpreter = new MyInterpreter(table.getRoot());
    }

    def "expression with unary var"() {
        given:
        FileWriter writer = new FileWriter("test.txt");
        writer.write("""
               int main(){
                    real v = (50.0 - 90.0 * 10.0)/3.0;
                    real x = -v;
               } 
        """)
        writer.close();
        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MySymbolTableBuilder table = new MySymbolTableBuilder(parser.program());
        MyInterpreter interpreter = new MyInterpreter(table.getRoot());
    }

    def "simple fun call"() {
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "int fun(int q, int w){" +
                "return q + w;" +
                "return q * w;" +
                "}" +
                "" +

                "int main(){" +
                "int v = 9;" +
                "int h = fun(9, 12);" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MySymbolTableBuilder table = new MySymbolTableBuilder(parser.program());
        MyInterpreter interpreter = new MyInterpreter(table.getRoot());


    }

    def "expression test"() {
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

    def "or condition test"() {
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

    def "and condition test"() {
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

    def "mixed condition test"() {
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

    def "program with if true without else statement"() {
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

    def "different types test"() {
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "{" +
                "real v = 10;" +
                "int x = 10;" +
                "v = v / 4;" +
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

    def "program with if false without else statement"() {
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

    def "program with if true with else statement"() {
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

    def "program with if false with else statement"() {
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

    def "while test"() {
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

    def "while with 0 iterations test"() {
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

    def "running main function"() {
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "int main(){" +
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

    def "multiple functions declarations "() {
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "int main(){" +
                "int v = 0;" +
                "while(v < 10){" +
                "v = v + 1;" +
                "};" +
                "}" +
                "int test(int a, int b, int c){" +
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

    def "function call"() {
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "int fun(int q, int w){" +
                "return q + w;" +
                "return q * w;" +
                "}" +
                "" +

                "int test(int a, int b, int c){" +
                "int k = 10;" +
                "a = a + 7;" +
                "b = b + 9;" +
                "c = c + a + b;" +
                "return fun(a, c);" +
                "}" +

                "int ret(){" +
                "int a = 12; " +
                "int d = 22;" +
                "return fun(fun(5, 3), d);" +
                "}" +

                "int main(){" +
                "int v = 9;" +
                "test(1,2,3);" +
                "int h = ret();" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MySymbolTableBuilder table = new MySymbolTableBuilder(parser.program());
        MyInterpreter interpreter = new MyInterpreter(table.getRoot());


    }

    def "if statement"() {
        given:
        FileWriter writer = new FileWriter("test.txt")
        writer.write("" +
                "int main(){" +
                "int v = 0;" +
                "if(v == 0){" +
                "int k = 66;" +
                "v = 10;" +
                "};" +
                "}"
        )

        writer.close();

        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MySymbolTableBuilder table = new MySymbolTableBuilder(parser.program());
        MyInterpreter interpreter = new MyInterpreter(table.getRoot());


    }

}

