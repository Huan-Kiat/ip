package huan.parser;

import huan.command.Command;
import huan.command.DeadlineCommand;
import huan.command.DeleteCommand;
import huan.command.EventCommand;
import huan.command.ExitCommand;
import huan.command.FindCommand;
import huan.command.InvalidCommand;
import huan.command.ListCommand;
import huan.command.MarkCommand;
import huan.command.OnCommand;
import huan.command.TodoCommand;
import huan.command.UnmarkCommand;

/**
 * Handles parsing of user commands and input.
 */
public class Parser {
    private static final String INTEGER_ERROR = "Task number must be an integer!";
    private static final String DATETIME_FORMAT = "<yyyy-MM-dd HHmm>";
    /**
     * Parses the user input to determine the input type.
     *
     * @param input Input string.
     * @return The corresponding Command.
     */
    public static Command parseInput(String input) {
        assert (input != null) : "Input should not be null";
        if (input.isEmpty()) {
            return new InvalidCommand("Invalid input!");
        }
        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();
        String remainder = (parts.length > 1) ? parts[1].trim() : "";

        switch (command) {
        case "bye":
            return new ExitCommand();
        case "list":
            return new ListCommand();
        case "mark": {
            if (remainder.isEmpty()) {
                return new InvalidCommand("Include task number to mark!");
            }
            try {
                int taskNum = Integer.parseInt(remainder);
                return new MarkCommand(taskNum);
            } catch (NumberFormatException e) {
                return new InvalidCommand(INTEGER_ERROR);
            }
        }
        case "unmark": {
            if (remainder.isEmpty()) {
                return new InvalidCommand("Include task number to unmark!");
            }
            try {
                int taskNum = Integer.parseInt(remainder);
                return new UnmarkCommand(taskNum);
            } catch (NumberFormatException e) {
                return new InvalidCommand(INTEGER_ERROR);
            }
        }
        case "delete": {
            if (remainder.isEmpty()) {
                return new InvalidCommand("Include task number to delete!");
            }
            try {
                int taskNum = Integer.parseInt(remainder);
                return new DeleteCommand(taskNum);
            } catch (NumberFormatException e) {
                return new InvalidCommand(INTEGER_ERROR);
            }
        }
        case "todo":
            return new TodoCommand(remainder);
        case "deadline": {
            String[] deadlineParts = remainder.split("/by", 2);
            if (deadlineParts.length < 2) {
                return new InvalidCommand("Follow format: deadline <desc> /by " + DATETIME_FORMAT);
            }
            String desc = deadlineParts[0].trim();
            String by = deadlineParts[1].trim();
            return new DeadlineCommand(desc, by);
        }
        case "event": {
            String[] fromParts = remainder.split("/from", 2);
            if (fromParts.length < 2) {
                return new InvalidCommand(
                        "Follow format: event <desc> /from " + DATETIME_FORMAT + " /to " + DATETIME_FORMAT
                );
            }
            String desc = fromParts[0].trim();

            String[] toParts = fromParts[1].split("/to", 2);
            if (toParts.length < 2) {
                return new InvalidCommand(
                        "Follow format: event <desc> /from " + DATETIME_FORMAT + " /to " + DATETIME_FORMAT
                );
            }
            String from = toParts[0].trim();
            String to = toParts[1].trim();
            return new EventCommand(desc, from, to);
        }
        case "on":
            if (remainder.isEmpty()) {
                return new InvalidCommand("Follow format: on " + DATETIME_FORMAT);
            }
            return new OnCommand(remainder);
        case "find":
            if (remainder.isEmpty()) {
                return new InvalidCommand("What do you want to find?");
            }
            return new FindCommand(remainder);

        default:
            return new InvalidCommand("Invalid input!");
        }
    }
}
