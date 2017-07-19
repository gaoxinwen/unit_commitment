package cn.edu.hust.hs;

import cn.edu.hust.util.MathUtils;

public class Location {
	// store the Location in an array to accommodate multi-dimensional problem space
	// UC问题即为机组台数*时段数（N*T）的二维数组，0/1变量
	private int[][] loc;

	public Location() {
    }

    public Location(int[][] loc) {
        this.loc = loc;
    }

    public int[][] getLoc() {
        return loc;
    }

    public void setLoc(int[][] loc) {
        this.loc = loc;
    }

    /**
     * 获取某个时段的机组组合
     * @param t 时段t，从0开始
     * @return uc
     */
    public int[] getUC(int t) {
        if (loc == null) {
            throw new IllegalArgumentException("Location is null");
        }

        return MathUtils.getColumn(loc, t);
    }
}
