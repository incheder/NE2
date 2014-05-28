package com.ninetoseven.series.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Episode implements Parcelable{
	private String showName;
	private String number;
	private String title;
	private String airdate;
	private String airtime;
	private String image;
	private String showId;

	public Episode() {
		// TODO Auto-generated constructor stub
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAirdate() {
		return airdate;
	}

	public void setAirdate(String airdate) {
		this.airdate = airdate;
	}

	public String getAirtime() {
		return airtime;
	}

	public void setAirtime(String airtime) {
		this.airtime = airtime;
	}

	@Override
	public int describeContents() {
		
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(showName);
		dest.writeString(number);
		dest.writeString(title);
		dest.writeString(airdate);
		dest.writeString(airtime);
		dest.writeString(image);
		dest.writeString(showId);
	}
	
	public Episode(Parcel in) {
		showName= in.readString();
		number= in.readString();
		title= in.readString();
		airdate= in.readString();
		airtime= in.readString();
		image= in.readString();
		showId= in.readString();
	}
	
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getShowId() {
		return showId;
	}

	public void setShowId(String showId) {
		this.showId = showId;
	}

	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Episode> CREATOR = new Parcelable.Creator<Episode>() {
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };
}
