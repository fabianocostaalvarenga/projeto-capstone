package com.fca.agenda;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.fca.agenda.dto.StudantDTO;
import com.fca.agenda.utils.ApplicationConstants;

public class StudantDataActivity extends AppCompatActivity {

    private StudantDTO dto;

    private EditText registration;
    private EditText studantName;
    private EditText birthDate;
    private EditText street;
    private EditText number;
    private EditText neighborhood;
    private EditText city;
    private EditText state;
    private EditText postalCode;
    private EditText allergies;
    private EditText bloodType;
    private EditText feverMedication;
    private EditText dosage;

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
        registration = (EditText) findViewById(R.id.registration);
        studantName = (EditText) findViewById(R.id.studantName);
        birthDate = (EditText) findViewById(R.id.birthDate);
        street = (EditText) findViewById(R.id.street);
        number = (EditText) findViewById(R.id.number);
        neighborhood = (EditText) findViewById(R.id.neighborhood);
        city = (EditText) findViewById(R.id.city);
        state = (EditText) findViewById(R.id.state);
        postalCode = (EditText) findViewById(R.id.postalCode);
        allergies = (EditText) findViewById(R.id.allergies);
        bloodType = (EditText) findViewById(R.id.bloodType);
        feverMedication = (EditText) findViewById(R.id.feverMedication);
        dosage = (EditText) findViewById(R.id.dosage);

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
