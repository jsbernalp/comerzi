package co.jonathanbernal.comerzi.di

import android.content.Context
import android.util.Log
import androidx.multidex.BuildConfig
import co.jonathanbernal.comerzi.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val TIME_OUT = 6

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun gson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun retrofitCountryServices(
        @ApplicationContext context: Context,
        gson: Gson,
    ): Retrofit {
        val httpClient = getHttpClientBuilder()
        return getRetrofitBuilder(
            httpClient.build(), context.getString(R.string.base_url), gson
        ).build()
    }

    private fun getRetrofitBuilder(
        httpClient: OkHttpClient,
        url: String,
        gson: Gson
    ): Retrofit.Builder =
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create(gson))

    private fun getHttpClientBuilder(): OkHttpClient.Builder {
        val clientBuilder = OkHttpClient.Builder()
            .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(logging)
            Log.e(this::class.simpleName, clientBuilder.toString())
        }

        return clientBuilder
    }

}