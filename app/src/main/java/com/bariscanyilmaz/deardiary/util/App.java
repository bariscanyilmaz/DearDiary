package com.bariscanyilmaz.deardiary.util;

import android.app.Application;
import android.content.Context;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

public class App {

    private static AppCompatActivity activity;

    public static AppCompatActivity getActivity() {
        return activity;
    }

    public static void setActivity(AppCompatActivity ac) {
     activity = ac;
    }
}
