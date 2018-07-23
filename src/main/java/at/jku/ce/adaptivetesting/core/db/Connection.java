package at.jku.ce.adaptivetesting.core.db;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.jku.ce.adaptivetesting.core.LogHelper;
import com.vaadin.data.Item;
import com.vaadin.ui.Table;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

/**
 * Created by Peter
 */

public class Connection {

    private static final String USER = "adaptivetesting";
    private static final String PASSWD = "r00t";
    private static final String CONNECT_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String SCHEMA = "ADAPTIVETESTING";
    private java.sql.Connection con;
    private Handle handle;

    protected Connection() {
        con = null;
    }

    protected void initialize () {
        try {
            con = DriverManager.getConnection(CONNECT_URL, USER, PASSWD);
            handle = Jdbi.open(con);

            // Change schema to ADAPTIVETESTING
            handle.execute("ALTER SESSION SET CURRENT_SCHEMA = " + SCHEMA);
            LogHelper.logInfo("Connection to database schema " + SCHEMA + " established");

        } catch (SQLException ex) {
            LogHelper.logError("SQLException: " + ex.getMessage());
            LogHelper.logError("SQLState: " + ex.getSQLState());
            LogHelper.logError("VendorError: " + ex.getErrorCode());
        } catch (Exception ex) {
            LogHelper.logError("Exception: " + ex.getMessage());
        }
    }

    protected java.sql.Connection getConnection() {
        if (connectionEstablished() == false) initialize();
        return con;
    }

    protected void closeConnection() {
        try {
            handle.close();
            con.close();
            LogHelper.logInfo("Connection to Database closed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected Table drawTable (String tableName, String sql) {
        if (sql == null && tableName != null) {
            sql = "SELECT * FROM " + tableName;
        }
        Table table = new Table();
        con = getConnection();
        PreparedStatement ps = null;

        try {
            con.setAutoCommit(false);
            ps = con.prepareStatement(sql.replaceAll(";", ""));
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                table.addContainerProperty(rsmd.getColumnLabel(i), String.class, null);
            }
            if (!rs.next()) {

            }
            else {
                do {
                    Object newItemId = table.addItem();
                    Item row = table.getItem(newItemId);
                    for (int j = 1; j <= rsmd.getColumnCount(); j++) {
                        row.getItemProperty(rsmd.getColumnLabel(j)).setValue(rs.getString(j));
                    }
                } while (rs.next());
            }
        } catch (SQLException e) {
            LogHelper.logError(e.getMessage());
            table.addContainerProperty("Error-Info", String.class, null);
            table.addItem(new Object[]{e.getMessage()}, 1);
        } finally {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        table.setPageLength(table.size());
        return table;
    }

    protected String executeQuery (String sql) {
        String resultSet;
        try {
            List<Map<String, Object>> mapList = handle.createQuery(
                    sql.replaceAll(";", "")
            ).mapToMap().list();
            resultSet = resultSetAsString(mapList);
        } catch (Exception ex) {
            LogHelper.logError(ex.getCause().toString());
            return "";
        }
        return "\n" + resultSet;
    }

    private String resultSetAsString(List<Map<String, Object>> mapList) throws Exception {
        TableBuilder tableBuilder = new TableBuilder();
        Map<String, Object> firstMap = mapList.get(0);
        Set<String> keySet = firstMap.keySet();

        tableBuilder.addKeySet(keySet);
        for (Map map : mapList) {
            tableBuilder.addEntrySet(map.entrySet());
        }
        return tableBuilder.toString();
    }

    protected double compareResults (String sql1, String sql2) throws Exception {
        // result of user input
        List<Map<String, Object>> mapList1 = handle.createQuery(
                sql1.replaceAll(";", "")
        ).mapToMap().list();
        // result of anserQuery
        List<Map<String, Object>> mapList2 = handle.createQuery(
                sql2.replaceAll(";", "")
        ).mapToMap().list();

        if (mapList1.size() == mapList2.size()) {
            for (int x = 0; x < mapList2.size(); x++) {
                LogHelper.logInfo("[row #" + x + "] Comparing: [USERINPUT]" + mapList1.get(x).toString() + " to [SOLUTION]" + mapList2.get(x));
                if (mapList1.get(x).equals(mapList2.get(x))) {
                    LogHelper.logInfo("[row values: coincide]");
                } else {
                    LogHelper.logError("[row values: do not coincide]");
                    return 0.0d;
                }
            }
            return 1.0d;
        } else return 0.0d;
    }

    protected boolean connectionEstablished() {
        if (con == null) {
            return false;
        } else return true;
    }
}

