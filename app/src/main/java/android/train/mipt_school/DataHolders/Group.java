package android.train.mipt_school.DataHolders;

import android.train.mipt_school.Items.ContactItem;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private static volatile Group instance;

    public String name;
    public String event;
    public String direction;
    public Long id;
    public List<ContactItem> users;
    public List<ContactItem> admins;

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

    public List<ContactItem> getUsers(){
        return this.users;
    }

    public void setUsers(List<ContactItem> users){
        this.users = users;
    }

    public List<ContactItem> getAdmins(){
        return this.admins;
    }

    public void setAdmins(List<ContactItem> admins){
        this.admins = admins;
    }

    public static Group getInstance() {
        Group localInstance = instance;
        if (localInstance == null) {
            synchronized (Group.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Group();
                }
            }
        }
        return localInstance;
    }
}