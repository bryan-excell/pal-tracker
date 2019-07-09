package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private Map<Long, TimeEntry> inMemoryDatabase = new HashMap<>();
    private long assignedId = 0L;

    public TimeEntry create(TimeEntry timeEntry) {
        TimeEntry updatedEntry = new TimeEntry(
                getNextId(),
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours()
        );
        inMemoryDatabase.put(updatedEntry.getId(), updatedEntry);
        return updatedEntry;
    }

    public TimeEntry find(long id) {
        return inMemoryDatabase.get(id);
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(inMemoryDatabase.values());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (!inMemoryDatabase.containsKey(id)) return null;
        inMemoryDatabase.remove(id);
        TimeEntry updateEntry = new TimeEntry(
                id,
                timeEntry.getProjectId(),
                timeEntry.getUserId(),
                timeEntry.getDate(),
                timeEntry.getHours()
        );
        inMemoryDatabase.put(updateEntry.getId(), updateEntry);
        return updateEntry;
    }

    public void delete(long id) {
        inMemoryDatabase.remove(id);
    }

    private long getNextId() {
        assignedId++;
        return assignedId;
    }
}
