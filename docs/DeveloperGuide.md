---
layout: default
title: Developer Guide
---
# MediTracker Developer Guide

## Acknowledgements
Ideas and structure for our Developer Guide: 
- [AddressBook 3](https://se-education.org/addressbook-level3/DeveloperGuide.html)

Ideas and structure for the User Guide: 
- [AddressBook 3](https://se-education.org/addressbook-level3/UserGuide.html)
- [Previous Team's User Guide](https://ay2223s1-cs2103t-w16-2.github.io/tp/UserGuide.html) for more styling inspiration and adaptation

Additional Packages used: 
- [JSON](https://github.com/stleary/JSON-java)
- GitHub's built-in Jekyll integration to allow us to render the documentation in a slightly different style.

<div style="page-break-after: always;"></div>

# Overview
* [MediTracker Developer Guide](#meditracker-developer-guide)
  * [Acknowledgements](#acknowledgements)
* [Overview](#overview)
* [Setting up and getting started](#setting-up-and-getting-started)
  * [Setting up the project in your computer](#setting-up-the-project-in-your-computer)
* [Design & implementation](#design--implementation)
  * [Add Medication Command](#add-medication-command)
    * [Expanding Step 7](#expanding-step-7)
  * [Modify Medication Command](#modify-medication-command)
  * [List Medication Command](#list-medication-command)
  * [Utilising the Period and TimeRange](#utilising-the-period-and-timerange)
  * [View Medication Command](#view-medication-command)
  * [Search Medication Command](#search-medication-command)
  * [Utilising the argument parser](#utilising-the-argument-parser)
  * [Storage Design component](#storage-design-component)
  * [Exporting data](#exporting-data-)
  * [Importing data](#importing-data)
  * [Simulated Time](#simulated-time)
* [Product scope](#product-scope)
  * [Target user profile](#target-user-profile)
  * [Value proposition](#value-proposition)
  * [User Stories](#user-stories)
  * [Non-Functional Requirements](#non-functional-requirements)
* [Glossary](#glossary)
* [Instructions for manual testing](#instructions-for-manual-testing)
  * [Launching the Program](#launching-the-program)
  * [Adding some medication information](#adding-some-medication-information)
  * [Modifying some medication information](#modifying-some-medication-information)
  * [Taking the medication](#taking-the-medication)
  * [Saving data](#saving-data)

<div style="page-break-after: always;"></div>

# Setting up and getting started
## Setting up the project in your computer

<div class="warning-box">
:warning: <strong>Warning: </strong>

Follow the steps in the following guide precisely...

</div>

First, fork this [repo](https://github.com/AY2324S2-CS2113T-T09-1/tp), 
and clone the fork into your computer.

We highly recommend you to use Intellij IDEA.
1. **Configure the JDK**: Follow the 
[guide IDEA: Configuring the JDK](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk) 
to ensure Intellij is configured to use JDK 11.
2. **Import the project as a Gradle project**: Follow the 
[guide IDEA: Importing a Gradle project](https://se-education.org/guides/tutorials/intellijImportGradleProject.html) 
to import the project into IDEA.
3. **Verify the setup**: 
   1. meditracker.MediTracker and try a few commands.
   You can refer to our [UserGuide](UserGuide.md).
   2. Run the tests to ensure they all pass using Gradle.
      Open a console and run the command gradlew clean test (Mac/Linux: ./gradlew clean test)

<div style="page-break-after: always;"></div>

# Design & implementation
<!-- Describe the design and implementation of the product. Use UML diagrams and short code snippets where applicable. -->

<!-- Comment: Perhaps add sequence diagram or class diagram here to show the interactions and relationships since this seems to involve quite a lot of classes? -->
## Add Medication Command
The add medication command extends from the Command parent class and implements the following operations:
- `execute()` - Adds the medication object into the respective medication managers.
- `createMedication()` - Creates and populates a Medication object with values derived from parsed command-line arguments and the current date.
- `assertionTest()` - Asserts that medicine has been added to both medication managers.

Given below is an example usage scenario, along with the sequence diagram and how the add command behaves at each step:

![AddCommand.png](images/AddCommand.png)
- Step 1. User initiates the process.
- Step 2. `AddCommand` constructs an instance using the constructor `AddCommand(String arguments)` which includes 
parsing the arguments.
- Step 3. `ArgumentList` parses the provided arguments.
- Step 4. `AddCommand` calls `execute()` to add the medication.
- Step 5. Inside `execute()`, AddCommand calls `createMedication()` to construct a `Medication` object.
- Step 6. Adds the newly created `Medication` object to `MedicationManager`.
- Step 7. Checks and updates daily medication records in `DailyMedicationManager`.
- Step 8. `Ui` displays success or error messages to the user.

<div style="page-break-after: always;"></div>

### Expanding Step 7
![sublist](images/AddToSubLists.png)
- Every medication is checked if they contain dosage for each `period` (morning, afternoon, evening) of the day.
- If `getDosage()` in each period is not null, then a new `dailyMedication` object is created with the `name`, `dosage` 
and `period` values.
- This `dailyMedication` is then added to the respective sub lists according to the `period`.
- Finally, the `dailyMedication` is saved into the text file and returns `true` if saved successfully.

---

<div style="page-break-after: always;"></div>

## Modify Medication Command
The modify medication command extends from Command parent class with the following methods:
- execute() - Executes the modify command and modifies an existing Medication object using the provided information
  in the medication list
- updateMedication(medication:Medication) - Updates medication info based on the arguments passed in by the user
- rollbackChanges(medication:Medication, medicationCopy:Medication) - Rollbacks the changes made to Medication and
  DailyMedication. The `medicationCopy` object will be read from and the `medication` will be written to for the
  rollback operation.

The `ModifyCommand` allows modifying information, such as the quantity, dosage, expiration date etc., that were
previously added with the `AddCommand`. Given below is the usage scenario of `ModifyCommand`:
![ModifyCommand.png](images/ModifyCommand.png)
- Step 1. User initiates `ModifyCommand` using `MediTracker`
- Step 2. `ModifyCommand` constructs an instance using the constructor `ModifyCommand(arguments: String)`.
- Step 3. Then it uses a `Map<ArgumentName, String>` to store the arguments parsed in
  `ArgumentList.parse(arguments: String)`.
- Step 4. A subsequent call to `execute()` will fetch the list index specified by the user using
  `Command.getListIndex(parsedArguments: Map<ArgumentName, String>)`
- Step 5. The `Medication` associated at that index of all the medications will be fetched with
  `MedicationManager.getMedication(listIndex: int)`
- Step 6. `AddCommand` will perform a deep copy of the Medication object before calling
  `updateMedication(medication: Medication)`
- Step 7. If successful, it will show the success message on the command-line interface. Else, it will
  show the error message and begin rollback process by calling
  `rollbackChanges(medication:Medication, medicationCopy:Medication)`

Sequence Diagram for `updateMedication(medication:Medication)`
![ModifyCommand_updateMedication.png](images/ModifyCommand_updateMedication.png)
- Step 1. Iterate through all the arguments parsed from the user.
- Step 2. If the `ArgumentName` is `ArgumentName.NAME`, the `DailyMedication` name will be updated.
- Step 3. Set the appropriate `Medication` value depending on the `ArgumentName` type.
- Step 4. Check the validity of the `Medication` object.
- Step 5. Check if dosage or repeat frequency has been modified. If it has been modified, show the
  warning message to the user to notify them of the changes being reflected after today.

Sequence Diagram for `rollbackChanges(medication:Medication, medicationCopy:Medication)`
![ModifyCommand_rollbackChanges.png](images/ModifyCommand_rollbackChanges.png)
- Step 1. Checks if `processedArguments` contain `ArgumentName.NAME`. If it does, revert the
  `DailyMedication` name as well.
- Step 2. Revert the `medication` object with the `medicationCopy`.

<div style="page-break-after: always;"></div>

## List Medication Command
![sublist](images/ListCommand.png)

The list medication command extends from Command parent class and contains the following methods:
- execute() - Executes the list command and performs its specific task, -t. 
- The task can be either `list -t all` to list all medications or `list -t today` to list medications for the day,
which is divided into three categories -> Morning, Afternoon and Evening.

**The 'list -t' command requires the following:**
- 'all' - to run printAllMedications() from the MedicationManager.

**The following commands print medications to be taken in a day:**
<!--Comment: Perhaps clarify what is the difference between inputting "today -m", "today -a" and "today -e"? -->
1. `today` - to run printMedications() from the DailyMedicationManager.
2. `today -m` to run printTodayMedications(List<Medication> medications, List<DailyMedication> subList, String period)
   from the DailyMedicationManager
3. `today -a` to run printTodayMedications(List<Medication> medications, List<DailyMedication> subList, String period)
   from the DailyMedicationManager
4. `today -e` to run printTodayMedications(List<Medication> medications, List<DailyMedication> subList, String period)
   from the DailyMedicationManager

* On first run, the programs reads into the MedicationManager and determines if a medication is to be added to today's 
list, based on the repeat value.
* The repeat value ranges from `1 to 7` (number of days in a week)
* This verifies if the user is taking that medication every day / every 2 days / every 3 days etc.
* Then, based on the dosage flags (from `add` command),
  * -dA [quantity] - will add the medication into the morningMedication list
  * -dM [quantity] - will add the medication into the afternoonMedication list
  * -dE [quantity] - will add the medication into the eveningMedication list
* Additional checks in `ListCommand.execute()` prevent user from entering unnecessary flags or words **after** 
`list -t all` and `list -t today (-m/-a/-e)`.

<div style="page-break-after: always;"></div>

## Utilising the Period and TimeRange
* A day is divided into three `Periods`: Morning, afternoon and evening
* `TimeRange` determines the time when a medication is to be taken
  * Morning: from `Midnight` to `Noon`
  * Afternoon: from `Noon` to `6pm`
  * Evening: from `6pm` to `Midnight`
* `timeRange.isWithinTimeRange` detects if the current time of the day falls into one of the 3 periods, 
then automatically checks off the relevant medication in the DailyMedication list, without user specifying 
`(-m/-a/-e)` **after** `take -l [index]`.

<div style="page-break-after: always;"></div>

## View Medication Command
The view medication command extends from Command parent class and contains the following:

With the help of a sequence diagram given below, it explains the usage scenario on how 
view command behaves at each step.

![ViewCommand.png](images/ViewCommand.png)
- Step 1. User initiates a viewCommand via MediTracker.
- Step 2. `ViewCommand` constructs an instance using the constructor `ViewCommand(String arguments)`.
- Step 3. Then it uses an `ArgumentList` to store the arguments.
- Step 4. The ArgumentList is parsed in `ArgumentParser`.
- Step 5. `If` the parsed arguments is one, then `ViewCommand` calls `execute()` to show the specific medication
- Step 6. `execute()` calls `printSpecificMedication(medication) in `MedicationManager`.
- Step 7. `MedicationManager` then gets the medication via `getMedication(medication)` 
and displays the medication via `printSpecificMed(medicationDetails)` in `Ui`.
- Step 8. `Ui` displays a success message to the user.
- Step 9. `Else`, if the parsed arguments is more than one, then `Ui` displays an error message to the user.

  
<div style="page-break-after: always;"></div>

The search medication command allows users to search for medications from the local medication library.
The search medication command extends from Command parent class and contains the following methods:
- execute() - Executes the search function. It searches the local medication library for medication names, what
  illnesses they treat and their side effects, and prints the results to the user.

Here is a sequence diagram that explains the usage for the search command:
![SearchCommand.png](images/SearchCommand.png)


<div style="page-break-after: always;"></div>

<!--Comment: Consider using a class diagram to illustrate this to provide visual feedback.-->

## Utilising the argument parser

The `ArgumentParser` requires the following to work:
1. `ArgumentList` object
2. Raw user input in `String`

The `ArgumentList` object constitutes a combination of `Argument` objects. The constructor for 
the `ArgumentList` takes in a variable amount of `Argument`-extended classes. Additional assertion
tests have been added to ensure that certain attributes of the `Argument` classes do not clash with
one another. `HelpArgument` is automatically added when an `ArgumentList` object is instantiated.
Calling `ArgumentList.parse` with empty `String` or invoking with the argument `-h` will trigger the
help message to be printed on the console.
<!--Comment: Consider explaining how "trigger" works -->

An `Argument` object consists of the following:
1. `name`: Name of the argument, uses enum type `ArgumentName` as this value is used by `ArgumentParser` as well
2. `flag`: Takes the form of `-*` where `*` represents any number of alphabetic characters
3. `help`: Used in printing help message to provide user with the usage for the argument
4. `isOptional`: A `boolean` value to specify whether this argument is optional or not. This value is utilised
by the `ArgumentParser` to determine whether the argument is required, and will throw a `ArgumentNotFound` 
exception if this argument is required but not found in user specified argument string.
5. `hasValue`: A `boolean` value to specify whether this argument has a corresponding value tied to it. 
`ArgumentParser` requires this to know whether to take the value specified by the user.

Additional information regarding the `ArgumentName` enum:
- To create a new enum, follow the following convention: `ALL_CAPS_NAME(“justFollowAttributeNamingConvention”)`
- This enum is used in `ArgumentParser` when it returns a `Map<ArgumentName, String>`, where the enum is used as
the key. In order to query the returned data structure, you can utilise the same enum.

In order to utilise the argument parser,
1. Determine if the `Argument` variant already exist. If not, create a new class and extend the `Argument` class.
2. Prior to completing the creation of your new class, create a new enum in `ArgumentName`.
3. In the class/method that you intend to use the `ArgumentParser`, create a `ArgumentList` and specify the required
`Argument` objects.
4. Finally, invoking `ArgumentList.parse` with the `String` object to obtain the parsed argument values.

Overview of the `meditracker.argument` core classes:
![ArgumentPackageClassDiagram.png](images/ArgumentPackageClassDiagram.png)

---

<div style="page-break-after: always;"></div>

## Storage Design component
The `storage` package primarily consists of utility classes that serves to write to and read from the files and managers.

This package has 2 core utility classes: `FilePathChecker` and `FileReaderWriter`.

The `FilePathChecker` class handles all the input checking from the `load` and `save` commands to make sure that the path supplied is supported by the Operating System.

The `FileReaderWriter` class serves as a facade class, the "interface" that handles all the exporting and importing related functionality.
All other functions and classes will have to call `FileReaderWriter` in order to read or save their data.

The `FileReaderWriter` then calls `JsonExporter`, `JsonImporter` and `DailyMedicationExporter` (all of them are package-private classes) depending on the kind of data and the operation (to save or load).

<div style="page-break-after: always;"></div>

## Exporting data 
![jsonFileExport.png](images/JsonFileExport.png)

How the exporting works:
1. There are many ways in which the data gets saved (or exported). One of the way is after the user entered the `add` or `modify` command.
2. The other way is when the user uses the `save` command with the path to save the data to.
3. Once the path validation checks by `FilePathChecker` are completed and determined to be valid, the `save` command will call the `FileReaderWriter` to:
   1. First write `Medication` related information to the JSON file. 
      1. The temporary file, which will be in the same directory as the JSON file, will be created and written to. In case of a write issue, the actual save file will not be affected.
      2. `JsonExporter` is then called to save the `Medication` data, converting it into a JSON-compatible format and then saved
   2. Next, it will write `DailyMedication` related information to the text files.
      1. The temporary file will be created in the same directory as the txt file.
      2. `DailyMedicationExporter` is then called to write to the txt file.

<div style="page-break-after: always;"></div>

## Importing data
![FileImport.png](images/FileImport.png)

How the exporting works:
1. When the program first starts up, the program will first attempt to load the `Medication` and `DailyMedication` information from the default save location.
2. Another way is for the user to use the `load` command with the path to the JSON file to load from. 
3. Once the path validation checks by `FilePathChecker` are completed and determined to be valid, the `load` command will call the `FileReaderWriter` to load all MediTracker related data.
4. The loading of the JSON file for `Medication` data is independent of that of `DailyMedication` data. The failure of loading of one will not affect the other.
   1. For the importing of the JSON file, the `Medication` data is loaded and then `MedicationManager` is then called to populate the `Medication` information
   2. The text file will be read, the daily medication data is loaded, and then `DailyMedicationManager` is called to populate the daily medication information.

## Simulated Time
This is implemented under `MediTrackerTime`. When the user types in `java -jar meditracker.jar -sim 2024-01-01T00:00:00Z` the program will first run the
`setUpSimulatedTime` function to check if the `-sim` flag is present and whether the following parameter is of the `YYYY-MM-DDTHH:MM:SSZ` format.
If it is of the right format, it will further proceed to set up and refer to the simulated time using `setMediTrackerTime`.
Otherwise, it will just refer to the system time as per normal. However, since we are using the `Clock.fixed` function, the time will be fixed at whatever was set in the commandline
and "time" will not progress.

<div style="page-break-after: always;"></div>

# Product scope
## Target user profile
People who are taking medications on a daily basis.

## Value proposition
MediTracker ensures that you would not forget your overall schedule on what time and day to take your medication.
Ensuring that you would not forget your next dose of medication.

## User Stories

| Version | As a ... | I want to ...                                                                | So that I can ...                                                                                                                                      |
|---------|----------|------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------|
| v1.0    | user     | enter my medications with its relevant information in one go                 | add my medication entry efficiently and refer to them when I forget my medication information                                                          |
| v1.0    | user     | indicate the medications that I have taken                                   | know when is my next dose and how well I am following doctors orders, and show how well I am taking my medications at the appropriate times of the day |
| v1.0    | user     | know what medications i have to take throughout the day                      | mark them as taken or not, thus not miss or forget to take what I have to take for the day                                                             |
| v1.0    | user     | edit the details in my medication list                                       | ensure that its medication information is up-to-date with my modifications                                                                             |
| v1.0    | user     | delete medications medication list                                           | remove medications that I do not need will not be there anymore                                                                                        |
| v1.0    | user     | search medications from existing medicine library                            | search medications locally to have a quick preview of them and their purpose without the web                                                           |
| v1.0    | user     | know the list of medications I have added                                    | have a quick overview of the medication list and check the quantity and expiry date of each medication                                                 |
| v1.0    | user     | Save data locally                                                            | Use the application and see the data even when offline between sessions (data is persistent)                                                           |
| v1.0    | user     | Be able to load existing data                                                | I don't have to start from a fresh state everytime                                                                                                     |
| v2.0    | user     | see how much to be taken at each specific time of the day                    | have a quick overview of the list of medications to take at the specific period of the day                                                             |
| v2.0    | user     | retrieve all the values related to a medication                              | have a quickly verify all the details I have input for that particular medication                                                                      |
| v2.0    | user     | search medications by their side effects from existing medicine library      | search medications by their side effects locally to have a quick preview of them and their purpose without the web                                     |
| v2.0    | user     | search medications by their illness treatment from existing medicine library | search medications by their illness treatment locally to have a quick preview of them and their purpose without the web                                |
| v2.0    | user     | mark a medication as taken without specifying the period                     | reduce the hassle of telling the program explicitly that I have taken a medication for that period of the day                                          |
| v2.0    | user     | Be able to save the file to a place of my choosing                           | I can easily refer or transfer the files to another system                                                                                             |
| v2.0    | user     | Be able to load existing data from a place I choose                          | load the data by typing rather than dragging to the default save location                                                                              |
| v2.1    | user     | know which medications are low in quantity                                   | filter from my list of medications and see which ones need to be restock soon                                                                          |
| v2.1    | user     | know which medications are going to be expired                               | filter from my list of medications and see which ones are going to be expired                                                                          |
| v2.1    | user     | filter my medications based on a keyword                                     | have a quick overview of the medication list and check the quantity and expiry date of each medication                                                 |

<div style="page-break-after: always;"></div>

## Non-Functional Requirements
- The user's program data (medication, daily medication) should be persistent between program sessions.
- Users should be able to modify the data separately from the program. 
- Users should be able to share or move the data easily between devices.
- Developers be able to test out certain time-based functionality (i.e. check if medicine has been taken on a certain date) without having to tweak the actual system clock.

# Glossary

| Term                  | Explanation                                                                                                                     |
|-----------------------|---------------------------------------------------------------------------------------------------------------------------------|
| JSON                  | JSON stands for JavaScript Object Notation, which is used to save **all** medications added by the user.                        |
| text file             | Medications to be taken for a particular day is saved in the text file, and a new text file is created at the start of the day. |
| Flags                 | Most commands in MediTracker uses the flags with the "-" to recognise values within certain fields. Eg. `-n`, `-a`, `-l`, etc.  |
| Medication Data       | Data related to the overview of the medication itself                                                                           |
| Daily Medication Data | Data related to the medication's current day dosage, including its status                                                       |

<div style="page-break-after: always;"></div>

# Instructions for manual testing
<div class="info-box">
:information_source: <strong>Info: </strong>
This section is only intended as a starting point for testers to get started on understanding <b>SOME</b> of the core functionalities. 
The tester is expected to do more exploratory testing based on the <a href="UserGuide">User Guide</a>.
</div>

## Launching the Program
Follow the [Quick Start](UserGuide#quick-start) Section of the User Guide

<div class="note-box">
:notebook: <strong>Note: </strong>
If you want to simulate a time (Say 1 May 2024 6pm) instead of the default system time,
Instead of running <code>java -jar meditracker.jar</code>, you can use <code>java -jar meditracker.jar -sim 2024-05-01T18:00:00Z</code> instead.
Please remember that the time will be fixed at that time
</div>

## Adding some medication information
Start populating some medication data with the following commands:
- `add -n Test Medication -q 100 -e 2026-02-02 -dM 1 -dA 2 -dE 3 -rep 4 -r nil`
- `add -n Test Medication Two -q 300 -e 2026-02-02 -dM 10 -dA 20 -dE 30.5 -rep 2 -r nil`

<div class="note-box">
:notebook: <strong>Note: </strong>
Our medication parser restricts the naming of medication to only alphabetic letters and space.
</div>

Both should succeed without issues. You can run `list -t all` to check the current medication added.

At the same time, the Daily Medication information is also updated with the relevant dosage for the day. You can view those with the `list -t today` command.

## Modifying some medication information
Modify the second medication's name using the following: 
- `modify -l 2 -n New Test Medication Name`

Run `list -t all` to see that the medication name has been changed.

## Taking the medication
Use `take -l 1` to take the medication. By default, the program chooses the time based on either the simulated time or the system time.

You can run `list -t today` to see that the medication has been taken.

<div style="page-break-after: always;"></div>

## Saving data
By default, data is automatically saved to the default location (under the `data` folder). you can just `exit` the program.

The information will be saved as readable text files `.json` and `.txt` files. You can simulate a corrupted file by modifying, removing any of the fields or special syntax characters or introduce additional parameters.
