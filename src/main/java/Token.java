public class Token {
    enum TokenType {
        String, QuotedString, Digit, Delimiter, Keyword, SoftKeyword, ModifierKeyword, SpecialIdentifier, SpecialSymbol, Identifier, LineComment, Comment
    };

    // Type of the token
    TokenType tokenType;

    // Token lexeme
    String lexeme;

    public Token (TokenType tokenType, String lexeme) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
    }

    @Override
    public String toString() {
        return this.tokenType.toString() + ": " + lexeme;
    }
}
