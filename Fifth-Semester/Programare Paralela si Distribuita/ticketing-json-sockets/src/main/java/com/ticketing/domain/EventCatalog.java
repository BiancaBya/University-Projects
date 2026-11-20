package com.ticketing.domain;

import java.util.*;

public class EventCatalog {

    public static class Category {
        public final int categoryId;
        public final String name;
        public final long price;
        public final int initialStock;
        private int availableStock;

        public Category(int categoryId, String name, long price, int initialStock) {
            this.categoryId = categoryId;
            this.name = name;
            this.price = price;
            this.initialStock = initialStock;
            this.availableStock = initialStock;
        }

        public int getAvailableStock() {
            return availableStock;
        }

        public void decreaseAvailable(int k) {
            this.availableStock -= k;
        }

        public void increaseAvailable(int k) {
            this.availableStock += k;
        }
    }

    public static class Event {
        public final int eventId;
        public final String name;
        private final Map<Integer, Category> categories = new HashMap<>();

        public Event(int eventId, String name) {
            this.eventId = eventId;
            this.name = name;
        }

        public void addCategory(Category c) {
            categories.put(c.categoryId, c);
        }

        public Category getCategory(int categoryId) {
            return categories.get(categoryId);
        }

        public Collection<Category> getCategories() {
            return categories.values();
        }
    }

    private final Map<Integer, Event> events = new HashMap<>();

    public void addEvent(Event e) {
        events.put(e.eventId, e);
    }

    public Event getEvent(int eventId) {
        return events.get(eventId);
    }

    public Collection<Event> getEvents() {
        return events.values();
    }

    public static EventCatalog sampleCatalog() {
        EventCatalog cat = new EventCatalog();

        Event e1 = new Event(1, "E_1");
        e1.addCategory(new Category(1, "VIP", 500, 30));
        e1.addCategory(new Category(2, "CAT_1", 250, 60));
        e1.addCategory(new Category(3, "BALCON", 150, 80));
        cat.addEvent(e1);

        Event e2 = new Event(2, "E_2");
        e2.addCategory(new Category(1, "VIP", 450, 25));
        e2.addCategory(new Category(2, "CAT_1", 220, 55));
        e2.addCategory(new Category(3, "BALCON", 140, 90));
        cat.addEvent(e2);

        Event e3 = new Event(3, "E_3");
        e3.addCategory(new Category(1, "VIP", 600, 20));
        e3.addCategory(new Category(2, "CAT_1", 300, 50));
        e3.addCategory(new Category(3, "BALCON", 180, 100));
        cat.addEvent(e3);

        return cat;
    }
}
