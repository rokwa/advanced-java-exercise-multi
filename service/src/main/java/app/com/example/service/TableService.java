package com.example.service;

import com.example.model.Table;
import com.example.util.InputUtil;
import com.example.util.RandomUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public interface TableService {
    void generateTable(Table table);
    void displayTable(ArrayList<ArrayList<HashMap<String, String>>> table);
    void saveTable(Table table, File file);
    Table loadTableFromFile(File file) throws IOException;
	Table loadTableFromStream(Scanner scanner) throws IOException;
    void editCell(Table table, File file);
    void searchTable(Table table);
    void resetTable(Table table, File file);
    void sortTable(Table table, File file);
    void addRow(Table table, File file);
}
