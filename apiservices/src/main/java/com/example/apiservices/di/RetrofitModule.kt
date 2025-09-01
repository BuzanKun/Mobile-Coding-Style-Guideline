package com.example.apiservices.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.apiservices.data.source.network.services.SupplierApi
import com.example.apiservices.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideSupplyApi(@ApiServicesClient okHttpClient: OkHttpClient): SupplierApi {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SupplierApi::class.java)
    }

    @Provides
    @Singleton
    fun provideConnectivityInterceptor(@ApplicationContext context: Context): Interceptor =
        Interceptor { chain ->
            if (!isNetworkAvalaible(context)) {
                throw NoConnectivityException("No internet exception")
            }
            chain.proceed(chain.request())
        }

    @ApiServicesClient
    @Provides
    @Singleton
    fun provideOkHttpClient(connectivityInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, java.util.concurrent.TimeUnit.MINUTES)
            .readTimeout(10, java.util.concurrent.TimeUnit.MINUTES)
            .writeTimeout(10, java.util.concurrent.TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private fun isNetworkAvalaible(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    class NoConnectivityException(message: String) : IOException(message)
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApiServicesClient