package at.fhtw.mtcgapp.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DatabaseManager {
    INSTANCE;

    public Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/mtcgdb",
                    "mtcgdb",
                    "mtcgdb");


            connection.createStatement().execute("CREATE SCHEMA IF NOT EXISTS mtcg");

            connection.createStatement().execute("""
                        CREATE TABLE IF NOT EXISTS mtcg."user"
                        (
                            id BIGSERIAL PRIMARY KEY,
                            token uuid NOT NULL,
                            username character varying(32) COLLATE pg_catalog."default" NOT NULL,
                            password character varying(32) COLLATE pg_catalog."default" NOT NULL,
                            bio character varying(64) COLLATE pg_catalog."default",
                            image character varying(8) COLLATE pg_catalog."default",
                            coins integer,
                            elo integer,
                            battles_fought integer,
                            UNIQUE (username)
                        )
                    """);
            connection.createStatement().execute("ALTER TABLE IF EXISTS mtcg.user OWNER to mtcgdb;");

            connection.createStatement().execute("""
                    CREATE TABLE IF NOT EXISTS mtcg.session
                    (
                        id serial NOT NULL,
                        token character varying(64) NOT NULL,
                        fk_user_id bigint,
                        PRIMARY KEY (id),
                        UNIQUE (token),
                        CONSTRAINT fk_user_id FOREIGN KEY (fk_user_id)
                            REFERENCES mtcg."user" (id) MATCH SIMPLE
                            ON UPDATE NO ACTION
                            ON DELETE NO ACTION
                            NOT VALID
                    );
                    """);
            connection.createStatement().execute("ALTER TABLE IF EXISTS mtcg.session OWNER to mtcgdb;");

            return connection;
        } catch (SQLException e) {
            throw new DataAccessException("Datenbankverbindungsaufbau nicht erfolgreich", e);
        }
    }
}
