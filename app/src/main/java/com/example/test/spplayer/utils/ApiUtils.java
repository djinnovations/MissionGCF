package com.example.test.spplayer.utils;

import com.example.test.spplayer.interfaces.SCService;

/**
 * Created by parichit on 3/11/2018.
 */

public class ApiUtils {

    // Creating Retrofit service instances

    private static final SCService service = RetrofitClient.getClient().create(SCService.class);

    public static SCService getService() {
        return service;
    }
}
