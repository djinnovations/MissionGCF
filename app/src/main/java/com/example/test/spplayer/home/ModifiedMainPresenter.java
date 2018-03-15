package com.example.test.spplayer.home;

import com.example.test.spplayer.interfaces.ProgressUpdateView;
import com.example.test.spplayer.models.Track;
import com.example.test.spplayer.network.NetworkError;
import com.example.test.spplayer.network.RestPrimaryService;

import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by User on 15-03-2018.
 */

public class ModifiedMainPresenter {

    private final RestPrimaryService service;
    private final ProgressUpdateView view;
    private CompositeSubscription subscriptions;

    public ModifiedMainPresenter(RestPrimaryService service, ProgressUpdateView view) {
        this.service = service;
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }

    public void getTrackList(String date) {
        view.startProgress();

        Subscription subscription = service.getTrackList(new RestPrimaryService.GetTrackListCallback() {
            @Override
            public void onSuccess(List<Track> cityListResponse) {
                view.stopProgress();
                view.serverCallEnd(cityListResponse);
            }

            @Override
            public void onError(NetworkError networkError) {
                view.stopProgress();
                view.setErrMsg(networkError.getAppErrorMessage());
            }

        }, date);

        subscriptions.add(subscription);
    }

    public void onStop() {
        subscriptions.unsubscribe();
    }
}
