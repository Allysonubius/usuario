CREATE TABLE user_db.user_role_history_old (
    history_id BIGINT(32) NOT NULL AUTO_INCREMENT,
    id_user VARCHAR(55) NOT NULL,
    old_user_role BIGINT(32) NOT NULL,
    date_change TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (history_id),
    FOREIGN KEY (id_user) REFERENCES user_db.user_tb(id_user),
    FOREIGN KEY (old_user_role) REFERENCES user_db.user_roles(role_id)
);