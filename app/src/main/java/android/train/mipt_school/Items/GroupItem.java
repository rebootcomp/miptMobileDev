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
}
