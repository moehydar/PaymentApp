package com.cashi.payment.android

import android.app.Application
import com.cashi.payment.android.di.androidModule
import com.cashi.payment.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class PaymentApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PaymentApplication)
            modules(sharedModule, androidModule)
        }
    }
}