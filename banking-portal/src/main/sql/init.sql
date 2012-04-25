drop table USERS_AUTHORITY;
drop table AUTHORITY;
drop table USERS;

CREATE TABLE IF NOT EXISTS AUTHORITY (
  id int(10) NOT NULL AUTO_INCREMENT,
  role varchar(255) NOT NULL,
  PRIMARY KEY (id)
);
INSERT INTO AUTHORITY (role) VALUES
('ROLE_USER'),
('ROLE_ADMIN');

CREATE TABLE IF NOT EXISTS USERS (
  id int(10) NOT NULL AUTO_INCREMENT,
  create_date date,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  name varchar(255) NOT NULL,
  surname varchar(255) NOT NULL,
  email varchar(255) NOT NULL,
  last_login date,
  image BLOB,
  enabled tinyint(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

INSERT INTO USERS (username, password, enabled, name, surname, email) VALUES
('test', 'test', 1, 'test', 'test', 'test@test.de'),
('admin', 'admin', 1, 'admin', 'admin', 'admin@admin.de');

CREATE TABLE IF NOT EXISTS USERS_AUTHORITY (
  id int(10) NOT NULL AUTO_INCREMENT,
  user_id int(10) NOT NULL,
  authority_id int(10) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES USERS(id),
  FOREIGN KEY (authority_id) REFERENCES AUTHORITY(id)
);

INSERT INTO USERS_AUTHORITY (user_id, authority_id) VALUES
((select id from users where username='test'), (select id from authority where role='ROLE_USER')),
((select id from users where username='admin'), (select id from authority where role='ROLE_USER')),
((select id from users where username='admin'), (select id from authority where role='ROLE_ADMIN'))
;
