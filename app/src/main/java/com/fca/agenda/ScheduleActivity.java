package com.fca.agenda;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fca.agenda.dto.CommunicationDTO;
import com.fca.agenda.dto.ImagesDTO;
import com.fca.agenda.service.AttachmentService;
import com.fca.agenda.service.CommunicationService;
import com.fca.agenda.utils.ApplicationConstants;
import com.fca.agenda.utils.DateUtils;
import com.fca.agenda.utils.PermissionCheck;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.glide.slider.library.Animations.DescriptionAnimation;
import com.glide.slider.library.SliderLayout;
import com.glide.slider.library.SliderTypes.TextSliderView;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private static final String TAG = ScheduleActivity.class.getSimpleName();

    private SliderLayout mDemoSlider;
    private CommunicationDTO dto;
    private Menu menu;
    private FloatingActionMenu materialDesignFAM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);

        mDemoSlider = findViewById(R.id.slider);

        floatButtonInitialize();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (null != intent.getExtras()) {
            dto = intent.getParcelableExtra(ApplicationConstants.COMMUNICATION);
            initializeComponent(dto);
            gliderInitialize(dto);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        updateStatusBarIcon();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.comunicattion_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_share:
                launchShareIntent(dto);
                break;
            case R.id.action_favorite:
                CommunicationService.changeFavoriteStatus(this, dto);
                updateStatusBarIcon();
                Snackbar.make(findViewById(R.id.coordinator), getString(R.string.favorite_success), Snackbar.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    private void floatButtonInitialize() {
        FloatingActionButton fbOpenPage = (FloatingActionButton) materialDesignFAM.findViewById(R.id.fb_open_page);
        fbOpenPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dto != null && dto.getPageUrl() != null && dto.getPageUrl().length() > 0) {
                    AttachmentService.pageView(ScheduleActivity.this, dto.getPageUrl());
                } else {
                    Snackbar.make(findViewById(R.id.coordinator), getString(R.string.url_for_view_invalid), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        FloatingActionButton fbAttachFile = (FloatingActionButton) materialDesignFAM.findViewById(R.id.fb_attach_file);
        fbAttachFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PermissionCheck.readAndWriteExternalStorage(ScheduleActivity.this)) {
                    if (dto != null && dto.getFileIdGoogleDrive() != null && dto.getFileIdGoogleDrive().length() > 0) {
                        AttachmentService.downloadFile(ScheduleActivity.this, dto.getFileIdGoogleDrive());
                    } else {
                        Snackbar.make(findViewById(R.id.coordinator), getString(R.string.file_id_google_for_download_invalid), Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        });

        FloatingActionButton fbCheck = (FloatingActionButton) materialDesignFAM.findViewById(R.id.fb_check);
        fbCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommunicationService.changeCheckedStatus(getBaseContext(), dto);
                Snackbar.make(findViewById(R.id.coordinator), getString(R.string.checked_success), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void initializeComponent(CommunicationDTO dto) {
        TextView title = (TextView) findViewById(R.id.tv_title);
        TextView description = (TextView) findViewById(R.id.tv_description);
        TextView date = (TextView) findViewById(R.id.tv_date);

        title.setText(dto.getTitle());
        description.setText(dto.getDescription());
        date.setText(DateUtils.dateToString(new Date(dto.getDate())));
    }

    private void launchShareIntent(CommunicationDTO dto) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, dto.getTitle());
        shareIntent.putExtra(Intent.EXTRA_TEXT, dto.getTitle() + "\n\n\n" + dto.getDescription());
        shareIntent.setType(ApplicationConstants.ScheduleActivity.CONTENT_TYPE_SHARE);
        startActivity(Intent.createChooser(shareIntent, getString(R.string.action_share_title)));
    }

    private void gliderInitialize(CommunicationDTO dto) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(R.drawable.logo);

        List<ImagesDTO> imageDTOS = dto.getImagesDTOS();

        for (ImagesDTO imageDTO : imageDTOS) {
            TextSliderView sliderView = new TextSliderView(this);

            sliderView
                    .image(imageDTO.getImgUrl())
                    .description(imageDTO.getTitleImg())
                    .setRequestOption(requestOptions)
                    .setBackgroundColor(Color.WHITE)
                    .setProgressBarVisible(true);

            //add your extra information
            sliderView.bundle(new Bundle());
            sliderView.getBundle().putString(ApplicationConstants.ScheduleActivity.EXTRA, imageDTO.getTitleImg());
            mDemoSlider.addSlider(sliderView);
        }

        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(ApplicationConstants.ScheduleActivity.DURATION_SLIDER_TRANSITION);
    }

    private void updateStatusBarIcon() {
        CommunicationService.updateStatus(this, Collections.singletonList(dto));
        if (null != dto.getFavorite() && dto.getFavorite()) {
            this.menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_favorited);
        } else {
            this.menu.findItem(R.id.action_favorite).setIcon(R.drawable.ic_un_favorite);
        }
    }

}
