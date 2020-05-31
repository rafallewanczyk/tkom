package Parser;

import Lexer.MyLexer;
import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;
import Parser.AST_node.*;

import java.util.ArrayList;

public class MyParser {

    MyLexer lexer;
    MyToken current, tokenBuffer;
    public AST root; ///todo set private
//    Program program = new Program();

    public MyParser(MyLexer lexer) {
        this.lexer = lexer;
        current = lexer.nextToken();
//        tokenBuffer = new MyToken(MyTokenType.EMPTY, "empty", 0, 0);

    }

    public AST parse() {
        try {
            AST node = program();
            if (current.getType() != MyTokenType.EOF) {
                error("expected EOF got: " + current.getType() + " at " + current.getX() + ":" + current.getY());
            }
            return node;
        } catch (ParserException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }


    private void eat(MyTokenType type) throws ParserException {
        if (current.getType() == type) {
            current = lexer.nextToken();
            return;
        }

        error("got: " + current.getType() + " expected " + type + " at " + current.getX() + ":" + current.getY());

    }


    private void error(String message) throws ParserException {
        throw new ParserException(message);
    }

    private AST program() throws ParserException {
        AST node = new Program();
        while (current.getType() != MyTokenType.EOF) {
            ((Program) node).addFunction(function());
        }
        return node;
    }

    private AST function() throws ParserException {
        MyToken type = current;
        if (current.getType() == MyTokenType.INT || current.getType() == MyTokenType.ROM|| current.getType() == MyTokenType.REAL) {
            eat(type.getType());
        } else {
            error("expected some type got: " + current.getValue() + current + " at " + current.getX() + ":" + current.getY());
        }

        MyToken name = current;
        eat(MyTokenType.ID);
        AST parameters = function_parameters();

        return new FunDeclaration(name, new Type(type), parameters, compound_statement());
    }

    private AST function_parameters() throws ParserException {
        eat(MyTokenType.LEFT_PARENTESIS);
        if (current.getType() == MyTokenType.RIGHT_PARENTESIS) {
            eat(MyTokenType.RIGHT_PARENTESIS);
            return null;
        }

        MyToken type = current;
        if (type.getType() == MyTokenType.ROM|| type.getType() == MyTokenType.INT || type.getType() == MyTokenType.REAL) {
            eat(type.getType());
        } else {
            error("expected some type got: " + current.getValue() + current + " at " + current.getX() + ":" + current.getY());
        }
        MyToken id = current;
        eat(MyTokenType.ID);
        ArrayList<AST> parameters = new ArrayList<AST>();
        parameters.add(new Parameter(new Variable(id), new Type(type)));

        while (current.getType() == MyTokenType.COMMA) {
            eat(MyTokenType.COMMA);
            type = current;
            if (type.getType() == MyTokenType.ROMAN_NUMBER || type.getType() == MyTokenType.INT) {
                eat(type.getType());
            } else {
                error("expected some type got: " + current.getValue() + current + " at " + current.getX() + ":" + current.getY());
            }
            id = current;
            eat(MyTokenType.ID);
            parameters.add(new Parameter(new Variable(id), new Type(type)));
        }
        eat(MyTokenType.RIGHT_PARENTESIS);
        return new FunParameters(parameters);

    }

    private AST functionCall() throws ParserException {
        MyToken token = current;

        MyToken funName = current;
        eat(MyTokenType.ID);
        eat(MyTokenType.LEFT_PARENTESIS);

        ArrayList<AST> arguments = new ArrayList<AST>();
        if (current.getType() != MyTokenType.RIGHT_PARENTESIS) {
            AST node = assignable();
            arguments.add(node);
        }

        while (current.getType() == MyTokenType.COMMA) {
            eat(MyTokenType.COMMA);
            AST node = assignable();
            arguments.add(node);
        }

        eat(MyTokenType.RIGHT_PARENTESIS);

        return new FunCall(funName, arguments);

    }


    public AST compound_statement() throws ParserException { // todo set private
        eat(MyTokenType.LEFT_BRACE);
        ArrayList<AST> nodes = statement_list();
        eat(MyTokenType.RIGHT_BRACE);
        AST root = new Compound(nodes);
        return root;
    }

    private ArrayList<AST> statement_list() throws ParserException {
        ArrayList<AST> result = new ArrayList<AST>();

        while (current.getType() != MyTokenType.RIGHT_BRACE) {
            result.add(statement());
            eat(MyTokenType.SEMICOLLON);
        }


        return result;
    }

    private AST statement() throws ParserException {
        AST node;

        if (current.getType() == MyTokenType.ID && lexer.getCharacter() == '(') {
            node = functionCall();
            return node;
        }
        if (current.getType() == MyTokenType.ID) {
            node = assignmentStatement();
            return node;
        } else if (current.getType() == MyTokenType.INT || current.getType() == MyTokenType.REAL || current.getType() == MyTokenType.ROM) {
            node = initStatement();
            return node;
        } else if (current.getType() == MyTokenType.IF) {
            node = ifStatement();
            return node;
        } else if (current.getType() == MyTokenType.LOOP) {
            node = whileStatement();
            return node;
        } else if (current.getType() == MyTokenType.RETURN) {
            node = returnStatement();
            return node;
        }
        return null;
    }

    private AST returnStatement() throws ParserException {
        eat(MyTokenType.RETURN);

        return new ReturnStatement(assignable());
    }

    private AST assignable() throws ParserException {
        AST node;

        if (current.getType() == MyTokenType.ID && lexer.getCharacter() == '(') {
            node = functionCall();
        } else{
            node = expression();
        }
        return node;
    }

    private AST whileStatement() throws ParserException {
        eat(MyTokenType.LOOP);
        eat(MyTokenType.LEFT_PARENTESIS);
        AST condition = condition();
        eat(MyTokenType.RIGHT_PARENTESIS);
        AST trueBlock = compound_statement();
        return new WhileStatement(condition, trueBlock);
    }

    private AST ifStatement() throws ParserException {
        eat(MyTokenType.IF);
        eat(MyTokenType.LEFT_PARENTESIS);
        AST condition = condition();
        eat(MyTokenType.RIGHT_PARENTESIS);
        AST trueBlock = compound_statement();
        AST falseBlock = null;
        if (current.getType() == MyTokenType.ELSE) {
            eat(MyTokenType.ELSE);
            falseBlock = compound_statement();
        }
        return new IfStatement(condition, trueBlock, falseBlock);
    }

    private AST initStatement() throws ParserException {
        MyToken type = current;
        eat(type.getType());
        MyToken var = current;
        eat(MyTokenType.ID);

        AST node = null;

        if (current.getType() == MyTokenType.ASSIGNMENT_OP) {
            eat(MyTokenType.ASSIGNMENT_OP);
            return new VarDeclaration(new Variable(var), new Type(type), assignable());
        }

        return new VarDeclaration(new Variable(var), new Type(type), null);
    }

    private AST assignmentStatement() throws ParserException {
        MyToken left;
        AST right;
        left = current;
        eat(MyTokenType.ID);
        eat(MyTokenType.ASSIGNMENT_OP);
        right = assignable();
        return new AssignStatement(left, right);
    }


    public AST condition() throws ParserException { //todo set private
        AST node = andCondition();

        while (current.getType() == MyTokenType.OR_OP) {
            MyToken token = current;
            eat(MyTokenType.OR_OP);
            node = new BinLogicOperator(node, token, andCondition());
        }
        return node;
    }

    private AST andCondition() throws ParserException {
        AST node = equalityCondition();

        while (current.getType() == MyTokenType.AND_OP) {
            MyToken token = current;
            eat(MyTokenType.AND_OP);
            node = new BinLogicOperator(node, token, equalityCondition());
        }
        return node;
    }

    private AST equalityCondition() throws ParserException {
        AST node = relationalCondition();

        while (current.getType() == MyTokenType.EQUAL_OP) {
            MyToken token = current;
            eat(MyTokenType.EQUAL_OP);
            node = new BinLogicOperator(node, token, relationalCondition());
        }
        return node;
    }

    private AST relationalCondition() throws ParserException {
        AST node = primaryCondition();

        while (current.getType() == MyTokenType.RELATION_OP) {
            MyToken token = current;
            eat(MyTokenType.RELATION_OP);
            node = new BinLogicOperator(node, token, relationalCondition());
        }
        return node;
    }

    private AST primaryCondition() throws ParserException {
        MyToken token = current;

        if (token.getType() == MyTokenType.LEFT_PARENTESIS) {
            eat(MyTokenType.LEFT_PARENTESIS);
            AST node = condition();
            eat(MyTokenType.RIGHT_PARENTESIS);
            return node;
        } else {
            AST node = expression();
            return node;
        }

    }


    private AST factor() throws ParserException {
        MyToken token = current;

        if (token.getType() == MyTokenType.ADDITIVE_OP) {
            if (token.getValue().equals("-")) {
                eat(MyTokenType.ADDITIVE_OP);
                return new UnOperator(token, factor());
            }

            if (token.getValue().equals("+")) {
                eat(MyTokenType.ADDITIVE_OP);
                return new UnOperator(token, factor());
            }
        }
        if (current.getType() == MyTokenType.INT_NUMBER) {
            eat(MyTokenType.INT_NUMBER);
            return new IntNum(token);
        } else if (current.getType() == MyTokenType.REAL_NUMBER) {
            eat(MyTokenType.REAL_NUMBER);
            return new RealNum(token);
        } else if(current.getType() == MyTokenType.ROMAN_NUMBER){
            eat(MyTokenType.ROMAN_NUMBER);
            return new RomNum(token);
        }else if (token.getType() == MyTokenType.LEFT_PARENTESIS) {
            eat(MyTokenType.LEFT_PARENTESIS);
            AST node = expression();
            eat(MyTokenType.RIGHT_PARENTESIS);
            return node;
        } else if(current.getType() == MyTokenType.ID && lexer.getCharacter() == '('){
            AST node =functionCall();
            return node;
        }else {
            return variable();
        }
    }

    private AST term() throws ParserException {
        AST node = factor();

        while (current.getType() == MyTokenType.MULTIPLICATIVE_OP) {
            MyToken token = current;
            eat(MyTokenType.MULTIPLICATIVE_OP);
            node = new BinOperator(node, token, factor());
        }
        return node;
    }

    public AST expression() throws ParserException {//todo set privaate
        AST node = term();

        while (current.getType() == MyTokenType.ADDITIVE_OP) {
            MyToken token = current;
            eat(MyTokenType.ADDITIVE_OP);
            node = new BinOperator(node, token, term());
        }
        return node;
    }


    private AST variable() throws ParserException {
        AST node = new Variable(current);
        eat(MyTokenType.ID);
        return node;

    }


    public void allTokens() {
        while (current.getType() != MyTokenType.EOF) {
            System.out.println(current);
            current = lexer.nextToken();
        }
    }


}








