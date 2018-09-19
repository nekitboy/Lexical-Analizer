import java.util.ArrayList;
import java.util.Arrays;

public class LexicalAnalyzer {
    ArrayList <Token> tokens;
    private int cur; // Index of next character

    // Constructor of LexicalAnalyzer
    public LexicalAnalyzer(StringBuilder stream) throws Exception {
        cur = 0;
        LexicalParser parser = new LexicalParser();
        parser.parse(stream);
        tokens = parser.getTokens();
    }

    // Returns next Token of the precessed stream
    public Token nextToken() {
        if (cur >= tokens.size())
            return null;
        return tokens.get(cur++);
    }

    public boolean hasNextToken() {
        return cur < tokens.size();
    }
}
