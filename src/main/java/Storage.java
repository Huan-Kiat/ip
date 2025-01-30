import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    /**
     * This method does several things:
     *  Ensure that parent directory exists, create it if it does not exist
     *  Create file if it does not exist
     *  Read each line and parses the input
     */
    public void loadTasks() {
        try {
            TaskList tasks = new TaskList();
            File file = new File(filePath);
            //Create parent directory if missing
            file.getParentFile().mkdirs();

            //Create file if missing
            if (!file.exists()) {
                file.createNewFile();
                return;
            }

            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String task = scanner.nextLine().trim();
                String[] parts = task.split("\\|");
                //Check if format of task is correct
                if (parts.length < 3) {
                    System.out.println("Incorrect task format: " + task);
                    continue;
                }

                String type = parts[0].trim();
                String done = parts[1].trim();
                String description = parts[2].trim();

                switch (type) {
                case "T":
                    tasks.addTask(new Todo(description));
                    break;
                case "D":
                    if (parts.length != 4) {
                        System.out.println("Incorrect deadline format: " + task);
                        continue;
                    }
                    String by = parts[3].trim();
                    tasks.addTask(new Deadline(description, by));
                    break;
                case "E":
                    if (parts.length != 4) {
                        System.out.println("Incorrect event format: " + task);
                        continue;
                    }
                    String time = parts[3].trim();
                    String[] timeParts = time.split(" to ", 2);
                    if (timeParts.length != 2) {
                        System.out.println("Incorrect event time format: " + task);
                    }
                    String from = timeParts[0];
                    String to = timeParts[1];
                    tasks.addTask(new Event(description, from, to));
                    break;
                default:
                    System.out.println("Invalid Task: " + task);
                    continue;
                }

                if (done.equals("1")) {
                    Task newTask = tasks.getTask(tasks.getSize() - 1);
                    newTask.markAsDone();
                }

            }
            scanner.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method is responsible for writing user input into the file whenever:
     *  A task is added
     *  A task is marked
     */
    public void writeTasks() {
        try {
            TaskList tasks = new TaskList();
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file);

            for (Task task : tasks.getTaskList()) {
                if (task instanceof Todo t) {
                    // T | 1 | read book
                    writer.write("T | " + (t.isDone ? "1" : "0") + " | " + t.description);
                } else if (task instanceof Deadline d) {
                    // D | 0 | return book | June 6th
                    writer.write("D | " + (d.isDone ? "1" : "0")
                            + " | " + d.description + " | "
                            + d.by.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")));
                } else if (task instanceof Event e) {
                    // E | 0 | project meeting | Aug 6th 2-4pm
                    writer.write("E | " + (e.isDone ? "1" : "0")
                            + " | " + e.description + " | "
                            + e.from.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"))
                            + " to "
                            + e.to.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm")));
                }
                writer.write(System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
