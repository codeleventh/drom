import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Drom {

    public static void main(String[] args) throws IOException { // TODO: exception
        //BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

        Float maxTime, availability;

        Options options = new Options();
        options.addOption(Option.builder("t").hasArg().argName("ms")
                              .desc("max response time").required().build());
        options.addOption(Option.builder("u").hasArg().argName("N")
                              .desc("percentage of succesful responses").required().build());

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine cmd = parser.parse(options, args);
            maxTime = Float.valueOf(cmd.getOptionValue('t'));
            availability = Float.valueOf(cmd.getOptionValue('u'));
            if (availability > 100.0f) {
                throw new IllegalArgumentException();
            }

            BufferedReader stdin = new BufferedReader(new FileReader("p:\\drom\\src\\test\\resources\\access.log"));
            LogAnalyzer analyzer = new LogAnalyzer(stdin, maxTime, availability);
            analyzer.parse();
        } catch (ParseException | IllegalArgumentException e) {
            System.out.println("Wrong args.");
            new HelpFormatter().printHelp("java -jar drom.jar", options);
            System.exit(2);
        }
    }

}