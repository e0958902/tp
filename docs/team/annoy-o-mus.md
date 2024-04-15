# Song Xuan - Project Portfolio Page

## Overview: MediTracker
The MediTracker project aims to track the medication usage and ensure that the user stays on track with their medication schedule.
It also aims to keep track of the (past) medication that has been taken.

## Summary of Contributions
For my part, I implemented the storage package for the team. This includes 
- Saving and Writing to and from the `.json` and `.txt` files.
- Getting and Passing data to and from `MedicationManager` and `DailyMedicationManager` to write to the files above.
- Performing Path sanitisation checks.

Furthermore, I have implemented the `save` and `load` commands to make use of the above functionality and to allow 
users to be able to specify where they want to save to.

I have also implemented the simulated time feature for us (the developers) to test MediTracker related data from 
another time or date without having to tweak the system time, and a custom logger to log certain warnings to stderr.

<div style="page-break-after: always;"></div>
### Code related Contributions
[Link to Code Dashboard](https://nus-cs2113-ay2324s2.github.io/tp-dashboard/?search=annoy-o-mus&breakdown=true)

Key Features:
- Saving and Reading from files (Medication data to and from JSON)
  - [#25](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/25), [#27](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/27), [#55](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/55)
- Saving and reading from files (DailyMedication data to and from `.txt`)
  - [#48](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/48), [#121](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/121), [#226](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/226)
- Offer custom saving functionality with path sanitisation checks (`save` and `load` commands)
  - [#96](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/96), [#97](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/97), [#100](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/100)
- Logging for MediTracker (`MediLogger`)
  - [#41](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/41), [#45](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/45), [#46](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/46)
- Simulated Time for development (`MediTrackerTime`)
  - [#43](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/43)


### Miscellaneous Code Contributions (Styling and Quality related)
Issues [#140](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/140), [#146](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/146),[#222](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/222), [#241](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/241)

### Developer Guide Contributions
- Brought in DG comments from other users in the main repo ([#211](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/211))
- Populate some related instructions under manual testing.
- Introduced some Non-Functional Requirements relating to my part (Modify and share data, time support for developers)
- Introduced users stories relating to my part (saving and loading)
- Exporting and Importing data process (with sequence diagram)
- Storage Design component

### User Guide Contributions
<!-- To talk about Issue 25 -->

### PR Reviews
[#2](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/2), [#5](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/5), 
[#40](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/40), [#44](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/44), 
[#51](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/51), [#66](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/66), 
[#82](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/82), [#87](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/87), 
[#93](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/93), [#111](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/111), 
[#209](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/209), [#215](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/215), 
[#218](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/218), [#221](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/221), 
[#227](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/227), [#237](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/237)
[#240](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/240), [#246](https://github.com/AY2324S2-CS2113T-T09-1/tp/pull/246)


### Team-based tasks
- Added PR Template ([#11](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/11), [#21](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/21)).
- Set up the GitHub Team organisation and Repo
  - Restricted direct master branch merging
  - Disabled rebase and squash merging (thanks @wenenhoe)
- Brought in the JSON package (https://github.com/stleary/JSON-java) ([#26](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/26))
- Created the Jekyll configuration and introduced admonition template ([#76](https://github.com/AY2324S2-CS2113T-T09-1/tp/issues/76))
