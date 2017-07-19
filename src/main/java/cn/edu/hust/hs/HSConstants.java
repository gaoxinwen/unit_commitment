package cn.edu.hust.hs;

public interface HSConstants {

	/**
	 * 算法的一些基本参数就暂时定义在这里吧
	 */
    // Harmony memory size
    int HMS = 30;
    //the number of new generating candidates
    int NGC = 30;
    // Harmony memory considering rate
    double HMCR = 0.95;
    // Pitch adjusting rate
    double PAR = 0.3;
    // Band Width Damp Ratio
    double BW_damp = 0.995;
	
	// 最大迭代次数
	int MAX_ITERATION = 500;

    double C1 = 2.0;
    double C2 = 2.0;
    double W_UPPERBOUND = 1.0;
    double W_LOWERBOUND = 0.0;
	
	
}
