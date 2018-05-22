package com.fca.agenda.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fca.agenda.dto.CommunicationDTO;
import com.fca.agenda.dto.UserDTO;
import com.fca.agenda.integration.GoogleCloudPlataformClient;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fabiano.alvarenga on 09/05/18.
 */

public class UserDataService {

    private static final String TAG = UserDataService.class.getSimpleName();

    private static UserDTO userDTO;

    /**
     * Method responsible for asyncronous requesting the Calendar API in the GCP to retrieve the
     * UserDTO using as parameter the email of the logged in user
     *
     * @param context
     * @param email
     * @param callback
     */
    public static void asyncUserDTOByLoggedUser(@NonNull Context context, String email, final Callback callback) {
        GoogleCloudPlataformClient.GCPApiInterface apiInterface
                = GoogleCloudPlataformClient.getClientInstance(context).create(GoogleCloudPlataformClient.GCPApiInterface.class);

        Call<UserDTO> call = apiInterface.getUserByEmail(email);

        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });

    }

    /**
     * Method responsible for syncronous requesting the Calendar API in the GCP to retrieve the
     * UserDTO using as parameter the email of the logged in user
     *
     * @param context
     * @param email
     * @return
     */
    public static UserDTO syncUserDTOByLoggedUser(@NonNull Context context, String email) {
        UserDTO UserDTO = null;

        GoogleCloudPlataformClient.GCPApiInterface apiInterface
                = GoogleCloudPlataformClient.getClientInstance(context).create(GoogleCloudPlataformClient.GCPApiInterface.class);

        Call<UserDTO> call = apiInterface.getUserByEmail(email);

        try {
            UserDTO = call.execute().body();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return UserDTO;
    }
}
