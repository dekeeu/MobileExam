package com.dekeeu.exam.carshop;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.dekeeu.exam.carshop.Domain.Car;
import com.dekeeu.exam.carshop.Service.CarService;
import com.dekeeu.exam.carshop.Util.CarListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeActivity extends AppCompatActivity implements Observer {
    EditText carName;
    EditText carType;
    EditText carQuantity;

    CarService carService;

    ListView carListView;
    List<Car> cars = new ArrayList<>();
    CarListAdapter carListAdapter;

    ProgressDialog progressDialog;

    private static final String TAG = EmployeeActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        //carService = new CarService();

        carService = CarService.getInstance();
        carService.addObserver(this);

        carListView = findViewById(R.id.carsListView);
        carListAdapter = new CarListAdapter(this, cars, true);

        populateCarList();

        carListView.setOnItemClickListener((parent, view, position, id) -> {
            Car selectedCar = cars.get(position);
            Call<Car> removeCarCall = carService.getCarController().removeCar(selectedCar.getId());
            removeCarCall.enqueue(new Callback<Car>() {
                @Override
                public void onResponse(Call<Car> call, Response<Car> response) {
                    if(response.body() != null){
                        Toast.makeText(EmployeeActivity.this, "Car was deleted !", Toast.LENGTH_SHORT).show();
                        populateCarList();
                    }else{
                        Toast.makeText(EmployeeActivity.this, "There was an error here !", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Car> call, Throwable t) {
                    Toast.makeText(EmployeeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });




        Call<ResponseBody> deleteCarCall = carService.getCarController().deleteCar(1);
        deleteCarCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(EmployeeActivity.this, "Car was deleted!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EmployeeActivity.this, "Error: " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateListView(List<Car> cars){
        Log.w(TAG, "UpdateListView");
        Log.w(TAG, cars.toString());
        this.cars = cars;

        CarListAdapter carListAdapter = new CarListAdapter(this, cars, true);
        carListView.setAdapter(carListAdapter);

    }

    public void populateCarList(){
        showProgressDialog();
        Log.v(TAG, "Call API to get all cars !");


        Call<List<Car>> getAllCars = carService.getCarController().getAllCars();
        getAllCars.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                hideProgressDialog();


                if(response.body() != null){
                    cars = response.body();
                    //carListAdapter.notifyDataSetChanged();
                    updateListView(cars);
                }else{
                    Toast.makeText(EmployeeActivity.this, "Invalid response from the server !", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                hideProgressDialog();


                Toast.makeText(EmployeeActivity.this, "There was an error during your request !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addNewCar(View view) {
        showProgressDialog();
        Log.v(TAG, "Call API to add a car !");


        carName = findViewById(R.id.carName);
        carType = findViewById(R.id.carType);
        carQuantity = findViewById(R.id.carQuantity);

        String _carName = carName.getText().toString();
        String _carType = carType.getText().toString();
        int _carQuantity = Integer.parseInt(carQuantity.getText().toString());


        Call<Car> addNewCar = carService.getCarController().addCar(_carName, _carType, _carQuantity);
        
        addNewCar.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                hideProgressDialog();


                Toast.makeText(EmployeeActivity.this, "The car was added !", Toast.LENGTH_SHORT).show();
                //populateCarList();
                //updateListView(cars);
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                hideProgressDialog();


                Toast.makeText(EmployeeActivity.this, "There was an error here !", Toast.LENGTH_SHORT).show();
            }
        });

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
        Log.w(TAG, "Employee Observer Received Something: ");
        if(o instanceof CarService){
            Log.d(TAG, "Is instance of CarService");
            if(arg instanceof Car){
                Log.d(TAG, "Is instance of Car");
                Car receivedCar = (Car)arg;

                runOnUiThread(() -> {
                    cars.add(receivedCar);
                    updateListView(cars);
                });

            }
        }
    }
}
