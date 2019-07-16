package android.train.mipt_school;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.yandex.metrica.YandexMetrica;
import com.yandex.metrica.YandexMetricaConfig;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        YandexMetricaConfig config = YandexMetricaConfig.newConfigBuilder("7107bb80-2bd6-4c6b-9d7b-d80df6e2359c").build();
        // Initializing the AppMetrica SDK.
        YandexMetrica.activate(getApplicationContext(), config);
        // Automatic tracking of user activity.
        YandexMetrica.enableActivityAutoTracking(this);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS users (login TEXT,id TEXT,firstname TEXT, lastname TEXT,email TEXT,rating TEXT,country TEXT)");
        //db.execSQL("CREATE TABLE IF NOT EXISTS blogs (title TEXT,author TEXT,date TEXT,content TEXT,date_id INTEGER)");
       // db.execSQL("CREATE TABLE IF NOT EXISTS contests (id TEXT,name TEXT,startTimeSeconds INTEGER,duration TEXT,url TEXT, date TEXT)");
        //db.delete("contests", null, null);
        //db.execSQL("INSERT INTO users VALUES ('Tom Smith', 23);");
        db.close();
    }
}