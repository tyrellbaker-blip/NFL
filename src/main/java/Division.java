import java.util.ArrayList;
import java.util.List;

public class Division {
    private String name; // North, South, East, West
    private List<Team> teams;

    public Division(String name) {
        this.name = name;
        this.teams = new ArrayList<>();
    }

    public void addTeam(Team team) {
        if (teams.size() < 4) {
            teams.add(team);
        } else {
            throw new IllegalStateException("Division already has 4 teams");
        }
    }

    public String getName() {
        return name;
    }

    public List<Team> getTeams() {
        return teams;
    }
}