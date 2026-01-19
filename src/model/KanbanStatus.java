package model;

public enum KanbanStatus {
    TO_DO,
    IN_PROGRESS,
    DONE;

    public static KanbanStatus fromDb(String value) {
        return KanbanStatus.valueOf(value);
    }
}
