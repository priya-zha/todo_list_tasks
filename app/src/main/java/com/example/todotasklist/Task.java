package com.example.todotasklist;


public class Task {
    private int id;
    private String name;
    private String dateAdded;

    public Task(String name, String dateAdded, int id) {
        this.id = id;
        this.name = name;
        this.dateAdded = dateAdded;
    }

    public Task() {
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

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateAdded='" + dateAdded + '\'' +
                '}';
    }
}