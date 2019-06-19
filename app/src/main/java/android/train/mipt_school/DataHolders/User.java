package android.train.mipt_school.DataHolders;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.train.mipt_school.AllUsersPageFragment;
import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.JWTUtils;
import android.train.mipt_school.LoginActivity;
import android.train.mipt_school.MainActivity;
import android.train.mipt_school.RetrofitClient;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    private long userId; // идентификатор пользователя
    private long groupId; // отряд, в котором находится пользователь
    private long adm = 0;

    private ArrayList<ContactItem> allusers; // пользователи
    private ArrayList<ContactItem> friends; // контакаты пользователя
    private ArrayList<ScheduleItem> schedule; // расписание пользователя

    public String getPassword() {
        return password;
    }

    public long getAdm() {
        return adm;
    }

    public ArrayList<ContactItem> getAllUsers() {
        return allusers;
    }

    private static volatile User instance;

    public interface ResponseCallback {
        void call(String s);
    }

    private void userRequest(String username, String password, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .authenticateuser(username, password);
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

    public void userInfoRequest(Long id, final AllUsersPageFragment.ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .userInfo(id);
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
//                String s = "{\"bad_request\":\"" + t.toString() + "\"}";
                String s = "{\"bad_request\":\"Проверьте соединение с интернетом\"}";
                rc.call(s);
            }
        });
    }

    private void allUsersRequest(final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .allusers("Bearer " + token);
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
//                String s = "{\"bad_request\":\"" + t.toString() + "\"}";
                String s = "{\"bad_request\":\"Проверьте соединение с интернетом\"}";
                rc.call(s);
            }
        });
    }

    private void sheduleRequest(final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .allschedules("Bearer " + token);
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

    public void updateToken(final ResponseCallback responseCallback) {
        ResponseCallback rc = new ResponseCallback() {
            @Override
            public void call(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String error;
                    if (jsonObject.has("bad_request"))
                        error = jsonObject.getString("bad_request");
                    else {
                        String tmpToken = jsonObject.getString("token");
                        token = tmpToken;
                        JSONObject data = new JSONObject(JWTUtils.decoded(tmpToken));
                        JSONObject userData = data.getJSONObject("user_data");
                        firstName = userData.getString("firstname");
                        lastName = userData.getString("lastname");
                        thirdName = userData.getString("thirdname");
                        email = userData.getString("email");
                        userId = userData.getLong("id");
                        //groupId = data.getLong("groudid"); не добавлено еще
                        groupId = 0;
                        String approle = userData.getString("approle");
                        if (approle.equals("user"))
                            adm = 0;
                        else
                            adm = 1;
                        error = "success";
                    }
                    responseCallback.call(error);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        userRequest(userName, password, rc);
    }

    public void updateAllUsers(final LoginActivity.ResponseCallback responseCallback) {
        allusers = new ArrayList<>();
        ResponseCallback rc = new ResponseCallback() {
            @Override
            public void call(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String error;
                    if (jsonObject.has("bad_request"))
                        error = jsonObject.getString("bad_request");
                    else {
                        JSONArray data = jsonObject.getJSONArray("data");
                        int len = data.length();
                        for (int i = 0; i < len; i++) {
                            JSONObject tmp = data.getJSONObject(i);
                            allusers.add(new ContactItem(tmp.getLong("id"), tmp.getString("firstname") + " " + tmp.getString("lastname")));
                        }
                        error = "success";
                        Collections.sort(allusers, new Comparator<ContactItem>() {
                            @Override
                            public int compare(ContactItem lhs, ContactItem rhs) {
                                return lhs.getName().compareTo(rhs.getName());
                            }
                        });
                    }
                    responseCallback.call(error);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        allUsersRequest(rc);
    }

    public void init(final LoginActivity.ResponseCallback responseCallback) {

        allusers = new ArrayList<>();
        schedule = new ArrayList<>();

//        contacts.add(new ContactItem(123, "Василий Иванов", null));
//        contacts.add(new ContactItem(123, "Константин Смирнов", null));
//        contacts.add(new ContactItem(123, "Алексей Михайлов", null));
//        contacts.add(new ContactItem(123, "Никита Лебедев", null));
//        contacts.add(new ContactItem(123, "Владислав Морозов", null));
//        contacts.add(new ContactItem(123, "Антон Павлов", null));
//        contacts.add(new ContactItem(123, "Роман Орлов", null));
//        contacts.add(new ContactItem(123, "Николай Макаров", null));
//        contacts.add(new ContactItem(123, "Виктор Зайцев", null));
//        contacts.add(new ContactItem(123, "Павел Яковлев", null));
//        contacts.add(new ContactItem(123, "Семен Григорьев", null));
//        contacts.add(new ContactItem(123, "Леонид Киселев", null));
//        contacts.add(new ContactItem(123, "Михаил Сорокин", null));

        ResponseCallback rc = new ResponseCallback() {
            @Override
            public void call(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String error;
                    if (jsonObject.has("bad_request"))
                        error = jsonObject.getString("bad_request");
                    else {
                        JSONArray data = jsonObject.getJSONArray("data");
                        int len = data.length();
                        for (int i = 0; i < len; i++) {
                            JSONObject tmp = data.getJSONObject(i);
                            Long start = tmp.getLong("start");
                            Long end = tmp.getLong("end");
                            String group = tmp.getString("group");
                            String room = tmp.getString("room");
                            String name = tmp.getString("title");
                            String comment = tmp.getString("comment");
                            //responseCallback.call(comment);
                            schedule.add(new ScheduleItem(start, end, name, room, comment));
                        }
                        error = "success";
                    }
                    responseCallback.call(error);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        sheduleRequest(rc);
    }

    public void logIn(String userName, String password, final LoginActivity.ResponseCallback responseCallback) {
        this.userName = userName;
        this.password = password;
        ResponseCallback updateCallback = new ResponseCallback() {
            @Override
            public void call(String s) {
                if (s != null) {
                    responseCallback.call(s);
                } else {
                    responseCallback.call("success");
                }
            }
        };
        updateToken(updateCallback);
//        responseCallback.call("success");
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

    public ArrayList<ScheduleItem> getSchedule() {
        return schedule;
    }

    public long getGroupId() {
        return groupId;
    }
}
