import java.util.ArrayList;
import java.util.Arrays;

public class LexicalAnalyzer {
    // Keywords of Kotlin Languages
    // Represented as ArrayList <String>
    private ArrayList<String> keywords = new ArrayList<String>(Arrays.asList("as", "as?", "break", "class", "continue",
            "do", "else", "false", "for", "fun", "if", "in", "!in", "interface", "is", "!is", "null", "object",
            "package", "return", "super", "this", "throw", "true", "try", "typealias", "val", "var", "when",
            "while", "by", "catch", "constructor", "delegate", "dynamic", "field", "file", "finally", "get", "import",
            "init", "param", "property", "receiver", "set", "setparam", "where", "actual", "abstract", "annotation",
            "companion", "const", "crossinline", "data", "enum", "expect", "external", "final", "infix", "inline",
            "inner", "internal", "lateinit", "noinline", "open", "operator", "out", "override", "private", "protected",
            "public", "reified", "sealed", "suspend", "tailrec\", \"vararg\"", "it"));

    // Special Symbols of Kotlin Languages
    // Represented as ArrayList <String>
    private ArrayList <String> specialSymbols = new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "%", "=", "+=", "-=",
            "*=", "/=", "%=", "++", "--", "&&", "||", "!", "==", "!=", "===", "!==", "<", ">", "<=", ">=", "[", "]",
            "!!", "?.", "?:", "::", "..", ":", "?", "->", "@", ";", "$", "_", "(", ")", "{", "}", ",", "."));

    private ArrayList <Character> delimiters = new ArrayList<Character>(Arrays.asList(' ', '\n', '\t'));

    private ArrayList <Token> tokens = new ArrayList<Token>(); // List of all processed tokens

    private int tokenCursor; // Index of current token
    private int cursor; // Index of cursor character
    private StringBuilder stream; // Stream of symbols
    private int maximumKeyWordSize = -1;

    public boolean hasNextToken() {
        return tokenCursor < tokens.size();
    }

    public Token nextToken() {
        return tokenCursor < tokens.size() ? tokens.get(tokenCursor++) : null;
    }

    /*
        The method goes through source code in the stream and parses it filling 'tokens' array. If there is a
        lexical error in the source code, throws an exception.
     */
    public void parse(StringBuilder stream) throws Exception {
        tokenCursor = 0;
        cursor = 0;
        this.stream = stream;
        for (String keyword : keywords) {
            if (keyword.length() > maximumKeyWordSize) {
                maximumKeyWordSize = keyword.length();
            }
        }

        while (cursor < stream.length()) {
            boolean parsed = true;
            if (hasMoreSymbols(2) && cursorChar(2).equals("//")) {
                parseLineComment();
            }
            else if (hasMoreSymbols(2) && cursorChar(2).equals("/*")) {
                parseComment();
            }
            else if (delimiters.contains(cursorChar())) {
                parseDelimiter();
            }
            else if (cursorChar()=='"') {
                parseString();
            }
            else if ((cursorChar()>='0' && cursorChar()<='9') || (hasMoreSymbols(3) && cursorChar(3).equals("NaN"))) {
                parseNumber();
            }
            else {
                parsed = false;
                for (int i = maximumKeyWordSize; i > 0; i--) {
                    if (hasMoreSymbols(i)) {
                        if (checkWordWithLength(i)) {
                            nextChar(i);
                            parsed = true;
                            break;
                        }
                    }
                }
            }

            if (!parsed) {
                parseIdentifier();
            }
        }
    }

    private void parseComment() throws Exception {
        StringBuilder l = new StringBuilder();
        //nextChar(2);
        l.append(nextChar(2));
        while(hasMoreSymbols(2) && !cursorChar(2).equals("*/")) {
            l.append(nextChar());
        }
        if (hasMoreSymbols(2) && cursorChar(2).equals("*/")) {
            //nextChar(2);
            l.append(nextChar(2));
            tokens.add(new Token(Token.TokenType.Comment, l.toString()));
        }
        else {
            throw new Exception("Error in comment at " + cursor + " position in the source code file.");
        }
    }

    private void parseLineComment() throws Exception {
        StringBuilder l = new StringBuilder();
        while (hasMoreSymbols() && cursorChar() != '\n') {
            l.append(nextChar());
        }
        if (hasMoreSymbols() && cursorChar() == '\n' || !hasMoreSymbols()) {
            tokens.add(new Token(Token.TokenType.Comment, l.toString()));
        }
        else {
            throw new Exception("Error in comment at " + cursor + " position in the source code file.");
        }
    }

    private void parseIdentifier() throws Exception {
        StringBuilder l = new StringBuilder();

        if (Character.isLetter(cursorChar()) || cursorChar()=='_') {
            l.append(nextChar());
            while (hasMoreSymbols() && (Character.isLetter(cursorChar()) || cursorChar()=='_' || (cursorChar()>='0' && cursorChar()<='9'))) {
                l.append(nextChar());
            }
        }

        if (l.length()>0) {
            tokens.add(new Token(Token.TokenType.Identifier, l.toString()));
            return;
        }
        throw new Exception("Error in identifier at " + cursor + " position in the source code file.");
    }

    // Check that the word is contained in either keyword or specialSymbols list
    // or if it is identifier which has some keyword as a substring
    private boolean checkWordWithLength(int length) {
        String word = cursorChar(length);
        if (keywords.contains(word)) {
            if (hasMoreSymbols(length + 1)) {
                char charAtLength = cursorChar(length + 1).charAt(length);
                if (Character.isLetter(charAtLength) || Character.isDigit(charAtLength) || charAtLength == '_') {
                    try {
                        parseIdentifier();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    tokens.add(new Token(Token.TokenType.Keyword, word));
                }
                return true;
            }
        } else if (specialSymbols.contains(word)) {
            if (word == "(") {
                tokens.add(new Token(Token.TokenType.Opening, word));
                return true;
            }
            if (word == "{") {
                tokens.add(new Token(Token.TokenType.OpeningCurly, word));
                return true;
            }
            if (word == "[") {
                tokens.add(new Token(Token.TokenType.OpeningSquare, word));
                return true;
            }
            if (word == ")") {
                tokens.add(new Token(Token.TokenType.Closing, word));
                return true;
            }
            if (word == "}") {
                tokens.add(new Token(Token.TokenType.ClosingCurly, word));
                return true;
            }
            if (word == "]") {
                tokens.add(new Token(Token.TokenType.ClosingSquare, word));
                return true;
            }
            tokens.add(new Token(Token.TokenType.SpecialSymbol, word));
            return true;
        }
        return false;
    }

    private void parseDelimiter() {
        String del = "";
        switch (cursorChar()) {
            case ' ':
                del = "SPACE";
                break;
            case '\n':
                del = "ENDL";
                break;
            case '\t':
                del = "TAB";
                break;
        }
        nextChar();
        tokens.add(new Token(Token.TokenType.Delimiter, del));
    }

    private void parseNumber() throws Exception {
        if (hasMoreSymbols(3) && cursorChar(3).equals("NaN")) {
            tokens.add(new Token(Token.TokenType.Literal, "NaN"));
            return;
        }
        StringBuilder l = new StringBuilder();

        // handling hex and binary numbers is similar
        // we first append '0x' or '0b', append all permissable symbols,
        // and then check that number after '0x' or '0b' neither starts nor ends with '_'. If so, throw an exception
        if (hasMoreSymbols(2) && (cursorChar(2).equals("0x"))) {
            l.append(nextChar(2));
            while (hasMoreSymbols() && (checkCharForHex(cursorChar()) || cursorChar() == '_')) {
                l.append(nextChar());
            }
            String lexeme = l.toString();
            if (l.length() != 2 && lexeme.charAt(2) != '_' && lexeme.charAt(lexeme.length() - 1) != '_') {
                tokens.add(new Token(Token.TokenType.Literal, l.toString()));
                return;
            } else {
                throw new Exception("Exception in number at " + cursor + " position in the source code file.");
            }
        }
        if (hasMoreSymbols(2) && (cursorChar(2).equals("0b"))) {
            l.append(nextChar(2));
            while (hasMoreSymbols() && (cursorChar() == '0' || cursorChar() == '1')) {
                l.append(nextChar());
            }
            String lexeme = l.toString();
            if (l.length() != 2 && lexeme.charAt(2) != '_' && lexeme.charAt(lexeme.length() - 1) != '_') {
                tokens.add(new Token(Token.TokenType.Literal, l.toString()));
                return;
            } else {
                throw new Exception("Exception in number at " + cursor + " position in the source code file.");
            }
        }

        while (hasMoreSymbols() && ((cursorChar()>='0' && cursorChar()<='9') || cursorChar() == '_')) {
            l.append(nextChar());
        }
        // append part after the dot
        if (hasMoreSymbols() && cursorChar() == '.') {
            l.append(nextChar());
            while (hasMoreSymbols() && ((cursorChar()>='0' && cursorChar()<='9') || cursorChar() == '_')) {
                l.append(nextChar());
            }
        }

        // check that the last symbol so far is not underscore
        if (l.toString().charAt(l.toString().length() - 1) != '_') {
            // append 'f' or 'F' if there is such symbol
            if (hasMoreSymbols() && (cursorChar() == 'f' || cursorChar() == 'F')) {
                l.append(nextChar());
            } else {
                // if there is an exponent
                if (hasMoreSymbols() && (cursorChar() == 'e' || cursorChar() == 'E')) {
                    // append exp symbol
                    l.append(nextChar());
                    String exponent = "";
                    // build exponent number
                    while (hasMoreSymbols() && ((cursorChar()>='0' && cursorChar()<='9') || cursorChar() == '_')) {
                        exponent += nextChar();
                    }
                    // check that number in exponent is valid
                    if (exponent.charAt(0) != '_' && exponent.charAt(exponent.length() - 1) != '_') {
                        l.append(exponent);
                    } else {
                        throw new Exception("Exception in number at " + cursor + " position in the source code file.");
                    }
                }
            }
        } else {
            // if it is, throw exception
            throw new Exception("Exception in number at " + cursor + " position in the source code file.");
        }
        tokens.add(new Token(Token.TokenType.Literal, l.toString()));
    }

    private boolean checkCharForHex(char c) {
        ArrayList<Character> alphabet = new ArrayList<Character>(Arrays.asList('0', '1', '2', '3', '4', '5', '6',
                '7', '8', '9', 'a', 'A', 'b', 'B', 'c', 'C', 'd', 'D', 'e', 'E', 'f', 'F'));
        return alphabet.contains(c);
    }

    private void parseString() throws Exception {
        StringBuilder l = new StringBuilder();
        if (hasMoreSymbols(3) && cursorChar(3).equals("\"\"\"")) {
            l.append(nextChar(3));
            while (hasMoreSymbols(3) && !cursorChar(3).equals("\"\"\"")) {
                l.append(nextChar());
            }
            l.append(nextChar(3));
            if (l.lastIndexOf("\"\"\"") > 2) {
                tokens.add(new Token(Token.TokenType.Literal, l.toString()));
            }
            else {
                throw new Exception("Error in string at " + cursor + " position in the source code file.");
            }
        }
        else {
            l.append(nextChar());
            while (hasMoreSymbols() && (cursorChar() != '"') || l.charAt(l.length()-1)=='\\') {
                l.append(nextChar());
            }
            l.append(nextChar());
            if (l.lastIndexOf("\"")>0 && l.charAt(l.length()-1) == '\"') {
                tokens.add(new Token(Token.TokenType.Literal, l.toString()));
            }
            else {
                throw new Exception("Error in string at " + cursor + " position in the source code file.");
            }
        }
    }

    /*
        returns character at 'cursor' position
     */
    private char cursorChar() {
        return stream.charAt(cursor);
    }

    /*
        returns 'n' characters starting from 'cursor' position
     */
    private String cursorChar(int n) {
        return stream.substring(cursor, cursor+n);
    }

    /*
        returns character at 'cursor' position and moves cursor position by one
     */
    private char nextChar() {
        char ch = cursorChar();
        cursor++;
        return ch;
    }

    /*
        returns n characters starting from cursor position and moves cursor by n
     */
    private String nextChar(int n) {
        String s = cursorChar(n);
        cursor += n;
        return s;
    }

    /*
        returns true if source code file has 'n' more symbols after 'cursor' position, false otherwise
     */
    private boolean hasMoreSymbols(int n) {
        return cursor + n <= stream.length();
    }

    /*
        returns true if source code file has 1 more symbol after 'cursor' position, false otherwise
     */
    private boolean hasMoreSymbols() {
        return hasMoreSymbols(1);
    }
}
