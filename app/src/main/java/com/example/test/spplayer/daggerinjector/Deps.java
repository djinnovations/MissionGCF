package com.example.test.spplayer.daggerinjector;



import com.example.test.spplayer.home.ModifiedMainActivity;
import com.example.test.spplayer.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by DJphy on 15/03/18.
 */
@Singleton
@Component(modules = {NetworkModule.class,})
public interface Deps {
    void inject(ModifiedMainActivity homeActivity);
}
