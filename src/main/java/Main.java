import java.io.FileNotFoundException;
import java.util.*;

import Interpreter.CallStack.ActivationRecord;
import Interpreter.CallStack.ActivationType;
import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;
import Parser.AST_node;
import Parser.MySymbolTable.*;

import static Lexer.Token.MyTokenType.*;


public class Main {

    public static void main(String [] args) throws FileNotFoundException {
        Deque<Integer> s = new ArrayDeque<>();
        s.push(1);
        s.push(2);
        s.push(3);

        s.pop();

        Iterator<Integer> i = s.iterator();
        while(i.hasNext()){
            System.out.println(i.next());
        }



    }
}
