package com.example.test.spplayer.network;

import com.example.test.spplayer.models.Track;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by User on 15-03-2018.
 */

public class RestPrimaryService {


    private final NetworkService networkService;

    public RestPrimaryService(NetworkService networkService) {
        this.networkService = networkService;
    }

    public Subscription getTrackList(final GetTrackListCallback callback, String date) {

        return networkService.getRecentTracks(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends List<Track>>>() {
                    @Override
                    public Observable<? extends List<Track>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<List<Track>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(List<Track> trackListResponse) {
                        callback.onSuccess(trackListResponse);

                    }
                });
    }

    public interface GetTrackListCallback{
        void onSuccess(List<Track> trackListResponse);

        void onError(NetworkError networkError);
    }
}
