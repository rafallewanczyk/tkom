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
            if (casted.getExpression() == null) {
                GLOBAL_SCOPE.put(token, 0);
            } else {
                GLOBAL_SCOPE.put(token, visit(casted.getExpression()));
            }
        } else if (node instanceof BinLogicOperator) {
            BinLogicOperator casted = (BinLogicOperator) node;
            if (casted.operation.getValue().equals("||")) {
                return (visit(casted.left) != 0 || visit(casted.right) != 0) ? 1 : 0;
            }
            if (casted.operation.getValue().equals("&&")) {
                return (visit(casted.left) != 0 && visit(casted.right) != 0) ? 1 : 0;
            }
            if (casted.operation.getValue().equals("==")) {
                return (visit(casted.left) == visit(casted.right)) ? 1 : 0;
            }
            if (casted.operation.getValue().equals("!=")) {
                return (visit(casted.left) != visit(casted.right)) ? 1 : 0;
            }
            if (casted.operation.getValue().equals(">")) {
                return (visit(casted.left) > visit(casted.right)) ? 1 : 0;
            }

            if (casted.operation.getValue().equals("<")) {
                return (visit(casted.left) < visit(casted.right)) ? 1 : 0;
            }

            if (casted.operation.getValue().equals(">=")) {
                return (visit(casted.left) >= visit(casted.right)) ? 1 : 0;
            }

            if (casted.operation.getValue().equals("!=")) {
                return (visit(casted.left) != visit(casted.right)) ? 1 : 0;
            }

            if (casted.operation.getValue().equals("<=")) {
                return (visit(casted.left) <= visit(casted.right)) ? 1 : 0;
            }
        } else if (node instanceof IfStatement) {
            IfStatement casted = (IfStatement) node;
            int condition = visit(casted.getCondition());
            if (condition == 1) {
                return visit(casted.getTrueCompound());
            } else if (casted.getFalseCompound() != null) {
                return visit(casted.getFalseCompound());
            }

        }else if (node instanceof WhileStatement){
            WhileStatement casted = (WhileStatement)node;
            while(visit(casted.getCondition())== 1){
                visit(casted.getTrueCompound());
            }
        } else if(node instanceof Program){
            Program casted = (Program)node;
            AST main = null ;
            for(AST f : casted.getFunctions()){
                if(((FunDeclaration)f).getName().getValue().equals("main")){
                    main = f;
                }
            }

            if(main == null){
                return -1;
            }else{
                visit(((FunDeclaration)main).getCompound());
            }

        }


        return 0;

    }

    public HashMap<MyToken, Integer> results() {
        GLOBAL_SCOPE.entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
        return GLOBAL_SCOPE;
    }

}
