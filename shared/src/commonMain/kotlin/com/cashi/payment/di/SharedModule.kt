package com.cashi.payment.di

import com.cashi.payment.network.PaymentApiClient
import org.koin.dsl.module

val sharedModule = module {
    single { PaymentApiClient() }
}