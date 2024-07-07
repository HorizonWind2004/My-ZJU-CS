#include "diary.h"

// Constructor for DiaryManager class
DiaryManager::DiaryManager(const string& file_name) : file_name(file_name) {}

// Function to read diary entries from file
vector<DiaryEntry> DiaryManager::readEntries() {
    vector<DiaryEntry> entries;
    ifstream file(file_name);
    if (!file.is_open()) {
        cout << "Error: Unable to open file for reading.\n";
        return entries;
    }

    string line;
    DiaryEntry entry;
    // Loop through each line in the file
    while (getline(file, line)) {
        if (line.empty()) continue; // Skip empty lines
        if (line == ".") continue;  // Skip delimiter lines
        if (line == "##################################") break; // Stop reading at delimiter line
        if (line.find("Date:") == 0) {
            // If the line contains "Date:", it indicates a new entry
            // Store the previous entry if it exists
            if (!entry.date.empty() && !entry.content.empty()) {
                entries.push_back(entry);
                entry = {};
            }
            // Extract the date from the line
            entry.date = line.substr(6);
        } else {
            // If the line does not contain "Date:", it is part of the entry content
            entry.content += line + "\n";
        }
    }

    // Store the last entry if it exists
    if (!entry.date.empty() && !entry.content.empty()) {
        entries.push_back(entry);
    }

    file.close();
    return entries;
}

// Function to write diary entries to file
void DiaryManager::writeEntries(const vector<DiaryEntry>& entries) {
    ofstream file(file_name);
    if (!file.is_open()) {
        cout << "Error: Unable to open file for writing.\n";
        return;
    }

    // Loop through each entry and write it to the file
    for (const auto& entry : entries) {
        file << "Date: " << entry.date << "\n";
        file << entry.content << "\n";
        file << ".\n"; // Use '.' as the delimiter for entries
    }
    // Write delimiter line to indicate end of entries
    file << "##################################";
    file.close();
}

// Function to check if a date string is in valid YYYY-MM-DD format
bool isValidDateFormat(const string& date) {
    stringstream ss(date);
    char dash1, dash2;
    int year, month, day;
    // Parse the date string into year, month, and day
    if (!(ss >> year >> dash1 >> month >> dash2 >> day) || dash1 != '-' || dash2 != '-') {
        return false;
    }

    // Check if year, month, and day are within valid ranges
    if (year < 0 || month < 1 || month > 12 || day < 1 || day > 31) {
        return false;
    }

    // Check if day is valid for the given month
    if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) {
        return false;
    }

    // Check for February and leap year
    if (month == 2) {
        bool isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
        if ((isLeapYear && day > 29) || (!isLeapYear && day > 28)) {
            return false;
        }
    }

    return true;
}
