# Nick Chua - Project Portfolio Page

## Overview

### Summary of Contributions

### Code related Contributions
- Issue #14: Add Ui
- Issue #16: Add medication entry and information via one-line command
- Issue #52: Bridging DailyMedicationManager and MedicationManager
- Issue #71: Add assertion to Add command
- Issue #72: Modify Ui to be static
- Issue #74: More flags for Add command and Exception Handling

### Developer Guide Contributions
- Add command
### User Guide Contributions
- Add command
  - Description
  - Command Format
  - Example
  - Output

### PR Reviews
#42, #61, #62, #64, #70, #73, #75, #127, #131, #132

### Miscellaneous
#### Refactor:
- Package names
- Gradle configuration files
#### Ui:
- Develop the user interface for the MediTracker. 
- It includes getting the user input, printing the welcome, exit message and more.
#### Command:
- Create command abstract class, AddCommand and ExitCommand. Add JUnit test for AddCommand.
#### Medication:
- Create Medication class and MedicationList class
#### Defensive code:
- Handled exceptions in MediTracker class as well as invalid command input in Command class.