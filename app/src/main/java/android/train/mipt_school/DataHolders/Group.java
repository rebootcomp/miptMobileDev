package android.train.mipt_school.DataHolders;

import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Items.ScheduleItem;

import java.util.ArrayList;
import java.util.List;

public class Group {

    public String name;
    public String event;
    public String direction;
    public Long id;
    public List<ContactItem> users;
    public List<ContactItem> admins;
    public ArrayList<ScheduleItem> schedule; // расписание группы

    public String getEvent() {
        return event;
    }

    public String getDirection() {
        return direction;
    }

    public Long getId() {
        return id;
    }

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
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ContactItem> getUsers() {
        return this.users;
    }

    public void setUsers(List<ContactItem> users) {
        this.users = users;
    }

    public List<ContactItem> getAdmins() {
        return this.admins;
    }

    public void setAdmins(List<ContactItem> admins) {
        this.admins = admins;
    }

}