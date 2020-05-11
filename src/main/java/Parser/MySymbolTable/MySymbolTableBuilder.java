package Parser.MySymbolTable;

import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;
import Parser.AST;
import Parser.AST_node.*;

public class MySymbolTableBuilder {
    MySymbolTable symbolTable = new MySymbolTable();
    AST root;

    public MySymbolTableBuilder(AST root) {
        this.root = root;
        visit(root);
    }

    void visit(AST node) {
        if (node instanceof BinOperator) {
            BinOperator casted = (BinOperator) node;
            visit(casted.left);
            visit(casted.right);

        } else if (node instanceof Num) {
            return;
        } else if (node instanceof UnOperator) {
            UnOperator casted = (UnOperator) node;
            visit(casted.getExpression());
        } else if (node instanceof Compound) {
            Compound casted = (Compound) node;
            for (AST child : casted.getChildren()) {
                visit(child);
            }
        } else if (node instanceof AssignStatement) {
            AssignStatement casted = (AssignStatement) node;
            MyToken varName = casted.getToken();
            Symbol test = symbolTable.lookupSymbol(varName);
            if (test == null) {
                System.out.println("unknown variable name " + varName);
                return;
            }
            visit(casted.getValue());

        } else if (node instanceof Variable) {
            Variable casted = (Variable) node;
            MyToken varName = casted.getToken();
            Symbol test = symbolTable.lookupSymbol(varName);
            if (test == null) {
                System.out.println("unknown variable name " + varName);
                return;
            }

        } else if (node instanceof VarDeclaration) {
            VarDeclaration casted = (VarDeclaration) node;
            MyToken type = ((Type) casted.getType()).getToken();
            Symbol typeSybmol = symbolTable.lookupSymbol(type);
            //todo handle unknown type exception
            MyToken varName = ((Variable) casted.getVariable()).getToken();
            symbolTable.defineSymbol(new VarSymbol(varName, type));
        } else if (node instanceof BinLogicOperator) {
            BinLogicOperator casted = (BinLogicOperator) node;
            visit(casted.left);
            visit(casted.right);

        } else if (node instanceof WhileStatement) {
            WhileStatement casted = (WhileStatement) node;
            visit(casted.getCondition()); 
            visit(casted.getTrueCompound());
        } else if (node instanceof IfStatement) {
            IfStatement casted = (IfStatement) node;
            visit(casted.getCondition());
            visit(casted.getTrueCompound());
            visit(casted.getFalseCompound());
        } else if (node instanceof Program) {
            Program casted = (Program) node;
            for (AST f : casted.getFunctions()) {
                visit(((FunDeclaration) f).getCompound());
            }

        }
    }
}
