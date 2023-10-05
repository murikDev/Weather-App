package com.example.javalesson;

import static com.example.javalesson.utils.NerworkUtils.generateUrl;
import static com.example.javalesson.utils.NerworkUtils.getResponseFromUrl;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MainActivity extends AppCompatActivity {
    EditText inputText;
    TextView city;
    TextView temp;
    TextView description;
    TextView errorMessage;
    Button searchButton;
    ProgressBar loading;

//    class WeatherQueryTask extends AsyncTask<URL, Void, String> {
//        @Override
//        protected String doInBackground(URL... urls) {
//            String response = null;
//            try {
//                response = getResponseFromUrl(urls[0]);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            return response;
//        }
//        @Override
//        protected void onPostExecute(String response) {
//            String currentTemp;
//            String cityName;
//            String weatherDescription;
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                currentTemp = (int) (jsonObject.getJSONObject("main").getDouble("temp") - 273.15) +"°";
//                cityName = jsonObject.getString("name");
//                weatherDescription = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
//                System.out.println("----> " + description);
//
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//            temp.setText(currentTemp);
//            city.setText(cityName);
//            description.setText(weatherDescription);
//            inputText.getText().clear();
//        }
//    }
    private void showResult() {
        errorMessage.setVisibility(View.INVISIBLE);
        temp.setVisibility(View.VISIBLE);
        city.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        errorMessage.setVisibility(View.VISIBLE);
        temp.setVisibility(View.INVISIBLE);
        city.setVisibility(View.INVISIBLE);
        description.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.tv_city);
        inputText = findViewById(R.id.input_city);
        temp = findViewById(R.id.tv_temp);
        description = findViewById(R.id.tv_description);
        searchButton = findViewById(R.id.btn_search);
        errorMessage = findViewById(R.id.tv_error_message);
        loading = findViewById(R.id.pb_loading);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        searchButton.setOnClickListener(view -> {

            URL url = generateUrl(inputText.getText().toString());
            final String[] response = {null};

            loading.setVisibility(View.VISIBLE);

            executorService.execute(() -> {

                try {
                    response[0] = getResponseFromUrl(url);
                } catch (IOException e) {
                    response[0] = "City not found!";
                }

                handler.post(() -> {
                    if (response[0] != null && !response[0].equals("")) {
                        if (response[0].equals("City not found!")) {
                            errorMessage.setText(R.string.city_not_found);
                            showErrorMessage();
                            inputText.getText().clear();
                        } else {
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = new JSONObject(response[0]);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            String currentTemp;
                            String cityName;
                            String weatherDescription;
                            try {
                                currentTemp = (int) (jsonObject.getJSONObject("main").getDouble("temp") - 273.15) +"°";
                                cityName = jsonObject.getString("name");
                                weatherDescription = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            temp.setText(currentTemp);
                            city.setText(cityName);
                            description.setText(weatherDescription);
                            inputText.getText().clear();

                            showResult();
                        }
                    } else {
                        inputText.getText().clear();
                        showErrorMessage();
                    }
                    loading.setVisibility(View.INVISIBLE);
                });
            });
        });
    }
}

/*

{
    "coord":{
        "lon":61.8303,
        "lat":37.5938
    },
    "weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03d"}],
    "base":"stations",
    "main":{
        "temp":310.71,
        "feels_like":309.45,
        "temp_min":310.71,
        "temp_max":310.71,
        "pressure":1007,
        "humidity":21,
        "sea_level":1007,
        "grnd_level":982
    },
    "visibility":10000,
    "wind":{"speed":8.19,"deg":353,"gust":7.34},
    "clouds":{"all":49},
    "dt":1689674703,
    "sys":{"country":"TM","sunrise":1689641086,"sunset":1689693152},
    "timezone":18000,
    "id":1218667,
    "name":"Mary",
    "cod":200
}
 */