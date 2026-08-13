package com.example.workpulse.core.network

import com.example.workpulse.data.remote.AttendanceApi
import com.example.workpulse.data.remote.AttendanceRequestApi
import com.example.workpulse.data.remote.AuthApi
import com.example.workpulse.data.remote.CompOffApi
import com.example.workpulse.data.remote.LeaveApi

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cookieManager: CookieManager,
        loggingIneterceptor : HttpLoggingInterceptor
    ) : OkHttpClient{

        return OkHttpClient.Builder()
            .cookieJar(cookieManager)
            .addInterceptor(loggingIneterceptor)
            .connectTimeout(
                NetworkConstants.CONNECT_TIMEOUT,
                TimeUnit.SECONDS
            )
            .readTimeout(
                NetworkConstants.READ_TIME,
                TimeUnit.SECONDS
            )
            .writeTimeout(
                NetworkConstants.WRITE_TIME,
                TimeUnit.SECONDS
            )
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit{
        return Retrofit.Builder()
            .baseUrl(NetworkConstants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthApi(
        retrofit: Retrofit
    ): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAttendanceApi(
        retrofit: Retrofit
    ) : AttendanceApi {
        return  retrofit.create(AttendanceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLeaveApi(
        retrofit: Retrofit
    ): LeaveApi {

        return retrofit.create(LeaveApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAttendanceRequestApi(
        retrofit: Retrofit
    ): AttendanceRequestApi{
        return  retrofit.create(AttendanceRequestApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCompOffApplicationApi(
        retrofit: Retrofit
    ): CompOffApi{
        return retrofit.create(CompOffApi::class.java)
    }
}

