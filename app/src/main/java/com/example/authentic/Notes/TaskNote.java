package com.example.authentic.Notes;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TaskNote implements Comparable<TaskNote>{
    private int ID;
    private String name;
    private String description;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDiscription(String discription) {
        this.description = discription;
    }

    @Override
    public int compareTo(TaskNote taskNote) {
        return 0;
    }
}
