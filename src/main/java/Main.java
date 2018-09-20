import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        LexicalAnalyzer analyzer = new LexicalAnalyzer(readFile("in.txt"));
        while (analyzer.hasNextToken())
            System.out.println(analyzer.nextToken());
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

