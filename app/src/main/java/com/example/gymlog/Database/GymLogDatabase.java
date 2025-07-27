package com.example.gymlog.Database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.gymlog.Database.entities.GymLog;
import com.example.gymlog.Database.entities.User;
import com.example.gymlog.Database.typeConverters.LocalDateTypeConverter;
import com.example.gymlog.MainActivity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(LocalDateTypeConverter.class)

@Database(entities = {GymLog.class, User.class}, version = 4, exportSchema = false)
public abstract class GymLogDatabase extends RoomDatabase {

    public static final String USER_TABLE = "usertable";
    private static final String DATABASE_NAME = "GymLogdatabase";
    public static final String GYM_LOG_TABLE = "gymLogTable";

    private static volatile GymLogDatabase INSTANCE; // volatile means exists only in RAM
    private static final int NUMBER_OF_THREADS = 4; // limit number of threads being able to access database at a given time
    // allows us not to run database threads on the UI, which allows maximimum performance for UI

    static final ExecutorService databaseWriteExecuter = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    // creates 4 number of threads in a pool, then a pull thread out of pool when needed
    // once done, put thread back into pool

    static GymLogDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (GymLogDatabase.class){ // makes sure everything happens in a single thread. .class is a reference to the compiled version of the class
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            GymLogDatabase.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration().addCallback(addDefaultValues).build();
                }
            }

        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");

            databaseWriteExecuter.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();
                dao.deleteAll();
                User admin = new User("admin1", "admin1");
                admin.setAdmin(true);
                dao.insert(admin);
                User testUser1 = new User("testuser1", "testuser1");
                dao.insert(testUser1);
                });

        }

    };

    public abstract GymLogDAO gymLogDAO();

    public abstract UserDAO userDAO();
}
