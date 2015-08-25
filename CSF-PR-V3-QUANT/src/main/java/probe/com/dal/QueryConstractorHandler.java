package probe.com.dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yehia Farag this class responsible for contracting and handling
 * queries
 */
public class QueryConstractorHandler {

    private final List<String> typeList = new ArrayList<String>();
    private final List<String> valueList = new ArrayList<String>();

    /**
     *
     * @param type
     * @param value
     */
    public void addQueryParam(String type, String value) {
        typeList.add(type);
        valueList.add(value);

    }

    /**
     *
     * @param selectStat
     * @return
     */
    public PreparedStatement initStatment(PreparedStatement selectStat) {
        try {

            for (int x = 0; x < typeList.size(); x++) {
                String type = typeList.get(x);
                if (type.equalsIgnoreCase("String")) {
                    selectStat.setString(x + 1, valueList.get(x));

                } else if (type.equalsIgnoreCase("Integer")) {
                    selectStat.setInt(x, Integer.valueOf(valueList.get(x)));

                } else if (type.equalsIgnoreCase("double")) {
                    selectStat.setDouble(x, Double.valueOf(valueList.get(x)));

                }

            }
        } catch (SQLException sqlex) {
            System.err.println("at error " + this.getClass().getName() + "  at line " + 57 + "   " + sqlex.getLocalizedMessage());
        }

        return selectStat;
    }

}
