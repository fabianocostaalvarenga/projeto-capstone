package com.fca.agenda;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.fca.agenda.databinding.ActivityMainBinding;
import com.fca.agenda.dto.CommunicationDTO;
import com.fca.agenda.dto.MainResponseDTO;
import com.fca.agenda.service.AttachmentService;
import com.fca.agenda.service.CommunicationService;
import com.fca.agenda.utils.ApplicationConstants;
import com.fca.agenda.utils.PermissionCheck;
import com.fca.agenda.widget.CommunicationWidgetProvider;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivityHelper
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ActivityMainBinding activityMainBinding;

    private NavigationView navigationView;

    private MainResponseDTO mainResponseDTO;

    @Override
    public void bind(View view) {
        super.bind(view);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        startGoogleSigIn();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = activityMainBinding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        headerUpdateUI(googleSignIn.getLastSignedAccountInformation());

        bind(findViewById(R.id.content_main));

        if(null == savedInstanceState) {
            requestListCommunicationByLoggedUser();
        }
    }

    @Override
    protected void onStart() {
        if(null != mainResponseDTO) {
            updateRecycleView(mainResponseDTO.getListCommunication());
        }
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ApplicationConstants.MAIN_RESPONSE, this.mainResponseDTO);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        this.mainResponseDTO = savedInstanceState.getParcelable(ApplicationConstants.MAIN_RESPONSE);
        updateRecycleView(mainResponseDTO.getListCommunication());
    }

    private void requestListCommunicationByLoggedUser() {
        requestListCommunication(googleSignIn.getLastSignedAccountInformation(),
                new ApiResult() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        mainResponseDTO = (MainResponseDTO) response.body();
                        updateRecycleView(mainResponseDTO.getListCommunication());
                    }
                });
    }

    private void updateRecycleView(List<CommunicationDTO> communicationDTOS) {
        CommunicationService.updateStatus(this, communicationDTOS);
        prepareRecycleView(communicationDTOS);
    }

    @Override
    public void loginSuccessFull(GoogleSignInAccount googleSignInAccount) {
        headerUpdateUI(googleSignInAccount);
        requestListCommunicationByLoggedUser();
        Snackbar.make(findViewById(R.id.content_main), getString(R.string.welcome_login) + " " + googleSignInAccount.getDisplayName(), Snackbar.LENGTH_LONG).show();
        CommunicationWidgetProvider.writeEmailInCharedPreference(MainActivity.this, googleSignInAccount.getEmail());
    }

    @Override
    public void loginFailed(Status status) {
        headerUpdateUI(null);
        Snackbar.make(findViewById(R.id.content_main), getString(R.string.bad_login), Snackbar.LENGTH_LONG).show();
        CommunicationWidgetProvider.writeEmailInCharedPreference(MainActivity.this, null);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_filter_all:
                Snackbar.make(findViewById(R.id.content_main), getString(R.string.filter_all_message), Snackbar.LENGTH_SHORT).show();
                updateRecycleView(CommunicationService.filterBy(mainResponseDTO, CommunicationService.FILTER_BY_ALL));
                break;
            case R.id.action_filter_by_favorite:
                Snackbar.make(findViewById(R.id.content_main), getString(R.string.filter_favorite_message), Snackbar.LENGTH_SHORT).show();
                updateRecycleView(CommunicationService.filterBy(mainResponseDTO, CommunicationService.FILTER_BY_FAVORITE));
                break;
            case R.id.action_filter_by_read:
                Snackbar.make(findViewById(R.id.content_main), getString(R.string.filter_read_message), Snackbar.LENGTH_SHORT).show();
                updateRecycleView(CommunicationService.filterBy(mainResponseDTO, CommunicationService.FILTER_BY_CHECKED));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final int itemId = item.getItemId();
        switch (itemId) {
            case R.id.nav_login:
                if (null != googleSignIn.getLastSignedAccountInformation()) {
                    googleSignIn.signOut(new ResultCallback() {
                        @Override
                        public void onResult(@NonNull Result result) {
                            updateRecycleView(new ArrayList<CommunicationDTO>());
                            mainResponseDTO = null;
                            Snackbar.make(findViewById(R.id.content_main), getString(R.string.sign_out), Snackbar.LENGTH_SHORT).show();
                            CommunicationWidgetProvider.writeEmailInCharedPreference(MainActivity.this, null);
                        }
                    });
                } else {
                    googleSignIn.signIn();
                }
                headerUpdateUI(googleSignIn.getLastSignedAccountInformation());
                break;
            case R.id.nav_general_orientation:
                downloadFile(mainResponseDTO, ApplicationConstants.AttachmentService.GENERAL_ORIENTATION);
                break;
            case R.id.nav_calendar:
                Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
                builder.appendPath("time");
                ContentUris.appendId(builder, System.currentTimeMillis());
                Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
                startActivity(intent);
                break;
            case R.id.nav_scheduler:
                downloadFile(mainResponseDTO, ApplicationConstants.AttachmentService.SCHOOL_HOURS);
                break;
            case R.id.nav_personal_Data:
                launchIntentPersonalDataActivity();
                break;
            case R.id.nav_about:
                openAboutScreen();
                break;
        }

        activityMainBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void downloadFile(MainResponseDTO mainResponseDTO, String typeDoc) {

        if(null == googleSignIn.getLastSignedAccountInformation()) {
            Snackbar.make(findViewById(R.id.content_main), getString(R.string.user_logged_out), Snackbar.LENGTH_LONG).show();
            return;
        }

        if(PermissionCheck.readAndWriteExternalStorage(this)) {
            if (null != mainResponseDTO && null != mainResponseDTO.getGeneralOrientationFileId()
                    && typeDoc.equalsIgnoreCase(ApplicationConstants.AttachmentService.GENERAL_ORIENTATION)) {
                AttachmentService.downloadFile(this, mainResponseDTO.getGeneralOrientationFileId());
            } else if (null != mainResponseDTO && null != mainResponseDTO.getSchoolHours()
                    && typeDoc.equalsIgnoreCase(ApplicationConstants.AttachmentService.SCHOOL_HOURS)) {
                AttachmentService.downloadFile(this, mainResponseDTO.getSchoolHours());
            } else {
                Snackbar.make(findViewById(R.id.content_main), getString(R.string.file_id_google_for_download_invalid), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case ApplicationConstants.GoogleSignInClient.RC_SIGN_IN:
                googleSignIn.proceedSigIn(data);
                break;
        }
    }

    private void headerUpdateUI(GoogleSignInAccount account) {
        String username = getString(R.string.unknown_user_message);
        String email = getString(R.string.unknown_user_message_instruct);
        String titleSignInAction = getString(R.string.drawer_item_login);

        Glide.with(this)
                .load(R.drawable.ic_avatar)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(((ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_avatar)));

        if (null != account) {
            username = account.getDisplayName();
            email = account.getEmail();
            titleSignInAction = getString(R.string.drawer_item_logout);

            if (null != account.getPhotoUrl()) {
                Glide.with(this)
                        .load(account.getPhotoUrl())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                        .into(((ImageView) navigationView.getHeaderView(0).findViewById(R.id.iv_avatar)));
            }
        }

        navigationView.getMenu().getItem(0).setTitle(titleSignInAction);

        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_username)).setText(username);
        ((TextView) navigationView.getHeaderView(0).findViewById(R.id.tv_email)).setText(email);

    }

    private void prepareRecycleView(final List<CommunicationDTO> communicationDTOS) {

        final GridLayoutManager layout = new GridLayoutManager(this, 1);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_communication);
        recyclerView.setLayoutManager(layout);

        recyclerView.setAdapter(new CommunicationAdapter(this, communicationDTOS, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                launchIntentCommunicationActivityList(communicationDTOS.get(position));
            }
        }));
    }

    private void launchIntentCommunicationActivityList(CommunicationDTO communicationDTO) {
        Intent launchIntentScheduleActivityList = new Intent(this, ScheduleActivity.class);
        launchIntentScheduleActivityList.putExtra(ApplicationConstants.COMMUNICATION, communicationDTO);
        startActivity(launchIntentScheduleActivityList);
    }

    private void launchIntentPersonalDataActivity() {
        if(null != googleSignIn.getLastSignedAccountInformation()) {
            Intent launchIntentPersonalDataActivity = new Intent(this, PersonalDataActivity.class);
            startActivity(launchIntentPersonalDataActivity);
        } else {
            Snackbar.make(findViewById(R.id.content_main), getString(R.string.user_logged_out), Snackbar.LENGTH_LONG).show();
        }
    }

    private void openAboutScreen() {
        AlertDialog.Builder customdialog = new AlertDialog.Builder(MainActivity.this);
        customdialog.setView(getLayoutInflater().inflate(R.layout.about, null));

        customdialog.create().show();
    }

}
