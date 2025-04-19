package com.example.project3;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HashTable {
    private final int INITIAL_CAPACITY = 16;
    private final float LOAD_FACTOR = 0.25f;
    private Entry[] table;
    private int size;
    private int capacity;

    public HashTable() {
        this.capacity = INITIAL_CAPACITY;
        this.table = new Entry[capacity];
        this.size = 0;
    }
    public int getSize(){
        return size;
    }
    public int hashFunction(LocalDate date) {
        int uniqueInt =(date.getYear()%100) + (date.getMonthValue() * 10) +(date.getDayOfMonth()*100);
        return uniqueInt % capacity;
    }
    public void insertDate(LocalDate date, AVLTree avlTree) {
        if ((float) size / capacity >= LOAD_FACTOR) {
            resizeTable();
        }

        int index = hashFunction(date);
        int i = 1;

        while (table[index] != null && table[index].getFlag() == 'E' && table[index].getFlag() == 'D') {
            if (table[index].getDate().equals(date)) {
                table[index].setValue(avlTree); // Update existing entry
                return;
            }
            index = (hashFunction(date) + (i * i)) % capacity;
            i++;
        }
            table[index] = new Entry(date, avlTree, 'F');
            size++;

    }

    public void updateDate(LocalDate oldDate, LocalDate newDate) {
        if (!containsDate(oldDate) || containsDate(newDate)) {
            return;
        }
        AVLTree oldTree = getAVLTree(oldDate);
        deleteDate(oldDate);
        insertDate(newDate, oldTree);
    }


    public void deleteDate(LocalDate date) {
        int index = hashFunction(date);
        int i = 1;

        while (table[index] != null) {
            if (table[index].getDate().isEqual(date)) {
                table[index].setFlag('D');
                size--;
                return;
            }
            index = (hashFunction(date) + (i * i)) % capacity;
            i++;
        }
    }
    public String getNextHashTableEntryUpFrom(int startIndex) {
        for (int i = startIndex + 1; i < capacity; i++) {
            if (table[i] != null && table[i].getFlag() != 'E' && table[i].getFlag() != 'D') {
                return "Index: " + i + ", Date: " + table[i].getDate();
            }
        }
        return "No more entries.";
    }

    public String getNextHashTableEntryDownFrom(int startIndex) {
        for (int i = startIndex - 1; i >= 0; i--) {
            if (table[i] != null && table[i].getFlag() != 'E' && table[i].getFlag() != 'D') {
                return "Index: " + i + ", Date: " + table[i].getDate();
            }
        }
        return "No more entries.";
    }

    public int getDateIndex(LocalDate date) {
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null && table[i].getDate().equals(date)) {
                return i;
            }
        }
        return -1;
    }
    private void resizeTable() {
        int newCapacity = getNextPrime(capacity * 4);
        Entry[] newTable = new Entry[newCapacity];

        List<Entry> entriesToRehash = new ArrayList<>();

        for (Entry entry : table) {
            if (entry != null && entry.getFlag() == 'F') {
                entriesToRehash.add(entry);
            }
        }
        for (Entry entry : entriesToRehash) {
            int index = hashFunction(entry.getDate());
            while (newTable[index] != null) {
                index = (index + 1) % newCapacity;
            }

            newTable[index] = entry;
        }

        table = newTable;
        capacity = newCapacity;
    }



    private int getNextPrime(int num) {
        if (num % 2 == 0) {
            num--;
        }

        while (!isPrime(num)) {
            num += 2;
        }

        return num;
    }

    private boolean isPrime(int num) {
        if (num <= 1) {
            return false;
        }

        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }
    public boolean containsDate(LocalDate date) {
        int index = hashFunction(date);
        int i = 1;
        while (table[index] != null && table[index].getFlag() != 'E' && table[index].getFlag() != 'D') {
            if (table[index].getDate().equals(date)) {
                return true;
            }
            index = (hashFunction(date) + (i * i)) % capacity;
            i++;
        }
        return false;
    }
    public AVLTree getAVLTree(LocalDate date) {
        if (!containsDate(date)) {
            return null;
        }
        int index = hashFunction(date);
        int i = 1;
        while (table[index] != null && table[index].getFlag() != 'E' && table[index].getFlag() != 'D') {
            if (table[index].getDate().equals(date)) {
                return table[index].getValue();
            }
            index = (hashFunction(date) + (i * i)) % capacity;
            i++;
        }
        return null;
    }
    public List<LocalDate> getDates() {
        List<LocalDate> dates = new ArrayList<>();
        for (Entry entry : table) {
            if (entry != null && entry.getFlag() == 'F') {
                dates.add(entry.getDate());
            }
        }
        return dates;
    }
    public List<LocalDate> getAllDates() {
        List<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < capacity; i++) {
            if (table[i] != null) {
                if (table[i].getFlag() == 'F') {
                    dates.add(table[i].getDate());
                } else if (table[i].getFlag() == 'D') {
                    dates.add(LocalDate.of(1987, 6, 24));
                }
            } else {
                dates.add(LocalDate.of(1900, 1, 1));
            }
        }
        return dates;
    }


}