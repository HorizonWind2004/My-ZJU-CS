#include "diary.h"
#include <string>
#include <vector>
#include <iostream>
#include <algorithm>

int main() {
    DiaryManager diary("diary.txt");
    
    // Read existing diary entries from the file
    vector<DiaryEntry> entries = diary.readEntries();

    DiaryEntry new_entry;
    while (1) {
        cout << "Enter date (YYYY-MM-DD): ";
        getline(cin, new_entry.date);
        // Validate the format of the entered date
        if (isValidDateFormat(new_entry.date)) {
            break;
        } else {
            cout << "Please enter a VALID date!\n";
        }
    }
    
    cout << "Enter content (type '.' to finish):\n";
    string line;
    while (getline(cin, line) && line != ".") {
        new_entry.content += line + "\n"; // Append each line to the content
    }

    // Check if the entry with the same date already exists in the entries vector
    bool found = false;
    for (auto& entry : entries) {
        if (entry.date == new_entry.date) {
            entry = new_entry; // Update the existing entry
            found = true;
            break;
        }
    }

    // If the entry does not exist, add the new entry to the entries vector
    if (!found) {
        entries.push_back(new_entry);
    }

    // Write the updated entries vector to the file
    diary.writeEntries(entries);
    cout << "Entry added successfully.\n";

    return 0;
}
