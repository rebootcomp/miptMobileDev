package android.train.mipt_school.DataHolders;

import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Items.ScheduleItem;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String firstName;
    private String lastName;
    private String thirdName; // отчетство
    private String userName;
    private String email;
    private String password;

    private long userId; // идентификатор пользователя
    private long groupId; // отряд, в котором находится пользователь


    private ArrayList<ContactItem> contacts; // контакаты пользователя
    private ArrayList<ScheduleItem> schedule; // расписание пользователя

    private static volatile User instance;

    public void logIn(String firstName, String lastName, String thirdName, String email, String userName, long userId, long groupId) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.thirdName = thirdName;
        this.email = email;
        this.userName = userName;
        this.userId = userId;
        this.groupId = groupId;

        contacts = new ArrayList<>();
        schedule = new ArrayList<>();

        contacts.add(new ContactItem(123, "Василий Иванов", null));
        contacts.add(new ContactItem(123, "Константин Смирнов", null));
        contacts.add(new ContactItem(123, "Алексей Михайлов", null));
        contacts.add(new ContactItem(123, "Никита Лебедев", null));
        contacts.add(new ContactItem(123, "Владислав Морозов", null));
        contacts.add(new ContactItem(123, "Антон Павлов", null));
        contacts.add(new ContactItem(123, "Роман Орлов", null));
        contacts.add(new ContactItem(123, "Николай Макаров", null));
        contacts.add(new ContactItem(123, "Виктор Зайцев", null));
        contacts.add(new ContactItem(123, "Павел Яковлев", null));
        contacts.add(new ContactItem(123, "Семен Григорьев", null));
        contacts.add(new ContactItem(123, "Леонид Киселев", null));
        contacts.add(new ContactItem(123, "Михаил Сорокин", null));


        schedule.add(new ScheduleItem(new Date(28800000), new Date(32400000),
                "Открытая олимпиада школьников по информатике",
                "Главный Корпус МФТИ"));

        schedule.add(new ScheduleItem(new Date(54000000), new Date(64800000),
                "Завтрак",
                "Столовая МФТИ"));

        schedule.add(new ScheduleItem(new Date(79200000), new Date(82800000),
                "Обед",
                "Столовая МФТИ"));

        schedule.add(new ScheduleItem(new Date(36000000), new Date(45000000),
                "Контест",
                "Учебный корпус МФТИ"));
        schedule.add(new ScheduleItem(new Date(45000000), new Date(64800000),
                "Лекция по математике и криптографии",
                "Столовая МФТИ"));
        schedule.add(new ScheduleItem(new Date(36000000), new Date(64800000),
                "Заезд",
                "Фойе ЛК"));
        schedule.add(new ScheduleItem(new Date(45000000), new Date(64800000),
                "Интеллектуальная игра \"Что?Где?Когда?\"", "ЛК"));
        schedule.add(new ScheduleItem(new Date(45000000), new Date(32400000),
                "Открытие смены", "Актовый зал МФТИ"));
        schedule.add(new ScheduleItem(new Date(45000000), new Date(32400000),
                "Встреча с представителями компании hahaha",
                "Читальный зал"));
        schedule.add(new ScheduleItem(new Date(45000000), new Date(28800000),
                "Подъем",
                "Общежитие №4"));
        schedule.add(new ScheduleItem(new Date(36000000), new Date(28800000),
                "Мягкий отбой", "Общежитие №2"));
        schedule.add(new ScheduleItem(new Date(45000000), null,
                "Отбой", "Читальный зал №5"));
        schedule.add(new ScheduleItem(new Date(45000000), new Date(28800000),
                "Экскурсия в офис Yandex", "Общежитие №8"));
        schedule.add(new ScheduleItem(new Date(45000000), new Date(43200000),
                "Олимпиада ЗОШ", "Корпус прикладной математики"));
        schedule.add(new ScheduleItem(new Date(36000000), new Date(32400000),
                "Встреча с учениками МФТИ", "Лабораторный корпус, каб. 321"));
        schedule.add(new ScheduleItem(new Date(45000000), new Date(32400000),
                "Закрытие смены", "Актовый зал"));
        schedule.add(new ScheduleItem(new Date(36000000), new Date(45000000),
                "Празднование юбилея ЗОШ", "Общежитие МФТИ"));
        schedule.add(new ScheduleItem(new Date(64800000), null,
                "Отъезд", "МФТИ"));

    }

    public static User getInstance() {
        User localInstance = instance;
        if (localInstance == null) {
            synchronized (User.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new User();
                }
            }
        }
        return localInstance;
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

    public ArrayList<ContactItem> getContacts() {
        return contacts;
    }

    public ArrayList<ScheduleItem> getSchedule() {
        return schedule;
    }

    public long getGroupId() {
        return groupId;
    }
}
