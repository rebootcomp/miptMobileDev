package android.train.mipt_school.DataHolders;

import android.support.annotation.NonNull;
import android.train.mipt_school.Items.ContactItem;
import android.train.mipt_school.Items.DailyScheduleItem;
import android.train.mipt_school.Items.GroupItem;
import android.train.mipt_school.Items.ScheduleItem;
import android.train.mipt_school.JWTUtils;
import android.train.mipt_school.ResponseCallback;
import android.train.mipt_school.RetrofitClient;
import android.train.mipt_school.Tools.DateTools;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

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
    private String phone;
    private String deviceToken;

    //Для отображения профиля
    private boolean isEmailAvailable = true;
    private boolean isPhoneNumberAvailable = true;
    private boolean isVKAvailable = true;

    private long userId; // идентификатор пользователя

    private ArrayList<GroupItem> allGroups; // все id групп пользователя

    private long approle = 0;

    private ArrayList<ContactItem> allUsers; // пользователи
    // вроде лишнее allgroups хватает
    private ArrayList<Group> groups; // все группы пользователя

    private HashMap<Long, Integer> groupPosById;

    private ArrayList<ScheduleItem> schedule; // расписание пользователя

    private HashMap<Long, ScheduleItem> scheduleById;

    private ArrayList<DailyScheduleItem> dailySchedule; // расписание по дням


    private static volatile User instance;

    private void userRequest(String username, String password, String deviceToken, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .authenticateUser(username, password, deviceToken);
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
                } else {
                    Integer a = response.code();
                    s = a.toString();
                }
