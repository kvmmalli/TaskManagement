CREATE TABLE IF NOT EXISTS project (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS task (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000),
    status VARCHAR(50),
    priority INT,
    due_date DATE,
    project_id BIGINT,
    FOREIGN KEY (project_id) REFERENCES project(id)
);