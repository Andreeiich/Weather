package com.example.weather.AppModule;

import com.example.weather.MainActivity;
import com.example.weather.MainPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component
public interface AppModule {
    MainPresenter mainPresenter();

}
