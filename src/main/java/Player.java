public class Player extends Person {
    private String position; // QB, RB, WR, etc.
    private int jerseyNumber;
    private int yearsInLeague;
    private Team team;

    public Player(String name, int age, String position, int jerseyNumber, int yearsInLeague) {
        super(name, age);
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.yearsInLeague = yearsInLeague;
        this.team = null;
    }

    public String getPosition() {
        return position;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public int getYearsInLeague() {
        return yearsInLeague;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    @Override
    public String getRole() {
        return position;
    }
}