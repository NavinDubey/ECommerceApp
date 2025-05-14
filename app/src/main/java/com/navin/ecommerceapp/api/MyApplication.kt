package com.navin.ecommerceapp.api

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


/**
 * Application class used for initializing Dagger Hilt.
 * This class is annotated with @HiltAndroidApp to trigger Hilt code generation.
 */
@HiltAndroidApp
class MyApplication : Application() {
    /**
     * This method is called when the application is created.
     * It can be used to perform any necessary initialization.
     */
    override fun onCreate() {
        super.onCreate()
    }
}

