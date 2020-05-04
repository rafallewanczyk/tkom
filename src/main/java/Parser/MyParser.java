package Parser;

import Lexer.MyLexer;
import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;

import java.awt.image.AreaAveragingScaleFilter;
import java.util.ArrayList;
import java.util.List;

public class MyParser {

    MyLexer lexer;
    MyToken current, tokenBuffer;
    public AST root; ///todo set private
//    Program program = new Program();

    public MyParser(MyLexer lexer) {
        this.lexer = lexer;
        current = lexer.nextToken();
        tokenBuffer = new MyToken(MyTokenType.EMPTY, "empty", 0, 0);

    }

    private void eat(MyTokenType type) {
        if (current.getType() == type) {
            current = lexer.nextToken();
            return;
        }
        System.out.println("error: got " + current + " expected " + type);

    }

    private void eatAny(){
       current = lexer.nextToken();
    }

    private void error(){
        System.out.println("found error");
    }

    private AST factor() {
        MyToken token = current;
        if (current.getType() == MyTokenType.NUMBER) {
            eat(MyTokenType.NUMBER);
            return new Num(token);
        } else if (token.getType() == MyTokenType.LEFT_PARENTESIS) {
            eat(MyTokenType.LEFT_PARENTESIS);
            AST node = expression();
            eat(MyTokenType.RIGHT_PARENTESIS);
            return node;
        }else{
            AST node = variable();
            return node;
        }
    }

    private AST term() {
        AST node = factor();

        while (current.getType() == MyTokenType.MULTIPLICATIVE_OP) {
            MyToken token = current;
            eat(MyTokenType.MULTIPLICATIVE_OP);
            node = new BinOperator(node, token, factor());
        }
        return node;
    }

    private AST expression() {
        AST node = term();

        while (current.getType() == MyTokenType.ADDITIVE_OP) {
            MyToken token = current;
            eat(MyTokenType.ADDITIVE_OP);
            node = new BinOperator(node, token, term());
        }
        return node;
    }

    private AST program() {
        AST node = compound_statement();
        return node;
    }

    private AST compound_statement() {
        eat(MyTokenType.LEFT_BRACE);
        ArrayList<AST> nodes = statement_list();
        eat(MyTokenType.RIGHT_BRACE);
        AST root = new Compound();
        return root;
    }

    private ArrayList<AST> statement_list(){
        AST node = statement();

        ArrayList<AST> result = new ArrayList<AST>();
        result.add(node);

        while(current.getType() == MyTokenType.SEMICOLLON){
            eat(MyTokenType.SEMICOLLON);
            result.add(statement());
        }
        if(current.getType() == MyTokenType.ID){
            error();
        }
        return result;
    }

    private AST statement(){
        AST node;
        if(current.getType() == MyTokenType.ID){
           node = assignmentStatement();
           return node;
        }
        return null;
    }

    private AST assignmentStatement(){
        MyToken left;
        AST right;
        left = current;
        eat(MyTokenType.ID);
        eat(MyTokenType.ASSIGNMENT_OP);
        right = expression();
        return new AssignStatement(left, right);
    }

    private AST variable(){
        AST node = new Variable(current);
        eat(MyTokenType.ID);
        return node;

    }

    public AST parse() {
        AST node = program();
        if(current.getType() != MyTokenType.EOF){
            error();
        }
        return node;
//        System.out.println(visit(expression()));
//        return null;


//        root = expression();
//        return root;
////        program.addFunction();
//        System.out.println(program);
    }


    public void allTokens() {
        while (current.getType() != MyTokenType.EOF) {
            System.out.println(current);
            current = lexer.nextToken();
        }
    }


    //todo part of interpreter to delete

//todo end of interpreter part
}



 class BinOperator implements AST {
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


class Num implements AST {
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

class Compound implements AST {
    List<AST> children = new ArrayList<AST>();

}

class AssignStatement implements AST {
    MyToken token;
    AST value;

    public AssignStatement(MyToken token, AST value) {
        this.token = token;
        this.value = value;
    }
}

class Variable implements AST{
    MyToken token;
    String value;
    public Variable(MyToken token){
        this.token = token;
        value = token.getValue();
    }
}




