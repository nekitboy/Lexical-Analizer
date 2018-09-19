import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexicalAnalyzerTest {

    @Test
    void stringToken1() throws Exception {
        StringBuilder s = new StringBuilder("\"String\"");
        LexicalAnalyzer analyzer = new LexicalAnalyzer(s);
        assertEquals(analyzer.nextToken().toString(), "String: \"String\"");
    }

    @Test
    void stringToken2() throws Exception {
        StringBuilder s = new StringBuilder("\"Str    ing\"");
        LexicalAnalyzer analyzer = new LexicalAnalyzer(s);
        assertEquals(analyzer.nextToken().toString(), "String: \"Str    ing\"");
    }

    @Test
    void stringToken3() throws Exception {
        StringBuilder s = new StringBuilder("\"String\\\"\"");
        LexicalAnalyzer analyzer = new LexicalAnalyzer(s);
        assertEquals(analyzer.nextToken().toString(), "String: \"String\\\"\"");
    }

    @Test
    void quotedStringToken1() throws Exception {
        StringBuilder s = new StringBuilder("\"\"\"String\nString\"\"\"");
        LexicalAnalyzer analyzer = new LexicalAnalyzer(s);
        assertEquals(analyzer.nextToken().toString(), "QuotedString: \"\"\"String\nString\"\"\"");
    }

    @Test
    void quotedStringToken2() throws Exception {
        StringBuilder s = new StringBuilder("\"\"\"\nString\nString\n\"\"\"");
        LexicalAnalyzer analyzer = new LexicalAnalyzer(s);
        assertEquals(analyzer.nextToken().toString(), "QuotedString: \"\"\"\nString\nString\n\"\"\"");
    }

    @Test
    void digitToken1() throws Exception {
        StringBuilder s = new StringBuilder("1");
        LexicalAnalyzer analyzer = new LexicalAnalyzer(s);
        assertEquals(analyzer.nextToken().toString(), "Digit: 1");
    }

    @Test
    void digitToken2() throws Exception {
        StringBuilder s = new StringBuilder("112341414123123124213123");
        LexicalAnalyzer analyzer = new LexicalAnalyzer(s);
        assertEquals(analyzer.nextToken().toString(), "Digit: 112341414123123124213123");
    }
}