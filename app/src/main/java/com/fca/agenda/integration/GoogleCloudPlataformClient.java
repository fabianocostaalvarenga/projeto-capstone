package com.fca.agenda.integration;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

import com.fca.agenda.R;
import com.fca.agenda.dto.CommunicationDTO;
import com.fca.agenda.dto.MainResponseDTO;
import com.fca.agenda.dto.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by fabiano.alvarenga on 10/22/17.
 */

public class GoogleCloudPlataformClient {

    private static final String TAG = GoogleCloudPlataformClient.class.getSimpleName();

    private static Retrofit retrofit = null;

    /**
     * Method responsible for verifying device connectivity to the internet.
     *
     * @param context
     * @return
     */
    public static Boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Retrofit getClientInstance(final @NonNull Context context) {

        retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.google_cloud_platform_api))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public interface GCPApiInterface {

        @GET("communication/{email}/email")
        Call<MainResponseDTO> listCommunicationByEmail(@Path("email") String email);

        @GET("personal-data/{email}/email")
        Call<UserDTO> getUserByEmail(@Path("email") String email);

    }


}
