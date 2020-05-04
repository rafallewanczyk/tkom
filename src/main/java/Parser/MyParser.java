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

    private AST_node factor() {
        MyToken token = current;
        if(current.getValue().equals("+")){
            eat(MyTokenType.ADDITIVE_OP);
            AST_node node = new AST.UnOperator(token, factor());
            return node;
        }
        if(current.getValue().equals("-")){
            eat(MyTokenType.ADDITIVE_OP);
            AST_node node = new AST.UnOperator(token, factor());
            return node;
        }
        if (current.getType() == MyTokenType.NUMBER) {
            eat(MyTokenType.NUMBER);
            return new AST.Num(token);
        } else if (token.getType() == MyTokenType.LEFT_PARENTESIS) {
            eat(MyTokenType.LEFT_PARENTESIS);
            AST_node node = expression();
            eat(MyTokenType.RIGHT_PARENTESIS);
            return node;
        }else{
            AST_node node = variable();
            return node;
        }
    }

    private AST_node term() {
        AST_node node = factor();

        while (current.getType() == MyTokenType.MULTIPLICATIVE_OP) {
            MyToken token = current;
            eat(MyTokenType.MULTIPLICATIVE_OP);
            node = new AST.BinOperator(node, token, factor());
        }
        return node;
    }

    public AST_node expression() {//todo set privaate
        AST_node node = term();

        while (current.getType() == MyTokenType.ADDITIVE_OP) {
            MyToken token = current;
            eat(MyTokenType.ADDITIVE_OP);
            node = new AST.BinOperator(node, token, term());
        }
        return node;
    }

    private AST_node program() {
        AST_node node = compound_statement();
        return node;
    }

    private AST_node compound_statement() {
        eat(MyTokenType.LEFT_BRACE);
        ArrayList<AST_node> nodes = statement_list();
        eat(MyTokenType.RIGHT_BRACE);
        AST_node root = new AST.Compound();
        return root;
    }

    private ArrayList<AST_node> statement_list(){
        AST_node node = statement();

        ArrayList<AST_node> result = new ArrayList<AST_node>();
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

    private AST_node statement(){
        AST_node node;
        if(current.getType() == MyTokenType.ID){
           node = assignmentStatement();
           return node;
        }
        return null;
    }

    private AST_node assignmentStatement(){
        MyToken left;
        AST_node right;
        left = current;
        eat(MyTokenType.ID);
        eat(MyTokenType.ASSIGNMENT_OP);
        right = expression();
        return new AST.AssignStatement(left, right);
    }

    private AST_node variable(){
        AST_node node = new AST.Variable(current);
        eat(MyTokenType.ID);
        return node;

    }

    public AST_node parse() {
        AST_node node = program();
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








