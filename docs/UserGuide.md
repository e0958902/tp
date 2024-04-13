---
layout: default
title: User Guide
---
<!-- 
CREDIT: We try to follow the ideas and structure of the following sample user guide:
https://se-education.org/addressbook-level3/UserGuide.html
-->

# User Guide
## Introduction
MediTracker is a desktop app for users who want to track their medication intake on a daily basis. It offers intuitive
features that works seamlessly right from the very beginning. With MediTracker, you do not have to worry about missing
your daily medication dose. It tracks your daily intake and reminds you when your remaining amount is low, and enables
data storage so that you can review your past medication intake.

* [How to use the User Guide](#how-to-use-the-user-guide)
* [Quick Start](#quick-start)
* [Features](#features)
  - [Add Medication](#adding-a-medication-add)`add`
  - [List Medication](#listing-medications-list)
    - [Summary of all medications:](#all-medications)`list -t all`
    - [Summary of medications for the day](#daily-medications)`list -t today`
  - [View Medication](#view-medication-view)
    - [View Medication by index:](#View-Medication-by-index)`view -l`
    - [View Medication by name:](#View-Medication-by-name)`view -n`
    - [View Medication by quantity:](#View-Medication-by-quantity)`view -q`
    - [View Medication by expiry:](#View-Medication-by-expiry)`view -e`
    - [View Medication by remarks:](#View-Medication-by-remarks)`view -r`
  - [Update medication information](#update)
    - [Record taking of medication](#record-taking-of-medication)`take/untake -l`
    - [Modify medication information](#modify-medication-information)`modify -l`
  - [Delete medication information](#delete-a-medication)`delete -l`
  - [Search](#search)
      - [Access Medicine Database](#access-database)`search`
  - [Help](#help)
  - [General data management](#general-data-management)
    - [Saving to a file:](#saving-to-a-file-save)`save`
    - [Reading from a file:](#reading-from-a-file-load)`load`
    - [Editing the file](#editing-the-file)
    - [Advanced feature: Setting an arbitrary time](#advanced-feature-setting-an-arbitrary-time)
  - [Exit](#exit)
- [FAQ](#faq)
- [Glossary](#glossary)
- [Command Summary](#command-summary)

## How to Use the User Guide
<!-- @@author annoy-o-mus-reused
reused from: https://ay2223s1-cs2103t-w16-2.github.io/tp/UserGuide.html#admonition-boxes
with minor modifications
-->
Throughout this guide, you may find coloured boxes containing highlighted pieces of text.
They are admonitions to help you take note of the content within.

| **Symbol**           | **Representation** | **Meaning**                                                                                  |
|----------------------|--------------------|----------------------------------------------------------------------------------------------|
| :notebook:           | Note               | Something you should keep in mind.                                                           
| :bulb:               | Tip                | Something you might find useful.                                                             |
| :information_source: | Info               | Something you might find useful for the particular context.                                  |
| :warning:            | Warning            | Something that you should pay close attention to.                                            |
| :bangbang:           | Danger             | Something you you must pay attention to. There is a risk of program failure if not followed. |

<!-- @@author -->
Below are some examples of highlighted pieces of text:

<div class="note-box">
:notebook: <strong>Note: </strong>
A note to take note.
</div>

<div class="tip-box">
:bulb: <strong>Tip: </strong>
A useful tip.
</div>

<div class="info-box">
:information_source: <strong>Info: </strong>
Some informative text.
</div>

<div class="warning-box">
:warning: <strong>Warning: </strong>
Important text.
</div>

<div class="danger-box">
:bangbang: <strong>Danger: </strong>
Extremely important text.
</div>

## Quick Start
1. Ensure that you have Java 11 or above installed. 
   - If you using Windows:
     - Press the `Windows Key` on your keyboard and enter `Command Prompt` or `cmd` and press enter.
     - When the program opens, enter `java -version`.
   - If you are using MacOS/Linux:
     - Open the `Terminal`, then enter `java -version`.
   - If Java 11 is installed, you should get an output similar to below:
      - {SS}.
   - If you see otherwise, please follow the instructions from [here](https://docs.oracle.com/en/java/javase/11/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A).
3. Once that is settled, head over to the [link](https://github.com/AY2324S2-CS2113T-T09-1/tp/releases/latest) to obtain the latest version of `MediTracker`.
4. Save the file in an empty folder in `Desktop`, then this will be your home folder for `MediTracker`.
5. Double-click on meditracker.jar to start the app.
   - Alternatively, you may also use the Command Prompt/Terminal. 
   - Navigate to the home folder where you have saved the program in, and enter `java -jar meditracker.jar` to 
   start the app.


## Features 

#### NOTES about the command format:

- Each command starts with a command keyword
- Following which, you may specify additional options
- These options can be in round brackets `()` (which are mandatory arguments) and in square brackets `[]` (which are
optional arguments)
- Each option are specified with either just its flag (which is of the form `-*` where `*` signifies some alphabetic 
characters), or the flag coupled with a value

## Adding a medication: `add`

Adds a medication to the medication manager.

Format: `add -n MEDICATION_NAME -q QUANTITY -e EXPIRATION_DATE -dM DOSAGE_MORNING -dA DOSAGE_AFTERNOON 
-dE DOSAGE_EVENING -r REMARKS -rep REPEAT`

```
Options:
-n name                 Name of medication
-q quantity             Quantity of medication
-e expirationDate       Expiration date of medication
-dM dosageMorning       Morning dosage of medication
-dA dosageAfternoon     Afternoon dosage of medication
-dE dosageEvening       Evening dosage of medication
-r remarks              Additional remarks on medication
-rep                    How often to take medication (eg: Supply a number from 1 to 7)
```


<div class="tip-box">
:bulb: <strong>Tip: </strong>
The dosage and remarks tags are optional.
</div>

<div class="note-box">
:notebook: <strong>Note: </strong>
Remarks will default to Nil if it is not specified.
</div>

Examples:

* `meditracker> add -n Medication A -q 5000 -e 01/07/25 -dM 500 -dA 250 -r cause_dizziness -rep 1`
* `meditracker> add -n Medication B -q 1000 -e 30/09/24 -dM 500 -dA 250 -dE 50 -rep 6`

Output:
```
SUCCESS: Medicine has been added
```
## Listing medications: `list`
You can show the medications that you have added to the medication list, 
and show the medications that you will be taking for the day.

```
Usage:
	list (-t listType) [-m] [-a] [-e] [-h]
Options:
	-t listType     Lists medications accordingly
	-m              Time of day: Morning
	-a              Time of day: Afternoon
	-e              Time of day: Evening
	-h              Prints this help message
```

### All medications:
Displays the whole lists of medications that you have added to the medication list.

Example: `meditracker> list -t all`

Output:
```
You have 2 medications listed below.
   Name                           Quantity   Expiry     Remarks                       
1. Medication A                   5000.0     01/07/25   cause_dizziness  
2. Medication B                   1000.0     30/09/24
Your list of medications has been successfully shown!
```

### Daily medications:
Displays an overview of the list of medications that you will be taking for the day.

Example: `meditracker> list -t today`
* If you want to specify a particular period of the day, simply enter `list -t today` followed by:
  * `-m`: Morning (Midnight to 12 Noon)
  * `-a`: Afternoon (12pm to 6pm)
  * `-e`: Evening (6pm to Midnight)
  * Example: `meditracker> list -t today -m` - will display the list of medications to be taken in the morning only

Output:
```
Here are the Daily Medications you have to take today: 
Morning:
	1. [ ] Medication_A | 2.0
Afternoon:
	1. [ ] Medication_A | 1.0
```

<div class="info-box">

:information_source: <strong>Info: </strong>
If you want to mark the medication you have taken, you can refer to the [Take command](#take-a-medication) here.

</div>

## Viewing medications: `view`
You can view detailed information about the medications you have added in the medication list.

<div class="info-box">
:information_source: <strong>Info: </strong>

Perform [`list -t all`](#All-medications) first to display the lists of medications 
in medication list and use its fields for the features below.

</div>

This output will be shown if you used more than one flag and argument.
```
ERROR: You can only have one flag!
```
<div class="tip-box">
:bulb: <strong>Tip: </strong>
You are only allowed to use one flag and argument.
</div>

```
Usage:
	view [-l listIndex] [-n name] [-q quantity] [-e expirationDate] [-r remarks] [-h]
Options:
	-l listIndex          Index of item in list
	-n name               Name of medication
	-q quantity           Quantity of medication
	-e expirationDate     Expiration date of medication
	-r remarks            Additional remarks on medication
	-h                    Prints this help message
```

### View Medication by index:
By using the index shown in the medication list [`list -t all`](#all-medications), 
you can see all the fields of that medication index.

Format: `view -l MEDICATION_INDEX`

Example: `meditracker> view -l 1`

<div class="tip-box">
:bulb: <strong>Tip: </strong>
Only the first flag and argument will be used to 
show a medication by the specified index.
</div>

Output:
```
Name: Medication A
Quantity: 5000.0
Expiry Date: 01/07/25
Remarks: cause_dizziness
Morning Dosage: 500.0
Afternoon Dosage: 250.0
Evening Dosage: 0.0
Repeat: 1

SUCCESS: Medication details has been retrieved
```


### View Medication by name:
You can view all medication information by its name.

Format: `view -n MEDICATION_NAME`

Example: `meditracker> view -n medication_b`

<div class="tip-box">
:bulb: <strong>Tip: </strong>
Only the first flag and argument will be used to 
show a medication by the specified name.
</div>

Output:
```
Name: Medication B
Quantity: 1000.0
Expiry Date: 30/09/24
Remarks: 
Morning Dosage: 500.0
Afternoon Dosage: 250.0
Evening Dosage: 50.0
Repeat: 6

SUCCESS: Medication details has been retrieved
```


### View Medication by quantity:
You can view all medication information by its quantity.

Format: `view -q MEDICATION_QUANTITY`

Example: `meditracker> view -q 1000`

<div class="tip-box">
:bulb: <strong>Tip: </strong>
Only the first flag and argument will be used to 
show all medications that is less than or equal to the specified quantity.
</div>

Output:
```
Name: Medication B
Quantity: 1000.0
Expiry Date: 30/09/24
Remarks: null
Morning Dosage: 500.0
Afternoon Dosage: 250.0
Evening Dosage: 50.0
Repeat: 6

SUCCESS: Medication details has been retrieved
```


### View Medication by expiry:
You can view all medication information by its expiry year.

Format: `view -e MEDICINE_EXPIRY_IN_YY`

Example: `meditracker> view -e 25`

<div class="tip-box">
:bulb: <strong>Tip: </strong>
Only the first flag and argument will be used to 
show all medications that is expiring by that specified year.
</div>

Output:
```
Name: Medication A
Quantity: 5000.0
Expiry Date: 01/07/25
Remarks: cause_dizziness
Morning Dosage: 500.0
Afternoon Dosage: 250.0
Evening Dosage: 0.0
Repeat: 1

Name: Medication B
Quantity: 1000.0
Expiry Date: 30/09/24
Remarks: null
Morning Dosage: 500.0
Afternoon Dosage: 250.0
Evening Dosage: 50.0
Repeat: 6

SUCCESS: Medication details has been retrieved
```


### View Medication by remarks:
You can view all medication information by its remarks.

Format: `view -r MEDICINE_REMARKS`

Example: `meditracker> view -r dizziness`

<div class="tip-box">
:bulb: <strong>Tip: </strong>
Only the first flag and argument will be used to
show all medications that contains the specified remarks.
</div>

Output:
```
Name: Medication A
Quantity: 5000.0
Expiry Date: 01/07/25
Remarks: cause_dizziness
Morning Dosage: 500.0
Afternoon Dosage: 250.0
Evening Dosage: 0.0
Repeat: 1

SUCCESS: Medication details has been retrieved
```

## Update

### Modify Medication Information

If you require modification to the medication information, you can type `modify`, followed by the list index 
`-l listIndex`. You can obtain the list index from the Medication List by entering [`list -t all`](#all-medications).


<div class="note-box">
:notebook: <strong>Note: </strong>
This command allows multiple flags to be specified in one line.
(For each of the fields that you would like to modify, you can specify the corresponding flag and the new value to make
changes.)
</div>

```
Usage: 
    modify (-l listIndex) [-n name] [-q quantity] [-dM dosageMorning] [-dA dosageAfternoon] [-dE dosageEvening] 
[-e expirationDate] [-r remarks] [-rep] [-h]

Options:
	-l listIndex            Index of item in list
	-n name                 Name of medication
	-q quantity             Quantity of medication
	-dM dosageMorning       Morning dosage of medication
	-dA dosageAfternoon     Afternoon dosage of medication
	-dE dosageEvening       Evening dosage of medication
	-e expirationDate       Expiration date of medication
	-r remarks              Additional remarks on medication
	-rep                    How often to take medication (eg: Supply a number from 1 to 7)
	-h                      Prints this help message
```
Examples:

* `meditracker> modify -l 1 -n MedB`
* `meditracker> modify -l 1 -q 40 -dA 2`

Output:
```
SUCCESS: Medicine has been modified
```

### Record taking of medication

#### Take a medication

When you have taken your medication, you can type `take`, followed by the list index `-l listIndex` to mark it as taken.
You can obtain the list index by entering [`list -t today`](#daily-medications) and refer to the index for the 
medication you have taken. This command is time aware and will mark the index you specified based on what time of day 
it is. 

<div class="tip-box">
:bulb: <strong>Tip: </strong>
Optionally, you can specify `-m`, `-a`, `-e` to override the time aware feature.
</div>

```
Usage:
	take (-l listIndex) [-m] [-a] [-e] [-h]
Options:
	-l listIndex     Index of item in list
	-m               Time of day: Morning
	-a               Time of day: Afternoon
	-e               Time of day: Evening
	-h               Prints this help message
```

Examples:

* `meditracker> take -l 1`
* `meditracker> take -l 1 -m`

Output:
```
INFO: Medication quantity decreased: 40.0 -> 38.0
SUCCESS: Medicine has been taken
```

#### Untake a medication

If you have accidentally entered the wrong command and wish to un-take the medication, you can type `untake`, 
followed by the list index `-l listIndex`. Similarly, you can obtain the list index by entering 
[`list -t today`](#daily-medications) and refer to the index for the medication you wish to un-mark. This command is 
time aware and will mark the index of the medication based on what time of day it is. 

<div class="tip-box">
:bulb: <strong>Tip: </strong>
Optionally, you can specify `-m`, `-a`, `-e` to override the time aware feature.
</div>

```
Usage:
	untake (-l listIndex) [-m] [-a] [-e] [-h]
Options:
	-l listIndex     Index of item in list
	-m               Time of day: Morning
	-a               Time of day: Afternoon
	-e               Time of day: Evening
	-h               Prints this help message
```

Examples: 

* `meditracker> untake -l 1`
* `meditracker> untake -l 1 -m`

Output:
```
INFO: Medication quantity decreased: 38.0 -> 40.0
SUCCESS: Medicine has been untaken
```

## Delete a medication

To delete a medication, you can type `delete`, followed by the list index `-l listIndex`. You can obtain the list index 
by entering [`list -t all`](#all-medications) to refer to the list of all medications.

```
Usage:
	delete (-l listIndex) [-h]
Options:
	-l listIndex     Index of item in list
	-h               Prints this help message
```

Example: `meditracker> delete -l 2`

Output:
```
SUCCESS: Medicine has been deleted
```

## Search

To search for a medication in the library, you can type 'search', followed by the field you want to search for and the keyword 
`keyword`.
You have to include at least one of the following flags: `-n`, `-i`, `-s`, `-a` to specify the field you want to 
search for.

Usage:
```
    search ([-n name] [-i illness] [-s sideEffects] [-a allFields] keyword) [-h]
```
Options:
```
    -n name                 Name of medication
    -i illness              Illness that the medication is used for
    -s sideEffects          Side effects of the medication
    -a allFields            Search all fields
    -h                      Prints this help message
```
Examples:
- `search -n Medication A`: Search for medication with the name `Medication A`
- `search -i Headache`: Search for medication that treats `Headache`
- `search -s Dizziness`: Search for medication with side effect `Dizziness`
- `search -a keyword`: Search for `keyword` in all fields in the library (name, illness, side effect)

Output:
```
Here are the search results:
1. Medication A; Treats: Headache; May cause: Dizziness
2. Medication B; Treats: Fever; Side Effects: Dizziness
```
## Help

If in any situation you are stuck while using MediTracker, please do not worry. The help command is specifically 
designed to guide users to use the MediTracker. All you need to do is to type in `help`.

Usage:
```
meditracker> help
```

Output:
```
____________________________________________________________
Here are the commands you can use with MediTracker:

exit: Exits MediTracker.
help: Lists all available commands and their description.
add: Adds a medication to the medication manager.
view: Get information of a specific medication.
modify: Modify medication information.
list: Summary of medications for the day.
delete: Delete medication information.
search: Access medicine database.
take: Record taking of medication.
untake: Record untaking of medication.
save: Saves the JSON file to the specified path.
load: Loads the JSON file from the specified path.

For more details about each command, simply type in the command name.
____________________________________________________________
```
## General Data Management




### Saving to a file: `save`
Saves the JSON file to the specified path.

Format: `save [-o saveFile]`

Examples:
- `save`: Save the file to the default location.
- `save -o data/testfolder/output.json`: Saves the file

The default save location is `data/MediTrackerData.json`.
As far as possible, refrain from using spaces in the file path, especially with `-h` i.e. `path/to -h/`. 
This will trigger the help message to be displayed rather than processing the saving.

Also, ensure that the file does not end with a space or a dot (.); the file must end with `.json`.

Ensure that the folder you are going to write to has the proper access rights. Don't write to system folders; it will likely fail.

### Reading from a file: `load`
Loads the JSON files from the specified path.

Format: `load (-in loadFile)`

Examples:
- `load -in data/to/inputFile.json`

The default load location is `data/MediTrackerData.json`. This is done automatically when the program starts.
You should make sure that this file is present in the specified path. Alternatively, you can use the `load` command
to load the file.

As far as possible, refrain from using spaces in the file path, especially with `-h` i.e. `path/to -h/`.
This will trigger the help message to be displayed rather than processing the saving.

A prompt will then ask for your confirmation to overwrite existing data as an additional layer of safeguard.

WARNING: Loading will overwrite existing running data, so be sure to save a copy first before deciding to overwrite.
See the part on [Editing the File](#editing-the-file) for warnings on modifying the saved file.

### Editing the file
The medication information are saved in a `.json` format while the daily medication information are saved
in the `.txt` format. Advanced users can modify the text files directly.

<div class="danger-box">
:bangbang: <strong>Danger: </strong>
If the changes to the data file makes either its format or some of the fields contained therein invalid,
MediTracker may:

1. Discard all the data and start with a fresh state

2. Tries to read some information, and fill in placeholder values for fields that fail to load.

3. Crash due to bad data.

</div>

> Therefore, only edit the file if you are confident you can update it correctly.

<div class="warning-box">
:warning: <strong>Warning: </strong>

If you edit valid data directly inside the `.json` file, you have to **delete the text file <TODAY_DATE>.txt in 
data/dailymed/**. Otherwise, the next time MediTracker runs, it will continue to read the old data saved in the 
<TODAY_DATE>.txt.

</div>

### Advanced feature: Setting an arbitrary time
This feature is intended for developers to test out the features. See the developer guide (To be updated) for more detailed explanation on its implementation.
To make use of this feature, simply add the following flag and argument **when running the program** (not during the program execution itself)

Example: `java -jar meditracker.jar -sim 2024-01-01T13:00:00Z`

The `-sim` flag informs the program that a simulated time is expected. The supplied date and time must be of the format specified above,
or else the parser will return an error. The supplied time is of the format `YYYY-MM-DDTHH:MM:SSZ`

In the above example, it will set the time for the program to be **1 Jan 2024, 1pm**.

## Exit: `exit`
A goodbye message is printed on the screen, and the program exits after.

Format: `exit`

Example: `meditracker> exit`

Output:
```
Thank you for using MediTracker. Hope to see you again!
```

## FAQ
**Q**: How do I transfer my data to another computer? 

**A**: By default, all meditracker-related data will be saved under the `data` folder. 
This folder resides at the same level as the `meditracker.jar` file.
To transfer the data, just copy/shift the `data` folder to the new computer, 
making sure it is at the same level as the `.jar` file.

## Glossary

## Command Summary

{Give a 'cheat sheet' of commands here}

* Add todo `todo n/TODO_NAME d/DEADLINE`
