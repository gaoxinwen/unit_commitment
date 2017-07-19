package cn.edu.hust.unit;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 不同机组只需修改MULTIPLE即可！
 * Created by Gaoxinwen on 2017/1/4.
 */
public class Constants {

    // 10的倍数，试用于20、40、60、80、100机组问题
    private static int MULTIPLE = 1;

    // 问题维度，10 units
    private static final int PROBLEM_DIMENSION0 = 10;
    // 时段长度，24 h
    public static final int TIME_PERIOD = 24;
    // 问题维度
    public static final int PROBLEM_DIMENSION = PROBLEM_DIMENSION0 * MULTIPLE;
    // 机组优先顺序，从1开始
    private static final int[] UNITPRIORITY0 = {1, 2, 5, 4, 3, 6, 7, 8, 9, 10};
    // 时段负荷
    private static final double[] LOAD0 = {700, 750, 850, 950, 1000, 1100, 1150, 1200, 1300, 1400, 1450, 1500,
            1400, 1300, 1200, 1050, 1000, 1100, 1200, 1400, 1300, 1100, 900, 800};

    public static List<Unit> unitList = new ArrayList<>();

    public static int[] UNITPRIORITY = new int[UNITPRIORITY0.length * MULTIPLE];

    public static double[] LOAD = new double[LOAD0.length];


    // Get load, unit priority list and units
    static {

        for (int i = 0; i < LOAD.length; i++) {
            LOAD[i] = LOAD0[i] * MULTIPLE;
        }

        for (int i = 0; i < UNITPRIORITY.length; i++) {
            UNITPRIORITY[i] = (UNITPRIORITY0[i % UNITPRIORITY0.length] - 1) * MULTIPLE + 1
                    + i / UNITPRIORITY0.length;
        }

        UnitDaoImpl unitDao = new UnitDaoImpl();
        List<Unit> tenUnits = unitDao.findAll();
        for (int i = 0; i < MULTIPLE; i++) {
            unitList.addAll(tenUnits);
        }
    }
}
