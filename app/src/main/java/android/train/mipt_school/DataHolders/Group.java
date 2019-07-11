package android.train.mipt_school.DataHolders;

import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Items.ScheduleItem;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private String name;
    private String event;
    private String direction;
    private Long id;
    private List<ContactItem> users;
    private List<ContactItem> admins;
    private ArrayList<ScheduleItem> schedule; // расписание группы


    public ArrayList<ScheduleItem> getSchedule() {
        return schedule;
    }

    Group() {
        name = "Default";
        users = new ArrayList<>();
        admins = new ArrayList<>();
    }

    Group(String name, List<ContactItem> users, List<ContactItem> admins) {
        this.name = name;
        this.users = users;
        this.admins = admins;
        this.event = "ЛОШ 2019 Август";
        this.direction = "Информатика профи";
    }

    public String getEvent() {
        return event;
    }

    public String getDirection() {
        return direction;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public List<ContactItem> getUsers() {
        return this.users;
    }

    public List<ContactItem> getAdmins() {
        return this.admins;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(List<ContactItem> users) {
        this.users = users;
    }

    public void setAdmins(List<ContactItem> admins) {
        this.admins = admins;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSchedule(ArrayList<ScheduleItem> schedule) {
        this.schedule = schedule;
    }

}