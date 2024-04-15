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
- Issue #159: [PE-D][Tester E] Remarks changes from null to empty upon restart of application
- Issue #182: [PE-D][Tester C] Lack of /help page
- Issue #184: [PE-D][Tester F] using an example from the user guide is causing an error
- Issue #191: [PE-D][Tester F] empty input is allowed when adding
- Issue #231: NoSuchElementException when program is terminated with Ctrl-C
- Issue #235: UG Command Summary, Table of Contents linking and back

### Developer Guide Contributions
- Add command specifications
- Update Add Command with sequence diagrams (Issue #138)
### User Guide Contributions
- Add command
  - Description
  - Command Format
  - Example
  - Output
- Command Summary
- Table of Contents

- Help command

### Team-based tasks
- Request permission for use of JSON-Java package in cs2113 forum
[(Issue #16)](https://github.com/nus-cs2113-AY2324S2/forum/issues/16)

### PR Reviews
#42, #61, #62, #64, #70, #73, #75, #127, #131, #132, #142, #144, #227

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