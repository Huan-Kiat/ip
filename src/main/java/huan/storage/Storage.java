package huan.storage;

import huan.exception.HuanException;
import huan.tasks.Deadline;
import huan.tasks.Event;
import huan.tasks.Task;
import huan.tasks.TaskList;
import huan.tasks.Todo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    public TaskList loadTasks() throws HuanException {
        try {
            TaskList tasks = new TaskList();
            File file = new File(filePath);
            //Create parent directory if missing
            file.getParentFile().mkdirs();

            //Create file if missing
            if (!file.exists()) {
                file.createNewFile();
                return tasks;
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
                    tasks.loadTask(new Todo(description));
                    break;
                case "D":
                    if (parts.length != 4) {
                        System.out.println("Incorrect deadline format: " + task);
                        continue;
                    }
                    String by = parts[3].trim();
                    tasks.loadTask(new Deadline(description, by));
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
                    tasks.loadTask(new Event(description, from, to));
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
            return tasks;
        } catch (IOException e) {
            throw new HuanException();
        }
    }

    /**
     * This method is responsible for writing user input into the file whenever:
     *  A task is added
     *  A task is marked
     */
    public void writeTasks(TaskList tasks) {
        try {
            File file = new File(filePath);
            FileWriter writer = new FileWriter(file);

            for (Task task : tasks.getTaskList()) {
                writer.write(task.fileFormat() + System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
