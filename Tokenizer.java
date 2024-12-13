import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    public enum TokenType {
        ASSIGN,
        PLUS,
        MINUS,
        MULTIPLY,
        LEFTP,
        RIGHTP,
        LITERAL,
        ID,
        SEMICOLON
    }

    public static class Token {
        private TokenType type;
        private String value;

        public Token(TokenType type, String value) {
            this.type = type;
            this.value = value;
        }

        public TokenType getType() {
            return type;
        }

        public String getValue() {
            return value;
        }
    }

    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int currentIndex = 0;

        while (currentIndex < input.length()) {
            char currentChar = input.charAt(currentIndex);

            if (currentChar == '=') {
                tokens.add(new Token(TokenType.ASSIGN, "="));
                currentIndex++;
            } else if (currentChar == '+') {
                tokens.add(new Token(TokenType.PLUS, "+"));
                currentIndex++;
            } else if (currentChar == '-') {
                tokens.add(new Token(TokenType.MINUS, "-"));
                currentIndex++;
            } else if (currentChar == '*') {
                tokens.add(new Token(TokenType.MULTIPLY, "*"));
                currentIndex++;
            } else if (currentChar == '(') {
                tokens.add(new Token(TokenType.LEFTP, "("));
                currentIndex++;
            } else if (currentChar == ')') {
                tokens.add(new Token(TokenType.RIGHTP, ")"));
                currentIndex++;
            } else if (currentChar == ';') {
                tokens.add(new Token(TokenType.SEMICOLON, ";"));
                currentIndex++;
            } else if (Character.isWhitespace(currentChar)) {
                currentIndex++;
            } else if (Character.isLetter(currentChar) || currentChar == '_') {
                int start = currentIndex;
                while (currentIndex < input.length() && 
                        (Character.isLetterOrDigit(input.charAt(currentIndex)) || 
                        input.charAt(currentIndex) == '_')) {
                    currentIndex++;
                }
                
                String identifier = input.substring(start, currentIndex);
                tokens.add(new Token(TokenType.ID, identifier));
            } else if (Character.isDigit(currentChar)) {
                if (currentChar == '0' && currentIndex + 1 < input.length() && 
                    Character.isDigit(input.charAt(currentIndex + 1))) {
                    throw new IllegalArgumentException("Error");
                }
                
                int start = currentIndex;
                while (currentIndex < input.length() && 
                        Character.isDigit(input.charAt(currentIndex))) {
                    currentIndex++;
                }
                
                String literal = input.substring(start, currentIndex);
                tokens.add(new Token(TokenType.LITERAL, literal));
            } else {
                currentIndex++;
            }
        }

        return tokens;
    }
}