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
  - [Add Medication:](#add)`add`
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
    - [Read from file: load](#read-from-file)
    - [Write to file: write](#write-to-file)
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

## Add

## List Medication: `list`
When listing the medication list, you can list the medication(s) that you need to take for the day.
The list can be further separated into the three different times of the day.

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
  * `-m`m: Morning (Midnight to 12 Noon)
  * `-a`: Afternoon (12pm to 6pm)
  * `-e`: Evening (6pm to Midnight)
  * Eg: `list -t today -m` - will display the list of medications to be taken in the morning

{SS}

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

## Help

## General Data Management



### Adding a todo: `todo`
Adds a new item to the list of todo items.

Format: `todo n/TODO_NAME d/DEADLINE`

* The `DEADLINE` can be in a natural language format.
* The `TODO_NAME` cannot contain punctuation.  

Example of usage: 

`todo n/Write the rest of the User Guide d/next week`

`todo n/Refactor the User Guide to remove passive voice d/13/04/2020`



### Saving to a file
Saves the JSON file at the specified path.

Format: `save [-o outputFile]`
- If the `-o outputFile` is not specified, it will write to its default save location.
- As far as possible, refrain from using spaces in the file path, especially with a -h i.e. path/to -h/
- Ensure that the file does not end with a space or a dot (.)
- System folders, don't save there. Ensure that the folder you are going to write to has the proper access rights

### Reading from a file
- A Prompt will ask for your confirmation as an additional layer of safeguard.
- Loading will overwrite existing running data, so be sure to save a copy first before deciding to overwrite
- (The rest are more or less the same as above)
- Rmb the JSON if you modify and you mess it up, either it will not load, or the program loads with corrupt data.
- So make sure you know what you are doing and keep a backup before you make any sort of modifications!

### Advanced Feature: Setting an arbitrary time
- Still show how to use it (simply)
- This is intended for the developers only.
- See Developer Guide for more information.

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: {your answer here}

- TODO: Add warning about modifying the text files

## Glossary

## Command Summary

{Give a 'cheat sheet' of commands here}

* Add todo `todo n/TODO_NAME d/DEADLINE`
