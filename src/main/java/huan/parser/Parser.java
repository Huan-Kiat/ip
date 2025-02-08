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
import huan.exception.HuanException;
import huan.tasks.TaskList;

/**
 * Handles parsing of user commands and input.
 */
public class Parser {

    /**
     * Parse the user input to determine the input type.
     *
     * @param input Input string.
     * @return The corresponding Command.
     */
    public static Command parseInput(String input) {
        if (input == null || input.isEmpty()) {
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
                return new InvalidCommand("Task number must be an integer!");
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
                return new InvalidCommand("Task number must be an integer!");
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
                return new InvalidCommand("Task number must be an integer!");
            }
        }
        case "todo":
            // remainder = "description"
            return new TodoCommand(remainder);
        case "deadline": {
            // expect "deadline <desc> /by <yyyy-MM-dd HHmm>"
            String[] deadlineParts = remainder.split("/by", 2);
            if (deadlineParts.length < 2) {
                return new InvalidCommand("Follow format: deadline <desc> /by <yyyy-MM-dd HHmm>");
            }
            String desc = deadlineParts[0].trim();
            String by = deadlineParts[1].trim();
            return new DeadlineCommand(desc, by);
        }
        case "event": {
            // expect "event <desc> /from <start> /to <end>"
            // e.g. event project meeting /from 2025-02-08 1000 /to 2025-02-08 1200
            String[] fromParts = remainder.split("/from", 2);
            if (fromParts.length < 2) {
                return new InvalidCommand(
                        "Follow format: event <desc> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>"
                );
            }
            String desc = fromParts[0].trim();

            String[] toParts = fromParts[1].split("/to", 2);
            if (toParts.length < 2) {
                return new InvalidCommand(
                        "Follow format: event <desc> /from <yyyy-MM-dd HHmm> /to <yyyy-MM-dd HHmm>"
                );
            }
            String from = toParts[0].trim();
            String to = toParts[1].trim();
            return new EventCommand(desc, from, to);
        }
        case "on":
            if (remainder.isEmpty()) {
                return new InvalidCommand("Follow format: on yyyy-MM-dd");
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

    /**
     * Parses user input for a deadline and returns the confirmation message.
     *
     * @param tasks Task list.
     * @param input User input.
     * @return The confirmation message.
     * @throws HuanException for invalid formats.
     */
    public static String parseDeadline(TaskList tasks, String input) throws HuanException {
        String[] deadlineParts = input.split("/by", 2);
        if (deadlineParts.length < 2 || deadlineParts[0].substring(9).trim().isEmpty()
                || deadlineParts[1].trim().isEmpty()) {
            throw new HuanException("Follow format \"deadline (description) /by (yyyy-MM-dd HHmm)\"");
        }
        String deadlineDescription = deadlineParts[0].substring(9).trim();
        String by = deadlineParts[1].trim();
        return tasks.addDeadline(deadlineDescription, by);
    }

    /**
     * Parses user input for a todo and returns the confirmation message.
     *
     * @param tasks Task list.
     * @param input User input.
     * @return The confirmation message.
     * @throws HuanException for invalid formats.
     */
    public static String parseToDo(TaskList tasks, String input) throws HuanException {
        if (input.trim().length() <= 4) {
            throw new HuanException("Todo description cannot be empty!");
        }
        String todoDescription = input.substring(5).trim();
        return tasks.addTodo(todoDescription);
    }

    /**
     * Parses user input for an event and returns the confirmation message.
     *
     * @param tasks Task list.
     * @param input User input.
     * @return The confirmation message.
     * @throws HuanException for invalid formats.
     */
    public static String parseEvent(TaskList tasks, String input) throws HuanException {
        String[] fromParts = input.split("/from", 2);
        if (fromParts.length < 2 || fromParts[0].substring(6).trim().isEmpty()) {
            throw new HuanException("Follow format "
                    + "\"event (description) /from (yyyy-MM-dd HHmm) /to (yyyy-MM-dd HHmm)\"");
        }
        String eventDescription = fromParts[0].substring(6).trim();
        String[] toParts = fromParts[1].split("/to", 2);
        if (toParts.length < 2 || toParts[0].trim().isEmpty() || toParts[1].trim().isEmpty()) {
            throw new HuanException("Follow format "
                    + "\"event (description) /from (yyyy-MM-dd HHmm) /to (yyyy-MM-dd HHmm)\"");
        }
        String from = toParts[0].trim();
        String to = toParts[1].trim();
        return tasks.addEvent(eventDescription, from, to);
    }
}
