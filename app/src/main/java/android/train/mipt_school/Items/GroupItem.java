package android.train.mipt_school.Items;

public class GroupItem {
    private String name;
    private Long groupId;
    private Long countOfUsers;

    public GroupItem(String name, Long groupId, Long countOfUsers) {
        this.name = name;
        this.groupId = groupId;
        this.countOfUsers = countOfUsers;
    }

    public String getName() {
        return name;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Long getCountOfUsers() {
        return countOfUsers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public void setCountOfUsers(Long countOfUsers) {
        this.countOfUsers = countOfUsers;
    }
}
