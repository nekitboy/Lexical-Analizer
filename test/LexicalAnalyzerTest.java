import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LexicalAnalyzerTest {

    @Test
    void stringOK1() throws Exception {
        StringBuilder s = new StringBuilder("\"String\"");
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        analyzer.parse(s);
        assertEquals(analyzer.nextToken().toString(), "Literal: \"String\"");
    }

    @Test
    void stringException() {
        StringBuilder s = new StringBuilder("\"\"\"String\\\"\"");
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        assertThrows(Exception.class, () -> analyzer.parse(s));
    }

    @Test
    void stringOK2() throws Exception {
        StringBuilder s = new StringBuilder("\"\"\"\nString\nString\n\"\"\"");
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        analyzer.parse(s);
        assertEquals(analyzer.nextToken().toString(), "Literal: \"\"\"\nString\nString\n\"\"\"");
    }

    @Test
    void numberOK() throws Exception {
        StringBuilder s = new StringBuilder("3.4e10");
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        analyzer.parse(s);
        assertEquals(analyzer.nextToken().toString(), "Literal: 3.4e10");
    }

    @Test
    void numberException() {
        StringBuilder s = new StringBuilder("0x_3");
        final LexicalAnalyzer analyzer = new LexicalAnalyzer();
        assertThrows(Exception.class, () -> analyzer.parse(s));
    }

    @Test
    void identifierException() {
        StringBuilder s = new StringBuilder("™");
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        assertThrows(Exception.class, () -> analyzer.parse(s));
    }
}
