package huan.ui;

import huan.tasks.TaskList;
import java.util.Scanner;

public class Ui {
    private Scanner scanner;
    
    /**
     * A line divider.
     */
    public static final String LINE = "____________________________________________________________";

    /**
     * Indentation spaces for formatting.
     */
    public static final String SPACE = "    ";

    /**
     * Handles user interaction: printing messages, formatting output, and reading input.
     */
    public void showLine() {
        System.out.println(SPACE + LINE);
    }

    /**
     * Prints the greeting.
     */
    public void printGreeting() {
        String greeting =  "Hello! I'm HUAN \n"
                + SPACE + "What can I do for you?";
        printFormat(greeting);
    }

    /**
     * Prints the exit.
     */
    public void printExit() {
        String exit = "Bye. Hope to see you again soon!";
        printFormat(exit);
    }

    /**
     * Formats the output string with indentation and divider lines.
     *
     * @param message The message to be formatted.
     */
    public static void printFormat(String message) {
        System.out.println(SPACE + LINE);
        System.out.println(SPACE + message);
        System.out.println(SPACE + LINE);
    }

    /**
     * Initialises the scanner and reads the trimmed input.
     *
     * @return The trimmed input.
     */
    public String readInput() {
        this.scanner = new Scanner(System.in);
        return scanner.nextLine().trim();
    }

    /**
     * Closes the scanner.
     */
    public void closeScanner() {
        this.scanner.close();
    }

    /**
     * Prints the list of tasks in required format
     *
     * @param tasks The tasklist to get the tasks from.
     */
    public void showTaskList(TaskList tasks) {
        showLine();
        if (tasks.isEmpty()) {
            System.out.println(SPACE + "No tasks added yet.");
        } else {
            System.out.println(SPACE + "Here are the tasks in your list:");
            for (int i = 0; i < tasks.getSize(); i++) {
                System.out.println(SPACE + (i + 1) + ". " + tasks.getTask(i).toString());
            }
        }
        showLine();
    }

    /**
     * Prints an error message indicating tasks could not be loaded from file.
     */
    public void showLoadingError() {
        printFormat("Error: Unable to load tasks from file!");
    }
}
