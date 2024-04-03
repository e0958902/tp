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
    - [Get information of specific medication](#one-medication)`more -l`
  - [Update medication information](#update)
    - [Record taking of medication](#take-untake)`take/untake -l`
    - [Modify medication information](#modify)`modify -l`
  - [Delete medication information](#delete)`delete -l`
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

## Add

## List Medication
### All medications:
Displays the whole lists of medications that you have added.

Format: `list -t all`

{SS}

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

## Delete

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

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: {your answer here}

- TODO: Add warning about modifying the text files

## Glossary

## Command Summary

{Give a 'cheat sheet' of commands here}

* Add todo `todo n/TODO_NAME d/DEADLINE`
