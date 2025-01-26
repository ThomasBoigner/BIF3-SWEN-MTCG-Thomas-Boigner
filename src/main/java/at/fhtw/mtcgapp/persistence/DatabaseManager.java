package at.fhtw.mtcgapp.persistence;

import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public enum DatabaseManager {
    INSTANCE;

    public static String databaseUrl = "jdbc:postgresql://localhost:5432/mtcgdb";

    public Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(
                    databaseUrl,
                    "mtcgdb",
                    "mtcgdb");
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.setSendFullScript(true);
            scriptRunner.setStopOnError(true);
            scriptRunner.runScript(new java.io.FileReader("init.sql"));
            return connection;
        } catch (SQLException | FileNotFoundException e) {
            throw new DataAccessException("Datenbankverbindungsaufbau nicht erfolgreich", e);
        }
    }
}
