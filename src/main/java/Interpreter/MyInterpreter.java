package Interpreter;

import Interpreter.CallStack.ActivationRecord;
import Interpreter.CallStack.ActivationType;
import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;
import Parser.AST;
import Parser.AST_node.*;
import Parser.MySymbolTable.FunctionSymbol;
import Parser.MySymbolTable.Symbol;

import java.util.*;


public class MyInterpreter {
    AST root;
    ArrayDeque<ActivationRecord> callStack = new ArrayDeque<>();
    boolean logStack = true;


    private ActivationRecord searchInStack(MyToken name) {
        Iterator<ActivationRecord> iter = callStack.iterator();
        while (iter.hasNext()) {
            try {
                ActivationRecord ret = iter.next();
                ret.getItem(name);
                return ret;
            } catch (NullPointerException e) {
                continue;
            }

        }
        return null;
    }

    public MyInterpreter(AST root) {
        this.root = root;
        visit(root);
    }

    private void log(String msg) {
        if (logStack) {
            System.out.println(msg);
        }
    }

    AST visit(AST node) {
        if (node instanceof BinOperator) {
            BinOperator casted = (BinOperator) node;
            AST left = visit(casted.left);
            AST right = visit(casted.right);

            if (left instanceof IntNum) {
                IntNum leftCasted = (IntNum) left;
                IntNum rightCasted = (IntNum) right;

                if (casted.operation.getValue().equals("+")) {
                    return new IntNum(leftCasted.value + rightCasted.value);
                }
                if (casted.operation.getValue().equals("-")) {
                    return new IntNum(leftCasted.value - rightCasted.value);
                }
                if (casted.operation.getValue().equals("/")) {
                    return new IntNum(leftCasted.value / rightCasted.value);
                }
                if (casted.operation.getValue().equals("*")) {
                    return new IntNum(leftCasted.value * rightCasted.value);
                }
            }

            if (left instanceof RealNum) {
                RealNum leftCasted = (RealNum) left;
                RealNum rightCasted = (RealNum) right;

                if (casted.operation.getValue().equals("+")) {
                    return new RealNum(leftCasted.value + rightCasted.value);
                }
                if (casted.operation.getValue().equals("-")) {
                    return new RealNum(leftCasted.value - rightCasted.value);
                }
                if (casted.operation.getValue().equals("/")) {
                    return new RealNum(leftCasted.value / rightCasted.value);
                }
                if (casted.operation.getValue().equals("*")) {
                    return new RealNum(leftCasted.value * rightCasted.value);
                }
            }
        }
//
//            if (casted.operation.getValue().equals("+")) {
//                return visit(casted.left) + visit(casted.right);
//            } else if (casted.operation.getValue().equals("-")) {
//                return visit(casted.left) - visit(casted.right);
//            } else if (casted.operation.getValue().equals("*")) {
//                return visit(casted.left) * visit(casted.right);
//            } else if (casted.operation.getValue().equals("/")) {
//                return visit(casted.left) / visit(casted.right);
//            }

        else if (node instanceof IntNum) {
            IntNum casted = (IntNum) node;
            return casted;

        } else if (node instanceof RealNum) {
            RealNum casted = (RealNum) node;
            return casted;
        } else if (node instanceof UnOperator) {
            UnOperator casted = (UnOperator) node;
            if (casted.getType().equals("-")) {
                AST value = visit(casted.getExpression());

                if (value instanceof IntNum) {
                    IntNum ret = new IntNum(-((IntNum) value).value);
                    return ret;
                }
                if (value instanceof RealNum) {
                    RealNum ret = new RealNum(-((RealNum) value).value);
                    return ret;
                }
            } else {
                return visit(casted.getExpression());
            }
        } else if (node instanceof Compound) {
            Compound casted = (Compound) node;
            for (AST child : casted.getChildren()) {
                if (child instanceof ReturnStatement)
                    return visit(child);
                else {
                    visit(child);
                }
            }

        } else if (node instanceof AssignStatement) {
            AssignStatement casted = (AssignStatement) node;
            MyToken token = casted.getToken();
            ActivationRecord ar = searchInStack(token);
            ar.pushItem(token, visit(casted.getValue()));

        } else if (node instanceof Variable) {
            Variable casted = (Variable) node;
            MyToken token = casted.getToken();
            ActivationRecord ar = searchInStack(token);
            AST val = ar.getItem(token);
            return val;

        } else if (node instanceof VarDeclaration) {
            VarDeclaration casted = (VarDeclaration) node;
            MyToken token = ((Variable) (casted.getVariable())).getToken();
            MyTokenType type = ((Type) casted.getType()).getToken().getType();
            ActivationRecord ar = callStack.peek();
            if (casted.getExpression() == null) {
                if (((Type) casted.getType()).getToken().getType() == MyTokenType.INT)
                    ar.pushItem(token, new IntNum(0));
                if (((Type) casted.getType()).getToken().getType() == MyTokenType.REAL)
                    ar.pushItem(token, new RealNum(0));
            } else {
                AST result = visit(casted.getExpression());
                if (((Type) casted.getType()).getToken().getType() == MyTokenType.INT)
                    ar.pushItem(token, (IntNum) result);
                if (((Type) casted.getType()).getToken().getType() == MyTokenType.REAL)
                    ar.pushItem(token, (RealNum) result);
            }
        } else if (node instanceof BinLogicOperator) {
            BinLogicOperator casted = (BinLogicOperator) node;
            var left = visit(casted.left);
            AST right = visit(casted.right);



            if (left instanceof IntNum) {
                IntNum castedLeft = (IntNum) left;
            }
            if (left instanceof RealNum) {
                RealNum castedLeft = (RealNum) left;
            }
            if (right instanceof IntNum) {
                IntNum castedRight = (IntNum) right;
            }
            if (right instanceof RealNum) {
                RealNum castedRight = (RealNum) left;
            }

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
//        } else if (node instanceof IfStatement) {
//            IfStatement casted = (IfStatement) node;
//            int condition = visit(casted.getCondition());
//
//            ActivationRecord ar = new ActivationRecord(new MyToken(MyTokenType.IF, "if"), ActivationType.IF, callStack.peek().getNestingLevel() + 1);
//            log("Entering " + ar.getName());
//            callStack.push(ar);
//
//            if (condition == 1) {
//                visit(casted.getTrueCompound());
//            } else if (casted.getFalseCompound() != null) {
//                visit(casted.getFalseCompound());
//            }
//            log("Leaving " + ar.getName());
//            log(callStack.peek().toString());
//            callStack.pop();
//
//        } else if (node instanceof WhileStatement) {
//            //todo callstack implementation
//            WhileStatement casted = (WhileStatement) node;
//            while (visit(casted.getCondition()) == 1) {
//                visit(casted.getTrueCompound());
//            }
        } else if (node instanceof ReturnStatement) {
            ReturnStatement casted = (ReturnStatement) node;
            AST eval = visit(casted.getRetValue());
            return eval;

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
                return null;
            } else {
                visit(((FunDeclaration) main).getCompound());
            }

            log("Leaving program");
            log(ar.toString());
            callStack.pop();
            return null;


        } else if (node instanceof FunCall) {
            FunCall casted = (FunCall) node;

            Symbol function = casted.getFunSymbol();
            ActivationRecord ar = new ActivationRecord(casted.getName(), ActivationType.FUNCION, function.scopeLevel + 1);

            ArrayList<Symbol> formal_params = ((FunctionSymbol) function).getParams();
            ArrayList<AST> arguments = casted.getArguments();

            for (int i = 0; i < formal_params.size(); i++) {
                ar.pushItem(formal_params.get(i).name, visit(arguments.get(i)));
            }
            log("Entering procedure " + casted.getName());
            callStack.push(ar);

            AST retVal = visit(((FunctionSymbol) function).getBody());

            log("Leaving procedure " + casted.getName());
            log(callStack.peek().toString());
            callStack.pop();

            return retVal;
        }
        return null;
    }

}
