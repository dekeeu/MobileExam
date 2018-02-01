package com.dekeeu.exam.carshop.Service;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dekeeu.exam.carshop.ClientActivity;
import com.dekeeu.exam.carshop.Controller.CarController;
import com.dekeeu.exam.carshop.DAO.CarDAO;
import com.dekeeu.exam.carshop.Domain.Car;
import com.dekeeu.exam.carshop.Util.CarSocketListener;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by dekeeu on 31/01/2018.
 */


public class CarService extends Observable {
    HttpLoggingInterceptor interceptor;
    OkHttpClient.Builder httpClient;
    Retrofit retrofit;
    CarController carController;

    CarDAO car_DAO;

    CarSocketListener carSocketListener;
    OkHttpClient client;
    Request request;
    WebSocket ws;

    private static CarService INSTANCE;


    private final String API_URL = "http://10.0.2.2:4000";
    private final String WS_API_URL = "ws://10.0.2.2:4000";

    private final String TAG = ClientActivity.class.getName();

    // Returns a single instance of this class, creating it if necessary.
    public static CarService getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new CarService();
        }
        return INSTANCE;
    }

    public void receiveCar(Car c){
        Log.w(TAG, "I got a car from WS: " + c.toString());
        setChanged();
        notifyObservers(c);

    }

    public CarService() {
        interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);

        retrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_URL)
                .build();

        carController = retrofit.create(CarController.class);

        carSocketListener = new CarSocketListener(this);
        client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();

        request = new Request.Builder()
                .url(WS_API_URL)
                .build();


        ws = client.newWebSocket(request, carSocketListener);

        client.dispatcher().executorService().shutdown();
        Log.d(TAG, "Client connected !");


    }


    public CarSocketListener getCarSocketListener() {
        return carSocketListener;
    }

    public OkHttpClient getClient() {
        return client;
    }

    public Request getRequest() {
        return request;
    }

    public WebSocket getWs() {
        return ws;
    }

    public HttpLoggingInterceptor getInterceptor() {
        return interceptor;
    }

    public void setInterceptor(HttpLoggingInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public OkHttpClient.Builder getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(OkHttpClient.Builder httpClient) {
        this.httpClient = httpClient;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public CarController getCarController() {
        return carController;
    }

    public void setCarController(CarController carController) {
        this.carController = carController;
    }

    public CarDAO getCar_DAO() {
        return car_DAO;
    }

    public void setCar_DAO(CarDAO car_DAO) {
        this.car_DAO = car_DAO;
    }

    public String getAPI_URL() {
        return API_URL;
    }

    public String getTAG() {
        return TAG;
    }
}
