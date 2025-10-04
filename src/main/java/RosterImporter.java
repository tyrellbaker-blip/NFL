import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RosterImporter {

    /**
     * Import players from an Excel file.
     * Expected columns: team | jersey_number | name | position | age | years_in_league
     * First row is treated as header and skipped.
     */
    public static List<Player> importFromExcel(File file) throws IOException {
        List<Player> players = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean firstRow = true;

            for (Row row : sheet) {
                // Skip header row
                if (firstRow) {
                    firstRow = false;
                    continue;
                }

                // Skip empty rows
                if (isRowEmpty(row)) {
                    continue;
                }

                try {
                    // Column mapping: team(0) | jersey_number(1) | name(2) | position(3) | age(4) | years_in_league(5)
                    String name = getCellValueAsString(row.getCell(2));
                    int jerseyNumber = getCellValueAsInt(row.getCell(1));
                    String position = getCellValueAsString(row.getCell(3));
                    int age = getCellValueAsInt(row.getCell(4));
                    int yearsInLeague = getCellValueAsInt(row.getCell(5));

                    Player player = new Player(name, age, position, jerseyNumber, yearsInLeague);
                    players.add(player);
                } catch (Exception e) {
                    // Skip invalid rows
                    System.err.println("Skipping invalid row: " + e.getMessage());
                }
            }
        }

        return players;
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private static int getCellValueAsInt(Cell cell) {
        if (cell == null) {
            return 0;
        }

        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return 0;
                }
            default:
                return 0;
        }
    }

    private static boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }

        for (int i = 0; i < 6; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }
}