//                    s = "{\"error\":\"Ошибка сервера\"}";
                rc.onResponse(s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "Проверьте соединение с интернетом";
                rc.onFailure(s);
            }
        });
    }

    public void logOut(final ResponseCallback rc) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("smth", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        deviceToken = task.getResult().getToken();
                        Log.d("result_token", deviceToken);
                    }
                });

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .logOut("Bearer " + token,userId, deviceToken);

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
                    s = "{\"error \":\"Ошибка сервера\"}" + response.code();
                rc.onResponse(s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "Проверьте соединение с интернетом";
                rc.onFailure(s);
            }
        });
    }

    public void sendMessage(String title, String body, String target, String target_name, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .sendMessage(title,body,target,target_name);
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

    public void userInfoRequest(long id, final ResponseCallback rc) {
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

    public void addGroupRequest(String groupName, Long directionId, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .addGroup("Bearer " + token, groupName, directionId);
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
                .allUsers("Bearer " + token);
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

    public void addScheduleRequest(long groupId, long start, long end, String comment,
                                   String title, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .addSchedule("Bearer " + token, groupId, start, end, comment, title);
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

    public void updateScheduleRequest(long scheduleId, long start, long end, String comment,
                                   String title, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updateSchedule("Bearer " + token, scheduleId, start, end, comment, title);
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

    public void allRoomsRequest(final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .allRooms("Bearer " + token);
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

    public void deleteUserFromGroupRequest(Long groupId, Long userId, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteUserFromGroup("Bearer " + token, groupId, userId.toString());
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
                } else {
                    s = "{\"error\":\"Ошибка сервера\"}";
                    rc.onResponse(s);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "Проверьте соединение с интернетом";
                rc.onFailure(s);
            }
        });
    }
  
    public void editUserRequest(String phone, String vkId, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .updatePhone("Bearer " + token, userId, phone, vkId);

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
                } else {
                    s = "{\"error\":\"Ошибка сервера\"}";
                    rc.onResponse(s);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String s = "Проверьте соединение с интернетом";
                rc.onFailure(s);
            }
        });
    }

    public void addUsersIntoGroupRequest(Long groupId, ArrayList<Long> usersId, final ResponseCallback rc) {
        String users = "";
        for (Long i : usersId) {
            users += i.toString() + ",";
        }
        users.substring(0, users.length() - 2);
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .addUserIntoGroup("Bearer " + token, groupId, users);
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

    public void renameGroupRequest(Long groupId, String newName, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .renameGroup("Bearer " + token, groupId, newName);
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

    public void deleteScheduleRequest(Long scheduleId, final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .deleteSchedule("Bearer " + token, scheduleId);
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

    public boolean updateAllRooms(String data) {
        allUsers = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("data")) {
                JSONArray allRoomsData = jsonObject.getJSONArray("data");
                int len = allRoomsData.length();
                for (int i = 0; i < len; i++) {
                    JSONObject tmp = allRoomsData.getJSONObject(i);
                    // todo: куда нибудь сохранить
                    long roomId = tmp.getLong("id");
                    String roomName = tmp.getString("room");
                    // есть еще поле schedules но вроде пока бесполезное
                }
                return true;
            } else if (jsonObject.has("new_token")) {
                String newToken = jsonObject.getString("new_token");
                token = newToken;
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public long updateAddedSchedule(String data) {
        allUsers = new ArrayList<>();
        try {
            Log.d("schedule_event_log", " response received : " + data);
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("data")) {
                JSONObject updatedScheduleData = jsonObject.getJSONObject("data");
                Long scheduleId = updatedScheduleData.getLong("id");
                return scheduleId;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void scheduleRequest(final ResponseCallback rc) {
        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi()
                .allSchedules("Bearer " + token);
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

    public boolean updateToken(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("token")) {
                String tmpToken = jsonObject.getString("token");
                token = tmpToken;
                JSONObject userData =
                        new JSONObject(JWTUtils.decoded(tmpToken)).getJSONObject("user_data");

                firstName = userData.getString("firstname");
                lastName = userData.getString("lastname");
                thirdName = userData.getString("thirdname");
                email = userData.getString("email");
                userId = userData.getLong("id");
                VK = userData.getString("vk_id");
                phone = userData.getString("phone");
                //groupId = data.getLong("groudid"); не добавлено еще
                JSONArray groups = userData.getJSONArray("groups");
                allGroups = new ArrayList<>();
                for (int i = 0; i < groups.length(); i++) {
                    long groupId = groups.getJSONObject(i).getLong("id");
                    String groupName = groups.getJSONObject(i).getString("group_name");
                    long usersCount = groups.getJSONObject(i).getLong("users_count");
                    String eventName = groups.getJSONObject(i).getString("event_name");
                    allGroups.add(new GroupItem(groupName, groupId, usersCount, eventName));
                }
                String ar = userData.getString("approle");
                if (ar.equals("user"))
                    approle = 0;
                else
                    approle = 1;
                return true;
            } else if (jsonObject.has("new_token")) {
                String newToken = jsonObject.getString("new_token");
                token = newToken;
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateAllUsers(String data) {
        allUsers = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("data")) {
                JSONArray allUsersData = jsonObject.getJSONArray("data");
                int len = allUsersData.length();
                for (int i = 0; i < len; i++) {
                    JSONObject tmp = allUsersData.getJSONObject(i);
                    String firstName = tmp.getString("firstname");
                    String lastName = tmp.getString("lastname");
                    String approle = tmp.getString("approle");
                    int apr = 0;
                    if (approle.equals("admin"))
                        apr = 1;
                    allUsers.add(new ContactItem(tmp.getLong("id"), firstName, lastName, apr));
                }
                Collections.sort(allUsers, new Comparator<ContactItem>() {
                    @Override
                    public int compare(ContactItem lhs, ContactItem rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });
                return true;
            } else if (jsonObject.has("new_token")) {
                String newToken = jsonObject.getString("new_token");
                token = newToken;
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Group updateGroupInfo(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("data")) {
                JSONObject groupData = jsonObject.getJSONObject("data");
                Group group = new Group();
                group.setName(groupData.getString("group_name"));
                group.setId(groupData.getLong("id"));
                group.setEvent(groupData.getString("event_name"));
                group.setDirection(groupData.getString("direction"));
                JSONArray users = groupData.getJSONArray("users");
                int len = users.length();
                for (int i = 0; i < len; i++) {
                    JSONObject tmp = users.getJSONObject(i);
                    String firstName = tmp.getString("firstname");
                    String lastName = tmp.getString("lastname");
                    String appr = tmp.getString("approle");
                    long id = tmp.getLong("id");
                    if (appr.equals("admin"))
                        group.getAdmins().add(new ContactItem(id, firstName, lastName, 1));
                    else
                        group.getUsers().add(new ContactItem(id, firstName, lastName));
                }

                JSONArray scheduleData = groupData.getJSONArray("schedules");
                len = scheduleData.length();
                for (int i = 0; i < len; i++) {
                    JSONObject tmp = scheduleData.getJSONObject(i);
                    Long start = tmp.getLong("start") * 1000L;
                    Long end = tmp.getLong("end") * 1000L;
//                    String room = tmp.getString("room");
                    String name = tmp.getString("title");
                    String comment = tmp.getString("comment");
                    String room = comment;
                    Long groupId = tmp.getLong("group_id");
                    Long scheduleId = tmp.getLong("id");
                    group.getSchedule().add(new ScheduleItem(
                            new Date(start),
                            new Date(end),
                            name,
                            room,
                            comment,
                            groupId,
                            scheduleId));
                }
                return group;
            } else if (jsonObject.has("new_token")) {
                String newToken = jsonObject.getString("new_token");
                token = newToken;
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Long getAddedGroupId(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("success")) {
                Long id = jsonObject.getLong("success");
                return id;
            } else if (jsonObject.has("new_token")) {
                String newToken = jsonObject.getString("new_token");
                token = newToken;
                return 0L;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public boolean init(String data) {
        try {
            allUsers = new ArrayList<>();
            schedule = new ArrayList<>();
            scheduleById = new HashMap<>();

            JSONObject jsonObject = new JSONObject(data);
            if (jsonObject.has("data")) {
                JSONArray scheduleData = jsonObject.getJSONArray("data");
                int len = scheduleData.length();
                for (int i = 0; i < len; i++) {
                    JSONObject tmp = scheduleData.getJSONObject(i);
                    Long start = tmp.getLong("start") * 1000L;
                    Long end = tmp.getLong("end") * 1000L;
//                    String room = tmp.getString("room");
                    String name = tmp.getString("title");
                    String comment = tmp.getString("comment");
                    String room = comment;
                    Long groupId = tmp.getLong("group_id");
                    Long scheduleId = tmp.getLong("id");
                    boolean isGroupMember = false;

                    for (GroupItem group : allGroups) {
                        if (group.getGroupId().equals(groupId)) {
                            isGroupMember = true;
                        }
                    }

                    if (isGroupMember) {
                        ScheduleItem item = new ScheduleItem(
                                new Date(start),
                                new Date(end),
                                name,
                                room,
                                comment,
                                groupId,
                                scheduleId);

                        schedule.add(item);
                        scheduleById.put(scheduleId, item);
                    }
                }

                prepareSchedule();
                return true;
            } else if (jsonObject.has("new_token")) {
                String newToken = jsonObject.getString("new_token");
                token = newToken;
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void logIn(String userName, String password, String deviceToken, final ResponseCallback responseCallback) {
        this.userName = userName;
        this.password = password;
        this.schedule = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.groupPosById = new HashMap<>();

        //для дебага

        // Admins
        /*ContactItem contactAdmin = new ContactItem(6227, "Админ", "Иван", 1);
        ArrayList<ContactItem> admins = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            admins.add(contactAdmin);
        }

        // Users
        ContactItem contactUser = new ContactItem(228, "Юзер", "Алеша", 0);
        ArrayList<ContactItem> users = new ArrayList<>();
        for (int i = 0; i < 25; i++) {
            users.add(contactUser);
        }

        Group group = new Group("Отряд 123", users, admins);
        group.setId(1L);
        this.groups = new ArrayList<>();
        groups.add(group);*/

        // для дебага

        userRequest(userName, password, deviceToken, responseCallback);
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

    public void prepareSchedule() {
        dailySchedule = new ArrayList<>();

        if (User.getInstance().getSchedule().size() == 0) {
            return;
        }

        // сортировка событий расписания по времени
        Collections.sort(User.getInstance().getSchedule(), new Comparator<ScheduleItem>() {
            @Override
            public int compare(ScheduleItem o1, ScheduleItem o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });

        ArrayList<ScheduleItem> buffer = new ArrayList<>();
        Date currentDate = User.getInstance().getSchedule().get(0).getStartDate();

        for (ScheduleItem item : User.getInstance().getSchedule()) {
            Date eventDate = item.getStartDate();
            if (DateTools.sameDay(eventDate, currentDate)) {
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

    public void setVK(String VK) {
        this.VK = VK;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public void setPhoneNumber(String phone) {
        this.phone = phone;
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

    public ArrayList<DailyScheduleItem> getDailySchedule() {
        return dailySchedule;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<ContactItem> getAllUsers() {
        return allUsers;
    }

    public boolean getVKAccess() {
        return isVKAvailable;
    }

    public boolean getEmailAccess() {
        return isEmailAvailable;
    }

    public boolean getPhoneNumberAccess() {
        return isPhoneNumberAvailable;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public String getFullName() {
        return getLastName() + " " + getFirstName() + " " + getLastName();
    }

    public ArrayList<GroupItem> getAllGroups() {
        return allGroups;
    }


    public HashMap<Long, ScheduleItem> getScheduleById() {
        return scheduleById;
    }

    public HashMap<Long, Integer> getGroupPosById() {
        return groupPosById;
    }

    public void setNullInstance() {
        instance = null;
    }

    public String groupID(){
        return allGroups.get(0).getGroupId() + "";
    }

    public String getPhone() {
        return phone;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}