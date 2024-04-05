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
  - [List Medication](#list-medication)
    - [Summary of all medications:](#all-medications)`list -t all`
    - [Summary of medications for the day](#daily-medications)`list -t today`
    - [Get information of specific medication](#Get-information-of-specific-medication)`view -l`
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
- [FAQ](#faq)
- [Glossary](#glossary)
- [Command Summary](#command-summary)

## How to Use the User Guide

## Quick Start
1. Ensure that you have Java 11 or above installed. 
   - If you using Windows:
     - Press the `Windows Key` on your keyboard and enter `Command Prompt` or `cmd` and press enter.
     - When the application opens, enter `java -version`.
   - If you are using MacOS/Linux:
     - Open the `Terminal`, then enter `java -version`.
   - If Java 11 is installed, you should get an output similar to below:
      - {SS}.
   - If you see otherwise, please follow the instructions from [here](https://docs.oracle.com/en/java/javase/11/install/overview-jdk-installation.html#GUID-8677A77F-231A-40F7-98B9-1FD0B48C346A).
3. Once that is settled, head over to the [link](https://github.com/AY2324S2-CS2113T-T09-1/tp/releases/latest) to obtain the latest version of `MediTracker`.
4. Save the file in an empty folder in `Desktop`, then this will be your home folder for `MediTracker`.
5. Double-click on meditracker.jar to start the app.
   - Alternatively, you may also use the Command Prompt/Terminal. 
   - Navigate to the home folder where you have saved the application in, and enter `java -jar meditracker.jar` to 
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

> Tip: The dosage and remarks tags are optional.

Examples:

* `add -n Medication_A -q 5000 -e 01/07/25 -dM 500 -dA 250 -r cause_dizziness -rep 1`
* `add -n Medication_B -q 1000 -e 30/09/24 -dM 500 -dA 250 -dE 50 -rep 6`

Output:
```
SUCCESS: Medicine has been added
```
## List Medication: `list`
You can list down the medications that you have added, or even print out a list of medications that you will be taking 
for the day.

### All medications:
Displays the whole lists of medications that you have added.

Format: `list -t all`

Output:
```
You have 2 medications listed below.
Format: Name | Quantity | Expiry Date | Remarks
	1. Medication_A | 60.0 | 01/07/25 | cause_dizziness
	2. Medication_B | 1000.0 | 01/09/25 | cause_headache
Your list of medications has been successfully shown!
```


### Get information of specific medication:
Format: `view -l 1`<br>
  This lists all relevant information regarding the specified medication.
Output:
```
Name: Medication_A
Quantity: 60.0
Expiry Date: 01/07/25
Remarks: cause_dizziness
Morning Dosage: 500.0
Afternoon Dosage: 250.0
Evening Dosage: 0.0
Repeat: 1
```

### Daily medications:
Displays an overview of the list of medications that you will be taking for the day.

Format: `list -t today`
* If you want to see for only a particular period of the day, simply enter `list -t today` followed by:
  * `-m`: Morning (Midnight to 12 Noon)
  * `-a`: Afternoon (12pm to 6pm)
  * `-e`: Evening (6pm to Midnight)
  * Eg: `list -t today -m` - will display the list of medications to be taken in the morning only

Output:
```
____________________________________________________________
meditracker> list -t today
Here are the Daily Medications you have to take today: 
Morning:
	1. [ ] Medication_A | 2.0
Afternoon:
	1. [ ] Medication_A | 1.0
____________________________________________________________
```

## Update

### Modify Medication Information

If you require modification to the medication information, you can type `modify`, followed by the list index 
`-l listIndex` printed in the summary list of medications to modify it. For each of the fields that you would
like to modify, you can specify the corresponding flag and the new value to make changes.

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

Output:
```
SUCCESS: Medicine has been modified
```

### Record taking of medication

#### Take a medication

When you have taken your medication, you can type `take`, followed by the list index `-l listIndex` 
printed in the summary list of daily medications to mark it as taken. This command is time aware and 
will mark the index of the medication based on what time of day it is. Optionally, you can specify 
`-m`, `-a`, `-e` to override the time aware feature.

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

Output:
```
SUCCESS: Medicine has been taken
```

#### Untake a medication

If you have accidentally entered the wrong command and wish to untake the medication, you can type `untake`, 
followed by the list index `-l listIndex` printed in the summary list of daily medications. This command is 
time aware and will mark the index of the medication based on what time of day it is. Optionally, you can 
specify `-m`, `-a`, `-e` to override the time aware feature.

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

Output:
```
SUCCESS: Medicine has been untaken
```

## Delete a medication

To delete a provided medication, you can type `delete`, followed by the list index `-l listIndex`
printed in the summary list of medications to remove it.

```
Usage:
	delete (-l listIndex) [-h]
Options:
	-l listIndex     Index of item in list
	-h               Prints this help message
```

Output:
```
SUCCESS: Medicine has been deleted
```

## Search

To search for a medication, you can type 'search', followed by the field you want to search for and the keyword 
`keyword`.
You have to include at least one of the following flags: `-n`, `-i`, `-s`, `-a` to specify the field you want to 
search for.

```
Usage:
    search ([-n name] [-i illness] [-s sideEffects] [-a allFields] keyword) [-h]
Options:
    -n name                 Name of medication
    -i illness              Illness that medication is used for
    -s sideEffects          Side effects of medication
    -a allFields            Search all fields
    -h                      Prints this help message
```
Examples:
- `search -n Medication_A`: Search for medication with the name `Medication_A`
- `search -i Headache`: Search for medication that treats `Headache`
- `search -s Dizziness`: Search for medication with side effect `Dizziness`
- `search -a keyword`: Search for `keyword` in all fields (name, illness, side effect)

Output:
```
Here are the search results:
1. Medication_A; Treats: Headache; Side Effects: Dizziness
2. Medication_B; Treats: Fever; Side Effects: Dizziness
```
## Help

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

WARNING: If the changes to the data file makes either its format or some of the fields contained therein invalid,
meditracker may:
1. Discard all the data and start with a fresh state
2. Tries to read some information, and fill in placeholder values for fields that fail to load
3. Crash due to bad data.

Therefore, only edit the file if you are confident you can update it correctly.

### Advanced feature: Setting an arbitrary time
This feature is intended for developers to test out the features. See the developer guide (To be updated) for more detailed explanation on its implementation.
To make use of this feature, simply add the following flag and argument **when running the program** (not during the program execution itself)

Example: `java -jar meditracker.jar -sim 2024-01-01T13:00:00Z`

The `-sim` flag informs the program that a simulated time is expected. The supplied date and time must be of the format specified above,
or else the parser will return an error. The supplied time is of the format `YYYY-MM-DDTHH:MM:SSZ`

In the above example, it will set the time for the program to be **1 Jan 2024, 1pm**.

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
