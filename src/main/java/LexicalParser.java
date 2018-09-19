import java.util.ArrayList;
import java.util.Arrays;

public class LexicalParser {
    // Keywords of Kotlin Languages
    // Represented as ArrayList <String>
    private ArrayList<String> keywords = new ArrayList<String>(Arrays.asList("as", "as?", "break", "class", "continue", "do", "else", "false", "for", "fun", "if", "in", "!in", "interface", "is", "!is", "null", "object", "package", "return", "super", "this", "throw", "true", "try", "typealias", "val", "var", "when", "while"));

    // Soft Keywords of Kotlin Languages
    // Represented as ArrayList <String>
    private ArrayList <String> softKeywords = new ArrayList<String>(Arrays.asList("by", "catch", "constructor", "delegate", "dynamic", "field", "file", "finally", "get", "import", "init", "param", "property", "receiver", "set", "setparam", "where", "L"));

    // Modifier Keywords of Kotlin Languages
    // Represented as ArrayList <String>
    private ArrayList <String> modifierKeywords = new ArrayList<String>(Arrays.asList("actual", "abstract", "annotation", "companion", "const", "crossinline", "data", "enum", "expect", "external", "final", "infix", "inline", "inner", "internal", "lateinit", "noinline", "open", "operator", "out", "override", "private", "protected", "public", "reified", "sealed", "suspend", "tailrec", "vararg"));

    // Special Identifiers of Kotlin Languages
    // Represented as ArrayList <String>
    private ArrayList <String> specialIdentifiers = new ArrayList<String>(Arrays.asList("field", "it"));

