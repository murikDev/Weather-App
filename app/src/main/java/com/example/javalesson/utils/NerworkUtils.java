package com.example.javalesson.utils;

import android.net.Uri;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class NerworkUtils {

    private static final String OPENWEATHER_API_BASE_URL = "https://api.openweathermap.org";
    private static final String METHOD = "/data/2.5/weather";
    private static final String API_KEY = "1a93420bd5a86143429d615ab4ce860c";
    private static final String CITY_PARAM = "q";
    private static final String API_KEY_PARAM = "appid";
    private static final String LANGUAGE_PARAM = "lang";
    private static final String LANGUAGE = "ru";

    public static URL generateUrl(String city) {
        Uri builtUri = Uri.parse(OPENWEATHER_API_BASE_URL+METHOD)
                .buildUpon()
                .appendQueryParameter(CITY_PARAM, city)
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(LANGUAGE_PARAM, LANGUAGE)
                .build();

        try {
            return new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getResponseFromUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        }catch (UnknownHostException e) {
            return null;
        }finally {
            urlConnection.disconnect();
        }
    }

}
