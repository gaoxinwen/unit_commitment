package cn.edu.hust.hs;

public class Velocity {
    // store the Velocity in an array to accommodate multi-dimensional problem space
    // Location为二维数组，Velocity也为二维数组（N*T）
    private double[][] vel;

    public Velocity(double[][] vel) {
        this.vel = vel;
    }

    public double[][] getPos() {
        return vel;
    }

    public void setPos(double[][] vel) {
        this.vel = vel;
    }

}
