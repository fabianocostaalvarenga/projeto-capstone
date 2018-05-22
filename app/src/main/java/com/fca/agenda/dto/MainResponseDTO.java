package com.fca.agenda.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * @author: fabiano.alvarenga
 *
 * May 17, 2018
 */

public class MainResponseDTO implements Parcelable {
	
	private List<CommunicationDTO> listCommunication;
	private String generalOrientationFileId;
	private String schoolHours;

	protected MainResponseDTO(Parcel in) {
		listCommunication = in.createTypedArrayList(CommunicationDTO.CREATOR);
		generalOrientationFileId = in.readString();
		schoolHours = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeTypedList(listCommunication);
		dest.writeString(generalOrientationFileId);
		dest.writeString(schoolHours);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<MainResponseDTO> CREATOR = new Creator<MainResponseDTO>() {
		@Override
		public MainResponseDTO createFromParcel(Parcel in) {
			return new MainResponseDTO(in);
		}

		@Override
		public MainResponseDTO[] newArray(int size) {
			return new MainResponseDTO[size];
		}
	};

	public List<CommunicationDTO> getListCommunication() {
		return listCommunication;
	}
	
	public String getGeneralOrientationFileId() {
		return generalOrientationFileId;
	}
	
	public String getSchoolHours() {
		return schoolHours;
	}
	
	public void setListCommunication(List<CommunicationDTO> listCommunication) {
		this.listCommunication = listCommunication;
	}
	
	public void setGeneralOrientationFileId(String generalOrientationFileId) {
		this.generalOrientationFileId = generalOrientationFileId;
	}
	
	public void setSchoolHours(String schoolHours) {
		this.schoolHours = schoolHours;
	}

}
