package huan.tasks;

import huan.exception.HuanException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskListTest {

    private TaskList tasks;
    private Deadline deadline;
    private Event event;
    private Todo todo;

    @BeforeEach
    void setUp() {
        tasks = new TaskList();
        deadline = new Deadline("Deadline Test", "2025-12-31 2359");
        event = new Event("Event Test", "2025-11-01 1000", "2025-11-01 1200");
        todo = new Todo("Todo Test");
    }

    @Test
    void addTask_addsTodoSuccessfully() {
        tasks.addTask(todo);
        assertEquals(1, tasks.getSize());
        assertEquals(todo.description, tasks.getTask(0).description);
        assertFalse(tasks.getTask(0).isDone);
    }

    @Test
    void addTask_addsDeadlineSuccessfully() {
        tasks.addTask(deadline);
        assertEquals(1, tasks.getSize());
        assertEquals(deadline.description, tasks.getTask(0).description);
        assertFalse(tasks.getTask(0).isDone);
        assertTrue(tasks.getTask(0) instanceof Deadline);
    }

    @Test
    void addTask_addsEventSuccessfully() {
        tasks.addTask(event);
        assertEquals(1, tasks.getSize());
        assertEquals(event.description, tasks.getTask(0).description);
        assertFalse(tasks.getTask(0).isDone);
        assertTrue(tasks.getTask(0) instanceof Event);
    }

    @Test
    void deleteTask_deletesSuccessfully() throws HuanException {
        tasks.addTask(todo);
        tasks.addTask(deadline);
        assertEquals(2, tasks.getSize());

        tasks.deleteTask(2); // 1-based index deletion
        assertEquals(1, tasks.getSize());
        assertEquals(todo.description, tasks.getTask(0).description);
    }

    @Test
    void deleteTask_invalidId_throwsHuanException() {
        tasks.addTask(todo);

        HuanException exception = assertThrows(HuanException.class, () -> {
            tasks.deleteTask(2); // Task index 2 doesn't exist
        });
        assertEquals("Invalid task number!", exception.getMessage());

        exception = assertThrows(HuanException.class, () -> {
            tasks.deleteTask(0); // Task index cannot be 0
        });
        assertEquals("Invalid task number!", exception.getMessage());
    }

    @Test
    void markTask_marksSuccessfully() throws HuanException {
        tasks.addTask(todo);
        tasks.markTask(1); // 1-based indexing
        assertTrue(tasks.getTask(0).isDone);
    }

    @Test
    void markTask_invalidId_throwsHuanException() {
        tasks.addTask(todo);

        HuanException exception = assertThrows(HuanException.class, () -> {
            tasks.markTask(2); // Task index 2 doesn't exist
        });
        assertEquals("Invalid task number!", exception.getMessage());

        exception = assertThrows(HuanException.class, () -> {
            tasks.markTask(0); // Task index cannot be 0
        });
        assertEquals("Invalid task number!", exception.getMessage());
    }

    @Test
    void unmarkTask_unmarksSuccessfully() throws HuanException {
        tasks.addTask(todo);
        tasks.markTask(1);
        assertTrue(tasks.getTask(0).isDone);

        tasks.unmarkTask(1);
        assertFalse(tasks.getTask(0).isDone);
    }

    @Test
    void unmarkTask_invalidId_throwsHuanException() {
        tasks.addTask(todo);

        HuanException exception = assertThrows(HuanException.class, () -> {
            tasks.unmarkTask(2); // Task index 2 doesn't exist
        });
        assertEquals("Invalid task number!", exception.getMessage());

        exception = assertThrows(HuanException.class, () -> {
            tasks.unmarkTask(0); // Task index cannot be 0
        });
        assertEquals("Invalid task number!", exception.getMessage());
    }

    @Test
    void onDate_invalidDate_throwsHuanException() {
        HuanException exception = assertThrows(HuanException.class, () -> {
            tasks.onDate("31-12-2025"); // Incorrect date format
        });
        assertEquals("Invalid date format! (yyyy-MM-dd)", exception.getMessage());
    }

    @Test
    void findTasks_findsMatchingTasks() {
        tasks.addTask(todo);
        tasks.addTask(deadline);
        tasks.addTask(new Todo("Another Test"));

        String result = tasks.findTasks("Test");
        assertTrue(result.contains("Todo Test"));
        assertTrue(result.contains("Deadline Test"));
        assertTrue(result.contains("Another Test"));
    }

    @Test
    void findTasks_noMatches_returnsMessage() {
        tasks.addTask(new Todo("Non-matching Task"));
        String result = tasks.findTasks("Random");
        assertEquals("No such task found!", result);
    }

    @Test
    void hasConflict_detectsEventConflicts() {
        tasks.addTask(event);
        Event overlappingEvent = new Event("Overlapping Event", "2025-11-01 1100", "2025-11-01 1300");

        String conflictMessage = tasks.hasConflict(overlappingEvent);
        assertTrue(conflictMessage.contains("clashes with another event"));
    }

    @Test
    void hasConflict_noConflictForTodo() {
        Todo simpleTask = new Todo("Simple Task");
        assertEquals("", tasks.hasConflict(simpleTask));
    }
}
