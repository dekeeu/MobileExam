package com.dekeeu.exam.carshop.Util;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.dekeeu.exam.carshop.Domain.Car;
import com.dekeeu.exam.carshop.R;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dekeeu on 30/01/2018.
 */

public class CarListAdapter extends ArrayAdapter {
    private final Activity context;
    private final List<Car> cars;

    TextView carName;
    TextView carType;
    TextView carQuantity;

    boolean showStatus = false;

    public CarListAdapter(Activity context1, List<Car> _cars, boolean showStatus) {
        super(context1, R.layout.carlistview_row, _cars);

        context = context1;
        cars = _cars;
        this.showStatus = showStatus;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View rowView = layoutInflater.inflate(R.layout.carlistview_row, null, true);

        Car currentCar = cars.get(position);

        carName = rowView.findViewById(R.id.carNameTextView);
        carType = rowView.findViewById(R.id.carTypeTextView);
        carQuantity = rowView.findViewById(R.id.carQuantityTextView);

        if(showStatus){
            carName.setText(currentCar.getName() + " --> " + currentCar.getStatus());
        }else{
            carName.setText(currentCar.getName());
        }
        carType.setText(currentCar.getType());
        carQuantity.setText(String.valueOf(currentCar.getQuantity()));
        return rowView;
    }
}
