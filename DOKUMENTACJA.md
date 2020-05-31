# Język ***rom*** obsługujący cyfry rzymskie
### Autor : Rafał Lewanczyk
# 1. O projekcie: 
Celem projektu jest stworzenie języka programowania pozwalającego na wykonywanie operacji na typach standardowych jak i liczbach rzymskich

### Założenia:  
- Język jest zaimplementowany w języku Java 
- Każdy program musi posiadać funkcję ```main``` będącą punktem wejściowym dla jednostki wykonywania programu 
- Jednym z typów wbudowanych (oprócz podstawowcyh int, real) jest typ obsługujący liczby rzymskie ```rom```
- Język udostępnia operacje matematyczne o różnym priorytecie wykonywania oraz naiwasy 
- Język pozwala na tworzenie zmiennych oraz obsługuje zakres widoczności 
- Język pozwala na tworzenie oraz wywoływanie funkcji 
- Dostępne są komentarze 
- Język udostępnia instrukcję warunkową ```if else``` 
- Język udostępnia instrukcję pętli ```while``` 

# 2. Poszczególne elementy projektu:
### 2.1 Lexer: 
Lexer został zrealizowany w pakiecie ```Lexer```. Na lexer składają sie kolejno : 
- ```Skaner``` - odpowiedzialny za czytanie kolejnych znaków z pliku wejściowego
- ```Token``` :
    - ```MyToken``` - klasa reprezentująca pojedyńczy token 
    - ```MyTokenPrefix``` - statyczna klasa zawierająca funkcje wspomagające rozpoznawanie tokenów
    - ```MyTokenType``` - enum z nazwami poszczególnych typów tokenów
- ```MyLexer``` - klasa realizująca automat lexera
- ```RomanNumerals``` :
    - ```RomanNumeralsLookup``` - enum zawierający poszczególne bazowe cyfry rzymskie
    - ```RomanLexer``` - klasa realizująca parsowanie cyfr rzymskich. Parsowanie odbywa się poprzez próbę rozpoznania jak najdłuższego prefixu liczby rzymskiej w w.w. enumeratorze. Po udanym rozpoznaniu prefix zostaje usunięty oraz algorytm działa na pozostałych znakach. W przypadku nie rozpoznania liczby ciąg znaków jest traktowany jako nazwa zmiennej lub funkcji. 

### 2.2 Parser
Parser został zrealizowany w pakiecie ```Parser```. Na parser składają sie kolejno: 
- ```AST``` - (Abstract Syntax Tree) interfejs reprezentujący pojedyńczy liść drzewa parsowania
- ```AST_node``` -  Zbiór statycznych klas implemenutujących interfejs `AST` w celu reprezentacji poszczególnych elementów gramatyki programu
- ```MyParser``` - Klasa realizująca budowanie drzewa oraz sprawdza poprawność gramatyki programu. Jej rezultatem jest drzewo parsowania.  

### 2.3 Analizator semantyczny 
Implementacja analizatora semantycznego znajduje się w pakiecie `Parser.MySymbolTable`. Zadaniem analizatora jest rozpozanie czy zmienne oraz funkcje zostały zadeklarowane zanim zostały użyte, kontrola wbudowancyh typów, analiza zagnieżdżonych bloków kodu. 
- ```BuiltInTypeSymbol``` - klasa reprezentująca typ wbudowany
- ```FunctionSymbol``` - klasa reprezentująca wszystkie potrzebne dane o funkcji. 
- ```MyScopedSymbolTable``` - klasa reprezenująca pojedyńczy scope w programie zawiera w sobie zadeklarowane zmienne oraz wskaźnik na scope w którym jest zagnieżdżona. Najwyższy scope zawiera w sobie również dostępne funkcje
- ```MyScopeSymbolTableBuilder``` - klasa realizująca analize drzewa parsowania algorytmem DFS . W konstruktorze przyjmuje drzewo wygenerowane przez parser, zwraca to samo drzewo z dodanymi informacjami potrzebnymi do wywoływania funkcji przez interpreter

### 2.4 Interpreter 
Na interprter składa sie stos wywołań, jak i sam interpreter
- ```CallStack```-pakiet zawierjący realizację stosu wywołań: 
    - ```ActivationType``` - enum zaiwerające możliwe typy warstwy stosy - wszystkie elementy otwierające nowy blok - if, while, funkcja
    - ```AcitvationRecord``` - implementacja pojedyńczej warstwy stosu wywołań. Znajduje się w niej hashmapa zawierająca każdą zadeklrowaną zmienna w danym bloku, oraz odpowiadającą jej wartość.  
