package Interpreter;

import Parser.AST;
import Parser.BinOperator;
import Parser.Num;

public class MyInterpreter {
    AST root;
    public MyInterpreter(AST root){
        this.root = root;
    }
    int visit(AST node) {
        int ret = 0;
        if (node instanceof BinOperator) {

            System.out.println(((BinOperator) node).operation);
            if (((BinOperator) node).operation.getValue().equals("+")) {
                ret = visit(((BinOperator) node).left) + visit(((BinOperator) node).right);
            }

            if (((BinOperator) node).operation.getValue().equals("-")) {
                ret = visit(((BinOperator) node).left) - visit(((BinOperator) node).right);
            }

            if (((BinOperator) node).operation.getValue().equals("*")) {
                ret = visit(((BinOperator) node).left) * visit(((BinOperator) node).right);
            }

            if (((BinOperator) node).operation.getValue().equals("/")) {
                ret = visit(((BinOperator) node).left) / visit(((BinOperator) node).right);
            }

        }
        if (node instanceof Num) {
            System.out.println(((Num) node).value);
            ret = ((Num) node).value;
        }
        return ret;
    }

}
