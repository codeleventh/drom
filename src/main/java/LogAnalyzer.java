import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogAnalyzer {

    BufferedReader reader;
    Float maxTime;
    Float availability;

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" dd/MM/yyyy:H:m:s");

    public LogAnalyzer(BufferedReader reader, Float maxTime, Float availability) {
        this.reader = reader;
        this.maxTime = maxTime;
        this.availability = availability;
    }

    void parse() throws IOException {
        Long lineNumber = 0l;
        try {
            String s;
            Stats stats = new Stats(availability);
            while ((s = reader.readLine()) != null) {
                lineNumber++;

                Boolean isFault = isFault(s);
                LocalTime time = getTime(s);

                stats.process(time, isFault);

                //                System.out.println(
                //                    String.format("%4d: %s\t%s -- %.2f",
                //                                  lineNumber,
                //                                  time.toLocalTime(),
                //                                  (isFault ? "FAIL" : "OK  "),
                //                                  stats.calcAvail()));
            }
            if (stats.getInSpan()) {
                System.out.println(stats);
            }
        } catch (IOException e) { // TODO:
            System.err.println("Error in line " + lineNumber + ": " + e.getMessage());
        }
    }

    LocalTime getTime(String line) {
        // TODO: appropriate split && error handling
        String[] s = line.split(" ");
        LocalTime time = LocalDateTime.parse(s[3].replace('[', ' '), formatter).toLocalTime();
        return time;
    }

    Boolean isFault(String line) {
        // TODO: appropriate split && error handling
        String[] s = line.split(" ");
        Integer code = new Integer(s[8]);
        Float time = new Float(s[10]);
        if (code == 500 || time > maxTime) {
            return true;
        } else {
            return false;
        }
    }
}
