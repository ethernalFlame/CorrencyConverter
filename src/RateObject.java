public class RateObject {
    private String name;
    private double rate;
    public RateObject(String name, double rate)
    { this.name = name;
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "";
    }

    public String getName() {
        return name;
    }

    public double getRate() {
        return rate;
    }
}
