package com.dekeeu.exam.carshop.DAO;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.dekeeu.exam.carshop.Domain.Car;

import java.util.List;

/**
 * Created by dekeeu on 31/01/2018.
 */

@Dao
public interface CarDAO {
    @Query("SELECT * FROM car")
    List<Car> getAllCars();

    @Insert
    void addCar(Car c);

    @Insert
    void addCar(Car... car);

    @Update
    void updateCar(Car c);

    @Update
    void updateCar(Car... car);

    @Delete
    void deleteCar(Car c);

    @Delete
    void deleteCar(Car... car);

    @Query("DELETE FROM car")
    void nukeTable();

    @Query("SELECT * FROM car WHERE id = :id")
    Car findCarByID(int id);
}
