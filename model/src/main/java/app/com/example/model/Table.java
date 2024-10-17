package com.example.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Table {
    private int width;
    private int height;
    private ArrayList<ArrayList<HashMap<String, String>>> data;

	// Constructor for creating new table
    public Table(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new ArrayList<>();
    }
	
	// Constructor for loading a table from file
    public Table(int width, int height, ArrayList<ArrayList<HashMap<String, String>>> data) {
        this.width = width;
        this.height = height;
        this.data = data;
    }

    public int getWidth() {
        return width;
    }
	
	public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
	
	public void setHeight(int height) {
        this.height = height;
    }

    public ArrayList<ArrayList<HashMap<String, String>>> getData() {
        return data;
    }

    public void setData(ArrayList<ArrayList<HashMap<String, String>>> data) {
        this.data = data;
    }
}
