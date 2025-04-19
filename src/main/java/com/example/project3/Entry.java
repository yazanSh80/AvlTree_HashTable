package com.example.project3;

import java.time.LocalDate;

public class Entry {
    Character  flag;
    AVLTree value;
    LocalDate date;


    public Entry(LocalDate date ,AVLTree value, Character  flag) {
        this.flag = flag;
        this.value = value;
        this.date = date;
    }

    public Character getFlag() {
        return flag;
    }

    public void setFlag(Character flag) {
        this.flag = flag;
    }

    public AVLTree getValue() {
        return value;
    }

    public void setValue(AVLTree value) {
        this.value = value;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}