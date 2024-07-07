#include "diary.h"
#include <string>
#include <vector>
#include <iostream>
#include <algorithm>

int main(int argc, char* argv[]) {
    // Check if the number of command-line arguments is valid
    if (argc != 2) {
        return puts("Error: Not valid argument!\nUsage: pdshow <date>") * 0;
    }

    // Get the date from the command-line argument
    string date(argv[1]);

    // Check if the date is in a valid format
    if (!isValidDateFormat(date)) {
        return puts("Error: Not valid date!") * 0;
    }

    // Create a DiaryManager object with the file name "diary.txt"
    DiaryManager diary("diary.txt");
    // Read existing diary entries from the file
    vector<DiaryEntry> entries = diary.readEntries();

    // Loop through each entry in the entries vector
    for (const auto& entry : entries) {
        // Check if the date of the entry matches the specified date
        if (entry.date == date) {
            // Print the entry's date, content, and separator line
            cout << "-----------------------------------\n";
            cout << "Date: " << entry.date << "\n";
            cout << entry.content << "\n";
            cout << "-----------------------------------\n";
            return 0; 
        }
    }

    cout << "Entry not found for date: " << date << ".\n";
    return 0;
}
