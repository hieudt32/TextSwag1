package com.example.baotu.textswag.model;


public class Font {
    private int id;
    private String name;
    private boolean selectMode;

    public Font() {
    }

    public Font(int id, String name, boolean selectMode) {
        this.id = id;
        this.name = name;
        this.selectMode = selectMode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelectMode() {
        return selectMode;
    }

    public void setSelectMode(boolean selectMode) {
        this.selectMode = selectMode;
    }
}
