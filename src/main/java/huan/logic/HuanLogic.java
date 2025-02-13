package huan.logic;

import huan.command.Command;
import huan.exception.HuanException;
import huan.parser.Parser;
import huan.storage.Storage;
import huan.tasks.TaskList;
import javafx.application.Platform;

/**
 * This class processes user commands and returns responses.
 */
public class HuanLogic {
    private huan.storage.Storage storage;
    private TaskList tasks;
    private static final String FILE_PATH = "data/huan.txt";

    /**
     * Activates the bot by loading tasks from storage.
     *
     */
    public HuanLogic() {
        storage = new Storage(FILE_PATH);
        assert (storage != null) : "Storage should not be null";
        try {
            tasks = storage.loadTasks();

        } catch (HuanException e) {
            tasks = new TaskList();
        }
        assert (tasks != null) : "Tasks should not be null";
    }
    /**
     * Processes the user input and returns a response string by delegating to Command classes.
     *
     * @param input User input.
     * @return Response message.
     */
    public String getResponse(String input) {
        try {
            Command command = Parser.parseInput(input);
            assert (command != null) : "Command should not be null";
            String response = command.execute(tasks, storage);
            if (command.isExit()) {
                Platform.exit();
            }
            return response;

        } catch (HuanException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }
}
