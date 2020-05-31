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
    AST output;

    public MyInterpreter(AST root, boolean log) {
        this.root = root;
        this.logStack = log;
        try {
            output = visit(root);
        } catch (InterpreterException e) {
            System.out.println(e.getMessage());
            output = null;
        }
    }

    public AST getOutput() {
        return output;
    }

    private void log(String msg) {
        if (logStack) {
            System.out.println(msg);
        }
    }

    private boolean typeComparison(String a, MyTokenType b){
        if(a.equals("IntNum") && b == MyTokenType.INT){
            return true;
        }
        if(a.equals("RealNum") && b == MyTokenType.REAL){
            return true;
        }
        if(a.equals("RomNum") && b == MyTokenType.ROM){
            return true;
        }
        return false;
    }
    private ActivationRecord searchInStack(MyToken name) {
        Iterator<ActivationRecord> iter = callStack.iterator();
        while (iter.hasNext()) {
            ActivationRecord ret = iter.next();
            if (ret.getItem(name) != null)
                return ret;


        }
        return null;
    }

    AST visit(AST node) throws InterpreterException {
        if (node instanceof BinOperator) {
            BinOperator casted = (BinOperator) node;
            AST left = visit(casted.left);
            AST right = visit(casted.right);

            if (left instanceof IntNum && right instanceof IntNum) {
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
            } else if (left instanceof RealNum && right instanceof RealNum) {
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
            } else if (left instanceof RomNum && right instanceof RomNum) {
                RomNum leftCasted = (RomNum) left;
                RomNum rightCasted = (RomNum ) right;

                if (casted.operation.getValue().equals("+")) {
                    return new RomNum(leftCasted.value + rightCasted.value);
                }
                if (casted.operation.getValue().equals("-")) {
                    return new RomNum(leftCasted.value - rightCasted.value);
                }
                if (casted.operation.getValue().equals("/")) {
                    return new RomNum(leftCasted.value / rightCasted.value);
                }
                if (casted.operation.getValue().equals("*")) {
                    return new RomNum(leftCasted.value * rightCasted.value);
                }
            } else {
                throw new InterpreterException("No matching types at " + casted.operation.getX() +  " : " + casted.operation.getY());
            }


        } else if (node instanceof IntNum) {
            return (IntNum) node;

        } else if (node instanceof RealNum) {
            return (RealNum) node;

        } else if (node instanceof RomNum) {
            return (RomNum) node;

        } else if (node instanceof UnOperator) {
            UnOperator casted = (UnOperator) node;
            if (casted.getType().equals("-")) {
                AST value = visit(casted.getExpression());

                if (value instanceof IntNum) {
                    return new IntNum(-((IntNum) value).value);
                }
                if (value instanceof RealNum) {
                    return new RealNum(-((RealNum) value).value);
                }
                if(value instanceof RomNum){
                    return new RomNum(-((RomNum) value).value);
                }
            } else {
                return visit(casted.getExpression());
            }
        } else if (node instanceof Compound) {
            Compound casted = (Compound) node;
            for (AST child : casted.getChildren()) {
               AST ret = visit(child);
               if(ret != null)
                   return ret;
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
            return ar.getItem(token);

        } else if (node instanceof VarDeclaration) {
            VarDeclaration casted = (VarDeclaration) node;
            MyToken token = ((Variable) (casted.getVariable())).getToken();
            MyTokenType type = ((Type) casted.getType()).getToken().getType();
            ActivationRecord ar = callStack.peek();
            if (casted.getExpression() == null) {
                if (type == MyTokenType.INT)
                    ar.pushItem(token, new IntNum(0));
                if (type == MyTokenType.REAL)
                    ar.pushItem(token, new RealNum(0));
                if (type == MyTokenType.ROM)
                    ar.pushItem(token, new RomNum(1));
            } else {
                AST result = visit(casted.getExpression());
                MyTokenType variableType = ((Type) casted.getType()).getToken().getType();
                if (variableType == MyTokenType.INT && result.getClass().equals(IntNum.class)) {
                    ar.pushItem(token, (IntNum) result);
                } else if (variableType == MyTokenType.REAL && result.getClass().equals(RealNum.class)) {
                    ar.pushItem(token, (RealNum) result);
                } else if (variableType == MyTokenType.ROM && result.getClass().equals(RomNum.class)) {
                    ar.pushItem(token, (RomNum) result);
                } else {
                    throw new InterpreterException("Types doesn't match at " + token.getX() + " : " + token.getY());
                }
            }
        } else if (node instanceof BinLogicOperator) {
            BinLogicOperator casted = (BinLogicOperator) node;
            AST left = visit(casted.left);
            AST right = visit(casted.right);


            if (left instanceof BoolNum && !(right instanceof BoolNum) || right instanceof BoolNum && !(left instanceof BoolNum)) {
                throw new InterpreterException("incorrect condition at " + casted.operation.getX() + " : " + casted.operation.getY());
            }
            if (left instanceof BoolNum && right instanceof BoolNum) {
                BoolNum castedLeft = (BoolNum) left;
                BoolNum castedRight = (BoolNum) right;

                if (casted.operation.getValue().equals("||")) {
                    return new BoolNum(castedLeft.value || castedRight.value);
                }
                if (casted.operation.getValue().equals("&&")) {
                    return new BoolNum(castedLeft.value && castedRight.value);
                }
            }

            RealNum castedLeft = new RealNum(0);
            RealNum castedRight = new RealNum(0);

            if (left instanceof RealNum) {
                castedLeft.value = (double) ((RealNum) left).value;
            }
            if (right instanceof RealNum) {
                castedRight.value = (double) ((RealNum) right).value;
            }
            if (left instanceof IntNum) {
                castedLeft.value = (double) ((IntNum) left).value;
            }
            if (right instanceof IntNum) {
                castedRight.value = (double) ((IntNum) right).value;
            }
            if (left instanceof RomNum) {
                castedLeft.value = (double) (( RomNum) left).value;
            }
            if (right instanceof RomNum) {
                castedRight.value = (double) (( RomNum) right).value;
            }

            if (casted.operation.getValue().equals("==")) {

                return (castedLeft.value.equals(castedRight.value)) ? new BoolNum(true) : new BoolNum(false);
            }
            if (casted.operation.getValue().equals("!=")) {
                return (!castedLeft.value.equals(castedRight.value)) ? new BoolNum(true) : new BoolNum(false);
            }
            if (casted.operation.getValue().equals(">")) {
                return (castedLeft.value > castedRight.value) ? new BoolNum(true) : new BoolNum(false);
            }

            if (casted.operation.getValue().equals("<")) {
                return (castedLeft.value < castedRight.value) ? new BoolNum(true) : new BoolNum(false);
            }

            if (casted.operation.getValue().equals(">=")) {
                return (castedLeft.value >= castedRight.value) ? new BoolNum(true) : new BoolNum(false);
            }

            if (casted.operation.getValue().equals("<=")) {
                return (castedLeft.value <= castedRight.value) ? new BoolNum(true) : new BoolNum(false);
            }

        } else if (node instanceof IfStatement) {
            IfStatement casted = (IfStatement) node;
            BoolNum condition = (BoolNum) visit(casted.getCondition());

            ActivationRecord ar = new ActivationRecord(new MyToken(MyTokenType.IF, "if"), ActivationType.IF, callStack.peek().getNestingLevel() + 1);
            log("Entering " + ar.getName());
            callStack.push(ar);
            AST retVal = null;
            if (condition.value) {
                retVal = visit(casted.getTrueCompound());
            } else if (casted.getFalseCompound() != null) {
                retVal = visit(casted.getFalseCompound());
            }
            log("Leaving " + ar.getName());
            log(callStack.peek().toString());
            callStack.pop();
            return retVal;

        } else if (node instanceof WhileStatement) {
            WhileStatement casted = (WhileStatement) node;
            AST ret = null;
            while (((BoolNum) visit(casted.getCondition())).value) {
                ActivationRecord ar = new ActivationRecord(new MyToken(MyTokenType.LOOP, "while"), ActivationType.LOOP, callStack.peek().getNestingLevel() + 1);
                callStack.push(ar);
                ret = visit(casted.getTrueCompound());
                callStack.pop();
                if(ret != null)
                    return ret ;
            }
        } else if (node instanceof ReturnStatement) {
            ReturnStatement casted = (ReturnStatement) node;
            return visit(casted.getRetValue());

        } else if (node instanceof Program) {
            Program casted = (Program) node;
            MyToken name = new MyToken(MyTokenType.ID, "program");
            ActivationRecord ar = new ActivationRecord(name, ActivationType.PROGRAM, 1);
            log("Enter Program");
            callStack.push(ar);
            log(ar.toString());
            AST main = null;
            AST retVal;
            for (AST f : casted.getFunctions()) {
                if (((FunDeclaration) f).getName().getValue().equals("main")) {
                    main = f;
                }
            }

            if (main == null) {
                return null;
            } else {
                retVal = visit(((FunDeclaration) main).getCompound());

            }
            MyTokenType returnType =((Type)((FunDeclaration)main).getType()).getToken().getType();
            if(!typeComparison(retVal.getClass().getSimpleName(), returnType)){
                throw new InterpreterException("incorrect return type at " + 0  + " : " + 0);
            }

            log("Leaving program");
            log(ar.toString());
            callStack.pop();
            return retVal;


        } else if (node instanceof FunCall) {
            FunCall casted = (FunCall) node;

            Symbol function = casted.getFunSymbol();
            MyTokenType a = function.type.getType();
            ActivationRecord ar = new ActivationRecord(casted.getName(), ActivationType.FUNCION, function.scopeLevel + 1);

            ArrayList<Symbol> formal_params = ((FunctionSymbol) function).getParams();
            ArrayList<AST> arguments = casted.getArguments();

            if(formal_params.size() != arguments.size()){
                throw new InterpreterException("incorrect number of arguments at " + casted.getName().getX() + " : " + casted.getName().getY());
            }

            for (int i = 0; i < formal_params.size(); i++) {
                if(!typeComparison(visit(arguments.get(i)).getClass().getSimpleName(),formal_params.get(i).type.getType())){
                   throw new InterpreterException("incorrect argument type at " + casted.getName().getX() + " : " + casted.getName().getY());
                }
                ar.pushItem(formal_params.get(i).name, visit(arguments.get(i)));
            }
            log("Entering procedure " + casted.getName());
            callStack.push(ar);

            AST retVal = visit(((FunctionSymbol) function).getBody());
            if(!typeComparison(retVal.getClass().getSimpleName(), function.type.getType())){
                throw new InterpreterException("incorrect return type at " + function.name.getX()  + " : " +function.name.getY());
            }

            log("Leaving procedure " + casted.getName());
            log(callStack.peek().toString());
            callStack.pop();

            return retVal;
        }
        return null;
    }

}
