package com.fca.agenda.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.fca.agenda.utils.ApplicationConstants;
import com.fca.agenda.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 * Created by fabiano.alvarenga on 04/05/18.
 */

public class StudantDTO implements Parcelable {

    private Long id;
    private String registration;
    private String name;
    private Date birthDate;
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String postalCode;
    private String allergies;
    private String bloodType;
    private String feverMedication;
    private String dosage;

    private String birthDateFormatted;

    public StudantDTO(Long id, String registration, String name, Date birthDate, String street, String number,
                      String neighborhood, String city, String state, String postalCode, String allergies, String bloodType,
                      String feverMedication, String dosage) {
        this.id = id;
        this.registration = registration;
        this.name = name;
        this.birthDate = birthDate;
        this.street = street;
        this.number = number;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.allergies = allergies;
        this.bloodType = bloodType;
        this.feverMedication = feverMedication;
        this.dosage = dosage;
    }

    protected StudantDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        birthDate = (Date) in.readSerializable();
        registration = in.readString();
        name = in.readString();
        street = in.readString();
        number = in.readString();
        neighborhood = in.readString();
        city = in.readString();
        state = in.readString();
        postalCode = in.readString();
        allergies = in.readString();
        bloodType = in.readString();
        feverMedication = in.readString();
        dosage = in.readString();
    }

    public static final Creator<StudantDTO> CREATOR = new Creator<StudantDTO>() {
        @Override
        public StudantDTO createFromParcel(Parcel in) {
            return new StudantDTO(in);
        }

        @Override
        public StudantDTO[] newArray(int size) {
            return new StudantDTO[size];
        }
    };

    public String getBirthDateFormatted() {
        return DateUtils.dateToString(this.birthDate);
    }

    public String getRegistration() {
        return registration;
    }

    public String getName() {
        return name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getAllergies() {
        return allergies;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getFeverMedication() {
        return feverMedication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void setFeverMedication(String feverMedication) {
        this.feverMedication = feverMedication;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeSerializable(birthDate);
        dest.writeString(registration);
        dest.writeString(name);
        dest.writeString(street);
        dest.writeString(number);
        dest.writeString(neighborhood);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(postalCode);
        dest.writeString(allergies);
        dest.writeString(bloodType);
        dest.writeString(feverMedication);
        dest.writeString(dosage);
    }
}
