import java.util.ArrayList;
import java.util.List;

public class League {
    private String name;
    private List<Conference> conferences;

    public League(String name) {
        this.name = name;
        this.conferences = new ArrayList<>();
    }

    public void addConference(Conference conference) {
        conferences.add(conference);
    }

    public String getName() {
        return name;
    }

    public List<Conference> getConferences() {
        return conferences;
    }

    public List<Team> getAllTeams() {
        List<Team> allTeams = new ArrayList<>();
        for (Conference conference : conferences) {
            allTeams.addAll(conference.getAllTeams());
        }
        return allTeams;
    }

    public int getTotalTeams() {
        return getAllTeams().size();
    }
}