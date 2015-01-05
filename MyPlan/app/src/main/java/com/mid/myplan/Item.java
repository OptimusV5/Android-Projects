package com.mid.myplan;

/**
 * Created by OptimusV5 on 2014/12/7.
 */
public class Item {
    private String name;
    private String time;
    private String table;
    private int position;
    private int alarm;
    public Item(String name, String time, String table, int position, int alarm) {
        this.name  = name;
        this.time  = time;
        this.table = table;
        this.position = position;
        this.alarm = alarm;
    }
    public void setName(String name) {
        this.name   = name;
    }

    public String getName() {
        return name;
    }

    public void setTime(String detail) {
        this.time = detail;
    }

    public String getTime() {
        return time;
    }
    public void setTable(String table) {
        this.time = table;
    }

    public String getTable() {
        return table;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int isAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }
}

