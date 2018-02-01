package com.dekeeu.exam.carshop.Domain;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;

/**
 * Created by dekeeu on 30/01/2018.
 */

@Entity
public class Car {
    @Expose
    @PrimaryKey
    @NonNull
    private Integer id;

    @Expose
    private String name;

    @Expose
    private Integer quantity;

    @Expose
    private String type;

    @Expose
    private String status;

    @Expose
    @Nullable
    private String buyTime;

    public Car(Integer id, String name, Integer quantity, String type, String status, @Nullable String buyTime) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.type = type;
        this.status = status;
        this.buyTime = buyTime;
    }

    @Nullable
    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(@Nullable String buyTime) {
        this.buyTime = buyTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", buyTime='" + buyTime + '\'' +
                '}';
    }
}
