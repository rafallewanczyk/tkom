import java.io.FileNotFoundException;

import Interpreter.CallStack.ActivationRecord;
import Interpreter.CallStack.ActivationType;
import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;
import Parser.AST_node;
import Parser.MySymbolTable.*;

import static Lexer.Token.MyTokenType.*;


public class Main {

    public static void main(String [] args) throws FileNotFoundException {
        ActivationRecord ar = new ActivationRecord(new MyToken(IF, "if"), ActivationType.FUNCION, 0);
        ar.pushItem(new MyToken(ID, "value"), 10);
        System.out.println(ar);

        int val = ar.getItem(new MyToken(ID, "value"));
        System.out.println(val);

        int val1 ;
        try{
            val1 = ar.getItem(new MyToken(ID, "test"));
        } catch (NullPointerException e){
            System.out.println("no such variable");
        }
        Integer v = 100;

    }
}
