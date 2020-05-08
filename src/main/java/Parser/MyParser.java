package Parser;

import Lexer.MyLexer;
import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;
import Parser.AST_node.*;
import Parser.AST;

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

    private void eatAny() {
        current = lexer.nextToken();
    }

    private void error() {
        System.out.println("found error");
    }


    public AST condition() { //todo set private
        AST node = andCondition();

        while (current.getType() == MyTokenType.OR_OP) {
            MyToken token = current;
            eat(MyTokenType.OR_OP);
            node = new BinLogicOperator(node, token, andCondition());
        }
        return node;
    }

    private AST andCondition() {
        AST node = equalityCondition();

        while (current.getType() == MyTokenType.AND_OP) {
            MyToken token = current;
            eat(MyTokenType.AND_OP);
            node = new BinLogicOperator(node, token, equalityCondition());
        }
        return node;
    }

    private AST equalityCondition() {
        AST node = relationalCondition();

        while (current.getType() == MyTokenType.EQUAL_OP) {
            MyToken token = current;
            eat(MyTokenType.EQUAL_OP);
            node = new BinLogicOperator(node, token, relationalCondition());
        }
        return node;
    }

    private AST relationalCondition() {
        AST node = primaryCondition();

        while (current.getType() == MyTokenType.RELATION_OP) {
            MyToken token = current;
            eat(MyTokenType.RELATION_OP);
            node = new BinLogicOperator(node, token, relationalCondition());
        }
        return node;
    }

    private AST primaryCondition() {
        MyToken token = current;

        if (current.getType() == MyTokenType.INT_NUMBER) {
            eat(MyTokenType.INT_NUMBER);
            return new Num(token);
        } else if (token.getType() == MyTokenType.LEFT_PARENTESIS) {
            eat(MyTokenType.LEFT_PARENTESIS);
            AST node = condition();
            eat(MyTokenType.RIGHT_PARENTESIS);
            return node;
        } else {
            AST node = variable();
            return node;
        }

    }


    private AST factor() {
        MyToken token = current;

        if (current.getType() == MyTokenType.INT_NUMBER) {
            eat(MyTokenType.INT_NUMBER);
            return new Num(token);
        } else if (token.getType() == MyTokenType.LEFT_PARENTESIS) {
            eat(MyTokenType.LEFT_PARENTESIS);
            AST node = expression();
            eat(MyTokenType.RIGHT_PARENTESIS);
            return node;
        } else {
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

    public AST expression() {//todo set privaate
        AST node = term();

        while (current.getType() == MyTokenType.ADDITIVE_OP) {
            MyToken token = current;
            eat(MyTokenType.ADDITIVE_OP);
            node = new BinOperator(node, token, term());
        }
        return node;
    }

    private AST function(){
        eat(MyTokenType.FUNCTION);
        MyToken name = current;
        eat(MyTokenType.ID);
        AST parameters= function_parameters();


        return new FunDeclaration(name, parameters, compound_statement());
    }

    private AST function_parameters(){
        eat(MyTokenType.LEFT_PARENTESIS);
        if(current.getType() == MyTokenType.RIGHT_PARENTESIS){
            eat(MyTokenType.RIGHT_PARENTESIS);
            return null;
        }

        MyToken type = current;
        if(type.getType() == MyTokenType.ROMMAN || type.getType() == MyTokenType.INT){
            eat(type.getType());
        }else {
            //todo throw error
        }
        MyToken id = current;
        eat(MyTokenType.ID);
        ArrayList<AST> parameters = new ArrayList<AST>();
        parameters.add(new Parameter(id, new Type(type)));

        while(current.getType() == MyTokenType.COMMA){
            eat(MyTokenType.COMMA);
            type = current;
            if(type.getType() == MyTokenType.ROMMAN || type.getType() == MyTokenType.INT){
                eat(type.getType());
            }else {
                //todo throw error
            }
            id = current;
            eat(MyTokenType.ID);
            parameters.add(new Parameter(id, new Type(type)));
        }
        eat(MyTokenType.RIGHT_PARENTESIS);
        return new FunParameters(parameters);

    }

    private AST program() {
        AST node = new Program();
        while(current.getType() != MyTokenType.EOF){
            ((Program)node).addFunction(function());
        }
        return node;
    }

    public AST compound_statement() { // todo set private
        eat(MyTokenType.LEFT_BRACE);
        ArrayList<AST> nodes = statement_list();
        eat(MyTokenType.RIGHT_BRACE);
        AST root = new Compound(nodes);
        return root;
    }

    private ArrayList<AST> statement_list() {
        AST node = statement();

        ArrayList<AST> result = new ArrayList<AST>();
        result.add(node);

        while (current.getType() == MyTokenType.SEMICOLLON) {
            eat(MyTokenType.SEMICOLLON);
            result.add(statement());
        }

        if (current.getType() == MyTokenType.ID) {
            error();
            return null;
        }
        return result;
    }

    private AST statement() {
        AST node;
        if (current.getType() == MyTokenType.ID) {
            node = assignmentStatement();
            return node;
        }

        if (current.getType() == MyTokenType.INT || current.getType() == MyTokenType.REAL) {
            node = initStatement();
            return node;
        }
        if(current.getType() == MyTokenType.IF){
            node = ifStatement();
            return node;
        }
        if(current.getType() == MyTokenType.LOOP){
            node = whileStatement();
            return node;
        }
        return null;
    }

    private AST whileStatement(){
        eat(MyTokenType.LOOP);
        eat(MyTokenType.LEFT_PARENTESIS);
        AST condition = condition();
        eat(MyTokenType.RIGHT_PARENTESIS);
        AST trueBlock = compound_statement();
        return new WhileStatement(condition, trueBlock);
    }
    private AST ifStatement(){
        eat(MyTokenType.IF);
        eat(MyTokenType.LEFT_PARENTESIS);
        AST condition = condition();
        eat(MyTokenType.RIGHT_PARENTESIS);
        AST trueBlock = compound_statement();
        AST falseBlock = null ;
        if(current.getType() == MyTokenType.ELSE){
            eat(MyTokenType.ELSE);
            falseBlock = compound_statement();
        }
        return new IfStatement(condition, trueBlock, falseBlock);
    }

    private AST initStatement() {
        MyToken type = current;
        eat(type.getType());
        MyToken var = current;
        eat(MyTokenType.ID);


        if (current.getType() == MyTokenType.ASSIGNMENT_OP) {
            eat(MyTokenType.ASSIGNMENT_OP);
            return new VarDeclaration(new Variable(var), new Type(type), expression());
        }

        return new VarDeclaration(new Variable(var), new Type(type), null);
    }

    private AST assignmentStatement() {
        MyToken left;
        AST right;
        left = current;
        eat(MyTokenType.ID);
        eat(MyTokenType.ASSIGNMENT_OP);
        right = expression();
        return new AssignStatement(left, right);
    }

    private AST variable() {
        AST node = new Variable(current);
        eat(MyTokenType.ID);
        return node;

    }

    public AST parse() {
        AST node = program();
        if (current.getType() != MyTokenType.EOF) {
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


}








