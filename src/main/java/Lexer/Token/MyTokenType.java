package Lexer.Token;

public enum MyTokenType {
    UNARY_OP, //!
    ASSIGNMENT_OP, //=
    OR_OP, //||
    AND_OP, //&&
    EQUAL_OP, //==
    REALTION_OP, //< > <= >=
    ADDITIVE_OP, //+ -
    MULTIPLICATIVE_OP, //* / %
    COMMENT, // //
    RIGHT_BRACE, //}
    LEFT_BRACE, //{
    RIGHT_PARENTESIS,//)
    LEFT_PARENTESIS,//(
    IF,// if
    ELSE,//else
    ID,// id
    COMMA,//,
    SEMICOLLON,//;
    LOOP,//while
    FUNCTION,//function
    VAR,//var
    NUMBER,//[0..9]*[.]
    ROMMAN,//IVX..
    QUOTE,// " "
    RETURN,
    EOF,
    EMPTY,
    UNKNOWN

}
