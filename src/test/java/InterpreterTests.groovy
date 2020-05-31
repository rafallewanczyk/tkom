import Interpreter.MyInterpreter
import Lexer.MyLexer
import Lexer.Token.MyToken
import Lexer.Token.MyTokenType
import Parser.AST
import Parser.AST_node
import Parser.MyParser
import Parser.MySymbolTable.MySymbolTableBuilder
import Parser.MySymbolTable.SemanticException
import spock.lang.Specification;

class InterpreterTests extends Specification {

    def generateProgram(String code){
        FileWriter writer = new FileWriter("test.txt");
        writer.write(code)
        writer.close();
        MyLexer lexer = new MyLexer("test.txt")
        MyParser parser = new MyParser(lexer)
        MySymbolTableBuilder table = new MySymbolTableBuilder(parser.parse(), true)
        MyInterpreter interpreter = new MyInterpreter(table.getRoot(), true)
        return interpreter.getOutput();
    }

    def expectInt(AST out, int expectedValue){
        return ((AST_node.IntNum)out).equals(new AST_node.IntNum(expectedValue))
    }

    def expectReal(AST out, double expectedValue){
        return ((AST_node.RealNum)out).equals(new AST_node.RealNum(expectedValue))
    }

    def expectNull(AST out){
        return out == null;
    }

    def "expression with integer"(){
        given:
        AST out = generateProgram("""
               int main(){
                    int v = 50 - 90 * 10; 
                    return v; 
               } 
        """)

        expect:
        expectInt(out, -850)
    }


    def "expression with real"() {
        given:
        AST out = generateProgram("""
               real main(){
                    real v = (50.0 - 90.0 * 10.0)/3.0;
                    return v; 
               } 
        """)

        expect:
        expectReal(out, (-850d/3d))
    }

    def "expression with var"() {
        given:
        AST out = generateProgram("""
               real main(){
                    real v = (50.0 - 90.0 * 10.0)/3.0;
                    real x = v - 5.6;
                    return x; 
               } 
        """)
        expect:
        expectReal(out, -850d/3d - 5.6d)
    }

    def "expression with unary var"() {
        given:
        AST out = generateProgram("""
               real main(){
                    real v = (50.0 - 90.0 * 10.0)/3.0;
                    real x = -v;
                    return x; 
               } 
        """)

        expect:
        expectReal(out, 850d/3d)
    }

    def "simple fun call"() {
        given:
        AST out = generateProgram("""
        int fun(int q, int w){
            return q + w;
            return q * w;
        }
        int main(){
            int v = 9;
            int h = fun(9, 12);
            return h; 
        }"""
        )

        expect:
        expectInt(out, 21);
    }

    def "program with if true without else statement"() {
        given:
        AST out = generateProgram("""
            int main(){
                int v = 10;
                int x = 10;
                if(x == 10) {
                v = 1 ;
                };
                x = x  + 1;
                return v; 
            }"""
        )
        expect:
        expectInt(out, 1);
    }

    def "different types test"() {
        given:
        AST out = generateProgram("""
            real main(){
                real v = 10.0;
                int x = 10;
                v = v / 4.0;
                return v; 
            }"""
        )

        expect:
        expectReal(out, 10d/4d);

    }

    def "program with if false without else statement"() {
        given:
        AST out = generateProgram("""
            int main(){
                int v = 10;
                int x = 10;
                if(x != 10){
                    v = 1 ;
                };
                x = x  + 1;
                return v; 
            }"""
        )

        expect:
        expectInt(out, 10)
    }

    def "program with if true with else statement"() {
        given:
        AST out = generateProgram("""
            int main(){
                int v = 10;
                int x = 10;
                if(v < 11) {
                    v = 1;
                } else {
                    v = 2;
                };
                x = x  + 1;
                return v; 
            }"""
        )
        expect:
        expectInt(out, 1);
   }

    def "program with if false with else statement"() {
        given:
        AST out = generateProgram("""
            int main(){
                int v = 10;
                int x = 10;
                if(x != 10) {
                    v = 1 ;
                } else {
                    v = 2;
                };
                x = x  + 1;
                return v; 
            }"""
        )
        expect:
        expectInt(out, 2);

    }

