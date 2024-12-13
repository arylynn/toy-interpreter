import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Interpreter {
    public static void main(String[] args) {
        String inputFile;

        if (args.length == 1) {
            inputFile = args[0];
        } else {
            Scanner scan = new Scanner(System.in);
            System.out.print("Enter the path to the input file: ");
            inputFile = scan.nextLine();
            scan.close();
        }
        
        String outputFile = "output.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            
            StringBuilder inputBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                inputBuilder.append(line).append("\n");
            }

            String input = inputBuilder.toString().trim();

            try {
                Tokenizer tokenizer = new Tokenizer();
                List<Tokenizer.Token> tokens = tokenizer.tokenize(input);
                
                Parser parser = new Parser(tokens);
                parser.parse();
                
                SymbolTable symbolTable = parser.getSymbolTable();
                
                boolean variablesPrinted = false;
                for (String varName : symbolTable.getVariableNames()) {
                    int value = symbolTable.resolve(varName);
                    String outputLine = varName + " = " + value;
                    writer.write(outputLine);
                    writer.newLine();
                    variablesPrinted = true;
                }
                
                if (!variablesPrinted) {
                    writer.write("Error");
                    writer.newLine();
                }

                System.out.println("Interpretation complete. Results written to " + outputFile);
            } catch (RuntimeException e) {
                writer.write("Error: " + e.getMessage());
                writer.newLine();
                
                System.out.println("Error during interpretation. Check " + outputFile);
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            System.exit(1);
        }
    }
}