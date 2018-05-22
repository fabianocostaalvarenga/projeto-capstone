package com.fca.agenda.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fca.agenda.dao.CommunicationDAO;
import com.fca.agenda.dto.CommunicationDTO;
import com.fca.agenda.dto.MainResponseDTO;
import com.fca.agenda.integration.GoogleCloudPlataformClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fabiano.alvarenga on 21/04/18.
 */

public class CommunicationService {

    private static final String TAG = CommunicationService.class.getSimpleName();

    public static final String FILTER_BY_ALL = "all";
    public static final String FILTER_BY_FAVORITE = "favorite";
    public static final String FILTER_BY_CHECKED = "checked";

    private static CommunicationDAO dao;
    private static CommunicationDTO communicationDTO;

    /**
     * Method responsible for asyncronous requesting the Calendar API in the GCP to retrieve the list of
     * communiqués using as parameter the email of the logged in user
     *
     * @param context
     * @param email
     * @param callback
     */
    public static void asyncListCommunicationByLoggedUser(@NonNull Context context, String email, final Callback callback) {
        GoogleCloudPlataformClient.GCPApiInterface apiInterface
                = GoogleCloudPlataformClient.getClientInstance(context).create(GoogleCloudPlataformClient.GCPApiInterface.class);

        Call<MainResponseDTO> call = apiInterface.listCommunicationByEmail(email);

        call.enqueue(new Callback<MainResponseDTO>() {
            @Override
            public void onResponse(Call<MainResponseDTO> call, Response<MainResponseDTO> response) {
                callback.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<MainResponseDTO> call, Throwable t) {
                callback.onFailure(call, t);
            }
        });

    }

    /**
     * Method responsible for syncronous requesting the Calendar API in the GCP to retrieve the list of
     * communiqués using as parameter the email of the logged in user
     *
     * @param context
     * @param email
     * @return
     */
    public static MainResponseDTO syncListCommunicationByLoggedUser(@NonNull Context context, String email) {
        MainResponseDTO mainResponseDTO = null;

        GoogleCloudPlataformClient.GCPApiInterface apiInterface
                = GoogleCloudPlataformClient.getClientInstance(context).create(GoogleCloudPlataformClient.GCPApiInterface.class);

        Call<MainResponseDTO> call = apiInterface.listCommunicationByEmail(email);

        try {
            mainResponseDTO = call.execute().body();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        return mainResponseDTO;
    }

    /**
     * Method responsible for updating the status of the
     * favorite icon and read icon in collection received from server.
     *
     * @param ctx
     * @param dtos
     */
    public static void updateStatus(@NonNull Context ctx, List<CommunicationDTO> dtos) {

        if (null == dtos) {
            return;
        }

        dao = new CommunicationDAO(ctx);
        for (CommunicationDTO dto : dtos) {
            communicationDTO = dao.findOne(dto.getId());
            if (communicationDTO != null) {
                dto.setChecked(communicationDTO.getChecked());
                dto.setFavorite(communicationDTO.getFavorite());
            }
        }
    }

    /**
     * Method responsible for filtering messages per action
     *
     * @param mainResponseDTO
     * @param filter
     * @return
     */
    public static List<CommunicationDTO> filterBy(MainResponseDTO mainResponseDTO, String filter) {

        List<CommunicationDTO> dtosResult = new ArrayList<>();

        if (null == mainResponseDTO || null == mainResponseDTO.getListCommunication() || mainResponseDTO.getListCommunication().isEmpty()) {
            return new ArrayList<>();
        }

        if(filter.equalsIgnoreCase(FILTER_BY_ALL)) {
            return mainResponseDTO.getListCommunication();
        }

        for (CommunicationDTO dto : mainResponseDTO.getListCommunication()) {
            if(filter.equalsIgnoreCase(FILTER_BY_FAVORITE)) {
                if (dto.getFavorite() != null && dto.getFavorite()) {
                    dtosResult.add(dto);
                }
            } else if(filter.equalsIgnoreCase(FILTER_BY_CHECKED)) {
                if (dto.getFavorite() != null && dto.getChecked()) {
                    dtosResult.add(dto);
                }
            }
        }
        return dtosResult;
    }

    /**
     * Method responsible for updating the status of the favorite icon.
     *
     * @param ctx
     * @param dto
     */
    public static void changeFavoriteStatus(@NonNull Context ctx, CommunicationDTO dto) {
        dao = new CommunicationDAO(ctx);
        if (null != dto.getFavorite() && dto.getFavorite()) {
            dto.setFavorite(Boolean.FALSE);
        } else {
            dto.setFavorite(Boolean.TRUE);
        }
        identityOperation(dto);
    }

    /**
     * Method responsible for updating the status of the read icon.
     *
     * @param ctx
     * @param dto
     */
    public static void changeCheckedStatus(@NonNull Context ctx, CommunicationDTO dto) {
        dao = new CommunicationDAO(ctx);
        if (null != dto.getChecked() && dto.getChecked()) {
            dto.setChecked(Boolean.FALSE);
        } else {
            dto.setChecked(Boolean.TRUE);
        }
        identityOperation(dto);
    }

    /**
     * Method responsible for ensuring that only one message from each will be favored.
     * This prevents duplicate records from being created in SQLite.
     *
     * @param dto
     */
    private static void identityOperation(CommunicationDTO dto) {

        CommunicationDTO communicationDTO = dao.findOne(dto.getId());

        if (null != communicationDTO) {
            if (!dto.getChecked() && !dto.getFavorite()) {
                dao.delete(dto.getId());
            } else {
                dao.update(dto);
            }
        } else {
            dao.insert(dto);
        }

    }

}
