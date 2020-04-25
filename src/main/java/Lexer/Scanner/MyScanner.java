package Lexer.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MyScanner {
    Character buffer;
    File text;
    Scanner scanner;

    public MyScanner(String file) throws FileNotFoundException {
        text = new File(file);
        scanner = new Scanner(text);
        scanner.useDelimiter("");
    }

    public char getNextSymbol(){
        if(scanner.hasNext()){
            buffer = scanner.next().charAt(0);
            return buffer;
        }else{
            return '\u001a';
        }

    }
}
