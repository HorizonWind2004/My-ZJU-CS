#ifndef DIARY_H
#define DIARY_H

#include <string>
#include <vector>
#include <iostream>
#include <algorithm>
#include <fstream>
#include <sstream>

using namespace std;

// Structure representing a diary entry with date and content
struct DiaryEntry {
    string date;    // Date of the entry
    string content; // Content of the entry
};

// Class for managing diary entries
class DiaryManager {
private:
    string file_name; // Name of the file storing diary entries
public:
    // Constructor to initialize DiaryManager with file name
    DiaryManager(const string& file_name);

    // Function to read diary entries from file
    vector<DiaryEntry> readEntries();

    // Function to write diary entries to file
    void writeEntries(const vector<DiaryEntry>& entries);
};

// Function to check if a date string is in valid YYYY-MM-DD format
bool isValidDateFormat(const string& date);

#endif
