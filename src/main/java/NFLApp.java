import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

public class NFLApp extends Application {
    private League nfl;
    private ComboBox<Team> teamSelector;
    private ListView<String> rosterListView;
    private Label rosterCountLabel;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initializeNFL();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Top: Team selector
        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        Label teamLabel = new Label("Select Team:");
        teamSelector = new ComboBox<>();
        teamSelector.getItems().addAll(nfl.getAllTeams());
        teamSelector.setOnAction(e -> updateRosterView());
        topBar.getChildren().addAll(teamLabel, teamSelector);

        // Center: Roster list
        VBox centerBox = new VBox(10);
        rosterCountLabel = new Label("Roster: 0/53");
        rosterListView = new ListView<>();
        rosterListView.setPrefHeight(400);
        centerBox.getChildren().addAll(rosterCountLabel, rosterListView);

        // Right: Add player form
        VBox rightPanel = createAddPlayerPanel();

        root.setTop(topBar);
        root.setCenter(centerBox);
        root.setRight(rightPanel);

        Scene scene = new Scene(root, 900, 500);
        primaryStage.setTitle("NFL Roster Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createAddPlayerPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(0, 0, 0, 20));
        panel.setPrefWidth(300);

        Label title = new Label("Add Player");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TextField nameField = new TextField();
        nameField.setPromptText("Player Name");

        TextField ageField = new TextField();
        ageField.setPromptText("Age");

        TextField positionField = new TextField();
        positionField.setPromptText("Position (e.g., QB, RB, WR)");

        TextField jerseyField = new TextField();
        jerseyField.setPromptText("Jersey Number");

        TextField yearsField = new TextField();
        yearsField.setPromptText("Years in League");

        Button addButton = new Button("Add to Roster");
        addButton.setPrefWidth(150);
        addButton.setOnAction(e -> {
            Team selectedTeam = teamSelector.getValue();
            if (selectedTeam == null) {
                showAlert("Please select a team first.");
                return;
            }

            try {
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String position = positionField.getText().trim();
                int jersey = Integer.parseInt(jerseyField.getText().trim());
                int years = Integer.parseInt(yearsField.getText().trim());

                Player player = new Player(name, age, position, jersey, years);
                selectedTeam.addPlayer(player);

                // Clear fields
                nameField.clear();
                ageField.clear();
                positionField.clear();
                jerseyField.clear();
                yearsField.clear();

                updateRosterView();
            } catch (NumberFormatException ex) {
                showAlert("Please enter valid numbers for age, jersey, and years.");
            } catch (IllegalStateException ex) {
                showAlert(ex.getMessage());
            }
        });

        Button importButton = new Button("Import from Excel");
        importButton.setPrefWidth(150);
        importButton.setOnAction(e -> importRosterFromExcel());

        panel.getChildren().addAll(
            title,
            new Label("Name:"), nameField,
            new Label("Age:"), ageField,
            new Label("Position:"), positionField,
            new Label("Jersey #:"), jerseyField,
            new Label("Years in League:"), yearsField,
            addButton,
            new Separator(),
            new Label("Or:"),
            importButton
        );

        return panel;
    }

