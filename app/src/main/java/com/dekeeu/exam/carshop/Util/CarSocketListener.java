package com.dekeeu.exam.carshop.Util;

import android.app.Activity;
import android.util.Log;

import com.dekeeu.exam.carshop.Domain.Car;
import com.dekeeu.exam.carshop.Service.CarService;
import com.google.gson.Gson;

import java.util.Observable;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by dekeeu on 01/02/2018.
 */

public class CarSocketListener extends WebSocketListener {
    private static final int NORMAL_CLOSURE_STATUS = 1000;
    private final String TAG = CarSocketListener.class.getName();

    private final CarService carService;

    public CarSocketListener(CarService carService) {
        this.carService = carService;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        Log.w(TAG, "Open socket !");

        //webSocket.send("pa");
        //webSocket.close(NORMAL_CLOSURE_STATUS, "Bye!");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        output(text);
        Gson gson = new Gson();
        Car receivedCar = gson.fromJson(text, Car.class);

        carService.receiveCar(receivedCar);

        //Log.w(TAG, "Received text : " + text);
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        output(bytes.hex());
        //Log.w(TAG, "Received bytes : " + bytes.toString());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        output("closing socket");
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        output("Closed socket: " + code + reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        output("Error:" + t.getMessage());
    }

    private void sendCar(Car c){

    }

    private void output(final String txt) {
        Runnable r = () -> Log.w(TAG, "Got smthing" + " " + txt);
        r.run();

    }
}
