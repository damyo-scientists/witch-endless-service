CREATE USER witch_endless_test WITH PASSWORD 'witch_endless_test' CREATEDB;
CREATE DATABASE witch_endless_test
    WITH
    OWNER = witch_endless_test
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    CONNECTION LIMIT = -1
