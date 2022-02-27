package com.example.weather;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRVadapter extends RecyclerView.Adapter<WeatherRVadapter.ViewHolder> {
    //public Context context;
    public ArrayList<WeatherRVmodal> weatherRVmodalArrayList;

    public WeatherRVadapter(ArrayList<WeatherRVmodal> weatherRVmodalArrayList) {
        //this.context = context;
        this.weatherRVmodalArrayList = weatherRVmodalArrayList;
    }

    @NonNull
    @Override
    public WeatherRVadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_rv_cardview, parent, false);
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_rv_cardview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVadapter.ViewHolder holder, int position) {
        Log.d("into adapter", "onBindViewHolder: ");
        WeatherRVmodal modal = weatherRVmodalArrayList.get(position);
        String day = modal.getDay();
        String maxTemp = modal.getMaxTemp();
        String minTemp = modal.getMinTemp();
        String cardImage = modal.getCardImage();
        String condition = modal.getCondition();
        holder.setData(day, maxTemp, minTemp, cardImage, condition);
        /*//Picasso.get().load("http:".concat(modal.getWeaicon())).into(holder.weatherIconIV);
        holder.windTV.setText("12 KM/h");
        //holder.windTV.setText(modal.getWindspeed().concat("Km/h"));
        SimpleDateFormat input_simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output_simpleDateFormat = new SimpleDateFormat("hh:mm aa");
        try{
            Date date = input_simpleDateFormat.parse(modal.getTime());
            holder.timeTV.setText(output_simpleDateFormat.format(date));
        }catch (ParseException e){
            e.printStackTrace();
        }*/
    }

    @Override
    public int getItemCount() {
        return weatherRVmodalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView TVcardDay, TVcardMaxTemp, TVcardMinTemp, TVCondition;
        private ImageView IVcardConditionIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TVcardDay = itemView.findViewById(R.id.cardTVday);
            TVcardMaxTemp = itemView.findViewById(R.id.RVCardMaxTemp);
            TVcardMinTemp = itemView.findViewById(R.id.RVCardMinTemp);
            TVCondition = itemView.findViewById(R.id.RVcardCondition);
            IVcardConditionIcon = itemView.findViewById(R.id.RvCardImage);
        }

        public void setData(String day, String maxTemp, String minTemp, String cardImage, String condition) {
            TVcardDay.setText(day);
            TVcardMaxTemp.setText(maxTemp.concat("°C /"));
            TVcardMinTemp.setText(minTemp.concat("°C"));
            Picasso.get().load("https://openweathermap.org/img/wn/" + cardImage + "@2x.png").into(IVcardConditionIcon);
            Log.i("picasso", "setData: ");
            TVCondition.setText(condition);
        }
    }
}

