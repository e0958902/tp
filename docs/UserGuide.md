<!-- 
CREDIT: We try to follow the ideas and structure of the following sample user guide:
https://se-education.org/addressbook-level3/UserGuide.html
-->

# User Guide

## Introduction

{Give a product intro}

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




### Saving to a file
Saves the JSON file at the specified path.

Format: `save [-o outputFile]`
- If the `-o outputFile` is not specified, it will write to its default save location.
- As far as possible, refrain from using spaces in the file path, especially with a -h i.e. path/to -h/
- Ensure that the file does not end with a space or a dot (.)
- System folders, don't save there. Ensure that the folder you are going to write to has the proper access rights

### Writing to a file
- A Prompt will ask for your confirmation as an additional layer of safeguard.
- Loading will overwrite existing running data, so be sure to save a copy first before deciding to overwrite
- (The rest are more or less the same as above)

## FAQ

**Q**: How do I transfer my data to another computer? 

**A**: {your answer here}

- TODO: Add warning about modifying the text files

## Command Summary

{Give a 'cheat sheet' of commands here}

* Add todo `todo n/TODO_NAME d/DEADLINE`
