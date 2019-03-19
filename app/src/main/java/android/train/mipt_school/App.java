package android.train.mipt_school;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("app.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS users (login TEXT,id TEXT,firstname TEXT, lastname TEXT,email TEXT,rating TEXT,country TEXT)");
        //db.execSQL("CREATE TABLE IF NOT EXISTS blogs (title TEXT,author TEXT,date TEXT,content TEXT,date_id INTEGER)");
       // db.execSQL("CREATE TABLE IF NOT EXISTS contests (id TEXT,name TEXT,startTimeSeconds INTEGER,duration TEXT,url TEXT, date TEXT)");
        //db.delete("contests", null, null);
        //db.execSQL("INSERT INTO users VALUES ('Tom Smith', 23);");
        db.close();
    }
}