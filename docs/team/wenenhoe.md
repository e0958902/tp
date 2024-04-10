# Hoe Wen En - Project Portfolio Page

## Overview
* [Summary of Contributions](#summary-of-contributions)
* [Code related Contributions](#code-related-contributions)
* [Developer Guide Contributions](#developer-guide-contributions)
* [User Guide Contributions](#user-guide-contributions)
* [PR Reviews](#pr-reviews)
* [Team-based tasks](#team-based-tasks)

### Summary of Contributions
<!-- TODO: Summarise contributions -->

### Code related Contributions
- Issue #19: Mark taken status for `DailyMedication`
- Issue #36: Added argument parser to handle all `Command`s' arguments +
             Modify medication information through command             
- Issue #37: Delete medication from `MedicationManager`
- Issue #60: Bug `modify` overwrite other fields erroneously
- Issue #65: Bug Modify medication name does not update `DailyMedication` name
- Issue #77: Add support for no value flag arguments
- Issue #86: Add help messages for the different commands
- Issue #91: Add time period arguments and quantity management logic
- Issue #103: Remove and replace unused arguments
- Issue #94: Program crashes when provided empty arguments
- Issue #102: Program crashes when provided invalid index
- Issue #118: Enhance Ui to have methods to print different types of messages
- Issue #119: Add tests and handle exceptions for index of bound, missing medication, 
              unknown arguments, arguments with no value specified
- Issue #120: Add missing logic to `modify` and `delete` command
- Issue #125: Remove unused guided prompt from argument classes
- Issue #128: Fix `modify` handle erroneous data and fix `add` error handling
- Issue #154: Fix `NumberFormatException` for `modify` command (Related to issues #155, #179)
- Issue #194: Fix `NumberFormatException` for `delete` command (Related to issues #204)
- Issue #156: Fix `NumberFormatException` for `take` command (Related to issues #195)
- Issue #178: Fix `take` and `untake` commands show unchanged if quantity unchanged
- Issue #203: Fix `modify` not updating daily list when dosages are modified

### Developer Guide Contributions
- Information related to how `ArgumentParser` works
- Included what is required to add on and further extend the argument package
if more `Argument`s are needed
- Added information on how help messages for `ArgumentList` is generated

### User Guide Contributions
- Added description on command format
- Added information on how to use `modify`, `delete`, `take` and `untake`

### PR Reviews
#3, #6, #7, #10, #12, #17, #22, #28, #33, 
#35, #47, #49, #58, #59, #73, #75, #80, #84,
#101, #108, #109, #112, #113, #117, #122, #124,
#132, #136, #141, #206, #208, #210

### Team-based tasks
- Manage product development and resolve feature conflicts
- Disabled squash and rebase merging for Pull Requests
- Published v1.0 product in GitHub releases
- Published v2.0 product and user guide in GitHub releases
- Issue triaging of bug reports from Practical Exam Dry Run
  (Grouping similar bug reports and assigning to relevant team members)
