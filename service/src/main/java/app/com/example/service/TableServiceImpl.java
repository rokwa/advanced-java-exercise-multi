package com.example.service;

import com.example.model.Table;
import com.example.util.InputUtil;
import com.example.util.RandomUtil;
import com.example.util.GenUtil;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Comparator;

public class TableServiceImpl implements TableService {
	Scanner sc = new Scanner(System.in);
	
	// Remove static
	// Change name to TableService.java
	// Turn to interface
	// Make TableServiceImpl
	
	// Generate table
	@Override
	public void generateTable(Table table) {
		int width = table.getWidth();
		int height = table.getHeight();
		ArrayList<ArrayList<HashMap<String, String>>> data = new ArrayList<>();
		
		for(int i = 0; i < height; i++){
			ArrayList<HashMap<String, String>> row = new ArrayList<>();
			for(int j = 0; j < width; j++){
				HashMap<String, String> cell = new HashMap<>();
				cell.put(RandomUtil.generateRandomStringWithLength(5), RandomUtil.generateRandomStringWithLength(5));
                row.add(cell);
			}
			data.add(row);
		}	
		table.setData(data);
	}
	
	// Display table
	@Override
    public void displayTable(ArrayList<ArrayList<HashMap<String, String>>> table) {
        //System.out.println("Displaying table: \n");
		System.out.println();
        for (ArrayList<HashMap<String, String>> row : table) {
            for (HashMap<String, String> cell : row) {
				for (HashMap.Entry<String, String> entry : cell.entrySet()) {
                    System.out.print("[" + entry.getKey() + ", " + entry.getValue() + "] ");
                }
			}
            System.out.print("\n\n");
        }
    }
	
