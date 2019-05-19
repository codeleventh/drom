import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Drom {

    public static void main(String[] args) throws IOException { // TODO: exception
        //        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader stdin = new BufferedReader(new FileReader("p:\\drom\\src\\test\\resources\\access.log"));
        LogAnalyzer analyzer = new LogAnalyzer(stdin, 45f, 99f);
        analyzer.parse();
    }

}