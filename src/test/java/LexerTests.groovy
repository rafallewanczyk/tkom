import spock.lang.Specification;
import Lexer.Add;

public class LexerTests extends Specification{
    def "test dodawnia" (){
        setup:
        Add f = new Add();
        expect:
        f.add(a, b) == c;

        where:
        a << [1, 2, 9]
        b << [4, 5, 6]
        c << [5, 7, 9]
    }
}