	@Override
	// Save table to file
    public void saveTable(Table table, File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (ArrayList<HashMap<String, String>> row : table.getData()) {
                ArrayList<String> cellStrings = new ArrayList<>();
                for (HashMap<String, String> cell : row) {
                    for (HashMap.Entry<String, String> entry : cell.entrySet()) {
                        cellStrings.add(entry.getKey() + "," + entry.getValue());
                    }
                }
                writer.write(String.join(" ", cellStrings));
                writer.newLine();
            }
            System.out.println("Table saved to file " + file.getName() + "\n");
        } catch (IOException e) {
            System.out.println("Error saving the table to file: " + e.getMessage());
        }
    }
	
	// Load the table from a file
	@Override
    public Table loadTableFromFile(File file) throws IOException {
        ArrayList<ArrayList<HashMap<String, String>>> tableData = new ArrayList<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String[] rowEntries = scanner.nextLine().split(" ");
                ArrayList<HashMap<String, String>> row = new ArrayList<>();
                for (String cellString : rowEntries) {
                    String[] keyValue = cellString.split(",");
                    HashMap<String, String> cell = new HashMap<>();
                    cell.put(keyValue[0], keyValue[1]);
                    row.add(cell);
                }
                tableData.add(row);
            }
        }
        int width = tableData.isEmpty() ? 0 : tableData.get(0).size();
        int height = tableData.size();
        return new Table(width, height, tableData);
    }
	
	// Load table from stream
	@Override
	public Table loadTableFromStream(Scanner scanner) throws IOException {
		ArrayList<ArrayList<HashMap<String, String>>> tableData = new ArrayList<>();
		while (scanner.hasNextLine()) {
			String[] rowEntries = scanner.nextLine().split(" ");
			ArrayList<HashMap<String, String>> row = new ArrayList<>();
			for (String cellString : rowEntries) {
				String[] keyValue = cellString.split(",");
				HashMap<String, String> cell = new HashMap<>();
				cell.put(keyValue[0], keyValue[1]);
				row.add(cell);
			}
			tableData.add(row);
		}

		int width = tableData.isEmpty() ? 0 : tableData.get(0).size();
		int height = tableData.size();
		return new Table(width, height, tableData);
	}
	
	// Edit cell in table
	@Override
	public void editCell(Table table, File file) {
		String keyToEdit = InputUtil.askForString("Enter the key of the cell to be edited (5 characters): ");
		
		// Variables to track the target cell, row, and column
		HashMap<String, String> targetCell = null;
		int targetRow = -1, targetCol = -1;
		
		boolean keyFound = false;
		
		// Search for the key in the entire table
		for (int rowIndex = 0; rowIndex < table.getHeight(); rowIndex++) {
			ArrayList<HashMap<String, String>> row = table.getData().get(rowIndex);
			
			// Use row's actual size instead of assuming table.getWidth()
			for (int colIndex = 0; colIndex < row.size(); colIndex++) {
				HashMap<String, String> cell = row.get(colIndex);
				if (cell.containsKey(keyToEdit)) {
					targetCell = cell;
					targetRow = rowIndex;
					targetCol = colIndex;
					keyFound = true; // Mark that the key was found
					break; // Exit inner loop
				}
			}
			if (keyFound) {
				break; // Exit outer loop if the key was found
			}
		}

		// If the key was not found, notify the user
		if (targetCell == null) {
			System.out.println("Key not found in the table.");
			return; // Exit the method early
		}
		
		// Display the current key-value pair
		for (HashMap.Entry<String, String> entry : targetCell.entrySet()) {
			System.out.println("[" + entry.getKey() + ", " + entry.getValue() + "]");
		}
		
		String newKey, newValue;
		boolean keyExists;
		do {
			newKey = InputUtil.askForString("Enter new key (5 characters): ");
			newValue = InputUtil.askForString("Enter new value (5 characters): ");
			keyExists = false;

			// Check for duplicate key in the entire table
			for (ArrayList<HashMap<String, String>> row : table.getData()) {
				for (HashMap<String, String> cell : row) {
					if (cell.containsKey(newKey) && cell != targetCell) {
						keyExists = true;
						System.out.println("Key already exists in the table. Please enter a different key.");
						break;
					}
				}
				if (keyExists) {
					break;
				}
			}
		} while (newKey.length() != 5 || newValue.length() != 5 || keyExists);

		targetCell.clear();
		targetCell.put(newKey, newValue);

		System.out.println("Cell updated successfully\n");
		displayTable(table.getData());
		saveTable(table, file);
	}
	
	@Override
	public void searchTable(Table table) {
		String searchTerm = InputUtil.askForString("Enter search term: ");
		Pattern pattern = Pattern.compile("(?=" + searchTerm + ")");
		int totalCount = 0;

		// Iterate through each row
		for (int i = 0; i < table.getHeight(); i++) {
			ArrayList<HashMap<String, String>> row = table.getData().get(i);

			// Iterate through each column (use row.size() instead of table.getWidth())
			for (int j = 0; j < row.size(); j++) {
				HashMap<String, String> cell = row.get(j);
				int cellCount = 0;

				for (HashMap.Entry<String, String> entry : cell.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();

					// Count occurrences in the key
					int keyCount = GenUtil.countOccurrences(pattern, key);
					if (keyCount > 0) {
						System.out.println("Match found in cell [" + i + "][" + j + "] key: " + key + " (" + keyCount + " occurrences)");
						cellCount += keyCount;
					}

					// Count occurrences in the value
					int valueCount = GenUtil.countOccurrences(pattern, value);
					if (valueCount > 0) {
						System.out.println("Match found in cell [" + i + "][" + j + "] value: " + value + " (" + valueCount + " occurrences)");
						cellCount += valueCount;
					}
				}

				// Report total occurrences found in this cell
				if (cellCount > 0) {
					System.out.println("Total occurrences in cell [" + i + "][" + j + "]: " + cellCount);
				}
				totalCount += cellCount;
			}
		}

		System.out.println("Total occurrences found: " + totalCount + "\n");
	}
	
	// Reset table function
	@Override
    public void resetTable(Table table, File file) {
        int newHeight = InputUtil.askForInt("Enter new table height: ");
        int newWidth = InputUtil.askForInt("Enter new table width: ");

		// Update new table dimensions and clear current table data
        table.setWidth(newWidth);
        table.setHeight(newHeight);
        table.setData(new ArrayList<>());

        System.out.println("Generating new table with dimensions: H = " + newHeight + ", W = " + newWidth);
        generateTable(table);

        displayTable(table.getData());
		saveTable(table, file);
    }
	
	// Sort table function
	@Override
	public void sortTable(Table table, File file) {
		for (ArrayList<HashMap<String, String>> row : table.getData()) {
			// Sort each row by combined key-value pairs
			row.sort(new Comparator<HashMap<String, String>>() {
				public int compare(HashMap<String, String> cell1, HashMap<String, String> cell2) {
					// Get the key-value pair for both cells
					String key1 = cell1.keySet().iterator().next();
					String value1 = cell1.get(key1);
					String combined1 = key1 + value1;

					String key2 = cell2.keySet().iterator().next();
					String value2 = cell2.get(key2);
					String combined2 = key2 + value2;

					// Compare the combined strings
					return combined1.compareTo(combined2);
				}
			});
		}

		System.out.println("Table sorted by key-value combinations in each row.\n");
		
		displayTable(table.getData());
		saveTable(table, file);
	}
	
	/* Add row function
	@Override
	public void addRow(Table table, File file) {
		ArrayList<HashMap<String, String>> newRow = new ArrayList<>();
		int width = table.getWidth();

		// Generate cells for the new row
		for (int i = 0; i < width; i++) {
			HashMap<String, String> cell = new HashMap<>();
			cell.put(RandomUtil.generateRandomStringWithLength(5), RandomUtil.generateRandomStringWithLength(5));
			newRow.add(cell);
		}

		// Add the new row to the table data
		table.getData().add(newRow);
		table.setHeight(table.getHeight() + 1);  // Update the height of the table

		System.out.println("New row added to the table.\n");
		displayTable(table.getData());
		saveTable(table, file);
	}
	*/
	
	// Add row function (custom columns)
	@Override
	public void addRow(Table table, File file) {
		// Ask the user for the number of columns for the new row
		int numColumns = InputUtil.askForInt("Enter the number of columns for the new row: ");

		ArrayList<HashMap<String, String>> newRow = new ArrayList<>();

		// Generate cells for the new row based on user input
		for (int i = 0; i < numColumns; i++) {
			HashMap<String, String> cell = new HashMap<>();
			cell.put(RandomUtil.generateRandomStringWithLength(5), RandomUtil.generateRandomStringWithLength(5));
			newRow.add(cell);
		}

		// Add the new row to the table data
		table.getData().add(newRow);

		// Update the table's height
		table.setHeight(table.getHeight() + 1);

		// Optional: Adjust the width of the table if needed
		if (numColumns > table.getWidth()) {
			table.setWidth(numColumns); // Update table width to match the largest row
		}

		System.out.println("New row with " + numColumns + " columns added to the table.\n");
		displayTable(table.getData());
		saveTable(table, file);
	}

}
