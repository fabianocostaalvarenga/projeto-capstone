package com.fca.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.fca.agenda.dto.StudantDTO;
import com.fca.agenda.utils.ApplicationConstants;

public class StudantDataActivity extends AppCompatActivity {

    private StudantDTO dto;

    private TextView registration;
    private TextView studantName;
    private TextView birthDate;
    private TextView street;
    private TextView number;
    private TextView neighborhood;
    private TextView city;
    private TextView state;
    private TextView postalCode;
    private TextView allergies;
    private TextView bloodType;
    private TextView feverMedication;
    private TextView dosage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_studant_data);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (null != intent.getExtras()) {
            dto = intent.getParcelableExtra(ApplicationConstants.STUDANT);
            initializeComponentWithValues(dto);
        }

    }

    private void initializeComponentWithValues(StudantDTO dto) {
        registration = (TextView) findViewById(R.id.registration);
        studantName = (TextView) findViewById(R.id.studantName);
        birthDate = (TextView) findViewById(R.id.birthDate);
        street = (TextView) findViewById(R.id.street);
        number = (TextView) findViewById(R.id.number);
        neighborhood = (TextView) findViewById(R.id.neighborhood);
        city = (TextView) findViewById(R.id.city);
        state = (TextView) findViewById(R.id.state);
        postalCode = (TextView) findViewById(R.id.postalCode);
        allergies = (TextView) findViewById(R.id.allergies);
        bloodType = (TextView) findViewById(R.id.bloodType);
        feverMedication = (TextView) findViewById(R.id.feverMedication);
        dosage = (TextView) findViewById(R.id.dosage);

        registration.setText(dto.getRegistration());
        studantName.setText(dto.getName());
        birthDate.setText(dto.getBirthDateFormatted());
        street.setText(dto.getStreet());
        number.setText(dto.getNumber());
        neighborhood.setText(dto.getNeighborhood());
        city.setText(dto.getCity());
        state.setText(dto.getState());
        postalCode.setText(dto.getPostalCode());
        allergies.setText(dto.getAllergies());
        bloodType.setText(dto.getBloodType());
        feverMedication.setText(dto.getFeverMedication());
        dosage.setText(dto.getDosage());

    }
}
