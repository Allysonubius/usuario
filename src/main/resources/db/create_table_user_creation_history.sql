CREATE TABLE user_db.user_creation_history (
    history_id BIGINT(32) NOT NULL AUTO_INCREMENT,
    id_user VARCHAR(55) NOT NULL,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(55) NOT NULL,
    user_role BIGINT(32) NOT NULL,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (history_id),
    FOREIGN KEY (id_user) REFERENCES user_db.user_tb(id_user),
    FOREIGN KEY (user_role) REFERENCES user_db.user_roles(role_id)
);