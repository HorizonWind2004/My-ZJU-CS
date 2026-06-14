#include "diary.h"
#include <string>
#include <vector>
#include <iostream>
#include <algorithm>

int main(int argc, char* argv[]) {
    if (argc != 2) {
        return puts("Error: Not vaild argument!\nUsage: pdremove <date>") * 0;
    }

    string date = argv[1];
    DiaryManager diary("diary.txt");
    vector<DiaryEntry> entries = diary.readEntries();

    bool found = false;
    for (auto it = entries.begin(); it != entries.end(); ++it) {
        if (it->date == date) {
            entries.erase(it);
            diary.writeEntries(entries);
            cout << "Entry removed successfully.\n";
            return 0;
        }
    }

    cout << "Entry not found for date: " << date << ".\n";
    return 0;
}
