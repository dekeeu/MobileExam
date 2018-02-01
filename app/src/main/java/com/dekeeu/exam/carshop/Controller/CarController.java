package com.dekeeu.exam.carshop.Controller;

import com.dekeeu.exam.carshop.Domain.Car;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by dekeeu on 30/01/2018.
 */

public interface CarController {
    @GET("/cars")
    Call<List<Car>> getCars();

    @GET("/all")
    Call<List<Car>> getAllCars();

    @POST("/buyCar")
    @FormUrlEncoded
    Call<Car> buyCar(
            @Field("id") Integer id,
            @Field("quantity") Integer quantity
    );

    @POST("/returnCar")
    @FormUrlEncoded
    Call<Car> returnCar(
        @Field("id") Integer id
    );

    @POST("/addCar")
    @FormUrlEncoded
    Call<Car> addCar(
            @Field("name") String name,
            @Field("type") String type,
            @Field("quantity") int quantity
    );

    @POST("/removeCar")
    @FormUrlEncoded
    Call<Car> removeCar(
            @Field("id") Integer id
    );

    @DELETE("/deleteCar/{id}")
    Call<ResponseBody> deleteCar(
            @Path("id") int id
    );




}
