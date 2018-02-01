package com.dekeeu.exam.carshop;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dekeeu.exam.carshop.DAO.CarDAO;
import com.dekeeu.exam.carshop.Domain.Car;
import com.dekeeu.exam.carshop.Domain.CarViewModel;
import com.dekeeu.exam.carshop.Repository.CarDatabase;
import com.dekeeu.exam.carshop.Service.CarService;
import com.dekeeu.exam.carshop.Util.CarListAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientCarsActivity extends AppCompatActivity {
    List<Car> cars = new ArrayList<>();
    ListView carListView;
    CarListAdapter carListAdapter;
    CarDAO car_dao;
    CarViewModel carViewModel;

    CarService carService;

    private final String TAG = ClientCarsActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_cars);

        //carService = new CarService();
        carService = CarService.getInstance();

        carViewModel = ViewModelProviders.of(this).get(CarViewModel.class);
        carViewModel.getCarList().observe(this, this::updateListView);

        this.carListView = findViewById(R.id.allClientCarsListView);
        this.carListAdapter = new CarListAdapter(this, cars, false);
        this.carListView.setAdapter(carListAdapter);
        this.car_dao = CarDatabase.getInstance(this).getCarDAO();

        carListView.setOnItemClickListener((parent, view, position, id) -> {
            Car selectedCar = this.cars.get(position);
            Log.w(TAG, "Selected CAR: " + selectedCar.toString());


            Long time1 = Long.parseLong(selectedCar.getBuyTime());
            Long time2 = System.currentTimeMillis() / 1000L;
            Long diff = time2 - time1;

            if((diff / (24 * 60 * 60 * 1000)) < 30){
                //removeCar(selectedCar);
                removeCar(selectedCar);
                returnCar(selectedCar);
            }
        });
    }

    public void updateListView(List<Car> cars){
        Log.w(TAG, "UpdateListView");
        Log.w(TAG, cars.toString());
        this.cars = cars;

        CarListAdapter carListAdapter = new CarListAdapter(this, cars, false);
        carListView.setAdapter(carListAdapter);

    }

    public void removeCar(Car c){
        Log.v(TAG, "Call DB API to remove a car !");

        Thread t = new Thread(() -> {
            car_dao.deleteCar(c);
            Log.w(TAG, "done");
        });

        t.run();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        this.carViewModel.loadCars();
    }

    public void removeAllCars(View view) {
        Log.v(TAG, "Call DB API to remove All Cars !");

        Thread t = new Thread(() -> {
            car_dao.nukeTable();
            Log.w(TAG, "done");
        });

        t.run();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        this.carViewModel.loadCars();

    }

    public void returnCar(Car c){
        Log.v(TAG, "Call API to return a car !");
        Log.d(TAG, "Return " + c.toString());


        Call<Car> returnCarCall = carService.getCarController().returnCar(c.getId());
        returnCarCall.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                Toast.makeText(ClientCarsActivity.this, "The car was returned !", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Toast.makeText(ClientCarsActivity.this, "Failed !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
