package com.example.kevin.parcial2.DBUtils;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.example.kevin.parcial2.DAOs.NewsDao;
import com.example.kevin.parcial2.Models.News;

@Database(entities = {News.class}, version = 1, exportSchema = false)
public abstract class AppRoomDatabase extends RoomDatabase {

    public abstract NewsDao newsDao();

    //La instancia que se utilizar�
    private static AppRoomDatabase INSTANCE;


    //Para obtener la instancia
    public static AppRoomDatabase getDatabase(final Context context) {
       if (INSTANCE == null) {
           synchronized (AppRoomDatabase.class) {
               if (INSTANCE == null) {
                   INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                           AppRoomDatabase.class, "app_database")
                           .build();

               }
           }
       }
       return INSTANCE;
   }

   public void destroyInstance() {
        INSTANCE = null;
    }

}
