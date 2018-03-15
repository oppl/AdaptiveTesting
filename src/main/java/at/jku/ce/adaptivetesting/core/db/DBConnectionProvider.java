package at.jku.ce.adaptivetesting.core.db;

import java.sql.Connection;

public class DBConnectionProvider {
    private static DBConnection conn = new DBConnection();

    public void initialize() {
        conn.initialize();
    }

    public Connection getInstance() {
        return conn.getInstance();
    }
}
