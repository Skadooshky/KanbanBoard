package model;

public class Card {
    private final int id;
    private final String title;
    private final String description;
    private final KanbanStatus status;
    private final String createdAt;   
    private final String dueDate;     
    private final String owner;
    private final String assignee;
    private final boolean expedite;   

    public Card(int id, String title, String description, KanbanStatus status,
                String createdAt, String dueDate, String owner, String assignee, boolean expedite) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.owner = owner;
        this.assignee = assignee;
        this.expedite = expedite;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public KanbanStatus getStatus() { return status; }
    public String getCreatedAt() { return createdAt; }
    public String getDueDate() { return dueDate; }
    public String getOwner() { return owner; }
    public String getAssignee() { return assignee; }
    public boolean isExpedite() { return expedite; }

    @Override
    public String toString() {
        String tag = expedite ? "[EXPEDITE] " : "";
        return tag + title + " | Due: " + dueDate + " | " + owner + " → " + assignee + " | " + createdAt;
    }
}
