package Parser;

import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;
import Parser.MySymbolTable.Symbol;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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


    public static class Num <T> implements AST{
        public MyToken token;
        public T value;
        public Num(T value){
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Num<?> num = (Num<?>) o;
            return Objects.equals(value, num.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    public static class RomNum extends Num<Integer> implements AST{
        public RomNum(MyToken token) {
            super(Integer.parseInt(token.getValue()));
            this.token = token;
        }

        public RomNum(int value){
            super(value);
        }
        @Override
        public String toString() {
            int value = this.value;
            if(value <=0 || value > 3999){
                return "OVERFLOW";
            }
            StringBuilder res = new StringBuilder();
            int num[] = {1,4,5,9,10,40,50,90,100,400,500,900,1000};
            String sym[] = {"I","IV","V","IX","X","XL","L","XC","C","CD","D","CM","M"};
            int i=12;
            while(value>0)
            {
                int div =value/num[i];
                value=value%num[i];
                while(div-- != 0)
                {
                    res.append(sym[i]);
                }
                i--;
            }
            return res.toString();
        }



    }

    public static class IntNum extends Num<Integer> implements AST{

        public IntNum(MyToken token) {
            super(Integer.parseInt(token.getValue()));
            this.token = token;
        }

        public IntNum(int value){
            super(value);
        }
        @Override
        public String toString() {
            return Integer.toString(value);
        }

    }


    public static class BoolNum extends Num<Boolean> implements AST{

        public BoolNum(MyToken token) {
            super(Boolean.parseBoolean(token.getValue()));
            this.token = token;
        }

        public BoolNum(boolean value){
            super(value);
        }
        @Override
        public String toString() {
            return Boolean.toString(value);
        }

    }

    public static class RealNum extends Num<Double> implements AST{

        public RealNum(MyToken token) {
            super(Double.parseDouble(token.getValue()));
            this.token = token;
        }

        public RealNum(double value){
            super(value);
        }
        @Override
        public String toString() {
            return Double.toString(value);
        }

    }

    public static class FunDeclaration implements AST{
        MyToken name;
        AST parameters;
        AST compound;
        AST type;

        public FunDeclaration(MyToken name, AST type, AST parameters, AST compound) {
            this.name = name;
            this.parameters = parameters;
            this.compound = compound;
            this.type = type;
        }

        public MyToken getName() {
            return name;
        }

        public AST getType() {
            return type;
        }

        public AST getParameters() {
            return parameters;
        }

        public AST getCompound() {
            return compound;
        }
    }

    public static class FunCall implements AST{
        MyToken name;
        ArrayList<AST> arguments;
        Symbol funSymbol;

        public void setFunSymbol(Symbol funSymbol) {
            this.funSymbol = funSymbol;
        }

        public Symbol getFunSymbol() {
            return funSymbol;
        }

        public FunCall(MyToken name, ArrayList<AST> arguments) {
            this.name = name;
            this.arguments = arguments;
        }

        public MyToken getName() {
            return name;
        }

        public ArrayList<AST> getArguments() {
            return arguments;
        }

    }

    public static class FunParameters implements AST{
        public ArrayList<AST> getParameters() {
            return parameters;
        }

        ArrayList<AST> parameters;

        public FunParameters(ArrayList<AST> parameters) {
            this.parameters = parameters;
        }
    }

    public static class ReturnStatement implements AST{
        AST retValue;

        public ReturnStatement(AST retValue) {
            this.retValue = retValue;
        }

        public AST getRetValue() {
            return retValue;
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
        AST variable;
        AST type;

        public AST getVariable() {
            return variable;
        }

        public AST getType() {
            return type;
        }

        public Parameter(AST variable, AST type) {
            this.variable = variable;
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
