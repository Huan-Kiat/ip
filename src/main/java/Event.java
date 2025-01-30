import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = LocalDateTime.parse(from, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
        this.to = LocalDateTime.parse(to, DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: "
                + from.format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mma"))
                + " to: "
                + to.format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mma")) + ")";
    }

    @Override
    public String fileFormat() {
        return "E | " + (isDone ? "1" : "0")
                + " | " + description + " | "
                + from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"))
                + " to "
                + to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"));
    }
}
