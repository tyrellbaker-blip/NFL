# Excel Roster Import Template

To import a roster from Excel, your file should have the following format:

## Column Structure
| team | jersey_number | name | position | age | years_in_league |
|------|---------------|------|----------|-----|-----------------|
| Kansas City | 15 | Patrick Mahomes | QB | 29 | 8 |
| Kansas City | 87 | Travis Kelce | TE | 35 | 12 |
| Kansas City | 95 | Chris Jones | DT | 30 | 9 |

## Instructions
1. First row must be headers (will be skipped during import)
2. Columns must be in this exact order:
   - Column A: team (text - ignored, select team in app)
   - Column B: jersey_number (number, 0-99)
   - Column C: name (text)
   - Column D: position (text, e.g., QB, RB, WR, TE, etc.)
   - Column E: age (number)
   - Column F: years_in_league (number)
3. Save as .xlsx or .xls format
4. In the app, select a team first, then click "Import from Excel"
5. The app will add up to 53 players (roster limit)