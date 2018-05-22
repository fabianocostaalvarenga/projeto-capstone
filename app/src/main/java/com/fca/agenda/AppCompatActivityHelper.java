package com.fca.agenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fca.agenda.integration.GoogleCloudPlataformClient;
import com.fca.agenda.integration.GoogleSignInCallback;
import com.fca.agenda.service.CommunicationService;
import com.fca.agenda.service.UserDataService;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fabiano.alvarenga on 06/05/18.
 */

public abstract class AppCompatActivityHelper extends AppCompatActivity
        implements GoogleSignInCallback {

    private static final String TAG = AppCompatActivityHelper.class.getSimpleName();

    public com.fca.agenda.integration.GoogleSignInClient googleSignIn;

    private ProgressDialog progressDialog = null;

    private View view;

    public void bind(final View view) {
        this.view = view;
    }

    /**
     * Method responsible for start GoogleSigInClient.
     * This Method should by used all in activity that get last logged user information.
     *
     */
    public void startGoogleSigIn() {
        googleSignIn = new com.fca.agenda.integration.GoogleSignInClient(this, this);
    }

    /**
     * Method responsible for verifying network status and requesting the calendar API returning
     * list the communication
     *
     * @param signInAccount
     * @param apiResult
     */
    public void requestListCommunication(final @NonNull GoogleSignInAccount signInAccount, final ApiResult apiResult) {
        if (GoogleCloudPlataformClient.isOnline(this)) {
            if (null != signInAccount && null != signInAccount.getEmail()) {
                showProgress();
                CommunicationService.asyncListCommunicationByLoggedUser(
                        this, signInAccount.getEmail(), new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                apiResult.onResponse(call, response);
                                hideProgress();
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_LONG).show();
                                hideProgress();
                            }
                        });
            } else {
                Snackbar.make(view, getString(R.string.user_logged_out), Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(view, getString(R.string.network_fail), Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Method responsible for verifying network status and requesting the calendar API returning
     * UserDTO
     *
     * @param signInAccount
     * @param apiResult
     */
    public void requestUserData(final @NonNull GoogleSignInAccount signInAccount, final ApiResult apiResult) {
        if (GoogleCloudPlataformClient.isOnline(this)) {
            if (null != signInAccount && null != signInAccount.getEmail()) {
                showProgress();
                UserDataService.asyncUserDTOByLoggedUser(
                        this, signInAccount.getEmail(), new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) {
                                apiResult.onResponse(call, response);
                                hideProgress();
                            }

                            @Override
                            public void onFailure(Call call, Throwable t) {
                                Snackbar.make(view, t.getMessage(), Snackbar.LENGTH_LONG).show();
                                hideProgress();
                            }
                        });
            } else {
                Snackbar.make(view, getString(R.string.user_logged_out), Snackbar.LENGTH_LONG).show();
            }
        } else {
            Snackbar.make(view, getString(R.string.network_fail), Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Method responsible for show progress dialog
     */
    public synchronized void showProgress() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }
        progressDialog = ProgressDialog.show(this, "", getString(R.string.loading_communication), true, false);
    }

    /**
     * Method responsible for hide progress dialog
     */
    public synchronized void hideProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
