package android.train.mipt_school.DataHolders;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.train.mipt_school.AllUsersPageFragment;
import android.train.mipt_school.DailyScheduleFragment;
import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Items.DailyScheduleItem;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.JWTUtils;
import android.train.mipt_school.LoginActivity;
import android.train.mipt_school.MainActivity;
import android.train.mipt_school.ResponseCallback;
import android.train.mipt_school.RetrofitClient;
import android.train.mipt_school.Tools.DateFormatter;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class User {
    private String firstName;
    private String lastName;
    private String thirdName; // отчетство
    private String userName;
    private String email;
    private String password;
    private String token;
    private String VK;
    private String phoneNumber;
    //Для отображения профиля
    private boolean isEmailAvailable = true;
    private boolean isPhoneNumberAvailable = true;
    private boolean isVKAvailable = true;

    private long userId; // идентификатор пользователя
    private long groupId; // отряд, в котором находится пользователь

    private long approle = 0;

    private ArrayList<ContactItem> allusers; // пользователи

    private ArrayList<ContactItem> friends; // контакаты пользователя
    private ArrayList<ScheduleItem> schedule; // расписание пользователя

    private ArrayList<DailyScheduleItem> dailySchedule; // расписание по дням


    private static volatile User instance;

    private void userRequest(String username, String password, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .authenticateuser(username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                if (response.code() == 200) {
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    s = "{\"error\":\"Ошибка сервера\"}";
                rc.onResponse(s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "Проверьте соединение с интернетом";
                rc.onFailure(s);
            }
        });
    }

    public void groupInfoRequest(long id, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .groupInfo(id, "Bearer " + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                if (response.code() == 200) {
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    s = "{\"error\":\"Ошибка сервера\"}";
                rc.onResponse(s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "Проверьте соединение с интернетом";
                rc.onFailure(s);
            }
        });
    }

    public void userInfoRequest(Long id, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .userInfo(id, "Bearer " + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                if (response.code() == 200) {
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    s = "{\"error\":\"Ошибка сервера\"}";
                rc.onResponse(s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "Проверьте соединение с интернетом";
                rc.onFailure(s);
            }
        });
    }

    public void allUsersRequest(final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .allusers("Bearer " + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                if (response.code() == 200) {
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    s = "{\"error\":\"Ошибка сервера\"}";
                rc.onResponse(s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "Проверьте соединение с интернетом";
                rc.onFailure(s);
            }
        });
    }

    public void scheduleRequest(final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .allschedules("Bearer " + token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                if (response.code() == 200) {
                    try {
                        s = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    s = "{\"error\":\"Ошибка сервера\"}";
                rc.onResponse(s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "{\"bad_request\":\"Проверьте соединение с интернетом\"}";
                rc.onFailure(s);
            }
        });
    }

    public boolean updateToken(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("token")) {
                String tmpToken = jsonObject.getString("token");
                token = tmpToken;
                JSONObject userData = new JSONObject(JWTUtils.decoded(tmpToken)).getJSONObject("user_data");
                firstName = userData.getString("firstname");
                lastName = userData.getString("lastname");
                thirdName = userData.getString("thirdname");
                email = userData.getString("email");
                userId = userData.getLong("id");
                VK = userData.getString("vk_id");
                phoneNumber = userData.getString("phone");
                //groupId = data.getLong("groudid"); не добавлено еще
                groupId = 0;
                String ar = userData.getString("approle");
                if (ar.equals("user"))
                    approle = 0;
                else
                    approle = 1;
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAllUsers(String data) {
        allusers = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("data")) {
                JSONArray allUsersData = jsonObject.getJSONArray("data");
                int len = allUsersData.length();
                for (int i = 0; i < len; i++) {
                    JSONObject tmp = allUsersData.getJSONObject(i);
                    allusers.add(new ContactItem(tmp.getLong("id"), tmp.getString("firstname") + " " + tmp.getString("lastname")));
                }
                Collections.sort(allusers, new Comparator<ContactItem>() {
                    @Override
                    public int compare(ContactItem lhs, ContactItem rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateGroupInfo(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("data")) {
                String tmpToken = jsonObject.getString("data");
                token = tmpToken;
                JSONObject groupData = new JSONObject(JWTUtils.decoded(tmpToken)).getJSONObject("group_data");
                Group group = Group.getInstance();
                group.name = groupData.getString("group_name");
                group.id = groupData.getLong("id");
                group.event = groupData.getString("event");
                group.direction = groupData.getString("direction");
                JSONArray users = groupData.getJSONArray("users");
                int len = users.length();
                for (int i = 0; i < len; i++) {
                    JSONObject tmp = users.getJSONObject(i);
                    String name = tmp.getString("firstname") + " " + tmp.getString("lastname");
                    String appr = tmp.getString("approle");
                    long id = tmp.getLong("id");
                    if (appr.equals("admin"))
                        group.getAdmins().add(new ContactItem(id, name, 1));
                    else
                        group.getUsers().add(new ContactItem(id, name));
                }
                JSONArray scheduleData = jsonObject.getJSONArray("data");
                len = scheduleData.length();
                for (int i = 0; i < len; i++) {
                    JSONObject tmp = scheduleData.getJSONObject(i);
                    Long start = tmp.getLong("start");
                    Long end = tmp.getLong("end");
                    String room = tmp.getString("room");
                    String name = tmp.getString("title");
                    String comment = tmp.getString("comment");
                    schedule.add(new ScheduleItem(start, end, name, room, comment));
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean init(String data) {
        try {
            allusers = new ArrayList<>();
            schedule = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("data")) {
                JSONArray scheduleData = jsonObject.getJSONArray("data");
                int len = scheduleData.length();
                for (int i = 0; i < len; i++) {
                    JSONObject tmp = scheduleData.getJSONObject(i);
                    Long start = tmp.getLong("start");
                    Long end = tmp.getLong("end");
                    String group = tmp.getString("group");
                    String room = tmp.getString("room");
                    String name = tmp.getString("title");
                    String comment = tmp.getString("comment");
                    schedule.add(new ScheduleItem(start, end, name, room, comment));
                }
                prepareSchedule();
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<ContactItem> getAllusers() {
        return allusers;
    }

    public ArrayList<ContactItem> getFriends() {
        return friends;
    }

    public void logIn(String userName, String password, final ResponseCallback responseCallback) {
        this.userName = userName;
        this.password = password;
        userRequest(userName, password, responseCallback);
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

    private void prepareSchedule() {
        dailySchedule = new ArrayList<>();

        if (User.getInstance().getSchedule().size() == 0) {
            return;
        }


        ArrayList<ScheduleItem> buffer = new ArrayList<>();
        String currentDate =
                DateFormatter.dayMonthFormat(User.getInstance().getSchedule().get(0).getStartDate());

        for (ScheduleItem item : User.getInstance().getSchedule()) {
            String eventDate = DateFormatter.dayMonthFormat(item.getStartDate());
            if (eventDate.equals(currentDate)) {
                buffer.add(item);
            } else {
                dailySchedule.add(new DailyScheduleItem(
                        currentDate,
                        (ArrayList<ScheduleItem>) buffer.clone()));

                buffer.clear();
                buffer.add(item);
                currentDate = eventDate;
            }
        }
        if (!buffer.isEmpty()) {
            dailySchedule.add(new DailyScheduleItem(
                    currentDate,
                    (ArrayList<ScheduleItem>) buffer.clone()));
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getVK() {
        return VK;
    }

    public String getPhoneNumber(){
        Log.i("PhoneNumber", phoneNumber);
        return phoneNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public long getApprole() {
        return approle;
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

    public ArrayList<ScheduleItem> getSchedule() {
        return schedule;
    }

    public long getGroupId() {
        return groupId;
    }
  
    public ArrayList<DailyScheduleItem> getDailySchedule() {
        return dailySchedule;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<ContactItem> getAllUsers() {
        return allusers;
    }
  
    public boolean getVKAccess(){
        return isVKAvailable;
    }

    public boolean getEmailAccess(){
        return isEmailAvailable;
    }

    public boolean getPhoneNumberAccess(){
        return isPhoneNumberAvailable;
}
