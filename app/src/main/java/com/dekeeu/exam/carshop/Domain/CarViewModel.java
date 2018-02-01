package com.dekeeu.exam.carshop.Domain;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;

import com.dekeeu.exam.carshop.Repository.CarDatabase;

import java.util.List;

/**
 * Created by dekeeu on 31/01/2018.
 */

public class CarViewModel extends AndroidViewModel {
    private MutableLiveData<List<Car>> carLiveData;
    private CarDatabase carDatabase;

    private final String TAG = CarViewModel.class.getName();


    public CarViewModel(Application application){
        super(application);
        carDatabase = CarDatabase.getInstance(application);

        Log.w(TAG, "called");
    }

    public LiveData<List<Car>> getCarList(){
        Log.w(TAG, "called2");
        if(carLiveData == null){
            carLiveData = new MutableLiveData<>();
            loadCars();
        }
        return carLiveData;
    }

    public void loadCars(){
        Log.w(TAG, "called3");
        carLiveData.postValue(carDatabase.getCarDAO().getAllCars());
    }

    public void deleteCar(Car c){
        new deleteAsyncTask(carDatabase).execute(c);
    }

    public void deleteAllCars(){
        new nukeTableAsyncTask(carDatabase).execute();
    }

    private static class nukeTableAsyncTask extends AsyncTask<Void, Void, Void>{
        private CarDatabase db;

        public nukeTableAsyncTask(CarDatabase db){
            this.db = db;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            db.getCarDAO().nukeTable();

            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Car, Void, Void>{
        private CarDatabase db;

        public deleteAsyncTask(CarDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(Car... cars) {
            db.getCarDAO().deleteCar(cars[0]);
            return null;
        }
    }
}
