import java.time.LocalTime;

public class Stats {

    private Boolean inSpan;
    private LocalTime start, end;

    private Long successes, fails;
    private Float minAvail;

    public Stats(Float minAvail) {
        this.minAvail = minAvail;
        successes = fails = 0l;
        inSpan = false;
    }

    public Float predictAvail() {
        return 100 * (float) (successes + 1) / (successes + fails + 1);
    }

    public Float calcAvail() {
        return 100 * (float) successes / (successes + fails);
    }

    public void process(LocalTime time, Boolean isFailure) {
        if (isFailure) {
            fails++;
        } else {
            if (inSpan && predictAvail() >= minAvail) {
                inSpan = false;
                System.out.println(this);
            }
            successes++;
        }

        if (inSpan) {
            end = time;
        }
        if (calcAvail() < minAvail && !inSpan) {
            inSpan = true;
            start = end = time;
        }
    }

    @Override
    public String toString() {
        return String.format("%s\t%s\t%.2f",
                             start, end, calcAvail());
    }

    public Boolean getInSpan() {
        return inSpan;
    }
}
