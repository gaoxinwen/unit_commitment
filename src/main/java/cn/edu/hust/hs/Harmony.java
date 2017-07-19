package cn.edu.hust.hs;

public class Harmony {
	// 适应度
	private double fitnessValue;
	// 取值，对应于机组组合问题，就是时段数*机组台数的二维数组
	private Location location;

	private Velocity velocity;

	public Harmony() {
        super();
    }

	public double getFitnessValue() {
        fitnessValue = ProblemSet.evaluate(location);
        return fitnessValue;
	}

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Velocity getVelocity() {
        return velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }
}
