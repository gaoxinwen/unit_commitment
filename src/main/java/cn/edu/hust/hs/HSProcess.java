package cn.edu.hust.hs;

import java.io.IOException;
import java.util.*;

import cn.edu.hust.unit.DispatchResult;
import cn.edu.hust.unit.EconomicDispatch;
import cn.edu.hust.util.ExcelUtils;
import cn.edu.hust.util.MathUtils;
import org.apache.commons.lang3.ArrayUtils;

import static cn.edu.hust.unit.Constants.*;

public class HSProcess implements HSConstants {
    private List<Harmony> harmonyMemory = new ArrayList<>();
    private double[] fitnessValueList = new double[HMS];
    private List<Location> pBestLocation = new ArrayList<>();
    private double[] pBestSolution = new double[HMS];
    private Location gbestLocation = new Location();
    private double gbestSolution;

    private Random generator = new Random();

    public double execute() {
        // 初始化和声记忆库
        initializeHM();

        // 计算适应度
        updateFitnessList();

        for (int i = 0; i < HMS; i++) {
            pBestLocation.add(harmonyMemory.get(i).getLocation());
            pBestSolution[i] = fitnessValueList[i];
        }
        // 最优和声
        int bestPos = MathUtils.getMinIndex(fitnessValueList);
        gbestSolution = fitnessValueList[bestPos];
        gbestLocation.setLoc(harmonyMemory.get(bestPos).getLocation().getLoc());
        System.out.println("Initial Solution :" + gbestSolution);


        // 开始迭代
        int t = 1;
        double w;
        final double PAR_MAX = 0.5;
        final double PAR_MIN = 0.01;

        long startTime = System.currentTimeMillis();

        while (t < MAX_ITERATION) {

            w = W_UPPERBOUND - Math.pow(((double) t) / MAX_ITERATION, 2) * (W_UPPERBOUND - W_LOWERBOUND);
            double PAR = PAR_MIN + (double) t / MAX_ITERATION * (PAR_MAX - PAR_MIN);

            List<Harmony> harmonyList = new ArrayList<>();
            harmonyList.addAll(harmonyMemory);

            double[] newFitnessList = new double[NGC];
            for (int n = 0; n < NGC; n++) {
                int[][] newLoc = new int[PROBLEM_DIMENSION][TIME_PERIOD];
                double[][] newVel = new double[PROBLEM_DIMENSION][TIME_PERIOD];
                for (int i = 0; i < PROBLEM_DIMENSION; i++) {
                    for (int j = 0; j < TIME_PERIOD; j++) {
                        // vel
                        int index = generator.nextInt(HMS);
                        newVel[i][j] = (w * harmonyMemory.get(index).getVelocity().getPos()[i][j])
                                + (generator.nextDouble() * C2) *
                                (gbestLocation.getLoc()[i][j] - harmonyMemory.get(index).getLocation().getLoc()[i][j]);
                        if (newVel[i][j] > ProblemSet.VEL_HIGH) {
                            newVel[i][j] = ProblemSet.VEL_HIGH;
                        }
                        if (newVel[i][j] < ProblemSet.VEL_LOW) {
                            newVel[i][j] = ProblemSet.VEL_LOW;
                        }

                        if (generator.nextDouble() < HMCR) {
                            // sigmiod函数
                            if (newVel[i][j] <= 0) {
                                if (generator.nextDouble() <= (1 - 2 / (1 + Math.exp(-newVel[i][j])))) {
                                    newLoc[i][j] = 0;
                                }
                            } else {
                                if (generator.nextDouble() <= (2 / (1 + Math.exp(-newVel[i][j])) - 1)) {
                                    newLoc[i][j] = 1;
                                }
                            }
                            if (generator.nextDouble() < PAR) {
                                // Global-best harmony search
                                newLoc[i][j] = gbestLocation.getLoc()[i][j];
                            }
                        } else {
                            newLoc[i][j] = (generator.nextDouble() < 0.5) ? 1 : 0;
                        }
                    }
                }

                // 修复策略
                repair(newLoc);

                Harmony newHarmony = new Harmony();
                newHarmony.setLocation(new Location(newLoc));
                newHarmony.setVelocity(new Velocity(newVel));

                harmonyList.add(newHarmony);
                newFitnessList[n] = ProblemSet.evaluate(new Location(newLoc));

            }

            // 随机联赛选择
            Map<Integer, Double> map = new TreeMap<>();
            for (int i = 0; i < HMS; i++) {
                map.put(i, fitnessValueList[i]);
            }

            for (int i =0; i < NGC; i++) {
                map.put(i + HMS, newFitnessList[i]);
            }
            Map<Integer, Double> sortedMap = MathUtils.sortByValue(map);
            List<Harmony> newHarmonyMemory = new ArrayList<>();
            int index = 0;
            for (Map.Entry<Integer, Double> entry : sortedMap.entrySet()) {
                newHarmonyMemory.add(harmonyList.get(entry.getKey()));
                fitnessValueList[index] = entry.getValue();
                index++;
                if (index >= HMS)
                    break;
            }

            harmonyMemory = newHarmonyMemory;


            // 重新得到最优和声
            int bestLoc = MathUtils.getMinIndex(fitnessValueList);
            if (fitnessValueList[bestLoc] < gbestSolution) {
                gbestLocation.setLoc(harmonyMemory.get(bestLoc).getLocation().getLoc());
                gbestSolution = fitnessValueList[bestLoc];
            }


            t++;

            System.out.println("ITERATION " + t + ": ");
            System.out.println("     Value: " + gbestSolution);
        }

        System.out.println("\nSolution found! the solution is:");

        Object[][] result = new Object[TIME_PERIOD][PROBLEM_DIMENSION];
        double fuelCost = 0;
        for (int i = 0; i < TIME_PERIOD; i++) {
            DispatchResult dispatchResult = EconomicDispatch.dispatch(gbestLocation.getUC(i), LOAD[i]);
            double[] temp = dispatchResult.getLoad();
            for (int j = 0; j < PROBLEM_DIMENSION; j++) {
                result[i][j] = temp[j];
            }
            fuelCost += dispatchResult.getFitness();
        }


        System.out.println("Start Cost :" + ProblemSet.getStartCost(gbestLocation));
        System.out.println("Fuel  Cost :" + fuelCost);
        System.out.println("Total Cost :" + ProblemSet.evaluate(gbestLocation));

        try {
            ExcelUtils.write07Excel("result/HSPSO_" + PROBLEM_DIMENSION + ".xlsx", "result", result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return gbestSolution;
    }

    /**
     * 初始化和声库
     */
    public void initializeHM() {
        for (int n = 0; n < HMS; n++) {
            Harmony harmony = new Harmony();

            // randomize location inside a space defined in Problem Set
            int[][] loc = new int[PROBLEM_DIMENSION][TIME_PERIOD];
            for (int i = 0; i < loc.length; i++) {
                for (int j = 0; j < loc[i].length; j++) {
                    loc[i][j] = (generator.nextDouble() < 0.5) ? 0 : 1;
                }
            }

            // randomize velocity in the range defined in Problem Set
            double[][] vel = new double[PROBLEM_DIMENSION][TIME_PERIOD];
            for (int j = 0; j < PROBLEM_DIMENSION; j++) {
                for (int k = 0; k < TIME_PERIOD; k++) {
                    vel[j][k] = ProblemSet.VEL_LOW + generator.nextDouble() *
                            (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
                }
            }

            Velocity velocity = new Velocity(vel);
            harmony.setVelocity(velocity);

            repair(loc);
            Location location = new Location(loc);
            harmony.setLocation(location);

            harmonyMemory.add(harmony);
        }
    }

    private void updateFitnessList() {
        for (int i = 0; i < HMS; i++) {
            fitnessValueList[i] = harmonyMemory.get(i).getFitnessValue();
        }
    }

    /**
     * 修复策略
     *
     * @param location 机组组合
     */
    private static void repair(int[][] location) {

        // step 1: Spinning reserve constraints repairing
        for (int t = 0; t < TIME_PERIOD; t++) {
            int[] uc = MathUtils.getColumn(location, t);
            // 保存状态为停机机组的index
            List<Integer> uncommitted = getUncommittedUnits(uc);

            // 不满足旋备要求，这里旋备容量取10%的时段负荷
            while (!checkSpinningReserve(uc, LOAD[t])) {
                int index = uncommitted.get(0);
                uncommitted.remove(0);

                location[index][t] = 1;
                uc[index] = 1;
            }
        }

        int[][] duration = getDuration(location);

        // step 2: Minimum up and down time constraints repairing
        for (int t = 0; t < TIME_PERIOD; t++) {

            timeRepair(t, location, duration);

            updateDuration(t, location, duration);
        }

        // step 3: Decommitment of excess units
        for (int t = 0; t < TIME_PERIOD; t++) {
            // 保存状态为停机机组的index
            List<Integer> committed = getCommittedUnits(MathUtils.getColumn(location, t));
            for (Integer index : committed) {
                int[] uc = MathUtils.getColumn(location, t);
                uc[index] = 0;
                // 满足旋转备用容量约束
                if (checkSpinningReserve(uc, LOAD[t])) {
                    // 前面开机满足最小开机时间，后面最小停机时间内的时段全部停机且满足旋转备用，则全部停机
                    if (duration[index][t] == 1 || duration[index][t] > unitList.get(index).getMinUpTime()) {
                        location[index][t] = 0;
                    }
                }
            }
            updateDuration(t, location, duration);
        }

        // step 4: Minimum up and down time constraints repairing
        // 考虑到特殊情况（0000011111[11111]1100000），中间的1是多余机组且初始解就是如此，去除之后会使后面两个时段
        // 不满足最小开机时间约束（MDT = MUT = 5），因此，再进行一遍最小开停机约束修复
        for (int t = 0; t < TIME_PERIOD; t++) {

            timeRepair(t, location, duration);

            updateDuration(t, location, duration);
        }
    }

    /**
     * 旋转备用容量约束
     *
     * @param uc   机组组合
     * @param load 出力
     * @return TRUE or FALSE
     */
    public static boolean checkSpinningReserve(int[] uc, double load) {

        double totalMaxLoad = 0;
        for (int i = 0; i < uc.length; i++) {
            if (uc[i] == 1) {
                totalMaxLoad += unitList.get(i).getPmax();
            }
        }

        // 990 和 990.0000...1比较!!!!!
        return (totalMaxLoad > 1.1 * load) || Math.abs(totalMaxLoad - 1.1 * load) < 0.001;
    }

    /**
     * 最小开停机时间修复策略（准确的来讲是开机修复，不允许停机）
     *
     * @param t        时段
     * @param location 机组组合
     * @param duration 开停机持续时间
     */
    public static void timeRepair(int t, int[][] location, int[][] duration) {

        for (int n = 0; n < PROBLEM_DIMENSION; n++) {

            int minUpTime = unitList.get(n).getMinUpTime();
            int minDownTime = unitList.get(n).getMinDownTime();

            int previous, previousStatus;
            if (t == 0) {
                previousStatus = unitList.get(n).getInitialStatus();
                previous = (previousStatus > 0) ? 1 : 0;
            } else {
                previousStatus = duration[n][t - 1];
                previous = location[n][t - 1];
            }

            // 由1-->0
            if (previous == 1 && location[n][t] == 0) {
                if (previousStatus < minUpTime) {
                    location[n][t] = 1;
                }

                if (t + minDownTime <= TIME_PERIOD && -duration[n][t + minDownTime - 1] < minDownTime) {
                    location[n][t] = 1;
                }

                if (t + minDownTime > TIME_PERIOD) {
                    int sum = 0;
                    for (int i = t; i < TIME_PERIOD; i++) {
                        sum += location[n][i];
                    }
                    if (sum > 0) {
                        location[n][t] = 1;
                    }
                }
            }

        }
    }


    /**
     * 根据某一时段改变的机组组合更新开停机持续时间
     *
     * @param t        时段
     * @param location 机组组合
     * @param duration 开停机持续时间
     */
    public static void updateDuration(int t, int[][] location, int[][] duration) {

        int[] ucOriginal = new int[PROBLEM_DIMENSION];
        for (int n = 0; n < PROBLEM_DIMENSION; n++) {
            ucOriginal[n] = (duration[n][t] > 0) ? 1 : 0;
        }

        for (int n = 0; n < PROBLEM_DIMENSION; n++) {
            // 只有开停机状态变化的才更新
            if (ucOriginal[n] != location[n][t]) {
                for (int i = t; i < TIME_PERIOD; i++) {
                    int previousStatus;
                    if (i == 0) {
                        previousStatus = unitList.get(n).getInitialStatus();
                    } else {
                        previousStatus = duration[n][i - 1];
                    }
                    // 开机状态
                    if (location[n][i] == 1) {
                        if (previousStatus > 0) {
                            duration[n][i] = previousStatus + 1;
                        } else {
                            duration[n][i] = 1;
                        }
                    } else {
                        if (previousStatus > 0) {
                            duration[n][i] = -1;
                        } else {
                            duration[n][i] = previousStatus - 1;
                        }
                    }
                }
            }
        }
    }

    /**
     * 根据机组组合确定开停机持续时间，正为开机，负为停机
     *
     * @param location 机组组合
     * @return 持续时间
     */
    public static int[][] getDuration(int[][] location) {

        int[][] duration = new int[PROBLEM_DIMENSION][TIME_PERIOD];
        for (int n = 0; n < PROBLEM_DIMENSION; n++) {
            for (int t = 0; t < TIME_PERIOD; t++) {
                int previousStatus;
                if (t == 0) {
                    previousStatus = unitList.get(n).getInitialStatus();
                } else {
                    previousStatus = duration[n][t - 1];
                }
                // 开机状态
                if (location[n][t] == 1) {
                    if (previousStatus > 0) {
                        duration[n][t] = previousStatus + 1;
                    } else {
                        duration[n][t] = 1;
                    }
                } else {
                    if (previousStatus > 0) {
                        duration[n][t] = -1;
                    } else {
                        duration[n][t] = previousStatus - 1;
                    }
                }
            }
        }

        return duration;
    }

    /**
     * 获取停机机组优先顺序的index
     *
     * @param uc 机组组合
     * @return index list
     */
    public static List<Integer> getUncommittedUnits(int[] uc) {
        List<Integer> uncommitted = new ArrayList<>();
        for (int i = 0; i < uc.length; i++) {
            if (uc[i] == 0) {
                uncommitted.add(UNITPRIORITY[i]);
            }
        }
        Collections.sort(uncommitted);

        List<Integer> result = new ArrayList<>();
        for (Integer integer : uncommitted) {
            result.add(ArrayUtils.indexOf(UNITPRIORITY, integer));
        }

        return result;
    }

    /**
     * 获取开机机组优先顺序的index
     *
     * @param uc 机组组合
     * @return index list
     */
    public static List<Integer> getCommittedUnits(int[] uc) {
        List<Integer> committed = new ArrayList<>();
        for (int i = 0; i < uc.length; i++) {
            if (uc[i] == 1) {
                committed.add(UNITPRIORITY[i]);
            }
        }
        Collections.sort(committed);
        Collections.reverse(committed);

        List<Integer> result = new ArrayList<>();
        for (Integer integer : committed) {
            result.add(ArrayUtils.indexOf(UNITPRIORITY, integer));
        }

        return result;
    }


}
