# Add a new diary entry
echo "Adding a new diary entry..."
./pdadd < sample_diary.txt
# List all diary entries
echo "Listing all diary entries..."
./pdlist
echo "Listing all diary entries between 2004-04-04 and 2024-04-19..."
./pdlist 2004-04-04 2024-04-19
# Show diary entry for a specific date
echo "Showing diary entry for 2024-04-18..."
./pdshow 2024-04-18
# Remove a diary entry for a specific date
echo "Removing diary entry for 2024-04-18..."
./pdremove 2024-04-18
# List all diary entries after removal
echo "Listing all diary entries after removal..."
./pdlist
