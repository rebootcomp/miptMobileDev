package android.train.mipt_school.Items;

import android.graphics.Bitmap;

public class ContactItem {
    private long userId;
    private long approle;
    private Bitmap image;
    private String name;

    private String firstName;
    private String lastName;

    public ContactItem(long userId, String name, Bitmap image) {
        this.userId = userId;
        this.name = name;
        this.image = image;
        this.approle = 0;
    }

    public ContactItem(long userId, String firstName, String lastName) {
        this.userId = userId;
        this.name = firstName + " " + lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.approle = 0;
    }

    public ContactItem(long userId, String firstName, String lastName, long approle) {
        this.userId = userId;
        this.name = firstName + " " + lastName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.approle = approle;
    }

    public Bitmap getImage() {
        return image;
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

}
