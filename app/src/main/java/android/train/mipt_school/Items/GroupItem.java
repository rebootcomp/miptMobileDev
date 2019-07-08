package android.train.mipt_school.Items;

public class GroupItem {
    private String name;
    private Long groupId;

    public GroupItem(String name, Long groupId) {
        this.name = name;
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public Long getGroupId() {
        return groupId;
    }
}
