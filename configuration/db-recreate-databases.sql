-- PostgreSQL database recreation 
--
DROP DATABASE awdatabase;
DROP DATABASE awauth;
DROP USER awag;

CREATE USER awag WITH
  LOGIN
  NOSUPERUSER
  INHERIT
  NOCREATEDB
  NOCREATEROLE
  NOREPLICATION
  CONNECTION LIMIT -1
  PASSWORD 'changeit';

CREATE DATABASE awdatabase
    WITH 
    OWNER = awag
    TEMPLATE = template0;

 CREATE DATABASE awauth
    WITH 
    OWNER = awag
    TEMPLATE = template0;
