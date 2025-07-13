package com.cashi.payment.android.di

import com.cashi.payment.android.data.FirebasePaymentStore
import com.cashi.payment.android.viewmodel.PaymentViewModel
import com.cashi.payment.android.viewmodel.TransactionHistoryViewModel
import com.cashi.payment.repository.PaymentRepository
import com.cashi.payment.repository.PaymentStore
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule = module {

    single<PaymentStore> { FirebasePaymentStore() }
    single { PaymentRepository(get(), get()) }

    viewModel { PaymentViewModel(get()) }
    viewModel { TransactionHistoryViewModel(get()) }
}