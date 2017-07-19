package cn.edu.hust.unit;

/**
 * Description:
 * Created by Gaoxinwen on 2017/1/3.
 */
public class Unit {
    private int id;
    private double Pmin;
    private double Pmax;
    private int minUpTime;
    private int minDownTime;
    private double hotStartCost;
    private double coldStartCost;
    private int coldStartHour;
    private int initialStatus;
    private double a;
    private double b;
    private double c;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPmin() {
        return Pmin;
    }

    public void setPmin(double pmin) {
        Pmin = pmin;
    }

    public double getPmax() {
        return Pmax;
    }

    public void setPmax(double pmax) {
        Pmax = pmax;
    }

    public int getMinUpTime() {
        return minUpTime;
    }

    public void setMinUpTime(int minUpTime) {
        this.minUpTime = minUpTime;
    }

    public int getMinDownTime() {
        return minDownTime;
    }

    public void setMinDownTime(int minDownTime) {
        this.minDownTime = minDownTime;
    }

    public double getHotStartCost() {
        return hotStartCost;
    }

    public void setHotStartCost(double hotStartCost) {
        this.hotStartCost = hotStartCost;
    }

    public double getColdStartCost() {
        return coldStartCost;
    }

    public void setColdStartCost(double coldStartCost) {
        this.coldStartCost = coldStartCost;
    }

    public int getColdStartHour() {
        return coldStartHour;
    }

    public void setColdStartHour(int coldStartHour) {
        this.coldStartHour = coldStartHour;
    }

    public int getInitialStatus() {
        return initialStatus;
    }

    public void setInitialStatus(int initialStatus) {
        this.initialStatus = initialStatus;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Unit{" +
                "id=" + id +
                ", Pmin=" + Pmin +
                ", Pmax=" + Pmax +
                ", minUpTime=" + minUpTime +
                ", minDownTime=" + minDownTime +
                ", hotStartCost=" + hotStartCost +
                ", coldStartCost=" + coldStartCost +
                ", coldStartHour=" + coldStartHour +
                ", initialStatus=" + initialStatus +
                ", a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