    def "while test"() {
        given:
        AST out = generateProgram("""
            int main(){
                int v = 0;
                while(v < 10 + 132){
                    int k = 100;
                     v = v + 1;
                };
                return v; 
            }"""
        )
       expect:
       expectInt(out, 142);


    }

    def "while with 0 iterations test"() {
        given:
        AST out = generateProgram("""
            int main(){
                int v = 0;
                while(v > 10){
                    v = v + 1;
                };
                return v; 
            }"""
        )
        expect:
        expectInt(out, 0);


    }

    def "running main function"() {
        given:
        AST out = generateProgram("""
            int main(){
                int v = 12;
                return v;
            }"""
        )
        expect:
        expectInt(out, 12);

    }

    def "multiple functions declarations "() {
        given:
        AST out = generateProgram("""
            int main(){
                int v = 0;
                while(v < 10){
                    v = v + 1;
                };
                return v; 
            }
            
            int test(int a, int b, int c){
                int k = 0;
                while(k < 10){
                    k = k + 1;
                };
            }"""
        )
        expect:
        expectInt(out, 10);
    }

    def "function call"() {
        given:
        AST out = generateProgram("""
            int fun(int q, int w){
                return q + w;
                return q * w;
            }
            
            int test(int a, int b, int c){
                int k = 10;
                a = a + 7;
                b = b + 9;
                c = c + a + b;
                return fun(a, c);
            }
            
            int ret(){
                int a = 12;
                 int d = 22;
                 return fun(fun(5, 3), d);
            }
            
            int main(){
                int v = 9;
                test(1,2,3);
                int h = ret();
                return h;
            }"""
        )
        expect:
        expectInt(out, 30)

    }

    def "if statement"() {
        given:
        AST out = generateProgram("""
            int main(){
                int v = 0;
                if(v == 0){
                    int k = 66;
                    v = 10;
                };
                return v;
            }"""
        )
        expect:
        expectInt(out, 10);


    }

    def "incorrect type initiation"() {
        given:
        AST out = generateProgram("""
            int main(){
                real v = 12;
                return v;
            }"""
        )
        expect:
        expectNull(out);

    }


    def "incorrect type int argument"(){
        given:
        AST out = generateProgram("""
            rom Rom(int a, int b){
                return VII + VII + X + X; 
            }
            
            int main(){
                int test = 123;
                rom liczba = VI;
                liczba = liczba + Rom(VI,78) * VII;  
                return liczba; 
                }
            """)
        expect:
        expectNull(out);
    }

    def "incorrect number of arguments"(){
        given:
        AST out = generateProgram("""
            rom Rom(int a, int b){
                return VII + VII + X + X; 
            }
            
            int main(){
                int test = 123;
                rom liczba = VI;
                liczba = liczba + Rom(45, 12,78) * VII;  
                return liczba; 
                }
            """)
        expect:
        expectNull(out);
    }

    def "incorrect return type"(){
        given:
        AST out = generateProgram("""
            int Rom(int a, int b){
                return VII + VII + X + X; 
            }
            
            int main(){
                int test = 123;
                rom liczba = VI;
                liczba = liczba + Rom(45, 78) * VII;  
                return liczba; 
                }
            """)
        expect:
        expectNull(out);
    }

    def "not semicollon at last instruction"(){
        given:
        AST out = generateProgram("""
            int main(){
                int test = 123;
                rom liczba = VI;
                return liczba 
                }
            """)
        expect:
        expectNull(out);
    }

    def "nested if check"(){
        given:
        AST out = generateProgram("""
            int main(){
                int n = 100;
                int k; 
                if(n < 200){
                    if(n > 15){
                        k = 100; 
                        return 10;
                        return 11;
                        
                    }; 
                    return 12; 
                };
                return 30; 
                return 20; 
            } 
            """)
        expect:
        expectInt(out, 10);
    }
    def "nested while with return"(){
        given:
        AST out = generateProgram("""
            int main(){
                int n = 100;
                int k; 
                while(n > 1){
                    while(n > 15){ 
                        return 10;
                        
                    }; 
                };
                return 30; 
                return 20; 
            } 
            """)
        expect:
        expectInt(out, 10);
    }
}

