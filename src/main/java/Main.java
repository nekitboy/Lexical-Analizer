import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        productionRun();
        //developerRun();
    }

    // Running version of application for production
    static void productionRun() throws IOException {
        LexicalAnalyzer analyzer = new LexicalAnalyzer();

        try {
            analyzer.parse(readFile("in.txt"));
        } catch (Exception ex) {
            System.out.println(ex.toString().trim());
        }
        System.out.print("***** PRESS ENTER TO GET NEXT TOKEN *****");
        while (analyzer.hasNextToken()) {
            Scanner in = new Scanner(System.in);
            if (in.hasNextLine())
                System.out.print(analyzer.nextToken());
        }

        if (!analyzer.hasNextToken()){
            System.out.println("\n***** NO MORE TOKENS *****");
        }

    }

    // For lexer test in development
    static void developerRun() throws IOException {
        LexicalAnalyzer analyzer = new LexicalAnalyzer();
        StringBuilder output = new StringBuilder();
        try {
            analyzer.parse(readFile("in.txt"));
        } catch (Exception ex) {
            output.append(ex.toString());
            BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
            out.write(output.toString().trim());
            out.close();
            return;
        }

        while (analyzer.hasNextToken())
            output.append(analyzer.nextToken() + "\n");
        BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
        out.write(output.toString().trim());
        out.close();

        System.out.println("List of all tokens of the in.txt file are available in out.txt");
    }

    // Make string from file
    private static StringBuilder readFile(String path) {
        StringBuilder b = new StringBuilder();
        try {
            Scanner in = new Scanner(new File(path));
            while (in.hasNextLine()) {
                b.append(in.nextLine());
                b.append("\n");
            }
            if (b.length()>0)
                b.deleteCharAt(b.length()-1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }
}

