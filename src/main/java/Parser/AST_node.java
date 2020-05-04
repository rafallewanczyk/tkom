package Parser;

import Lexer.Token.MyToken;

import java.util.ArrayList;
import java.util.List;

public class AST_node{
    public static class BinOperator implements AST{
        public AST right, left; // todo set private
        public MyToken operation; //todo set private

        public BinOperator(AST left, MyToken operation, AST right) {
            this.right = right;
            this.left = left;
            this.operation = operation;
        }

        @Override
        public String toString() {
            return operation.getValue();
        }

    }

    public static class UnOperator implements AST{
        MyToken token;
        AST expression;
        public UnOperator(MyToken token, AST expression){
            this.token = token ;
            this.expression = expression;
        }
        public String getType(){
            return token.getValue();
        }
        public AST getExpression(){
            return expression;
        }
    }


    public static class Num implements AST{
        MyToken token;
        public int value;

        public Num(MyToken token) {
            this.token = token;
            value = Integer.parseInt(token.getValue());
        }

        @Override
        public String toString() {
            return token.getValue();
        }

    }

    public static class Compound implements AST{
        List<AST> children = new ArrayList<AST>();

    }

    public static class AssignStatement implements AST{
        MyToken token;
        AST value;

        public AssignStatement(MyToken token, AST value) {
            this.token = token;
            this.value = value;
        }
    }

    public static class Variable implements AST{
        MyToken token;
        String value;
        public Variable(MyToken token){
            this.token = token;
            value = token.getValue();
        }
    }
}
