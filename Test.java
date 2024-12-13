import java.util.ArrayList;
import java.util.List;

public class Test {
    private static List<String> failedTests = new ArrayList<>();
    private static int totalTests = 0;
    private static int passedTests = 0;

    public static void main(String[] args) {
        testSimpleAssignment();
        testUnderscoreVariableName();
        testLeadingZeroRejection();
        testComplexNegation();
        testMultipleNegations();
        testArithmeticOperations();
        testUndefinedVariableResolution();
        testParenthesesAndNegation();
        testConsecutiveAssignments();
        printTestResults();
    }

    private static void testSimpleAssignment() {
        testStart("Simple Assignment");
        try {
            String input = "x = 5;";
            List<Tokenizer.Token> tokens = new Tokenizer().tokenize(input);
            Parser parser = new Parser(tokens);
            parser.parse();
            assertEquals(5, parser.getSymbolTable().resolve("x"), "Simple assignment failed");
            testPass();
        } catch (Exception e) {
            testFail(e);
        }
    }

    private static void testUnderscoreVariableName() {
        testStart("Underscore Variable Name");
        try {
            String input = "x_2 = 0;";
            List<Tokenizer.Token> tokens = new Tokenizer().tokenize(input);
            Parser parser = new Parser(tokens);
            parser.parse();
            assertEquals(0, parser.getSymbolTable().resolve("x_2"), "Underscore variable name failed");
            testPass();
        } catch (Exception e) {
            testFail(e);
        }
    }

    private static void testLeadingZeroRejection() {
        testStart("Leading Zero Rejection");
        try {
            String input = "x = 001;";
            new Tokenizer().tokenize(input);
            testFail("Failed to reject leading zeros");
        } catch (IllegalArgumentException e) {
            testPass();
        } catch (Exception e) {
            testFail(e);
        }
    }

    private static void testComplexNegation() {
        testStart("Complex Negation");
        try {
            String input = "x = 1; y = 2; z = ---(x+y)*(x+-y);";
            List<Tokenizer.Token> tokens = new Tokenizer().tokenize(input);
            Parser parser = new Parser(tokens);
            parser.parse();
            assertEquals(1, parser.getSymbolTable().resolve("x"), "x value incorrect");
            assertEquals(2, parser.getSymbolTable().resolve("y"), "y value incorrect");
            assertEquals(3, parser.getSymbolTable().resolve("z"), "z value incorrect");
            testPass();
        } catch (Exception e) {
            testFail(e);
        }
    }

    private static void testMultipleNegations() {
        testStart("Multiple Negations");
        try {
            String input = "a = -(-5); b = +(-5);";
            List<Tokenizer.Token> tokens = new Tokenizer().tokenize(input);
            Parser parser = new Parser(tokens);
            parser.parse();
            assertEquals(5, parser.getSymbolTable().resolve("a"), "Double negation failed");
            assertEquals(-5, parser.getSymbolTable().resolve("b"), "Unary plus with negation failed");
            testPass();
        } catch (Exception e) {
            testFail(e);
        }
    }

    private static void testArithmeticOperations() {
        testStart("Arithmetic Operations");
        try {
            String input = "x = 1 + 2 * 3; y = (4 - 2) * 2;";
            List<Tokenizer.Token> tokens = new Tokenizer().tokenize(input);
            Parser parser = new Parser(tokens);
            parser.parse();
            assertEquals(7, parser.getSymbolTable().resolve("x"), "Complex arithmetic failed");
            assertEquals(4, parser.getSymbolTable().resolve("y"), "Parentheses arithmetic failed");
            testPass();
        } catch (Exception e) {
            testFail(e);
        }
    }

    private static void testUndefinedVariableResolution() {
        testStart("Undefined Variable Resolution");
        try {
            String input = "x = y;";
            List<Tokenizer.Token> tokens = new Tokenizer().tokenize(input);
            Parser parser = new Parser(tokens);
            parser.parse();
            testFail("Failed to catch undefined variable");
        } catch (RuntimeException e) {
            testPass();
        } catch (Exception e) {
            testFail(e);
        }
    }

    private static void testParenthesesAndNegation() {
        testStart("Parentheses and Negation");
        try {
            String input = "x = -(2 + 3);";
            List<Tokenizer.Token> tokens = new Tokenizer().tokenize(input);
            Parser parser = new Parser(tokens);
            parser.parse();
            assertEquals(-5, parser.getSymbolTable().resolve("x"), "Negation with parentheses failed");
            testPass();
        } catch (Exception e) {
            testFail(e);
        }
    }

    private static void testConsecutiveAssignments() {
        testStart("Consecutive Assignments");
        try {
            String input = "x = 1; y = x; z = x + y;";
            List<Tokenizer.Token> tokens = new Tokenizer().tokenize(input);
            Parser parser = new Parser(tokens);
            parser.parse();
            assertEquals(1, parser.getSymbolTable().resolve("x"), "First assignment failed");
            assertEquals(1, parser.getSymbolTable().resolve("y"), "Second assignment failed");
            assertEquals(2, parser.getSymbolTable().resolve("z"), "Third assignment failed");
            testPass();
        } catch (Exception e) {
            testFail(e);
        }
    }

    private static void testStart(String testName) {
        totalTests++;
        System.out.println("Running test: " + testName);
    }

    private static void testPass() {
        passedTests++;
        System.out.println("  PASS");
    }

    private static void testFail(String message) {
        failedTests.add(message);
        System.out.println("  FAIL: " + message);
    }

    private static void testFail(Exception e) {
        failedTests.add(e.getMessage());
        System.out.println("  FAIL: " + e.getMessage());
        e.printStackTrace();
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + ". Expected: " + expected + ", but got: " + actual);
        }
    }

    private static void printTestResults() {
        System.out.println("\n--- Test Results ---");
        System.out.println("Total Tests: " + totalTests);
        System.out.println("Passed Tests: " + passedTests);
        System.out.println("Failed Tests: " + failedTests.size());
        if (!failedTests.isEmpty()) {
            System.out.println("\nFailed Tests:");
            for (String failedTest : failedTests) {
                System.out.println("- " + failedTest);
            }
            System.exit(1);
        }
    }
}
