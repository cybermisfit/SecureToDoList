package com.example.demo;

import javax.persistence.*;


@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String taskitem;
    private String username;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskitem() {
        return taskitem;
    }

    public void setTaskitem(String taskitem) {
        this.taskitem = taskitem;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
