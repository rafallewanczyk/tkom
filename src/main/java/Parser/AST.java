package Parser;

import Lexer.Token.MyToken;

import java.util.ArrayList;
import java.util.List;

public class AST{
    public static class BinOperator implements AST_node{
        public AST_node right, left; // todo set private
        public MyToken operation; //todo set private

        public BinOperator(AST_node left, MyToken operation, AST_node right) {
            this.right = right;
            this.left = left;
            this.operation = operation;
        }

        @Override
        public String toString() {
            return operation.getValue();
        }

    }

    public static class UnOperator implements AST_node{
        MyToken token;
        AST_node expression;
        public UnOperator(MyToken token, AST_node expression){
            this.token = token ;
            this.expression = expression;
        }
        public String getType(){
            return token.getValue();
        }
        public AST_node getExpression(){
            return expression;
        }
    }


    public static class Num implements AST_node{
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

    public static class Compound implements AST_node{
        List<AST_node> children = new ArrayList<AST_node>();

    }

    public static class AssignStatement implements AST_node{
        MyToken token;
        AST_node value;

        public AssignStatement(MyToken token, AST_node value) {
            this.token = token;
            this.value = value;
        }
    }

    public static class Variable implements AST_node{
        MyToken token;
        String value;
        public Variable(MyToken token){
            this.token = token;
            value = token.getValue();
        }
    }
}
