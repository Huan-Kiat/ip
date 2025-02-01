package huan.ui;

import huan.exception.HuanException;
import huan.parser.Parser;
import huan.storage.Storage;
import huan.tasks.TaskList;
import java.time.format.DateTimeParseException;

public class Huan {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private static final String FILE_PATH = "data/huan.txt";

    /**
     * Enum for available inputs.
     */
    public enum InputType {
        BYE, LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, INVALID, ON, FIND
    }

    /**
     * Activate the bot
     */
    public Huan(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = storage.loadTasks();
        } catch (HuanException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }


    /**
     * run() method to de-clutter main method
     */
    public void run() {
        ui.printGreeting();

        while (true) {
            String input = ui.readInput();
            InputType command = Parser.parseInput(input);

            try {
                switch (command) {
                case BYE:
                    ui.printExit();
                    ui.closeScanner();
                    return;
                case LIST:
                    ui.showTaskList(tasks);
                    break;
                case MARK:
                    String[] markParts = input.split(" ", 2);
                    if (markParts.length != 2){
                        throw new HuanException("Include task number to mark!");
                    }
                    int markId = Integer.parseInt(markParts[1]);
                    tasks.markTask(markId);
                    storage.writeTasks(tasks);
                    break;
                case UNMARK:
                    String[] unmarkParts = input.split(" ", 2);
                    if (unmarkParts.length != 2){
                        throw new HuanException("Include task number to unmark!");
                    }
                    int unmarkId = Integer.parseInt(unmarkParts[1]);
                    tasks.unmarkTask(unmarkId);
                    storage.writeTasks(tasks);
                    break;
                case DELETE:
                    String[] deleteParts = input.split(" ", 2);
                    if (deleteParts.length != 2){
                        throw new HuanException("Include task number to delete!");
                    }
                    int deleteId = Integer.parseInt(deleteParts[1]);
                    tasks.deleteTask(deleteId);
                    storage.writeTasks(tasks);
                    break;
                case TODO:
                    Parser.parseToDo(tasks, input);
                    storage.writeTasks(tasks);
                    break;
                case DEADLINE:
                    Parser.parseDeadline(tasks, input);
                    storage.writeTasks(tasks);
                    break;
                case EVENT:
                    Parser.parseEvent(tasks, input);
                    storage.writeTasks(tasks);
                    break;
                case ON:
                    String[] onParts = input.split(" ", 2);
                    if (onParts.length < 2) {
                        throw new HuanException("Follow format: on yyyy-MM-dd");
                    }
                    tasks.onDate(onParts[1].trim());
                    break;
                case FIND:
                    String[] findParts = input.split(" ", 2);
                    if (findParts.length < 2) {
                        throw new HuanException("What do you want to find?");
                    }
                    String keyword = findParts[1].trim();
                    tasks.findTasks(keyword);
                    break;
                case INVALID:
                    Ui.printFormat("Invalid input!");
                    break;
                }
            } catch (HuanException e) {
                Ui.printFormat("Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                Ui.printFormat("Error: Please input an integer for the Task number");
            } catch (DateTimeParseException e){
                Ui.printFormat("Error: Ensure you follow the format (yyyy-mm-dd HHmm)");
            } catch (Exception e) {
                Ui.printFormat("Error: OOPS! Something went wrong!" + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Huan(FILE_PATH).run();
    }
}