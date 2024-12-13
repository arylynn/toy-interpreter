import java.util.List;

public class Parser {
    private List<Tokenizer.Token> tokens;
    private int currentTokenIndex;
    private SymbolTable symbolTable;

    public Parser(List<Tokenizer.Token> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
        this.symbolTable = new SymbolTable();
    }

    private Tokenizer.Token getCurrentToken() {
        if (currentTokenIndex < tokens.size()) {
            return tokens.get(currentTokenIndex);
        }
        return null;
    }

    private Tokenizer.Token consume(Tokenizer.TokenType expectedType) {
        Tokenizer.Token currentToken = getCurrentToken();
        
        if (currentToken == null) {
            throw new RuntimeException("Unexpected end of input");
        }
        
        if (currentToken.getType() == expectedType) {
            currentTokenIndex++;
            return currentToken;
        }
        
        throw new RuntimeException("Unexpected token. Expected " + expectedType + ", found " + currentToken.getType());
    }

    public void parse() {
        program();
    }

    private void program() {
        while (currentTokenIndex < tokens.size()) {
            assignment();
        }
    }

    private void assignment() {
        Tokenizer.Token identifier = consume(Tokenizer.TokenType.ID);
        consume(Tokenizer.TokenType.ASSIGN);
        int value = exp();
        consume(Tokenizer.TokenType.SEMICOLON);
        symbolTable.assign(identifier.getValue(), value);
    }

    private int exp() {
        int value = term();
        while (getCurrentToken() != null && 
               (getCurrentToken().getType() == Tokenizer.TokenType.PLUS || 
                getCurrentToken().getType() == Tokenizer.TokenType.MINUS)) {
            Tokenizer.Token operator = getCurrentToken();
            if (operator.getType() == Tokenizer.TokenType.PLUS) {
                consume(Tokenizer.TokenType.PLUS);
                value += term();
            } else {
                consume(Tokenizer.TokenType.MINUS);
                value -= term();
            }
        }
        return value;
    }

    private int term() {
        int value = fact();
        while (getCurrentToken() != null && 
               getCurrentToken().getType() == Tokenizer.TokenType.MULTIPLY) {
            consume(Tokenizer.TokenType.MULTIPLY);
            value *= fact();
        }
        return value;
    }

    private int fact() {
        boolean negate = false;

        while (getCurrentToken() != null && 
               (getCurrentToken().getType() == Tokenizer.TokenType.MINUS || 
                getCurrentToken().getType() == Tokenizer.TokenType.PLUS)) {
            if (getCurrentToken().getType() == Tokenizer.TokenType.MINUS) {
                negate = !negate;
            }
            consume(getCurrentToken().getType());
        }
    
        Tokenizer.Token currentToken = getCurrentToken();
        if (currentToken == null) {
            throw new RuntimeException("Unexpected end of input");
        }
    
        int value;
        switch (currentToken.getType()) {
            case LEFTP:
                consume(Tokenizer.TokenType.LEFTP);
                value = exp();
                consume(Tokenizer.TokenType.RIGHTP);
                break;
            case LITERAL:
                value = Integer.parseInt(currentToken.getValue());
                consume(Tokenizer.TokenType.LITERAL);
                break;
            case ID:
                value = symbolTable.resolve(currentToken.getValue());
                consume(Tokenizer.TokenType.ID);
                break;
            default:
                throw new RuntimeException("Unexpected token type: " + currentToken.getType());
        }

        if (negate) {
            value = -value;
        }
    
        return value;
    }
    
    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}

class SymbolTable {
    private java.util.Map<String, Integer> variables = new java.util.HashMap<>();

    public void assign(String name, int value) {
        variables.put(name, value);
    }

    public int resolve(String name) {
        if (!variables.containsKey(name)) {
            throw new RuntimeException("Undefined variable: " + name);
        }
        return variables.get(name);
    }

    public java.util.Set<String> getVariableNames() {
        return variables.keySet();
    }
}