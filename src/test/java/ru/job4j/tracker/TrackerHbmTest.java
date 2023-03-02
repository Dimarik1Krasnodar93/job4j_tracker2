package ru.job4j.tracker;

import org.junit.Test;
import ru.job4j.tracker.model.Item;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class TrackerHbmTest {
    @Test
    public void whenAddNewItemThenTrackerHasSameItem() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = new Item();
            item.setName("test1");
            tracker.add(item);
            Item result = tracker.findById(item.getId());
            assertThat(result.getName()).isEqualTo("test1");
        }
    }

    @Test
    public void whenAddNewItemThenTrackerHasSameItemFindBy() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = new Item();
            item.setName("test1");
            tracker.add(item);
            Item result = tracker.findByName(item.getName()).get(0);
            assertThat(result.getName()).isEqualTo("test1");
        }
    }

    @Test
    public void whenAddNewItemThenTrackerHasSameItemDelete() throws Exception {
        try (var tracker = new HbmTracker()) {
            Item item = new Item();
            item.setName("test1");
            tracker.add(item);
            tracker.delete(item.getId());
            Item result = tracker.findById(item.getId());
            assertThat(result).isNull();
        }
    }
}