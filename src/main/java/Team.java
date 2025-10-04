import java.util.ArrayList;
import java.util.List;

public class Team {
    private static final int MAX_ROSTER_SIZE = 53;

    private String name;
    private String city;
    private Owner owner;
    private List<Coach> coaches;
    private List<Player> roster;
    private int wins;
    private int losses;

    public Team(String name, String city, Owner owner) {
        this.name = name;
        this.city = city;
        this.owner = owner;
        this.coaches = new ArrayList<>();
        this.roster = new ArrayList<>();
        this.wins = 0;
        this.losses = 0;
    }

    public void addCoach(Coach coach) {
        coaches.add(coach);
    }

    public void addPlayer(Player player) {
        if (roster.size() >= MAX_ROSTER_SIZE) {
            throw new IllegalStateException("Roster is full. Cannot exceed " + MAX_ROSTER_SIZE + " players.");
        }
        roster.add(player);
        player.setTeam(this);
    }

    public void removePlayer(Player player) {
        if (roster.remove(player)) {
            player.setTeam(null);
        }
    }

    public int getRosterSize() {
        return roster.size();
    }

    public int getAvailableRosterSpots() {
        return MAX_ROSTER_SIZE - roster.size();
    }

    public String getFullName() {
        return city + " " + name;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public Owner getOwner() {
        return owner;
    }

    public List<Coach> getCoaches() {
        return coaches;
    }

    public List<Player> getRoster() {
        return roster;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public void recordWin() {
        wins++;
    }

    public void recordLoss() {
        losses++;
    }

    @Override
    public String toString() {
        return getFullName();
    }
}