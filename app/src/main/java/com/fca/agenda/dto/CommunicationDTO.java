package com.fca.agenda.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabiano.alvarenga on 11/04/18.
 */

public class CommunicationDTO implements Parcelable {

    private UserDTO userDTO;

    private Long id;
    private List<ImagesDTO> imagesDTOS;
    private String pageUrl;
    private String pageTitle;
    private String fileIdGoogleDrive;
    private String fileTitle;
    private String title;
    private String description;
    private Long date;
    private Boolean favorite;
    private Boolean checked;

    public CommunicationDTO() {

    }

    protected CommunicationDTO(Parcel in) {
        userDTO = in.readParcelable(UserDTO.class.getClassLoader());
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        imagesDTOS = in.createTypedArrayList(ImagesDTO.CREATOR);
        pageUrl = in.readString();
        pageTitle = in.readString();
        fileIdGoogleDrive = in.readString();
        fileTitle = in.readString();
        title = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            date = null;
        } else {
            date = in.readLong();
        }
        byte tmpFavorite = in.readByte();
        favorite = tmpFavorite == 0 ? null : tmpFavorite == 1;
        byte tmpChecked = in.readByte();
        checked = tmpChecked == 0 ? null : tmpChecked == 1;
    }

    public static final Creator<CommunicationDTO> CREATOR = new Creator<CommunicationDTO>() {
        @Override
        public CommunicationDTO createFromParcel(Parcel in) {
            return new CommunicationDTO(in);
        }

        @Override
        public CommunicationDTO[] newArray(int size) {
            return new CommunicationDTO[size];
        }
    };

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ImagesDTO> getImagesDTOS() {
        if(null == imagesDTOS) {
            imagesDTOS = new ArrayList<>();
        }
        return imagesDTOS;
    }

    public void setImagesDTOS(List<ImagesDTO> imagesDTOS) {
        this.imagesDTOS = imagesDTOS;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getFileIdGoogleDrive() {
        return fileIdGoogleDrive;
    }

    public void setFileIdGoogleDrive(String fileIdGoogleDrive) {
        this.fileIdGoogleDrive = fileIdGoogleDrive;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(userDTO, flags);
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeTypedList(imagesDTOS);
        dest.writeString(pageUrl);
        dest.writeString(pageTitle);
        dest.writeString(fileIdGoogleDrive);
        dest.writeString(fileTitle);
        dest.writeString(title);
        dest.writeString(description);
        if (date == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(date);
        }
        dest.writeByte((byte) (favorite == null ? 0 : favorite ? 1 : 2));
        dest.writeByte((byte) (checked == null ? 0 : checked ? 1 : 2));
    }
}
