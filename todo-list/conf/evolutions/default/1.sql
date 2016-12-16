# --- !Ups
CREATE TABLE tasks (
  id BIGSERIAL PRIMARY KEY,
  status VARCHAR(50),
  action VARCHAR(200)
);

# --- !Downs
DROP TABLE IF EXISTS tasks;