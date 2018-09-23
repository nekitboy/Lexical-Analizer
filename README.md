# Lexical-Analizer For Kotlin Language

HA-2 for Compilers Construction course

Java Maven Project

Each token is represented by a token type and lexeme which is string representation of a token.
We divide all tokens into 12 different categories:
1) Literals can be strings or numbers. The only requirement for a string is that it starts with single or triple quote and ends with it. 
Numbers can have different forms: integer (a sequence of digits), hex (0x followed by sequence of digits or symbols in A-F range in both registers), binary (0b followed by sequence of 0s and 1s), float (2 sequences of digits separated by a dot symbol). All of those except binary numbers can end with an exponent notation (like 4e3), there is also an explicit notation for float numbers: 'f' or 'F' at the end of a number (it should always follow exponent notation if it is present).
2) Delimiters can be either space, tab or end of line symbol.
3) Keywords are special words of kotlin language (hard, soft keywords, modifiers and special identifiers combined).
4) Special symbols are operators and other special symbols of kotlin language.
5) Opening for opening brackets symbol '('.
6) Opening curly for opening curly brackets symbol '{'.
7) Opening square for opening square brackets sysmol ']'.
8) Closing brackets.
9) Closing curly brackets.
10) Closing square brackets. Rationale for making a category for each of the brackets is that it will be easier making a syntactic analyzer if there are discrete categories for brackets.
11) Identifiers can either be class, variable, method or parameter names.
12) Comments.



