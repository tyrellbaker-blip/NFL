public class Coach extends Person {
    private String position; // Head Coach, Offensive Coordinator, Defensive Coordinator, etc.
    private int yearsExperience;

    public Coach(String name, int age, String position, int yearsExperience) {
        super(name, age);
        this.position = position;
        this.yearsExperience = yearsExperience;
    }

    public String getPosition() {
        return position;
    }

    public int getYearsExperience() {
        return yearsExperience;
    }

    @Override
    public String getRole() {
        return position;
    }
}