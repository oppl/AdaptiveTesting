package at.jku.ce.adaptivetesting.core.db;

import com.vaadin.ui.Table;

/**
 * Created by Peter
 */

public class ConnectionProvider {

    private static Connection con = new Connection();

    public static void initialize() {
        con.initialize();
    }

    public static void closeConnection() {
        con.closeConnection();
    }

    public static boolean connectionEstablished() {
        return con.connectionEstablished();
    }

    public static Table drawTable (String tableName) {
        return con.drawTable(tableName, null);
    }

    public static Table drawResultTable (String sql) {
        return con.drawTable(null, sql);
    }

    public static String executeQuery (String sql) {
        return  con.executeQuery(sql);
    }

    public static double compareResults (String sql1, String sql2) throws Exception {
        return con.compareResults (sql1, sql2);
    }
}
