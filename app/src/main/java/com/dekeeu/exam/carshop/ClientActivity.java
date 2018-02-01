package com.dekeeu.exam.carshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dekeeu.exam.carshop.Controller.CarController;
import com.dekeeu.exam.carshop.DAO.CarDAO;
import com.dekeeu.exam.carshop.Domain.Car;
import com.dekeeu.exam.carshop.Repository.CarDatabase;
import com.dekeeu.exam.carshop.Service.CarService;
import com.dekeeu.exam.carshop.Util.CarListAdapter;
import com.dekeeu.exam.carshop.Util.CarSocketListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientActivity extends AppCompatActivity implements Observer {
    private List<Car> cars;

    private final String TAG = ClientActivity.class.getName();


    ListView carListView;
    CarController carController;

    EditText selectedID;
    EditText selectedQuantity;

    CarDAO car_DAO;
    ProgressDialog progressDialog;

    public void populateListView(){
        CarListAdapter carListAdapter = new CarListAdapter(this, cars, false);
        carListView.setAdapter(carListAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        carListView = findViewById(R.id.carListView);
        //CarService carService = new CarService();
        CarService carService = CarService.getInstance();
        carController = carService.getCarController();


        getCars();

    }

    public void getCars(){
        showProgressDialog();

        Call<List<Car>> getCarsCall = carController.getCars();

        Log.v(TAG, "Call API to get all cars");

        getCarsCall.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                Log.v(TAG, "I got cars");
                Log.d(TAG, response.body().toString());

                View v = findViewById(R.id.retryBtn);
                v.setVisibility(View.GONE);

                cars = response.body();

                hideProgressDialog();

                populateListView();
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                hideProgressDialog();

                Log.v(TAG, t.toString());
                Toast.makeText(ClientActivity.this, "There is a problem with your internet connection ! Retry", Toast.LENGTH_SHORT).show();
                View v = findViewById(R.id.retryBtn);
                v.setVisibility(View.VISIBLE);

            }
        });
    }

    public void tryOnceMore(View view) {
        getCars();
    }

    public void buyCar(View view) {
        showProgressDialog();


        selectedID = findViewById(R.id.selectedID);
        selectedQuantity = findViewById(R.id.selectedQuantity);

        String selectedIDValue = selectedID.getText().toString();
        String selectedQuantityValue = selectedQuantity.getText().toString();

        int id = Integer.parseInt(selectedIDValue);
        int quantity = Integer.parseInt(selectedQuantityValue);


        Call<Car> buyCarCall = carController.buyCar(id, quantity);
        Log.v(TAG, "Call API to buy car ");

        buyCarCall.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                hideProgressDialog();

                 if(response.body() == null){
                     try {
                         JSONObject jsonObjectError = new JSONObject(response.errorBody().string());
                         String errorMessage = jsonObjectError.getString("text");
                         Toast.makeText(ClientActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                         Log.d(TAG, errorMessage);
                     } catch (IOException e) {
                         e.printStackTrace();
                     } catch (JSONException e) {
                         e.printStackTrace();
                     }
                 }else{
                     Log.d(TAG, "I bought a car");
                     Log.d(TAG, response.body().toString());

                     Car boughtCar = response.body();
                     boughtCar.setBuyTime(String.valueOf(System.currentTimeMillis() / 1000L));
                     Log.d(TAG, "Bought Car:" + boughtCar.toString());

                     storeCar(boughtCar);

                }


            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                hideProgressDialog();

                Toast.makeText(ClientActivity.this, "Can't buy that ! You're offline !", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void storeCar(Car c){
        Log.v(TAG, "Call DB API to store a car !");

        car_DAO = CarDatabase.getInstance(this).getCarDAO();
        Car _c = car_DAO.findCarByID(c.getId());

        if(_c != null){
            _c.setQuantity(c.getQuantity() + _c.getQuantity());
            _c.setBuyTime(c.getBuyTime());
            car_DAO.updateCar(_c);
        }else {

            car_DAO.addCar(c);
        }
        redirectToCars();
    }

    public void redirectToCars(){
        Intent intent = new Intent(this, ClientCarsActivity.class);
        startActivity(intent);
    }

    public void loadLocalCars(View view) {
        Intent intent = new Intent(this, ClientCarsActivity.class);
        startActivity(intent);
    }

    public void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading..");
            progressDialog.setIndeterminate(true);
        }
        progressDialog.show();
    }

    public void hideProgressDialog(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof CarService){
            if(arg instanceof Car){
                Log.d(TAG, "Is instance of Car");
                Car receivedCar = (Car)arg;

                runOnUiThread(() -> {
                    cars.add(receivedCar);
                    populateListView();
                });
            }
        }
    }
}
