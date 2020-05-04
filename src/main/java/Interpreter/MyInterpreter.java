package Interpreter;

import Parser.AST;
import Parser.AST_node.*;




public class MyInterpreter {
    AST root;

    public MyInterpreter(AST root) {
        this.root = root;
        System.out.println(visit(root));
    }

    int visit(AST node) {
        if (node instanceof BinOperator) {
            BinOperator casted = (BinOperator) node;
            System.out.println(casted.operation);
            if (casted.operation.getValue().equals("+")) {
                return visit(casted.left) + visit(casted.right);
            }

            if (casted.operation.getValue().equals("-")) {
                return visit(casted.left) - visit(casted.right);
            }

            if (casted.operation.getValue().equals("*")) {
                return visit(casted.left) * visit(casted.right);
            }

            if (casted.operation.getValue().equals("/")) {
                return visit(casted.left) / visit(casted.right);
            }

        }
        if (node instanceof Num) {
            Num casted = (Num) node;
            System.out.println(casted.value);
            return casted.value;
        }

        if (node instanceof UnOperator) {
            UnOperator casted = (UnOperator) node;
            if (casted.getType().equals("-")) {
                return -visit(casted.getExpression());
            } else {
                return visit(casted.getExpression());
            }
        }
        return 0;

    }

}
