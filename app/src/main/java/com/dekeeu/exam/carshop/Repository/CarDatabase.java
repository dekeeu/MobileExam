package com.dekeeu.exam.carshop.Repository;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.dekeeu.exam.carshop.DAO.CarDAO;
import com.dekeeu.exam.carshop.Domain.Car;

/**
 * Created by dekeeu on 31/01/2018.
 */

@Database(entities = {Car.class}, version = 1)
public abstract class CarDatabase extends RoomDatabase {
    private static final String DB_NAME = "carDatabase.db";
    private static volatile CarDatabase instance;

    public static synchronized CarDatabase getInstance(Context context){
        if(instance == null){
            instance = create(context);
        }
        return instance;
    }

    private static CarDatabase create(final Context context){
        return Room.databaseBuilder(
                context.getApplicationContext(),
                CarDatabase.class,
                DB_NAME
        ).allowMainThreadQueries() //not good
                .build();

    }

    public abstract CarDAO getCarDAO();
}
