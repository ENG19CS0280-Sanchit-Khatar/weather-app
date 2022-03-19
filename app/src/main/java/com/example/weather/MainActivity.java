package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    private ProgressBar initalloading;
    private TextView tvcityname, tvtemp, tvcurrwea, tvTempFeelslike, tvDateAndTime;
    private ImageView ivtempicon, llsearchicon, backimg;
    private RecyclerView recyclerView;
    private ArrayList<WeatherRVmodal> WeatherRVmodalArrayList;
    private WeatherRVadapter adapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String CityName;
    private EditText llSearchEdittxt;
    private Button tryAgainbtn;
    private RelativeLayout NoInternetlayout;
    int day;
    private LinearLayoutManager linearLayoutManager;
    String[] DaysArray = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);//used for full screen
        setContentView(R.layout.activity_main);
        constraintLayout = findViewById(R.id.home);
        ivtempicon = findViewById(R.id.TVtempicon);
        tvDateAndTime = findViewById(R.id.TVdate_and_time);
        tvTempFeelslike = findViewById(R.id.TVTempFeel);
        tvcityname = findViewById(R.id.Cityname);
        tvtemp = findViewById(R.id.TVtemprature);
        tvcurrwea = findViewById(R.id.TVcurrwea);
        initalloading = findViewById(R.id.progressbar);
        constraintLayout = findViewById(R.id.home);
        tryAgainbtn = findViewById(R.id.TryAgainBtn);
        NoInternetlayout = findViewById(R.id.RLNoInternet);
        llsearchicon = findViewById(R.id.llsearchicon);
        llSearchEdittxt = findViewById(R.id.lledittxtcityinput);
        backimg = findViewById(R.id.blockclr);
        llSearchEdittxt.setOnEditorActionListener(editorListner);

        WeatherRVmodalArrayList = new ArrayList<>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        CityName = getCityName(location.getLongitude(), location.getLatitude());
        getWeather(location.getLatitude(), location.getLongitude(), CityName);

        llsearchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = llSearchEdittxt.getText().toString();
                if (city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter city name", Toast.LENGTH_SHORT).show();
                } else {
                    //tvcityname.setText(CityName);
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getLatLong(city);
                }
            }
        });
        tryAgainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llSearchEdittxt.setText("");
                getWeather(location.getLatitude(), location.getLongitude(), CityName);
            }
        });
    }

    private TextView.OnEditorActionListener editorListner = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                String city = llSearchEdittxt.getText().toString();
                // for closing the key board
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                getLatLong(city);
            }
            return false;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Provide Permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(Double longi, Double lati) {
        String cityName = "Not found";
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(lati, longi, 10);
            for (Address address : addressList) {
                if (address != null) {
                    String city = address.getLocality();
                    if (city != null && !city.equals("")) {
                        cityName = city;
                        //Log.i("got_city", cityName);
                    } else {
                        llSearchEdittxt.setText("");
                        Toast.makeText(this, "City not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    public void getLatLong(String cityName) {
        String url = "http://api.openweathermap.org/geo/1.0/direct?q=" + cityName + "&limit=1&appid=de71050760ff841915ade960f4a58fac";
        Log.i("check_url", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() == 0) {
                        tvcityname.setText("");
                        Toast.makeText(MainActivity.this, "Couldn't Find City", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        String city = jsonObject.getString("name");
                        Double lati = jsonObject.getDouble("lat");
                        Double longi = jsonObject.getDouble("lon");
                        llSearchEdittxt.setText("");
                        getWeather(lati, longi, city);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //Log.d("latlong_response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    SetNoInternet(true);
                } else if (error instanceof ParseError) {
                    llSearchEdittxt.setText("");
                    Toast.makeText(MainActivity.this, "Couldn't Find City", Toast.LENGTH_SHORT).show();
                }
                error.printStackTrace();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void getWeather(Double lat, Double longi, String cityname) {
        String url = "https://api.openweathermap.org/data/2.5/onecall?lat=" + lat + "&" + "lon=" + longi + "&units=metric&exclude=hourly,minutely&appid=de71050760ff841915ade960f4a58fac";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                initalloading.setVisibility(View.GONE);
                constraintLayout.setVisibility(View.VISIBLE);
                try {
                    WeatherRVmodalArrayList.clear();
                    DecimalFormat decimalFormat = new DecimalFormat("0");
                    Double temp = response.getJSONObject("current").getDouble("temp");
                    tvcityname.setText(cityname);
                    String timezone = response.getString("timezone");

                    String dateinfo = dateAndTime(timezone);
                    day = getdaynum(dateinfo);
                    String value = String.valueOf(day);
                    Log.d("check day", value);
                    Double Temp_feels_like = response.getJSONObject("current").getDouble("feels_like");
                    JSONArray currentArray = response.getJSONObject("current").getJSONArray("weather");
                    JSONObject currentWeatherObject = currentArray.getJSONObject(0);
                    String condition = currentWeatherObject.getString("description");
                    String Wea_Icon_url = "https://openweathermap.org/img/wn/" + currentWeatherObject.getString("icon") + "@2x.png";
                    tvcurrwea.setText(condition);
                    tvtemp.setText(decimalFormat.format(temp) + "°C");
                    tvTempFeelslike.setText("Feels Like " + decimalFormat.format(Temp_feels_like) + "°C");
                    Picasso.get().load(Wea_Icon_url).into(ivtempicon);
                    JSONArray rvDailyArray = response.getJSONArray("daily");
                    int count = 0;
                    String dayName = "";
                    for (int i = 0; i < rvDailyArray.length(); i++) {
                        JSONObject dailyObject = rvDailyArray.getJSONObject(i);
                        if (i == 0) {
                            dayName = "Today";
                        } else if ((day + i) > DaysArray.length - 1) {
                            dayName = DaysArray[count];
                            count++;
                        } else {
                            dayName = DaysArray[day + i];
                        }
                        Double maxTemp = dailyObject.getJSONObject("temp").getDouble("max");
                        Double minTemp = dailyObject.getJSONObject("temp").getDouble("min");
                        JSONArray jsonArray = dailyObject.getJSONArray("weather");
                        String conditionIcon = jsonArray.getJSONObject(0).getString("icon");
                        String description = jsonArray.getJSONObject(0).getString("description");
                        WeatherRVmodalArrayList.add(new WeatherRVmodal(dayName, decimalFormat.format(maxTemp), decimalFormat.format(minTemp), conditionIcon, description));
                    }
                    initRecyclerView();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError) {
                    SetNoInternet(false);
                } else if (error instanceof ParseError) {
                    tvcityname.setText("");
                    Toast.makeText(MainActivity.this, "Enter Valid City Name", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

    public String dateAndTime(String timezone) {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("E',' dd/MM/yy hh:mm a");
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        String today = dateFormat.format(date);
        today = today.replace("am", "AM");
        today = today.replace("pm", "PM");
        tvDateAndTime.setText(today);
        final String substring = today.substring(0, 3);
        return substring;
    }

    public int getdaynum(String day) {
        switch (day) {
            case "Mon": {
                return 0;
            }
            case "Tue": {
                return 1;
            }
            case "Wed": {
                return 2;
            }
            case "Thu": {
                return 3;
            }
            case "Fri": {
                return 4;
            }
            case "Sat": {
                return 5;
            }
            case "Sun": {
                return 6;
            }
            default:
                return -1;
        }
    }

    public void initRecyclerView() {
        recyclerView = findViewById(R.id.RVforecastcards);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WeatherRVadapter(WeatherRVmodalArrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void SetNoInternet(boolean flag) {
        if (flag == true) constraintLayout.setVisibility(View.GONE);
        initalloading.setVisibility(View.GONE);
        NoInternetlayout.setVisibility(View.VISIBLE);
    }
}