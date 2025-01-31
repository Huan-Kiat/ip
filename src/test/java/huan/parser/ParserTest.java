package huan.parser;

import huan.exception.HuanException;
import huan.tasks.TaskList;
import huan.ui.Huan;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    private TaskList tasks = new TaskList();

    @Test
    void parseInput_validCommands_returnsCorrectInputType() {
        assertEquals(Huan.InputType.TODO, Parser.parseInput("todo read book"));
        assertEquals(Huan.InputType.DEADLINE, Parser.parseInput("deadline submit report /by 2023-10-10 1800"));
        assertEquals(Huan.InputType.EVENT, Parser.parseInput("event meeting /from 2023-10-10 0900 /to 2023-10-10 1000"));
        assertEquals(Huan.InputType.MARK, Parser.parseInput("mark 1"));
        assertEquals(Huan.InputType.UNMARK, Parser.parseInput("unmark 2"));
        assertEquals(Huan.InputType.DELETE, Parser.parseInput("delete 3"));
        assertEquals(Huan.InputType.ON, Parser.parseInput("on 2023-10-10"));
        assertEquals(Huan.InputType.BYE, Parser.parseInput("bye"));
        assertEquals(Huan.InputType.LIST, Parser.parseInput("list"));
    }

    @Test
    void parseInput_invalidCommands_returnsInvalid() {
        assertEquals(Huan.InputType.INVALID, Parser.parseInput("wrong command"));
        assertEquals(Huan.InputType.INVALID, Parser.parseInput(""));
        assertEquals(Huan.InputType.INVALID, Parser.parseInput("   "));
        assertEquals(Huan.InputType.INVALID, Parser.parseInput(null));
    }

    @Test
    void parseToDo_invalidInput_throwsHuanException() {
        String input = "todo ";
        assertThrows(HuanException.class, () -> Parser.parseToDo(tasks, input));

        String input2 = "todo"; // No space after 'todo'
        assertThrows(HuanException.class, () -> Parser.parseToDo(tasks, input2));
    }

    @Test
    void parseDeadline_invalidInput_throwsHuanException_missingByKeyword() {
        String input = "deadline deadline Test"; // Missing /by keyword
        assertThrows(HuanException.class, () -> Parser.parseDeadline(tasks, input));
    }

    @Test
    void parseDeadline_invalidInput_throwsHuanException_missingDescription() {
        String input = "deadline /by 2023-10-10 1800"; // Missing description
        assertThrows(HuanException.class, () -> Parser.parseDeadline(tasks, input));
    }

    @Test
    void parseEvent_invalidInput_throwsHuanException_missingFromKeyword() {
        String input = "event event Test /to 2023-10-10 1000"; // Missing /from keyword
        assertThrows(HuanException.class, () -> Parser.parseEvent(tasks, input));
    }

    @Test
    void parseEvent_invalidInput_throwsHuanException_missingDescription() {
        String input = "event /from 2023-10-10 0900 /to 2023-10-10 1000"; // Missing description
        assertThrows(HuanException.class, () -> Parser.parseEvent(tasks, input));
    }


}

