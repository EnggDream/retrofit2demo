package com.enggdream.retrofitdemo;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by PareshDudhat on 21-01-2017.
 */

public class RestClient {
    private static RestClient restClient;
    Retrofit retrofit;
    private WebServices webServices;
    private String baseUrl;

    private RestClient(String baseUrl) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .readTimeout(100, TimeUnit.SECONDS)
                .build();
        retrofit = new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(
                GsonConverterFactory.create(
                        new GsonBuilder()
                                .excludeFieldsWithoutExposeAnnotation()
                                .create()))
                .client(httpClient)
                .build();
        this.baseUrl = baseUrl;
        webServices = retrofit.create(WebServices.class);
    }

    public static RestClient getInstance()  {
        if (restClient == null) {
            restClient = new RestClient("https://api.github.com/");
        }
        return restClient;
    }

    public WebServices getWebServices() {
        return webServices;
    }
}
