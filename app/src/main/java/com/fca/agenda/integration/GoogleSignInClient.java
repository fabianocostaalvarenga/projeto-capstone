package com.fca.agenda.integration;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.fca.agenda.R;
import com.fca.agenda.utils.ApplicationConstants;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.concurrent.Executor;

/**
 * Created by fabiano.alvarenga on 15/04/18.
 */

public class GoogleSignInClient implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = GoogleSignInClient.class.getSimpleName();

    private GoogleApiClient mGoogleApiClient;
    private com.google.android.gms.auth.api.signin.GoogleSignInClient mGoogleSignInClient;
    private AppCompatActivity activity;

    private GoogleSignInCallback callback;

    private ProgressDialog progressDialog = null;

    public GoogleSignInClient(@NonNull AppCompatActivity activity, GoogleSignInCallback callback) {

        this.activity = activity;
        this.callback = callback;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(activity.getString(R.string.apiKey))
                .requestServerAuthCode(activity.getString(R.string.apiKey))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(this.activity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        mGoogleSignInClient = com.google.android.gms.auth.api.signin.GoogleSignIn.getClient(activity, gso);
    }

    /**
     * Method responsible for returning the last logged in information
     *
     * @return GoogleSignInAccount
     */
    public GoogleSignInAccount getLastSignedAccountInformation() {
        return com.google.android.gms.auth.api.signin.GoogleSignIn.getLastSignedInAccount(this.activity);
    }

    /**
     * Method responsible for initiating a login intent
     */
    public void signIn() {
        showProgress();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        this.activity.startActivityForResult(signInIntent, ApplicationConstants.GoogleSignInClient.RC_SIGN_IN);
    }

    /**
     * Method responsible for verifying the activityForResult of the login intention previously
     * launched by the "sidIn()" method.
     *
     * @param intent
     */
    public void proceedSigIn(Intent intent) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
        if (null != result && result.isSuccess()) {
            callback.loginSuccessFull(result.getSignInAccount());
        } else {
            callback.loginFailed(null != result ? result.getStatus() : new Status(GoogleSignInStatusCodes.SIGN_IN_FAILED));
        }
        hideProgress();
    }

    /**
     * Method responsible for current user logout
     */
    public void signOut(final ResultCallback callback) {
        showProgress();
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Log.i(TAG, activity.getString(R.string.sign_out));
                hideProgress();
                callback.onResult(status);
            }
        });
    }

    /**
     * Method responsible for revoking the previously granted accesses google
     * accounts for this App.
     */
    public void revokeAccess() {
        showProgress();
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener((Executor) this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG, activity.getString(R.string.revoke_google_account));
                        hideProgress();
                    }
                });
    }

    /**
     * Method responsible for show progress dialog
     */
    private void showProgress() {
        if(progressDialog != null) {
            progressDialog.cancel();
        }
        progressDialog = ProgressDialog.show(activity, "", activity.getString(R.string.processing), true, false);
    }

    /**
     * Method responsible for hide progress dialog
     */
    private void hideProgress() {
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, connectionResult.getErrorMessage() + " - " + connectionResult.getErrorCode());
        hideProgress();
    }

}
