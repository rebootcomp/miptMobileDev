package android.train.mipt_school.Items;

public class GroupItem {
    private String name;
    private Long groupId;
    private Long countOfUsers;
    private String eventName;

    public GroupItem(String name, Long groupId, Long countOfUsers, String eventName) {
        this.name = name;
        this.groupId = groupId;
        this.countOfUsers = countOfUsers;
        this.eventName = eventName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
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
