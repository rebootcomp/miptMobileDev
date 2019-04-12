package android.train.mipt_school.DataHolders;

public class OtherUsers {
    private String firstName;
    private String lastName;
    private String thirdName; // отчетство
    private String userName;
    private String email;
    private long userId; // идентификатор пользователя

    public OtherUsers(String firstName, String lastName, String thirdName, String userName, String email, long userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.thirdName = thirdName;
        this.userName = userName;
        this.email = email;
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public long getUserId() {
        return userId;
    }
}
