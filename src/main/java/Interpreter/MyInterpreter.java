package Interpreter;

import Interpreter.CallStack.ActivationRecord;
import Interpreter.CallStack.ActivationType;
import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;
import Parser.AST;
import Parser.AST_node.*;
import Parser.MySymbolTable.FunctionSymbol;
import Parser.MySymbolTable.Symbol;

import java.util.ArrayList;
import java.util.Stack;


public class MyInterpreter {
    AST root;
    int out;
    Stack<ActivationRecord> callStack = new Stack<>();
    boolean logStack = true;

    public int getOut() {
        return out;
    }

    public MyInterpreter(AST root) {
        this.root = root;
        out = visit(root);
    }

    private void log(String msg) {
        if (logStack) {
            System.out.println(msg);
        }
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
            ActivationRecord ar = callStack.peek();
            ar.pushItem(token, visit(casted.getValue()));
        } else if (node instanceof Variable) {
            Variable casted = (Variable) node;
            MyToken token = casted.getToken();
            ActivationRecord ar = callStack.peek();
            int val = ar.getItem(token);

            return val;
        } else if (node instanceof VarDeclaration) {
            VarDeclaration casted = (VarDeclaration) node;
            MyToken token = ((Variable) (casted.getVariable())).getToken();
            ActivationRecord ar = callStack.peek();
            if (casted.getExpression() == null) {
                ar.pushItem(token, 0);
            } else {
                ar.pushItem(token, visit(casted.getExpression()));
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

        } else if (node instanceof WhileStatement) {
            WhileStatement casted = (WhileStatement) node;
            while (visit(casted.getCondition()) == 1) {
                visit(casted.getTrueCompound());
            }
        } else if (node instanceof Program) {
            Program casted = (Program) node;
            MyToken name = new MyToken(MyTokenType.ID, "program");
            ActivationRecord ar = new ActivationRecord(name, ActivationType.PROGRAM, 1);
            log("Enter Program");
            callStack.push(ar);
            log(ar.toString());
            AST main = null;
            for (AST f : casted.getFunctions()) {
                if (((FunDeclaration) f).getName().getValue().equals("main")) {
                    main = f;
                }
            }

            if (main == null) {
                return -1;
            } else {
                visit(((FunDeclaration) main).getCompound());
            }

            log("Leaving program");
            log(ar.toString());
            callStack.pop();



        } else if (node instanceof FunCall) {
            FunCall casted = (FunCall) node;

            ActivationRecord ar = new ActivationRecord(casted.getName(), ActivationType.FUNCION, callStack.peek().getNestingLevel() + 1);

            Symbol function = casted.getFunSymbol();
            ArrayList<Symbol> formal_params = ((FunctionSymbol) function).getParams();
            ArrayList<AST> arguments = casted.getArguments();

            for (int i = 0; i < formal_params.size(); i++) {
                ar.pushItem(formal_params.get(i).name, visit(arguments.get(i)));
            }
            log("Entering procedure " + casted.getName());
            callStack.push(ar);

            visit(((FunctionSymbol) function).getBody());

            log("Leaving procedure " + casted.getName());
            log(callStack.peek().toString());
            callStack.pop();

            return 0;
        }
        return 0;

    }

//    public HashMap<MyToken, Integer> results() {
//        GLOBAL_SCOPE.entrySet().forEach(entry -> {
//            System.out.println(entry.getKey() + " " + entry.getValue());
//        });
//        return GLOBAL_SCOPE;
//    }

}
