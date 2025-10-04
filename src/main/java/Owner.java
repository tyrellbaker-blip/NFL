public class Owner extends Person {
    private double netWorth;

    public Owner(String name, int age, double netWorth) {
        super(name, age);
        this.netWorth = netWorth;
    }

    public double getNetWorth() {
        return netWorth;
    }

    @Override
    public String getRole() {
        return "Owner";
    }
}