package cn.edu.hust.hs;

import cn.edu.hust.unit.Constants;
import cn.edu.hust.unit.EconomicDispatch;
import cn.edu.hust.unit.Unit;
import cn.edu.hust.util.MathUtils;

import java.util.List;

import static cn.edu.hust.unit.Constants.LOAD;

public class ProblemSet {
	public static final double VEL_LOW = -10;
	public static final double VEL_HIGH = 10;

    /**
     * Total cost
     * @param location uc
     * @return total cost
     */
    public static double evaluate(Location location) {

	    final int T = location.getLoc()[0].length;

	    double fitness = 0;
	    for (int t = 0; t < T; t++) {
            int[] uc = location.getUC(t);
            fitness += EconomicDispatch.dispatch(uc, LOAD[t]).getFitness();
        }

        fitness += getStartCost(location);

		return fitness;
	}

    /**
     * start cost
     * @param location uc
     * @return start cost
     */
	public static double getStartCost(Location location) {

        final int N = location.getLoc().length;
        final int T = location.getLoc()[0].length;

        List<Unit> unitList = Constants.unitList;

        int[] initialUC = new int[N];
        int[] initialDuration = new int[N];
        for (int i = 0; i < N; i++) {
            initialDuration[i] = unitList.get(i).getInitialStatus();
            initialUC[i] = (initialDuration[i] > 0) ? 1 : 0;
        }

        double cost = 0;
        int[][] duration = HSProcess.getDuration(location.getLoc());

        for (int t = 0; t < T; t++) {
            int[] previousUC;
            int[] previousDuration;
            if (t == 0) {
                previousUC = initialUC;
                previousDuration = initialDuration;
            } else {
                previousUC = location.getUC(t - 1);
                previousDuration = MathUtils.getColumn(duration, t - 1);
            }
            int[] nowUC = location.getUC(t);
            for (int n = 0; n < N; n++) {
                // 开机状态
                Unit unit = unitList.get(n);
                if (previousUC[n] == 0 && nowUC[n] == 1) {
                    if (-previousDuration[n] <= unit.getMinDownTime() + unit.getColdStartHour()) {
                        cost += unit.getHotStartCost();
                    } else {
                        cost += unit.getColdStartCost();
                    }
                }
            }
        }

	    return cost;
    }

}
