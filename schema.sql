CREATE TABLE IF NOT EXISTS cards (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT,
    status TEXT NOT NULL,                          
    created_at TEXT NOT NULL,
    due_date TEXT NOT NULL,                        
    owner TEXT NOT NULL,
    assignee TEXT NOT NULL,
    expedite INTEGER NOT NULL DEFAULT 0            
);

INSERT INTO cards (title, description, status, due_date, owner, assignee, expedite)
VALUES
('Set up DB', 'Create schema + connection', 'TO_DO', '2026-01-30', 'Shivarn', 'Shivarn', 1),
('Build board UI', 'Swing lanes + lists', 'IN_PROGRESS', '2026-02-05', 'Shivarn', 'Shivarn', 0),
('Demo done', 'Shows in DONE', 'DONE', '2026-01-20', 'Shivarn', 'Shivarn', 0);
