package com.fca.agenda.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabiano.alvarenga on 04/05/18.
 */

public class UserDTO implements Parcelable {

    private Long id;
    private String email;
    private String responsableName;
    private String mobilePhone;

    private String coResponsableName;
    private String coMobilePhone;

    private List<StudantDTO> studantDTOs;

    public UserDTO(Long id, String email, String responsableName, String mobilePhone, String coResponsableName,
                   String coMobilePhone, List<StudantDTO> studantDTOs) {
        this.id = id;
        this.email = email;
        this.responsableName = responsableName;
        this.mobilePhone = mobilePhone;
        this.coResponsableName = coResponsableName;
        this.coMobilePhone = coMobilePhone;
        this.studantDTOs = studantDTOs;
    }

    protected UserDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        email = in.readString();
        responsableName = in.readString();
        mobilePhone = in.readString();
        coResponsableName = in.readString();
        coMobilePhone = in.readString();
        studantDTOs = in.createTypedArrayList(StudantDTO.CREATOR);
    }

    public static final Creator<UserDTO> CREATOR = new Creator<UserDTO>() {
        @Override
        public UserDTO createFromParcel(Parcel in) {
            return new UserDTO(in);
        }

        @Override
        public UserDTO[] newArray(int size) {
            return new UserDTO[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public String getResponsableName() {
        return responsableName;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public String getCoResponsableName() {
        return coResponsableName;
    }

    public String getCoMobilePhone() {
        return coMobilePhone;
    }

    public List<StudantDTO> getStudantDTOs() {
        if(null == studantDTOs) {
            studantDTOs = new ArrayList<>();
        }
        return studantDTOs;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setResponsableName(String responsableName) {
        this.responsableName = responsableName;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public void setCoResponsableName(String coResponsableName) {
        this.coResponsableName = coResponsableName;
    }

    public void setCoMobilePhone(String coMobilePhone) {
        this.coMobilePhone = coMobilePhone;
    }

    public void setStudantDTOs(List<StudantDTO> studantDTOs) {
        this.studantDTOs = studantDTOs;
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
        dest.writeString(email);
        dest.writeString(responsableName);
        dest.writeString(mobilePhone);
        dest.writeString(coResponsableName);
        dest.writeString(coMobilePhone);
        dest.writeTypedList(studantDTOs);
    }
}
