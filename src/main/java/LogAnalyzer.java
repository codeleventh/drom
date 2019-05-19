import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LogAnalyzer {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" dd/MM/yyyy:H:m:s");
    private BufferedReader reader;
    private Float maxTime;
    private Float availability;

    LogAnalyzer(BufferedReader reader, Float maxTime, Float availability) {
        this.reader = reader;
        this.maxTime = maxTime;
        this.availability = availability;
    }

    public void parse() throws IOException {
        Long lineNumber = 0L;
        try {
            String s;
            Stats stats = new Stats(availability);
            while ((s = reader.readLine()) != null) {
                lineNumber++;

                Boolean isFault = isFault(s);
                LocalTime time = getTime(s);

                stats.process(time, isFault);
                //                System.out.println(
                //                    String.format("%4d: %s\t%4s -- %.2f",
                //                                  lineNumber,
                //                                  time,
                //                                  (isFault ? "FAIL" : "OK"),
                //                                  stats.calcAvail()));
            }
            if (stats.getInSpan()) {
                System.out.println(stats);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Wrong format at line " + lineNumber);
        }
    }

    private LocalTime getTime(String line) throws IllegalArgumentException {
        try {
            String[] s = line.split(" ");
            return LocalDateTime.parse(s[3].replace('[', ' '), formatter).toLocalTime();
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    private Boolean isFault(String line) throws IllegalArgumentException {
        try {
            String[] s = line.split(" ");
            Integer code = new Integer(s[8]);
            Float time = new Float(s[10]);
            return code == 500 || time > maxTime;
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }
}
