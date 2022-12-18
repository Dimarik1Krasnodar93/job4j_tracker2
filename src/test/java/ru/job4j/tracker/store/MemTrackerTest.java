package ru.job4j.tracker.store;

import org.junit.Ignore;
import org.junit.Test;
import ru.job4j.tracker.action.ReplaceAction;
import ru.job4j.tracker.input.Input;
import ru.job4j.tracker.output.ConsoleOutput;
import ru.job4j.tracker.model.Item;
import ru.job4j.tracker.output.Output;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

public class MemTrackerTest {

    @Test
    public void whenAddNewItemThenTrackerHasSameItem() {
        MemTracker memTracker = new MemTracker();
        Item item = new Item("test1");
        memTracker.add(item);
        Item result = memTracker.findById(item.getId());
        assertThat(result.getName(), is(item.getName()));
    }

    @Test
    public void whenFindAll() {
        MemTracker memTracker = new MemTracker();
        Item item1 = new Item("first");
        Item item2 = new Item("second");
        memTracker.add(item1);
        memTracker.add(item2);
        List<Item> expected = List.of(item1, item2);
        List<Item> result = memTracker.findAll();
        assertThat(result, is(expected));
    }

    @Test
    public void whenFindByName() {
        MemTracker memTracker = new MemTracker();
        Item item1 = new Item("first");
        Item item2 = new Item("second");
        Item item3 = new Item("first");
        memTracker.add(item1);
        memTracker.add(item2);
        memTracker.add(item3);
        List<Item> expected = List.of(item1, item3);
        List<Item> result = memTracker.findByName("first");
        assertThat(result, is(expected));
    }

    @Test
    public void whenFindById() {
        MemTracker memTracker = new MemTracker();
        Item item1 = new Item("first");
        Item item2 = new Item("second");
        Item item3 = new Item("first");
        memTracker.add(item1);
        memTracker.add(item2);
        memTracker.add(item3);
        Item result = memTracker.findById(item2.getId());
        assertThat(result, is(item2));
    }

    @Test
    public void whenFindByIdNotFound() {
        MemTracker memTracker = new MemTracker();
        Item item1 = new Item("first");
        Item item2 = new Item("second");
        Item item3 = new Item("first");
        memTracker.add(item1);
        memTracker.add(item2);
        memTracker.add(item3);
        Item result = memTracker.findById(-1);
        assertThat(result, is(nullValue()));
    }

    @Test
    public void whenReplace() {
        MemTracker memTracker = new MemTracker();
        Item item1 = new Item("first");
        memTracker.add(item1);
        memTracker.replace(item1.getId(), new Item("second"));
        assertThat(memTracker.findById(item1.getId()).getName(), is("second"));
    }

    @Test
    public void whenDelete() {
        MemTracker memTracker = new MemTracker();
        Item item1 = new Item("first");
        memTracker.add(item1);
        memTracker.delete(item1.getId());
        assertThat(memTracker.findById(item1.getId()), is(nullValue()));
    }

    @Ignore
    @Test
    public void whenReplaceMockito() {
        Output out = new ConsoleOutput();
        MemTracker tracker = new MemTracker();
        tracker.add(new Item("Replaced item"));
        String replacedName = "New item name";
        ReplaceAction rep = new ReplaceAction(out);

        Input input = mock(Input.class);
        when(input.askInt(any(String.class))).thenReturn(1);
        when(input.askStr(any(String.class))).thenReturn(replacedName);
        when(input.askInt(any(String.class))).thenReturn(1);
        when(input.askStr(any(String.class))).thenReturn(replacedName);

        rep.execute(input, tracker);

        String ln = System.lineSeparator();
        assertThat(out.toString(), is("=== Edit item ===" + ln + "Edit item is done." + ln));
        assertThat(tracker.findAll().get(0).getName(), is(replacedName));

    }


    @Test
    public void whenDeleteMockito() {
        MemTracker memTracker = new MemTracker();
        Item itemDelete = mock(Item.class);
        when(itemDelete.getId()).thenReturn(1);
        memTracker.add(itemDelete);
        memTracker.delete(itemDelete.getId());
        assertThat(memTracker.findById(itemDelete.getId()), is(nullValue()));
    }

    @Test
    public void whenFindByIdActionMockito() {
        MemTracker memTracker = new MemTracker();
        Item item = mock(Item.class);
        when(item.getId()).thenReturn(1);
        memTracker.add(item);
        assertThat(memTracker.findById(1), is(item));
    }

    @Test
    public void whenFindByNameActionMockito() {
        MemTracker memTracker = new MemTracker();
        Item item = mock(Item.class);
        when(item.getName()).thenReturn("test");
        memTracker.add(item);
        assertThat(memTracker.findByName("test").get(0).getName(), is("test"));
    }
}
