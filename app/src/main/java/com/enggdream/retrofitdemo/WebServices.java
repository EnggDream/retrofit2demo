package com.enggdream.retrofitdemo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by PareshDudhat on 21-01-2017.
 */

interface WebServices {
    @GET("users/{username}")
    Call<User> getUser(@Path("username") String username);

}
