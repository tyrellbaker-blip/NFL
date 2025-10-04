import java.util.ArrayList;
import java.util.List;

public class Conference {
    private String name; // AFC or NFC
    private List<Division> divisions;

    public Conference(String name) {
        this.name = name;
        this.divisions = new ArrayList<>();
    }

    public void addDivision(Division division) {
        if (divisions.size() < 4) {
            divisions.add(division);
        } else {
            throw new IllegalStateException("Conference already has 4 divisions");
        }
    }

    public String getName() {
        return name;
    }

    public List<Division> getDivisions() {
        return divisions;
    }

    public List<Team> getAllTeams() {
        List<Team> allTeams = new ArrayList<>();
        for (Division division : divisions) {
            allTeams.addAll(division.getTeams());
        }
        return allTeams;
    }
}