package Lexer.RomanNumerals;

public enum RomanNumeralsLookup {

    ;
    public enum THAUSENDS{
        M(1000),
        MM(2000),
        MMM(3000);

        private int value;
        private THAUSENDS(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }
    }
    public enum HUNDREDS{
        C(100),
        CC(200),
        CCC(300),
        CD(400),
        D(500),
        DC(600),
        DCC(700),
        DCCC(800),
        CM(900);

        private int value;
        private HUNDREDS(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }
    }
    public enum TENS{
        X(10),
        XX(20),
        XXX(30),
        XL(40),
        L(50),
        LX(60),
        LXX(70),
        LXXX(80),
        XC(90);

        private int value;
        private TENS(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }
    }
    public enum ONES{
        I(1),
        II(2),
        III(3),
        IV(4),
        V(5),
        VI(6),
        VII(7),
        VIII(8),
        IX(9);

        private int value;
        private ONES(int value){
            this.value = value;
        }
        public int getValue(){
            return value;
        }
    }

}
