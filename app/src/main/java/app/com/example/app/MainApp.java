package com.example.app;

import com.example.service.TableService;
import com.example.service.TableServiceImpl;
import com.example.model.Table;
import com.example.util.InputUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {

        TableService tableService = new TableServiceImpl();

        File tableFile = null;
        Table table = null;

        if (args.length == 0) {
            // No parameter provided
            System.out.println("No parameter provided. Using default table file from resources.");

            // Load the default file from the classpath (resources folder)
            try (InputStream defaultFileStream = MainApp.class.getClassLoader().getResourceAsStream("default_table.txt")) {
                if (defaultFileStream == null) {
                    System.out.println("Default file not found in resources. Exiting.");
                    System.exit(1);
                }

                Scanner scanner = new Scanner(defaultFileStream);
                table = tableService.loadTableFromStream(scanner);
                tableService.displayTable(table.getData());
				
				// Ask user for new file name to save the table
                String newFileName = InputUtil.askForString("Enter the name of the new file to save the table: ");
                tableFile = new File(newFileName + ".txt");
				
				// Save the table to the new file without asking for a file name
                tableService.saveTable(table, tableFile);

				/*
                // Ask user for new table dimensions
                int tblHeight = InputUtil.askForInt("Enter new table height: ");
                int tblWidth = InputUtil.askForInt("Enter new table width: ");
                System.out.println("You have entered the following dimensions: H = " + tblHeight + ", W = " + tblWidth);
                System.out.println("Generating new table...\n");

                // Generate new table with user dimensions
                table = new Table(tblWidth, tblHeight);
                tableService.generateTable(table);
                tableService.displayTable(table.getData());

                // Ask user for new file name to save the table
                String newFileName = InputUtil.askForString("Enter the name of the new file to save the table: ");
                tableFile = new File(newFileName + ".txt");

                // Save the new table to the specified file
                tableService.saveTable(table, tableFile);
                System.out.println("Table saved to " + tableFile.getName());
				*/

            } catch (IOException e) {
                System.out.println("Error loading default file: " + e.getMessage());
                System.exit(1);
            }

        } else {
            // File provided as argument
            tableFile = new File(args[0]);

            if (tableFile.exists() && tableFile.length() > 0) {
                System.out.println("Opening file " + tableFile.getName());
                try {
                    table = tableService.loadTableFromFile(tableFile);
                    tableService.displayTable(table.getData());
                } catch (IOException e) {
                    System.out.println("Error loading file; " + e.getMessage());
                }
            } else {
                try {
                    if (tableFile.createNewFile()) {
                        System.out.println(tableFile.getName() + " file created.");
                    } else {
                        System.out.println("Opening file " + tableFile.getName());
                    }
                } catch (IOException e) {
                    System.out.println("Error creating file: " + e.getMessage());
                }

                int tblHeight = InputUtil.askForInt("Enter table height: ");
                int tblWidth = InputUtil.askForInt("Enter table width: ");
                System.out.println("You have entered the following dimensions: H = " + tblHeight + ", W = " + tblWidth);
                System.out.println("Generating table...\n");

                table = new Table(tblWidth, tblHeight);
                tableService.generateTable(table);
                tableService.displayTable(table.getData());
                tableService.saveTable(table, tableFile);
            }
        }

        // Main menu loop
        boolean exit = false;
        while (!exit) {
            System.out.println("[1] - EDIT");
            System.out.println("[2] - PRINT");
            System.out.println("[3] - SEARCH");
            System.out.println("[4] - RESET");
            System.out.println("[5] - SORT");
            System.out.println("[6] - ADD ROW");
            System.out.println("[7] - EXIT");
            System.out.print("Please enter number: ");

            switch (InputUtil.askForInt("")) {
                case 1:
                    tableService.editCell(table, tableFile);
                    break;
                case 2:
                    tableService.displayTable(table.getData());
                    break;
                case 3:
                    tableService.searchTable(table);
                    break;
                case 4:
                    tableService.resetTable(table, tableFile);
                    break;
                case 5:
                    tableService.sortTable(table, tableFile);
                    break;
                case 6:
                    tableService.addRow(table, tableFile);
                    break;
                case 7:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }
}
