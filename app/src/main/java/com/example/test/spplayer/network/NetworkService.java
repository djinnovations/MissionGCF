package com.example.test.spplayer.network;

import com.example.test.spplayer.models.Track;
import com.example.test.spplayer.utils.Config;

import java.util.List;

import rx.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by User on 15-03-2018.
 */

public interface NetworkService {

    @GET("/tracks?client_id=" + Config.CLIENT_ID)
    Observable<List<Track>> getRecentTracks(@Query("created_at") String date);
}
