package cn.edu.hust.unit;

import java.sql.SQLException;
import java.util.List;

/**
 * Description:
 * Created by Gaoxinwen on 2017/1/3.
 */
public interface UnitDao {
    List<Unit> findAll() throws SQLException;
}
