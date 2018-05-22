package com.fca.agenda.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fabiano.alvarenga on 19/04/18.
 */

public class ImagesDTO implements Parcelable {

    private Long communicationId;
    private String imgUrl;
    private String titleImg;

    public ImagesDTO(Long communicationId, String imgUrl, String titleImg) {
        this.communicationId = communicationId;
        this.imgUrl = imgUrl;
        this.titleImg = titleImg;
    }

    protected ImagesDTO(Parcel in) {
        if(in.readByte() == 0) {
            communicationId = null;
        } else {
            communicationId = in.readLong();
        }
        imgUrl = in.readString();
        titleImg = in.readString();
    }

    public static final Creator<ImagesDTO> CREATOR = new Creator<ImagesDTO>() {
        @Override
        public ImagesDTO createFromParcel(Parcel in) {
            return new ImagesDTO(in);
        }

        @Override
        public ImagesDTO[] newArray(int size) {
            return new ImagesDTO[size];
        }
    };

    public Long getCommunicationId() {
        return communicationId;
    }

    public void setCommunicationId(Long communicationId) {
        this.communicationId = communicationId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitleImg() {
        return titleImg;
    }

    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (communicationId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(communicationId);
        }
        dest.writeString(imgUrl);
        dest.writeString(titleImg);
    }
}
