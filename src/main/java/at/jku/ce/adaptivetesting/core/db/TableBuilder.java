package at.jku.ce.adaptivetesting.core.db;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Peter
 */

public class TableBuilder {
    private List<String[]> rows = new LinkedList<>();

    public void addRow(String... cols) {
        rows.add(cols);
    }

    public void addKeySet(Set<String> keySet) {
        String[] cols = keySet.toArray(new String[keySet.size()]);
        rows.add(cols);
        createLine(cols);
    }

    public void addEntrySet(Set<Object> set) {
        Object[] objArr = set.toArray();
        String[] cols = new String[objArr.length];
        for (int x = 0; x < objArr.length; x++) {
            cols[x] = objArr[x].toString().split("=")[1];
        }
        rows.add(cols);
    }

    private int[] colWidths() {
        int cols = -1;

        for(String[] row : rows)
            cols = Math.max(cols, row.length);

        int[] widths = new int[cols];

        for(String[] row : rows) {
            for(int colNum = 0; colNum < row.length; colNum++) {
                widths[colNum] = Math.max(widths[colNum], StringUtils.length(row[colNum]));
            }
        }
        return widths;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();

        int[] colWidths = colWidths();

        for(String[] row : rows) {
            for(int colNum = 0; colNum < row.length; colNum++) {
                buf.append(StringUtils.rightPad(StringUtils.defaultString(row[colNum]), colWidths[colNum]));
                buf.append(' ');
            }
            buf.append('\n');
        }
        return buf.toString();
    }

    private void createLine(String[] cols) {
        StringBuilder temp = new StringBuilder();
        String[] lines = new String[cols.length];
        for (int x = 0; x < cols.length; x++) {
            for (int y = 0; y < cols[x].length(); y++) {
                temp.append("-");
            }
            lines[x] = temp.toString();
            temp.setLength(0);
        }
        rows.add(lines);
    }
}
