package Parser.MySymbolTable;

import Lexer.Token.MyToken;
import Lexer.Token.MyTokenType;
import Parser.AST;
import Parser.AST_node.*;

import java.util.ArrayList;

public class MySymbolTableBuilder {
    AST root;
    MyScopedSymbolTable currentScope = null;

    public MySymbolTableBuilder(AST root) {
        this.root = root;
        visit(root);
    }

    public AST getRoot() {
        return root;
    }

    void visit(AST node) throws SemanticException {
        if (node instanceof BinOperator) {
            BinOperator casted = (BinOperator) node;
            visit(casted.left);
            visit(casted.right);

        } else if (node instanceof IntNum) {
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
            Symbol test = currentScope.lookupSymbol(varName);
            if (test == null) {
                throw new SemanticException("unknown variable name" + varName + " at " + varName.getX() + ":"+varName.getY());
            }
            visit(casted.getValue());

        } else if (node instanceof Variable) {
            Variable casted = (Variable) node;
            MyToken varName = casted.getToken();
            Symbol test = currentScope.lookupSymbol(varName);
            if (test == null) {
                throw new SemanticException("unknown variable name" + varName + " at " + varName.getX() + ":"+varName.getY());
            }

        } else if (node instanceof VarDeclaration) {
            VarDeclaration casted = (VarDeclaration) node;
            MyToken type = ((Type) casted.getType()).getToken();
            Symbol typeSybmol = currentScope.lookupSymbol(type);
            //todo handle unknown type exception
            MyToken varName = ((Variable) casted.getVariable()).getToken();
            if (currentScope.lookupSymbol(varName) != null) {
                throw new SemanticException("redeclaration of variable" +  varName + " at " + varName.getX() + ":"+varName.getY());
            }
            currentScope.defineSymbol(new VarSymbol(varName, type));
            visit(casted.getExpression());
        } else if (node instanceof BinLogicOperator) {
            BinLogicOperator casted = (BinLogicOperator) node;
            visit(casted.left);
            visit(casted.right);

        } else if (node instanceof WhileStatement) {
            //todo semantic analysis
            WhileStatement casted = (WhileStatement) node;
            visit(casted.getCondition());
            visit(casted.getTrueCompound());
        } else if (node instanceof IfStatement) {
            IfStatement casted = (IfStatement) node;

            MyScopedSymbolTable scope = new MyScopedSymbolTable(new MyToken(MyTokenType.IF, "if"), currentScope.getScopeLevel() +1, currentScope);
            currentScope = scope;
            visit(casted.getCondition());
            System.out.println("ENTER " + currentScope.getScopeName());

            visit(casted.getTrueCompound());
            visit(casted.getFalseCompound());
            currentScope.print();

            System.out.println("LEAVE " + currentScope.getScopeName());
            currentScope = scope.getEnclosingScope();
        } else if (node instanceof Program) {
            currentScope = new MyScopedSymbolTable(new MyToken(MyTokenType.ID, "GLOBAL"), 0, null);
            currentScope.addBuiltins();
            System.out.println("ENTER " + currentScope.getScopeName());
            Program casted = (Program) node;
            for (AST f : casted.getFunctions()) {
                visit(f);
            }
            currentScope.print();
            System.out.println("LEAVE "+currentScope.getScopeName());


        } else if (node instanceof FunDeclaration) {
            FunDeclaration casted = (FunDeclaration) node;
            MyToken funName = casted.getName();
            MyToken type = ((Type) casted.getType()).getToken();
            FunctionSymbol functionSymbol = new FunctionSymbol(funName, type);
            functionSymbol.setBody(casted.getCompound());
            currentScope.defineSymbol(functionSymbol);
            MyScopedSymbolTable funScope = new MyScopedSymbolTable(funName, currentScope.getScopeLevel()+ 1, currentScope);
            currentScope = funScope;
            System.out.println("ENTER " + currentScope.getScopeName());
            try {

                ArrayList<AST> parameters = ((FunParameters) (casted.getParameters())).getParameters();
                for (AST p : parameters) {
                    MyToken parameterType = ((Type) (((Parameter) p).getType())).getToken();
                    MyToken parameterName = ((Variable) (((Parameter) p).getVariable())).getToken();
                    VarSymbol parameter = new VarSymbol(parameterName, parameterType);
                    currentScope.defineSymbol(parameter);
                    functionSymbol.addParameter(parameter);

                }
            } catch (NullPointerException e) {

            }
            visit(casted.getCompound());
            currentScope.print();
            System.out.println("LEAVE " + currentScope.getScopeName());
            currentScope = currentScope.getEnclosingScope();

        } else if(node instanceof FunCall){
            FunCall casted = (FunCall)node;
            for(AST p : casted.getArguments()){
                visit(p);
            }
            Symbol funSymbol = currentScope.lookupSymbol(casted.getName());
            if(funSymbol == null){
                throw new SemanticException("unknown function call" + casted.getName() + " at " + casted.getName().getX() + ":" + casted.getName().getY());
            }
            casted.setFunSymbol(funSymbol);
            //todo check arguments nuber
            //todo check exsistance of function
        } else if(node instanceof  ReturnStatement){
            ReturnStatement casted = (ReturnStatement)node;
            visit(casted.getRetValue());
        }
    }
}
