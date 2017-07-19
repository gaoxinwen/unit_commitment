package cn.edu.hust.unit;

import cn.edu.hust.util.MathUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Description:参见https://cn.mathworks.com/matlabcentral/fileexchange/53726-simple-economic-dispatch-with-out-losses
 * Created by Gaoxinwen on 2017/1/4.
 */
public class EconomicDispatch {

    private static final double TOLERANCE = 0.001;


    /**
     * Lambda-Iteration Algorithm
     *
     * @param uc   机组组合
     * @param load 出力
     * @return 费用
     */
    public static DispatchResult dispatch(int[] uc, double load) {

        List<Unit> unitList = new ArrayList<>();
        for (int i = 0; i < uc.length; i++) {
            if (uc[i] == 1) {
                unitList.add(Constants.unitList.get(i));
            }
        }

        // assume lambda
        List<Double> bList = new ArrayList<>();
        List<Double> cList = new ArrayList<>();
        for (Unit unit : unitList) {
            bList.add(unit.getB());
            cList.add(1 / unit.getC());
        }
        double lambda = Collections.max(bList);

        double dp = load;
        double[] P = new double[unitList.size()];

        while (Math.abs(dp) > TOLERANCE) {
            for (int i = 0; i < unitList.size(); i++) {
                Unit unit = unitList.get(i);
                double temp = (lambda - unit.getB()) / 2 / unit.getC();
                temp = Math.min(temp, unit.getPmax());
                temp = Math.max(temp, unit.getPmin());
                P[i] = temp;
            }
            dp = load - MathUtils.sum(P);
            lambda += dp * 2 / MathUtils.sum(cList);
        }


        double fitness = 0;
        int index = 0;
        for (Unit unit : unitList) {
            fitness += unit.getA() + unit.getB() * P[index] + unit.getC() * P[index] * P[index];
            index++;
        }

        DispatchResult result = new DispatchResult();
        result.setFitness(fitness);

        // 停机的出力为0
        double[] PP = new double[uc.length];
        int j = 0;
        for (int i = 0; i < PP.length; i++) {
            if (uc[i] == 1) {
                PP[i] = P[j++];
            }
        }
        result.setLoad(PP);

        return result;

    }
}
