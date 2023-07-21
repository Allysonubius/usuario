CREATE TABLE user_db.user_history_new (
    history_id BIGINT(32) NOT NULL AUTO_INCREMENT,
    id_user VARCHAR(55) NOT NULL,
    new_username VARCHAR(255),
    new_password VARCHAR(255),
    new_email VARCHAR(55),
    new_user_role BIGINT(32),
    date_change TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (history_id),
    FOREIGN KEY (id_user) REFERENCES user_db.user_tb(id_user),
    FOREIGN KEY (new_user_role) REFERENCES user_db.user_roles(role_id)
);