- ```MyInterpreter``` - klasa reprezentująca interpreter. Algorytm DFS poruszający sie po drzewiwe parsowania kolejno wykonujący program. 


# Zasady obowiązujące w języku ***rom***
- Na cały program składa się zbiór funkcji 
- Każda funkcja musi mieć typ oraz jeśli zwraca wartość musi być ona zgodnaz typem funkcji
- Jako pierwsza zostaje wywołana funkcja ```main()```, oraz zwracana przez nią wartość jest wyjściem całego programu
- Wszystkie funkjce wywołane w funkcji ```main``` muszą zostać zadeklarowane nad nią
- Każda zmienna musi być wcześniej zadeklarowana z type
- Dla typu ```real``` należy używać stałych z ```.``` np ```real v = 12.0```, wyrażenie ```real v = 12``` jest niepoprawne
- Działania można wykonwyać tylko pomiędzy zgodnymi typami: 
    - wyrażenie jest niepoprawne ponieważ ```a``` jest typu ```int``` tak jak ```1```
     ```
        int a = 10;
        real b = a + 1;
    ``` 
    - poprawione wyrażenie 
    ```
        real a = 10.0; 
        real b = a + 1.0;
    ```
- każdy blok składa sie z listy poleceń zakończonych średnikiem. 
- `if` oraz `while` traktowane są również jako polecenia zatem konieczne jest dodanie średnika po zamkniętej klamrze
    


### Przykładowy kod źródłowy: 
```
rom silnia(rom n){
    if(n< II){
        return I;
    };
    return n * silnia(n-I);
}

rom main(){
	//program liczacy silnie z 5
    return silnia(V);
}
```
rezultatem jest `CXX`

### Uruchomienie programu 
Program jest aplikacją konsolową przyjmującą dwa argumenty: 
- `log` - `true/flase` informacja czy wyświtlić działanie analizatora semantycznego oraz działanie interprtera. 
- `file` - plik z kodem do wykonania.  

Przykładowe uruchomienie: ```java -jar TKOM.jar true program.rom```

### Gramatyka:
```
program= { functionDef } 
functionDef= "function" id parameters statementBlock 
parameters= "(" [ id { "," id } ] ")" 
arguments= "(" [ assignable { "," assignable } ] ")" 
statementBlock= "{" { ifStatement | whileStatement | returnStatement |
initStatement ";" | assignStatement ";" | funCall ";" | "continue" ";" |
"break" ";" | statementBlock } "}" 
ifStatement= "if" "(" condition ")" statementBlock [ "else" statementBlock ] 
whileStatement= "while" "(" condition ")" statementBlock 
returnStatement= "return" assignable ";" 
initStatement= "var" id [ assignmentOp assignable ] 
assignStatement= variable assignmentOp assignable 
assignable= funCall | expression
 
expression= multiplicativeExpr { additiveOp multiplicativeExpr } 
multiplicativeExpr= primaryExpr { multiplicativeOp primaryExpr } 
primaryExpr= ( literal | variable | parenthExpr )
parenthExpr= "(" expression ")" 

condition= andCond { orOp andCond } 
andCond= equalityCond { andOp equalityCond } 
equalityCond= relationalCond { equalOp relationalCond } 
relationalCond= primaryCond { relationOp primaryCond } 
primaryCond= [ unaryOp ] ( parenthCond | variable | literal ) 
parenthCond= "(" condition ")" 

unaryOp= "!" 
assignmentOp= "=" 
orOp= "or" | "||" 
andOp= "and" | "&&" 
equalOp= "==" | "!=" 
relationOp= "<" | ">" | "<=" | ">=" 
additiveOp= "+" | "­" 
multiplicativeOp= "*" | "/" | "%" 
comment= "//"


literal= numberLiteral
variable= id [ indexOp ] 
funCall= id arguments 
numberLiteral= [ "­" ] "Infinity" | finiteNumber 
finiteNumber= digit { digit } [ "." { digit } ] 
romanNumber= roman { roman } [ "." {roman} ]
numberLiteral= [ "-" ] "Infinity" | finiteNumber 
id= letter { digit | letter }
letter= "a".."z" | "_" | "$" 
roman= "I" "V" "X" "L" "C" "M" "D" 
digit= "0".."9" 

``` 
