package com.fca.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fca.agenda.dto.StudantDTO;
import com.fca.agenda.dto.UserDTO;
import com.fca.agenda.utils.ApplicationConstants;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Status;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PersonalDataActivity extends AppCompatActivityHelper {

    private static final int COLUMN = 1;
    private EditText responsableName;
    private EditText email;
    private EditText mobilePhone;
    private EditText coResponsableName;
    private EditText coMobilePhone;

    @Override
    public void bind(View view) {
        super.bind(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_personal_data);
        setSupportActionBar(toolbar);

        showStateView(null);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        startGoogleSigIn();

        requestUserDTOByLoggedUser();

        bind(findViewById(R.id.personal_data));

    }

    private void requestUserDTOByLoggedUser() {
        requestUserData(googleSignIn.getLastSignedAccountInformation(),
                new ApiResult() {
                    @Override
                    public void onResponse(Call call, Response response) {

                        UserDTO userDTO = (UserDTO) response.body();

                        showStateView(userDTO);

                        email = (EditText) findViewById(R.id.email);
                        email.setText(userDTO.getEmail());

                        responsableName = (EditText) findViewById(R.id.responsableName);
                        responsableName.setText(userDTO.getResponsableName());

                        mobilePhone = (EditText) findViewById(R.id.mobilePhone);
                        mobilePhone.setText(userDTO.getMobilePhone());

                        coResponsableName = (EditText) findViewById(R.id.coResponsableName);
                        coResponsableName.setText(userDTO.getCoResponsableName());

                        coMobilePhone = (EditText) findViewById(R.id.coMobilePhone);
                        coMobilePhone.setText(userDTO.getCoMobilePhone());

                        email.clearFocus();

                        prepareRecycleView(userDTO.getStudantDTOs());
                    }
                });
    }

    private void showStateView(UserDTO userDTO) {
        if(null != userDTO && null != userDTO.getEmail() && null != userDTO.getResponsableName()) {

            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.personal_data);
            linearLayout.setVisibility(View.VISIBLE);
            TextView textView = (TextView) findViewById(R.id.no_data_found);
            textView.setVisibility(View.GONE);

        } else {

            TextView textView = (TextView) findViewById(R.id.no_data_found);
            textView.setVisibility(View.VISIBLE);
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.personal_data);
            linearLayout.setVisibility(View.GONE);

        }
    }

    private void prepareRecycleView(final List<StudantDTO> studantDTOS) {

        final GridLayoutManager layout = new GridLayoutManager(this, COLUMN);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_personal_data);
        recyclerView.setLayoutManager(layout);

        recyclerView.setAdapter(new PersonalDataAdapter(this, studantDTOS, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                launchIntentStudantActivityView(studantDTOS.get(position));
            }
        }));
    }

    private void launchIntentStudantActivityView(StudantDTO studantDTO) {
        Intent launchIntentScheduleActivityList = new Intent(this, StudantDataActivity.class);
        launchIntentScheduleActivityList.putExtra(ApplicationConstants.STUDANT, studantDTO);
        startActivity(launchIntentScheduleActivityList);
    }

    @Override
    public void loginSuccessFull(GoogleSignInAccount googleSignInAccount) {

    }

    @Override
    public void loginFailed(Status status) {

    }

}
