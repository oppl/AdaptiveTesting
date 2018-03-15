package at.jku.ce.adaptivetesting.core.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import at.jku.ce.adaptivetesting.core.LogHelper;

public class DBConnection {

    private static final String USER = "root";
    private static final String PASSWD = "r00t";
    private static final String CONNECT_URL = "jdbc:mysql://localhost/test";
    private Connection conn;

    protected DBConnection () {
        conn = null;
    }

    protected void initialize () {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(CONNECT_URL, USER, PASSWD);
            LogHelper.logInfo("Connection to Database \"" + conn.getCatalog() + "\" established");

        } catch (SQLException ex) {
            LogHelper.logError("SQLException: " + ex.getMessage());
            LogHelper.logError("SQLState: " + ex.getSQLState());
            LogHelper.logError("VendorError: " + ex.getErrorCode());
        } catch (Exception ex) {
            LogHelper.logError("Exception: " + ex.getMessage());
        }
    }
    protected Connection getInstance() {
        return conn;
    }
}

