package com.example.test.spplayer.interfaces;

import com.example.test.spplayer.Config;
import com.example.test.spplayer.models.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by parichit on 7/10/2017.
 */

public interface SCService {
    @GET("/tracks?client_id=" + Config.CLIENT_ID)
    Call<List<Track>> getRecentTracks(@Query("created_at") String date);
}
