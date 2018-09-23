public class Token {
    enum TokenType {
        Literal, Delimiter, Keyword, SpecialSymbol, Opening, OpeningCurly, OpeningSquare,
        Closing, ClosingCurly, ClosingSquare, Identifier, Comment
    }

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
