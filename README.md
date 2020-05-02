# Język obsługujący cyfry rzymskie
### Autor : Rafał Lewanczyk
#### Cel projektu: 
celem projektu jest stworzenie języka programowania pozwalającego na wykonywanie operacji na typach standardowych jak i liczbach rzymskich

### Założenia:  
- Język zostanie zaimplementowany w języku Java 
- Każdy program musi posiadać funkcję ```main``` będącą punktem wejściowym dla jednostki wykonywania programu 
- Jednym z typów wbudowanych (oprócz podstawowcyh string, int, bool) jest typ obsługujący liczby rzymskie
- Język udostępnia operacje matematyczne o różnym priorytecie wykonywania oraz naiwasy 
- Język pozwala na tworzenie zmiennych oraz obsługuje zakres widoczności 
- Język pozwala na tworzenie oraz wywoływanie funkcji 
- Dostępne są komentarze 
- Język udostępnia instrukcję warunkową ```if else``` 
- Język udostępnia instrukcję pętli ```while``` 
### Wymagania funkcjonalne: 
- Odczytywanie, parsowanie i analiza kodu źródłowego zapisanego w plikach tekstowych 
- Kontrola poprwaności wprowadzonych danych oraz umiejętne wykrywanie oraz zaznaczanie błędów
- Wykonywanie operacji na liczbach rzymskich
- Pilnowanie poprawności zapisu liczb rzymskich 
### Uruchomienie programu 
Program będzie aplikacją konsolową, uruchamianą poprzez wywołanie z argumentem zawierającym ścieżkę do kodu źródłowego do kompilacji 
podanym na standardowym wejściu. Wynik poszczególnych kroków analizy oraz wynik samego uruchomienia programu zostanie 
wyświetlony na standardowym wyjściu.\
Przykład: 
```./compiler sourcefile.rom```
### Przykładowy kod źródłowy: 
```
function pow(rom x, int y)
{
    var res = x; 
    var counter = 1; 
    while(counter < y)
    {
        res = res * x;  
        counter = counter + 1; 
    }
    return res; 
}

function main()
{
    var base = XV;
    var exponent = 2;
    var result = pow(base, exponent);
    if(result > XX)
    {
        //this is a comment 
        return result; 
    }
    else 
    {
        return 0; 
    }
}
```
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
### Analiza programu 
- Analiza leksykalna (lexer) + analiza składniowa (parser)
- Analiza semantyczna (analizator semantyczny) 
- Wykonanie zbioru instrukcji (moduł wykonawczy) 