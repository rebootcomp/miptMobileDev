package android.train.mipt_school.DataHolders;

import android.content.Intent;
import android.os.Handler;
import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.JWTUtils;
import android.train.mipt_school.LoginActivity;
import android.train.mipt_school.MainActivity;
import android.train.mipt_school.RetrofitClient;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.content.ContextCompat.startActivity;

public class User {
    private String firstName;
    private String lastName;
    private String thirdName; // отчетство
    private String userName;
    private String email;
    private String password;
    private String token;


    private String returnedString = null;
    private ResponseCallback rc = null;
    private String error = null;

    private long userId; // идентификатор пользователя
    private long groupId; // отряд, в котором находится пользователь

    private ArrayList<ContactItem> contacts; // контакаты пользователя
    private ArrayList<ScheduleItem> schedule; // расписание пользователя

    private static volatile User instance;

    public interface ResponseCallback {
        void call(String s);
        void tkn(String s);
    }

    public void userRequest(String username, String password) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .getmdluser(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                if (response.code() == 200 || response.code() == 201) {
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    s = "{\"bad_request\":\"Ошибка сервера\"}";
                rc.call(s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "{\"bad_request\":\"Проверьте соединение с интернетом\"}";
                rc.call(s);
            }
        });
    }

    public void init() {
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

    public void logIn(String userName, String password, final LoginActivity.ResponseCallback responseCallback) {
        this.userName = userName;
        this.password = password;
        error = null;
        rc = new ResponseCallback() {
            @Override
            public void call(String s) {
                returnedString = s;
            }
            @Override
            public void tkn(String s) {
                token = s;
            }
        };
        userRequest(userName, password);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                try {
                    if (returnedString == null) {
                        error = "Проверьте соединение с интернетом";
                    } else {
                        JSONObject jsonObject = new JSONObject(returnedString);
                        if (jsonObject.has("bad_request"))
                            error = jsonObject.getString("bad_request");
                        else {
                            String token = jsonObject.getString("token");
                            rc.tkn(token);
                            JSONObject data = new JSONObject(JWTUtils.decoded(token));
                            JSONObject userData = data.getJSONObject("user_data");
                            firstName = userData.getString("firstname");
                            lastName = userData.getString("lastname");
                            thirdName = userData.getString("thirdname");
                            email = userData.getString("email");
                            userId = userData.getLong("id");
                            //groupId = data.getLong("groudid"); не добавлено еще
                            groupId = 0;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (error != null) {
                    responseCallback.call(error);
                } else {
                    init();
                    responseCallback.call("success");
                }
            }
        }, 1000);
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

    public String getToken() {
        return token;
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