    // Special Symbols of Kotlin Languages
    // Represented as ArrayList <String>
    private ArrayList <String> specialSymbols = new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "%", "=", "+=", "-=", "*=", "/=", "%=", "++", "--", "&&", "||", "!", "==", "!=", "===", "!==", "<", ">", "<=", ">=", "[", "]", "!!", "?.", "?:", "::", "..", ":", "?", "->", "@", ";", "$", "_", "(", ")", "{", "}", ",", "."));

    private ArrayList <Character> delimiters = new ArrayList<Character>(Arrays.asList(' ', '\n', '\t'));

    private ArrayList <Token> tokens = new ArrayList<Token>(); // List of all processed tokens

    private int cur; // Index of cur character
    private StringBuilder stream; // Processed stream



    public void parse(StringBuilder stream) throws Exception {
        cur = 0;
        this.stream = stream;

        while (cur < stream.length()) {
            boolean parsed = true;
            if (isMore(2) && curChar(2).equals("//")) {
                parseLineComment();
            }
            else if (isMore(2) && curChar(2).equals("/*")) {
                parseComment();
            }
            else if (delimiters.contains(curChar())) {
                parseDelimiter();
            }
            else if (curChar()=='"') {
                parseString();
            }
            else if (curChar()>='0' && curChar()<='9') {
                parseDigit();
            }
            else {
                parsed = false;
                int maxN = maximumWordSize();
                for (int i = maxN; i > 0; i--) {
                    if (isMore(i) && parseWords(i)) {
                        parsed = true;
                        break;
                    }
                }
            }

            if (!parsed) {
                parsed = parseIdentifier();
            }

            if (!parsed) {
                // TODO error
            }
        }
    }

    private void parseComment() throws Exception {
        StringBuilder l = new StringBuilder();
        l.append(nextChar(2));
        while(isMore(2) && !curChar(2).equals("*/")) {
            l.append(nextChar());
        }
        if (isMore(2) && curChar(2).equals("*/")) {
            l.append(nextChar(2));
            tokens.add(new Token(Token.TokenType.Comment, l.toString()));
        }
        else {
            throw new Exception("Error in COMMENT lexeme: " + l.toString());
            // TODO error
        }
    }

    // Parse LineComments
    private void parseLineComment() throws Exception {
        StringBuilder l = new StringBuilder();
        while (isMore() && curChar() != '\n') {
            l.append(nextChar());
        }
        if (isMore() && curChar() == '\n') {
            l.append(nextChar());
            tokens.add(new Token(Token.TokenType.LineComment, l.toString()));
        }
        else {
            throw new Exception("Error in LINECOMMENT: " + l.toString());
        }
    }

    // Parse Identifier
    private boolean parseIdentifier() {
        StringBuilder l = new StringBuilder();

//        if (curChar()=='_') {
//            String s = "_";
//            for (int i = 2; isMore(i); i++) {
//                s = curChar(i);
//                if (s.charAt(s.length()-1) != '_') {
//                    break;
//                }
//            }
//            if (!(Character.isLetter(s.charAt(s.length()-1)) || s.charAt(s.length()-1)=='$' || (s.charAt(s.length()-1)>='0' && s.charAt(s.length()-1)<='9')))
//                return false;
//        }

        if (Character.isLetter(curChar()) || (curChar()=='_' || curChar()=='$')) {
            l.append(nextChar());
            while (isMore() && (Character.isLetter(curChar()) || curChar()=='_' || curChar()=='$' || (curChar()>='0' && curChar()<='9'))) {
                l.append(nextChar());
            }
        }

        if (l.length()>0) {
            tokens.add(new Token(Token.TokenType.Identifier, l.toString()));
            return true;
        }
        return false;
    }

    // Parse reserved word
    private boolean parseWords(int n) {
        String l = curChar(n);
        boolean b = !isMore(n+1) || checkWord(n);
        if (b) {
            Token.TokenType type = null;
            if (keywords.contains(l)) {
                type = Token.TokenType.Keyword;
            }
            else if (softKeywords.contains(l)) {
                type = Token.TokenType.SoftKeyword;
            }
            else if (modifierKeywords.contains(l)) {
                type = Token.TokenType.ModifierKeyword;
            }
            else if (specialIdentifiers.contains(l)) {
                type = Token.TokenType.SpecialIdentifier;
            }
            else if (specialSymbols.contains(l)) {
                type = Token.TokenType.SpecialSymbol;
            }

            if (type != null) {
                tokens.add(new Token(type, l));
                nextChar(n);
                return true;
            }
        }

        return false;
    }

    // Check that after Word which ends on letter there is no other letter after
    private boolean checkWord(int n) {
        return !(Character.isLetter(curChar(n).charAt(n-1)) && (Character.isLetter(curChar(n+1).charAt(n)) || curChar(n).charAt(n-1)=='$' || (curChar(n).charAt(n-1)>='0' && curChar(n).charAt(n-1)<='9')) &&
                !(curChar(n).charAt(n-1)=='_' && (Character.isLetter(curChar(n+1).charAt(n)) || curChar(n).charAt(n-1)=='$' || (curChar(n).charAt(n-1)>='0' && curChar(n).charAt(n-1)<='9'))));
    }

    // Determine maximum word size for parsing words
    private int maximumWordSize() {
        int max = 0;
        for (String s: keywords) {
            max = Math.max(max, s.length());
        }
        for (String s: specialSymbols) {
            max = Math.max(max, s.length());
        }
        for (String s: softKeywords) {
            max = Math.max(max, s.length());
        }
        for (String s: specialIdentifiers) {
            max = Math.max(max, s.length());
        }
        for (String s: modifierKeywords) {
            max = Math.max(max, s.length());
        }

        return max;
    }

    // Parse Delimiter
    private void parseDelimiter() {
        String del = "";
        switch (curChar()) {
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

    // Parse Digit tokens
    private void parseDigit() {
        StringBuilder l = new StringBuilder();
        while (isMore() && curChar()>='0' && curChar()<='9') {
            l.append(nextChar());
        }
        tokens.add(new Token(Token.TokenType.Digit, l.toString()));
    }

    // Parse String and QuotedString tokens
    private void parseString() throws Exception {
        StringBuilder l = new StringBuilder();
        if (isMore(3) && curChar(3).equals("\"\"\"")) {
            l.append(nextChar(3));
            while (isMore(3) && !curChar(3).equals("\"\"\"")) {
                l.append(nextChar());
            }
            l.append(nextChar(3));
            if (l.lastIndexOf("\"\"\"") > 2) {
                tokens.add(new Token(Token.TokenType.QuotedString, l.toString()));
            }
            else {
                // TODO error
            }
        }
        else {
            l.append(nextChar());
            while (isMore() && (curChar() != '"' || l.charAt(l.length()-1)=='\\')) {
                l.append(nextChar());
            }
            l.append(nextChar());
            if (l.lastIndexOf("\"")>0 && l.charAt(l.length()-1) == '\"' && l.charAt(l.length()-2) != '\\') {
                tokens.add(new Token(Token.TokenType.String, l.toString()));
            }
            else {
                throw new Exception("Wrong String token: " + l);
                // TODO error
            }
        }
    }

    // Returns current character of the processing stream
    private char curChar() {
        return stream.charAt(cur);
    }

    // Returns current n characters of the processing stream
    private String curChar(int n) {
        return stream.substring(cur, cur+n);
    }

    // Returns current character and moves cur by one
    private char nextChar() {
        char ch = curChar();
        cur++;
        return ch;
    }

    // Returns current n characters and moves cur by n
    private String nextChar(int n) {
        String s = curChar(n);
        cur += n;
        return s;
    }

    private boolean isMore(int n) {
        return cur + n <= stream.length();
    }

    private boolean isMore() {
        return isMore(1);
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }
}
