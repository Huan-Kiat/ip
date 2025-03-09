package huan.parser;

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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {

    @Test
    void parseInput_validCommands_returnsCorrectCommandType() {
        assertTrue(Parser.parseInput("todo read book") instanceof TodoCommand);
        assertTrue(Parser.parseInput("deadline submit report /by 2023-10-10 1800") instanceof DeadlineCommand);
        assertTrue(Parser.parseInput("event meeting /from 2023-10-10 0900 /to 2023-10-10 1000")
                instanceof EventCommand);
        assertTrue(Parser.parseInput("mark 1") instanceof MarkCommand);
        assertTrue(Parser.parseInput("unmark 2") instanceof UnmarkCommand);
        assertTrue(Parser.parseInput("delete 3") instanceof DeleteCommand);
        assertTrue(Parser.parseInput("on 2023-10-10") instanceof OnCommand);
        assertTrue(Parser.parseInput("bye") instanceof ExitCommand);
        assertTrue(Parser.parseInput("list") instanceof ListCommand);
        assertTrue(Parser.parseInput("find meeting") instanceof FindCommand);
    }

    @Test
    void parseInput_invalidCommands_returnsInvalidCommand() {
        assertTrue(Parser.parseInput("wrong command") instanceof InvalidCommand);
        assertTrue(Parser.parseInput("") instanceof InvalidCommand);
        assertTrue(Parser.parseInput("   ") instanceof InvalidCommand);
        assertTrue(Parser.parseInput(null) instanceof InvalidCommand);
    }

    @Test
    void parseInput_markInvalidNumber_returnsInvalidCommand() {
        assertTrue(Parser.parseInput("mark abc") instanceof InvalidCommand);
        assertTrue(Parser.parseInput("unmark xyz") instanceof InvalidCommand);
        assertTrue(Parser.parseInput("delete one") instanceof InvalidCommand);
    }

    @Test
    void parseDeadline_invalidInput_returnsInvalidCommand() {
        assertTrue(Parser.parseInput("deadline deadline Test") instanceof InvalidCommand);
        assertTrue(Parser.parseInput("deadline /by 2023-10-10 1800") instanceof InvalidCommand);
    }

    @Test
    void parseEvent_invalidInput_returnsInvalidCommand() {
        assertTrue(Parser.parseInput("event event Test /to 2023-10-10 1000") instanceof InvalidCommand);
        assertTrue(Parser.parseInput("event /from 2023-10-10 0900 /to 2023-10-10 1000") instanceof InvalidCommand);
    }

    @Test
    void parseOnCommand_invalidInput_returnsInvalidCommand() {
        assertTrue(Parser.parseInput("on") instanceof InvalidCommand);
    }

    @Test
    void parseFindCommand_invalidInput_returnsInvalidCommand() {
        assertTrue(Parser.parseInput("find") instanceof InvalidCommand);
    }
}
