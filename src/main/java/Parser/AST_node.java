package Parser;

import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;

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
        ArrayList<AST> children ;

        public Compound(ArrayList<AST> children){
            this.children = children;
        }

        public ArrayList<AST> getChildren(){
            return children;
        }

    }

    public static class AssignStatement implements AST{
        MyToken token;
        AST value;

        public AssignStatement(MyToken token, AST value) {
            this.token = token;
            this.value = value;
        }

        public AST getValue(){
            return value;
        }
        public MyToken getToken(){
            return token;
        }
    }

    public static class Variable implements AST{
        MyToken token;
        String value;
        public Variable(MyToken token){
            this.token = token;
            value = token.getValue();
        }

        public MyToken getToken(){
            return token;
        }

        public String getValue(){
            return value;
        }
    }

    public static class VarDeclaration implements AST{
        AST variable;
        AST type;
        AST expression;
        public VarDeclaration(AST variable, AST type, AST expression){
            this.variable = variable;
            this.type = type;
            this.expression = expression;
        }

        public AST getVariable() {
            return variable;
        }

        public AST getExpression() {
            return expression;
        }
    }

    public static class Type implements AST{
        MyToken token;
        public Type(MyToken token){
            this.token = token;
        }
    }
}