    private void updateRosterView() {
        Team selectedTeam = teamSelector.getValue();
        if (selectedTeam == null) {
            rosterListView.getItems().clear();
            rosterCountLabel.setText("Roster: 0/53");
            return;
        }

        rosterListView.getItems().clear();
        for (Player player : selectedTeam.getRoster()) {
            String playerInfo = String.format("#%d %s - %s (%d yrs, age %d)",
                player.getJerseyNumber(),
                player.getName(),
                player.getPosition(),
                player.getYearsInLeague(),
                player.getAge()
            );
            rosterListView.getItems().add(playerInfo);
        }

        rosterCountLabel.setText(String.format("Roster: %d/53 (%d spots available)",
            selectedTeam.getRosterSize(),
            selectedTeam.getAvailableRosterSpots()
        ));
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void importRosterFromExcel() {
        Team selectedTeam = teamSelector.getValue();
        if (selectedTeam == null) {
            showAlert("Please select a team first.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel File");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls")
        );

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file == null) {
            return;
        }

        try {
            List<Player> players = RosterImporter.importFromExcel(file);

            int added = 0;
            int skipped = 0;

            for (Player player : players) {
                try {
                    selectedTeam.addPlayer(player);
                    added++;
                } catch (IllegalStateException e) {
                    skipped++;
                }
            }

            Alert result = new Alert(Alert.AlertType.INFORMATION);
            result.setTitle("Import Complete");
            result.setHeaderText(null);
            result.setContentText(String.format("Import complete!\n\nAdded: %d players\nSkipped: %d players (roster full or invalid data)", added, skipped));
            result.showAndWait();

            updateRosterView();
        } catch (Exception e) {
            showAlert("Error importing file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeNFL() {
        nfl = new League("National Football League");

        Conference afc = new Conference("AFC");
        Conference nfc = new Conference("NFC");

        // Create divisions
        Division afcEast = new Division("East");
        Division afcNorth = new Division("North");
        Division afcSouth = new Division("South");
        Division afcWest = new Division("West");

        Division nfcEast = new Division("East");
        Division nfcNorth = new Division("North");
        Division nfcSouth = new Division("South");
        Division nfcWest = new Division("West");

        // AFC East
        afcEast.addTeam(new Team("Bills", "Buffalo", new Owner("Terry Pegula", 73, 7.7e9)));
        afcEast.addTeam(new Team("Dolphins", "Miami", new Owner("Stephen Ross", 84, 10.1e9)));
        afcEast.addTeam(new Team("Patriots", "New England", new Owner("Robert Kraft", 83, 11.1e9)));
        afcEast.addTeam(new Team("Jets", "New York", new Owner("Woody Johnson", 77, 3.5e9)));

        // AFC North
        afcNorth.addTeam(new Team("Ravens", "Baltimore", new Owner("Steve Bisciotti", 64, 7.0e9)));
        afcNorth.addTeam(new Team("Bengals", "Cincinnati", new Owner("Mike Brown", 89, 2.0e9)));
        afcNorth.addTeam(new Team("Browns", "Cleveland", new Owner("Jimmy Haslam", 71, 5.3e9)));
        afcNorth.addTeam(new Team("Steelers", "Pittsburgh", new Owner("Art Rooney II", 72, 3.0e9)));

        // AFC South
        afcSouth.addTeam(new Team("Texans", "Houston", new Owner("Janice McNair", 87, 5.4e9)));
        afcSouth.addTeam(new Team("Colts", "Indianapolis", new Owner("Jim Irsay", 65, 4.2e9)));
        afcSouth.addTeam(new Team("Jaguars", "Jacksonville", new Owner("Shad Khan", 74, 12.0e9)));
        afcSouth.addTeam(new Team("Titans", "Tennessee", new Owner("Amy Adams Strunk", 66, 2.5e9)));

        // AFC West
        afcWest.addTeam(new Team("Broncos", "Denver", new Owner("Rob Walton", 80, 72.0e9)));
        afcWest.addTeam(new Team("Chiefs", "Kansas City", new Owner("Clark Hunt", 59, 2.0e9)));
        afcWest.addTeam(new Team("Raiders", "Las Vegas", new Owner("Mark Davis", 69, 1.9e9)));
        afcWest.addTeam(new Team("Chargers", "Los Angeles", new Owner("Dean Spanos", 74, 2.4e9)));

        // NFC East
        nfcEast.addTeam(new Team("Cowboys", "Dallas", new Owner("Jerry Jones", 82, 15.0e9)));
        nfcEast.addTeam(new Team("Giants", "New York", new Owner("John Mara", 70, 3.0e9)));
        nfcEast.addTeam(new Team("Eagles", "Philadelphia", new Owner("Jeffrey Lurie", 73, 4.9e9)));
        nfcEast.addTeam(new Team("Commanders", "Washington", new Owner("Josh Harris", 60, 8.5e9)));

        // NFC North
        nfcNorth.addTeam(new Team("Bears", "Chicago", new Owner("Virginia McCaskey", 101, 6.0e9)));
        nfcNorth.addTeam(new Team("Lions", "Detroit", new Owner("Sheila Ford Hamp", 72, 2.4e9)));
        nfcNorth.addTeam(new Team("Packers", "Green Bay", new Owner("Mark Murphy", 69, 1.0e9)));
        nfcNorth.addTeam(new Team("Vikings", "Minnesota", new Owner("Zygi Wilf", 74, 6.0e9)));

        // NFC South
        nfcSouth.addTeam(new Team("Falcons", "Atlanta", new Owner("Arthur Blank", 82, 7.6e9)));
        nfcSouth.addTeam(new Team("Panthers", "Carolina", new Owner("David Tepper", 66, 20.6e9)));
        nfcSouth.addTeam(new Team("Saints", "New Orleans", new Owner("Gayle Benson", 77, 4.8e9)));
        nfcSouth.addTeam(new Team("Buccaneers", "Tampa Bay", new Owner("Glazer Family", 70, 6.2e9)));

        // NFC West
        nfcWest.addTeam(new Team("Cardinals", "Arizona", new Owner("Michael Bidwill", 59, 2.3e9)));
        nfcWest.addTeam(new Team("Rams", "Los Angeles", new Owner("Stan Kroenke", 77, 14.0e9)));
        nfcWest.addTeam(new Team("49ers", "San Francisco", new Owner("Jed York", 44, 3.5e9)));
        nfcWest.addTeam(new Team("Seahawks", "Seattle", new Owner("Jody Allen", 64, 20.3e9)));

        // Build hierarchy
        afc.addDivision(afcEast);
        afc.addDivision(afcNorth);
        afc.addDivision(afcSouth);
        afc.addDivision(afcWest);

        nfc.addDivision(nfcEast);
        nfc.addDivision(nfcNorth);
        nfc.addDivision(nfcSouth);
        nfc.addDivision(nfcWest);

        nfl.addConference(afc);
        nfl.addConference(nfc);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
