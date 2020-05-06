package Interpreter;

import Lexer.Token.MyToken;
import Parser.AST;
import Parser.AST_node.*;

import java.util.HashMap;


public class MyInterpreter {
    AST root;
    int out;
    HashMap<MyToken, Integer> GLOBAL_SCOPE = new HashMap<MyToken, Integer>();

    public int getOut() {
        return out;
    }

    public MyInterpreter(AST root) {
        this.root = root;
        out = visit(root);
    }

    int visit(AST node) {
        if (node instanceof BinOperator) {
            BinOperator casted = (BinOperator) node;
            if (casted.operation.getValue().equals("+")) {
                return visit(casted.left) + visit(casted.right);
            } else if (casted.operation.getValue().equals("-")) {
                return visit(casted.left) - visit(casted.right);
            } else if (casted.operation.getValue().equals("*")) {
                return visit(casted.left) * visit(casted.right);
            } else if (casted.operation.getValue().equals("/")) {
                return visit(casted.left) / visit(casted.right);
            }

        } else if (node instanceof Num) {
            Num casted = (Num) node;
            return casted.value;
        } else if (node instanceof UnOperator) {
            UnOperator casted = (UnOperator) node;
            if (casted.getType().equals("-")) {
                return -visit(casted.getExpression());
            } else {
                return visit(casted.getExpression());
            }
        } else if (node instanceof Compound) {
            Compound casted = (Compound) node;
            for (AST child : casted.getChildren()) {
                visit(child);
            }
        } else if (node instanceof AssignStatement) {
            AssignStatement casted = (AssignStatement) node;
            MyToken token = casted.getToken();
            GLOBAL_SCOPE.put(token, visit(casted.getValue()));
        } else if (node instanceof Variable) {
            Variable casted = (Variable) node;
            MyToken token = casted.getToken();
            int val = GLOBAL_SCOPE.get(token);

            return val;
        } else if (node instanceof VarDeclaration) {
            VarDeclaration casted = (VarDeclaration) node;
            MyToken token = ((Variable) (casted.getVariable())).getToken();
            if(casted.getExpression() == null){
                GLOBAL_SCOPE.put(token, 0);
            }else{
                GLOBAL_SCOPE.put(token, visit(casted.getExpression()));
            }
        } else if(node instanceof BinLogicOperator){
            BinLogicOperator casted = (BinLogicOperator)node;
            if(casted.operation.getValue().equals("||")){
                return (visit(casted.left) != 0 || visit(casted.right) !=0) ? 1 : 0;
            }
            if(casted.operation.getValue().equals("&&")){
                return (visit(casted.left) != 0 && visit(casted.right) !=0) ? 1 : 0;
            }
            if(casted.operation.getValue().equals("==")){
                return (visit(casted.left) == visit(casted.right)) ? 1 : 0;
            }
            if(casted.operation.getValue().equals(">")){
                return (visit(casted.left) > visit(casted.right)) ? 1 : 0;
            }

            if(casted.operation.getValue().equals("<")){
                return (visit(casted.left) < visit(casted.right)) ? 1 : 0;
            }

            if(casted.operation.getValue().equals(">=")){
                return (visit(casted.left) >= visit(casted.right)) ? 1 : 0;
            }

            if(casted.operation.getValue().equals("!=")){
                return (visit(casted.left) != visit(casted.right)) ? 1 : 0;
            }

            if(casted.operation.getValue().equals("<=")){
                return (visit(casted.left) <= visit(casted.right)) ? 1 : 0;
            }
        } else if(node instanceof Bool){
            Bool casted = (Bool)node;
            return casted.isValue() ? 1 : 0;
        }

        return 0;

    }

    public void results() {
        GLOBAL_SCOPE.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
    }

}
