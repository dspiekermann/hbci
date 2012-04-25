drop table USERS;

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

