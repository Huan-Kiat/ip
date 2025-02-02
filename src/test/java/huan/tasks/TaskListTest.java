package huan.tasks;

import huan.exception.HuanException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests tje TaskList class methods.
 */
public class TaskListTest {

    private TaskList tasks = new TaskList();
    private Deadline deadline = new Deadline("Deadline Test", "2000-01-01 2359");
    private Event event = new Event("Event Test", "2000-01-01 1000", "2025-01-01 1200");
    private Todo todo = new Todo("Todo Test");

    /**
     * Tests adding a todo task to the list.
     */
    @Test
    void addTask_addsTodoSuccessfully() {
        tasks.addTodo(todo.description);
        assertEquals(1, tasks.getSize());
        assertEquals(todo.description, tasks.getTask(0).description);
        assertFalse(tasks.getTask(0).isDone);
    }

    /**
     * Tests adding a deadline task to the list.
     */
    @Test
    void addTask_addsDeadlineSuccessfully() {
        tasks.addDeadline(deadline.description, "2025-12-31 2359");
        assertEquals(1, tasks.getSize());
        assertEquals(deadline.description, tasks.getTask(0).description);
        assertFalse(tasks.getTask(0).isDone);
        assertTrue(tasks.getTask(0) instanceof Deadline);
    }

    /**
     * Tests adding an event task to the list.
     */
    @Test
    void addTask_addsEventSuccessfully() {
        tasks.addEvent(event.description, "2025-11-01 1000", "2025-11-01 1200");
        assertEquals(1, tasks.getSize());
        assertEquals(event.description, tasks.getTask(0).description);
        assertFalse(tasks.getTask(0).isDone);
        assertTrue(tasks.getTask(0) instanceof Event);
    }

    /**
     * Tests deleting a task by ID successfully.
     */
    @Test
    void deleteTask_deletesSuccessfully() throws HuanException {
        tasks.addTodo(todo.description);
        tasks.addDeadline(deadline.description, "2025-12-31 2359");
        assertEquals(2, tasks.getSize());

        tasks.deleteTask(1); // Delete the first task
        assertEquals(1, tasks.getSize());
        assertEquals(deadline.description, tasks.getTask(0).description);
    }

    /**
     * Tests that deleting a task with invalid ID throws a HuanException.
     */
    @Test
    void deleteTask_invalidId_throwsHuanException() {
        tasks.addTodo(todo.description);
        HuanException exception = assertThrows(HuanException.class, () -> {
            tasks.deleteTask(0);
        });
        assertEquals("Invalid task number!", exception.getMessage());

        exception = assertThrows(HuanException.class, () -> {
            tasks.deleteTask(2);
        });
        assertEquals("Invalid task number!", exception.getMessage());
    }

    /**
     * Tests marking a task by ID reflects in the task isDone variable.
     */
    @Test
    void markTask_marksSuccessfully() throws HuanException {
        tasks.addTodo(todo.description);
        tasks.markTask(1);
        assertTrue(tasks.getTask(0).isDone);
    }

    /**
     * Tests unmarking a task by ID reflects in the task isDone variable.
     */
    @Test
    void unmarkTask_unmarksSuccessfully() throws HuanException {
        tasks.addTodo(todo.description);
        tasks.markTask(1);
        assertTrue(tasks.getTask(0).isDone);

        tasks.unmarkTask(1);
        assertFalse(tasks.getTask(0).isDone);
    }

    /**
     * Tests that marking a task with an invalid ID throws a HuanException.
     */
    @Test
    void markTask_invalidId_throwsHuanException() {
        tasks.addTodo(todo.description);
        HuanException exception = assertThrows(HuanException.class, () -> {
            tasks.markTask(0);
        });
        assertEquals("Invalid task number!", exception.getMessage());

        exception = assertThrows(HuanException.class, () -> {
            tasks.markTask(2);
        });
        assertEquals("Invalid task number!", exception.getMessage());
    }

    /**
     * Tests that unmarking a task with an invalid ID throws a HuanException.
     */
    @Test
    void unmarkTask_invalidId_throwsHuanException() {
        tasks.addTodo(todo.description);
        HuanException exception = assertThrows(HuanException.class, () -> {
            tasks.unmarkTask(0);
        });
        assertEquals("Invalid task number!", exception.getMessage());

        exception = assertThrows(HuanException.class, () -> {
            tasks.unmarkTask(2);
        });
        assertEquals("Invalid task number!", exception.getMessage());
    }

    /**
     * Tests that an invalid date format passed to onDate throws a HuanException.
     */
    @Test
    void onDate_invalidDate_throwsHuanException() {
        HuanException exception = assertThrows(HuanException.class, () -> {
            tasks.onDate("31-12-2025");
        });
        assertEquals("Invalid date format! (yyyy-MM-dd)", exception.getMessage());
    }
}

