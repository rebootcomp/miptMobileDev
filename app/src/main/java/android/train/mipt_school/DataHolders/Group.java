package android.train.mipt_school.DataHolders;

import java.util.ArrayList;
import java.util.List;

public class Group {

    public String name;
    public List<User> users;
    public List<User> admins;

    Group() {
        name = "Default";
        users = new ArrayList<>();
        admins = new ArrayList<>();
    }

    Group(String name, List<User> users, List<User> admins) {
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

    public List<User> getUsers(){
        return this.users;
    }

    public void setUsers(List<User> users){
        this.users = users;
    }

    public List<User> getAdins(){
        return this.admins;
    }

    public void setAdmins(List<User> admins){
        this.admins = admins;
    }
}