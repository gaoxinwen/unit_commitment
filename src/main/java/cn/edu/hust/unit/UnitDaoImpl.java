package cn.edu.hust.unit;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import java.sql.SQLException;
import java.util.List;

/**
 * Description:
 * Created by Gaoxinwen on 2017/1/3.
 */
public class UnitDaoImpl implements UnitDao {

    private static final String URL = "jdbc:mysql://localhost/unit_commitment?autoReconnect=true&useSSL=false";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "delldell";
    private static SimpleDriverDataSource dataSource;

    static {
        dataSource = new SimpleDriverDataSource();
        try {
            dataSource.setDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
    }

    public List<Unit> findAll() {

        String sqlSelect = "SELECT * FROM unit";

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate.query(sqlSelect, (resultSet, i) -> {
            Unit unit = new Unit();
            unit.setId(resultSet.getInt("id"));
            unit.setPmin(resultSet.getDouble("P_min"));
            unit.setPmax(resultSet.getDouble("P_max"));
            unit.setMinUpTime(resultSet.getInt("Min_up_time"));
            unit.setMinDownTime(resultSet.getInt("Min_down_time"));
            unit.setHotStartCost(resultSet.getDouble("Hot_start_cost"));
            unit.setColdStartCost(resultSet.getDouble("Cold_start_Cost"));
            unit.setColdStartHour(resultSet.getInt("Cold_start_hour"));
            unit.setInitialStatus(resultSet.getInt("Initial_status"));
            unit.setA(resultSet.getDouble("a"));
            unit.setB(resultSet.getDouble("b"));
            unit.setC(resultSet.getDouble("c"));

            return unit;
        });
    }
}
