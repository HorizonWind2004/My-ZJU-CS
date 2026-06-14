#include "diary.h"
#include <string>
#include <vector>
#include <iostream>
#include <algorithm>

int main(int argc, char* argv[]) {
    // Check if the number of command-line arguments is valid
    if (argc != 3 && argc != 1) {
        return puts("Error: Not valid argument!\nUsage: pdlist (<start_date> <end_date>)") * 0;
    }

    string start_date, end_date;
    // If two command-line arguments are provided, assign them to start_date and end_date
    if (argc == 3) {
        start_date.assign(argv[1]);
        end_date.assign(argv[2]);
        // Check if the provided dates are valid
        if (!isValidDateFormat(start_date) || !isValidDateFormat(end_date)) {
            return puts("Error: Not valid date!") * 0;
        }
    }

    // Create a DiaryManager object with the file name "diary.txt"
    DiaryManager diary("diary.txt");
    // Read existing diary entries from the file
    vector<DiaryEntry> entries = diary.readEntries();

    // Sort the entries vector by date in ascending order
    sort(entries.begin(), entries.end(), [](const DiaryEntry& a, const DiaryEntry& b) {
        return a.date < b.date;
    });

    // Flag to track if any entries are found within the specified date range
    int flag = 0;
    cout << "-----------------------------------\n";
    // Loop through each entry in the sorted entries vector
    for (const auto& entry : entries) {
        // Check if no date range is specified or if the entry falls within the specified date range
        if (argc == 1 || (start_date <= entry.date && end_date >= entry.date)) {
            // Set the flag to indicate that entries are found
            flag = 1;
            cout << "Date: " << entry.date << "\n";
            cout << entry.content << "\n";
            cout << "-----------------------------------\n";
        }
    }
    if (!flag) {
        puts("Entry not found.");
    }
    return 0;
}