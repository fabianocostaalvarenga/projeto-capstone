package com.fca.agenda.integration;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Status;

/**
 * Created by fabiano.alvarenga on 15/04/18.
 */

public interface GoogleSignInCallback {
    void loginSuccessFull(GoogleSignInAccount googleSignInAccount);
    void loginFailed(Status status);
}
