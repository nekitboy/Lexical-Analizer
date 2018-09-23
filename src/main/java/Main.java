import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
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

