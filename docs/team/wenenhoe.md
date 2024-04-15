# Hoe Wen En - Project Portfolio Page

## Overview
* [Summary of Contributions](#summary-of-contributions)
* [Code related Contributions](#code-related-contributions)
* [Developer Guide Contributions](#developer-guide-contributions)
* [User Guide Contributions](#user-guide-contributions)
* [PR Reviews](#pr-reviews)
* [Team-based tasks](#team-based-tasks)

### Summary of Contributions
- Implemented argument parser to
  - Handle all `Command`'s arguments ([#40](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/40))
  - Generate and print help messages for each `Command` ([#87](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/87))
  - (Planned but cancelled) guided prompt
- Implemented `take` and `untake` commands
  - Underlying data structure to manage taken status ([#20](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/20))
  - Commands themselves ([#54](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/54))
- Implemented `modify` command ([#40](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/40))
- Implemented `delete` command ([#44](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/44))
- Enhance Ui to have methods to print different types of messages 
  ([#111](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/111))
- Refactor data validation to unify all into Medication setters 
  ([#221](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/221))
- Refactor argument exceptions to simplify exceptions thrown 
  ([#229](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/229))

### Code related Contributions
- [#19](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/19):
  Mark taken status for `DailyMedication`
- [#36](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/36): 
  Added argument parser to handle all `Command`s' arguments + 
  Modify medication information through command             
- [#37](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/37):
  Delete medication from `MedicationManager`
- [#60](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/60):
  Bug `modify` overwrite other fields erroneously
- [#65](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/65):
  Bug Modify medication name does not update `DailyMedication` name
- [#77](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/77):
  Add support for no value flag arguments
- [#86](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/86):
  Add help messages for the different commands
- [#91](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/91):
  Add time period arguments and quantity management logic
- [#103](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/103):
  Remove and replace unused arguments
- [#94](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/94):
  Program crashes when provided empty arguments
- [#102](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/102):
  Program crashes when provided invalid index
- [#118](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/118):
  Enhance Ui to have methods to print different types of messages
- [#119](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/119):
  Add tests and handle exceptions for index of bound, missing medication, 
  unknown arguments, arguments with no value specified
- [#120](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/120):
  Add missing logic to `modify` and `delete` command
- [#125](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/125):
  Remove unused guided prompt from argument classes
- [#128](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/128):
  Fix `modify` handle erroneous data and fix `add` error handling
- [#154](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/154):
  Fix `NumberFormatException` for `modify` command 
  (Related to issues [#155](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/155),
  [#179](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/179))
- [#194](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/194):
  Fix `NumberFormatException` for `delete` command 
  (Related to issues [#204](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/204))
- [#156](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/156):
  Fix `NumberFormatException` for `take` command 
  (Related to issues [#195](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/195))
- [#178](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/178):
  Fix `take` and `untake` commands show unchanged if quantity unchanged
- [#203](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/203):
  Fix `modify` not updating daily list when dosages are modified
- [#205](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/205):
  Fix large value not handled by `add` and `modify`
- [#224](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/224):
  Move all validation to Medication setters so all values are validated
- [#123](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/123):
  Refactor argument exceptions to simplify exceptions thrown

### Developer Guide Contributions
- Information related to how `ArgumentParser` works
- Included what is required to add on and further extend the argument package
if more `Argument`s are needed
- Added information on how help messages for `ArgumentList` is generated
- Added sequence diagrams for `ModifyCommand`
- Added class diagram for `meditracker.argument` package core classes

### User Guide Contributions
- Added description on command format
- Added information on how to use `modify`, `delete`, `take` and `untake`

### PR Reviews
[#3](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/3),
[#6](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/6),
[#7](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/7),
[#10](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/10),
[#12](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/12),
[#17](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/17),
[#22](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/22),
[#28](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/28),
[#33](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/33),
[#35](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/35),
[#47](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/47),
[#49](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/49),
[#58](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/58),
[#59](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/59),
[#73](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/73),
[#75](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/75),
[#80](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/80),
[#84](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/84),
[#101](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/101),
[#108](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/108),
[#109](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/109),
[#112](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/112),
[#113](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/113),
[#117](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/117),
[#122](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/122),
[#124](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/124),
[#132](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/132),
[#136](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/136),
[#141](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/141),
[#206](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/206),
[#208](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/208),
[#210](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/210),
[#216](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/216),
[#219](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/219),
[#220](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/220),
[#228](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/228),
[#230](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/230),
[#232](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/232),
[#234](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/234)

### Team-based tasks
- Manage product development and resolve feature conflicts
- Disabled squash and rebase merging for Pull Requests
- Published v1.0 product in GitHub releases
- Published v2.0 product and user guide in GitHub releases
- Issue triaging of bug reports from Practical Exam Dry Run
  (Grouping similar bug reports and assigning to relevant team members)
- Added bottom navigation bar for quick access to Table of Contents 
and Command Summary in User Guide
