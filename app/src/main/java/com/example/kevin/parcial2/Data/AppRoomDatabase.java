package com.example.kevin.parcial2.Data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.kevin.parcial2.DAOs.NewsDao;
import com.example.kevin.parcial2.ModelsAndEntities.News;

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
                           .addCallback(sRoomDatabaseCallback)
                           .build();

               }
           }
       }
       return INSTANCE;
   }

   public void destroyInstance() {
        INSTANCE = null;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
                new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    public static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final NewsDao mDao;


        PopulateDbAsync(AppRoomDatabase db) {
            mDao = db.newsDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            return null;
        }

    }



}
