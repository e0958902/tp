<!-- 
CREDIT: We try to follow the ideas and structure of the following sample user guide:
https://se-education.org/addressbook-level3/UserGuide.html
-->

# User Guide

## Introduction

## Quick Start

{Give steps to get started quickly}

1. Ensure that you have Java 11 or above installed.
1. Down the latest version of `Duke` from [here](http://link.to/duke).

## Features 

{Give detailed description of each feature}

### Adding a todo: `todo`
Adds a new item to the list of todo items.

Format: `todo n/TODO_NAME d/DEADLINE`

* The `DEADLINE` can be in a natural language format.
* The `TODO_NAME` cannot contain punctuation.  

Example of usage: 

`todo n/Write the rest of the User Guide d/next week`

`todo n/Refactor the User Guide to remove passive voice d/13/04/2020`

### List Medication: `list`
When listing the medication list, you can list the medication(s) that you need to take for the day. 
The list can be further separated into the three different times of the day.

### Summary of all medications
* Syntax: `list -t all`<br>
  This lists a compiled list of all the medications that you have inputted and also shows 
the remaining amount of each medication with its corresponding dosage
* Output:
```
You have 2 medications listed below.
Format: Name | Quantity | Expiry Date | Remarks
	1. Medication_A | 60.0 | 01/07/25 | cause_dizziness
	2. Medication_B | 1000.0 | 01/09/25 | cause_headache
Your list of medications has been successfully shown!
```

### View more information on a specific medication
* Syntax: `view -l 1`<br>
  This lists all relevant information regarding the specified medication.
* Output:
```
Name: Medication_A
Quantity: 60.0
Expiry Date: 01/07/25
Remarks: cause_dizziness
Morning Dosage: 500.0
Afternoon Dosage: 250.0
Evening Dosage: 0.0
Repeat: 1
Day Added: 91
```


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

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: {your answer here}

- TODO: Add warning about modifying the text files

## Command Summary

{Give a 'cheat sheet' of commands here}

* Add todo `todo n/TODO_NAME d/DEADLINE`
