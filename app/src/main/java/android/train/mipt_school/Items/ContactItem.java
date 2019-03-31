package android.train.mipt_school.Items;

import android.graphics.Bitmap;

public class ContactItem {
    private long userId;
    private Bitmap image;
    private String name;

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public ContactItem(long userId, String name, Bitmap image) {
        this.userId = userId;
        this.name = name;
        this.image = image;
    }

    public Bitmap getImage() {
        return image;
    }
}
