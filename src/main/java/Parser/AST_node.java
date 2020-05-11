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
    public static class BinLogicOperator implements AST{
        public AST right, left; // todo set private
        public MyToken operation; //todo set private

        public BinLogicOperator(AST left, MyToken operation, AST right) {
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

    public static class FunDeclaration implements AST{
        MyToken name;
        AST parameters;
        AST compound;

        public FunDeclaration(MyToken name, AST parameters, AST compound) {
            this.name = name;
            this.parameters = parameters;
            this.compound = compound;
        }

        public MyToken getName() {
            return name;
        }

        public AST getParameters() {
            return parameters;
        }

        public AST getCompound() {
            return compound;
        }
    }

    public static class FunParameters implements AST{
       ArrayList<AST> parameters;

        public FunParameters(ArrayList<AST> parameters) {
            this.parameters = parameters;
        }
    }

    public static class IfStatement implements AST{
        AST condition;
        AST trueCompound;
        AST falseCompound;

        public AST getCondition() {
            return condition;
        }

        public AST getTrueCompound() {
            return trueCompound;
        }

        public AST getFalseCompound() {
            return falseCompound;
        }

        public IfStatement(AST condition, AST trueCompound, AST falseCompound) {
            this.condition = condition;
            this.trueCompound = trueCompound;
            this.falseCompound = falseCompound;
        }
    }

    public static class WhileStatement implements AST {
        AST condition;
        AST trueCompound;

        public AST getCondition() {
            return condition;
        }

        public AST getTrueCompound() {
            return trueCompound;
        }

        public WhileStatement(AST condition, AST trueCompound) {
            this.condition = condition;
            this.trueCompound = trueCompound;
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

        public AST getType() {
            return type;
        }

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

    public static class Parameter implements AST{
        MyToken name;
        AST type;

        public Parameter(MyToken name, AST type) {
            this.name = name;
            this.type = type;
        }
    }

    public static class Type implements AST{
        public MyToken getToken() {
            return token;
        }

        MyToken token;
        public Type(MyToken token){
            this.token = token;
        }
    }

    public static class Program implements AST{
        private ArrayList<AST> functions = new ArrayList<AST>();

        public ArrayList<AST> getFunctions() {
            return functions;
        }
        public void addFunction(AST function){
            functions.add(function);
        }
    }
}
