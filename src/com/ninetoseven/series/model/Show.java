package com.ninetoseven.series.model;

import android.os.Parcel;
import android.os.Parcelable;

//good old POJO
public class Show implements Parcelable{
	private String showName;
	private String id;
	private String showLink;
	private String seasons;
	private String image;
	private String started;
	private String ended;
	private String status;
	private String summary;
	private String runtime;
	private String network;
	private String airtime;
	private String airday;
	private String timezone;
	private Episode latestepisode;
	private Episode nextepisode;
	
	public Show() {
		// TODO Auto-generated constructor stub
	}

	public String getShowName() {
		return showName;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getShowLink() {
		return showLink;
	}

	public void setShowLink(String showLink) {
		this.showLink = showLink;
	}

	public String getSeasons() {
		return seasons;
	}

	public void setSeasons(String seassons) {
		this.seasons = seassons;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getStarted() {
		return started;
	}

	public void setStarted(String started) {
		this.started = started;
	}

	public String getEnded() {
		return ended;
	}

	public void setEnded(String ended) {
		this.ended = ended;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setRuntime(String runtime) {
		this.runtime = runtime;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getAirtime() {
		return airtime;
	}

	public void setAirtime(String airtime) {
		this.airtime = airtime;
	}

	public String getAirday() {
		return airday;
	}

	public void setAirday(String airday) {
		this.airday = airday;
	}

	public String getTimezone() {
		return timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public Episode getLatestepisode() {
		return latestepisode;
	}

	public void setLatestepisode(Episode latestepisode) {
		this.latestepisode = latestepisode;
	}

	public Episode getNextepisode() {
		return nextepisode;
	}

	public void setNextepisode(Episode nextepisode) {
		this.nextepisode = nextepisode;
	}
	
	@Override
	public int describeContents() {
		
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(showName);
		dest.writeString(id);
		dest.writeString(showLink);
		dest.writeString(seasons);
		dest.writeString(image);
		dest.writeString(started);
		dest.writeString(ended);
		dest.writeString(status);
		dest.writeString(summary);
		dest.writeString(runtime);
		dest.writeString(network);
		dest.writeString(airtime);
		dest.writeString(airday);
		dest.writeString(timezone);
		dest.writeParcelable(latestepisode, flags);
		dest.writeParcelable(nextepisode, flags);
	}
	
	public Show (Parcel in) {
		showName= in.readString();
		id= in.readString();
		showLink= in.readString();
		seasons= in.readString();
		image= in.readString();
		started= in.readString();
		ended= in.readString();
		status= in.readString();
		summary= in.readString();
		runtime= in.readString();
		network= in.readString();
		airtime= in.readString();
		airday= in.readString();
		timezone= in.readString();
		latestepisode= in.readParcelable(getClass().getClassLoader());
		nextepisode= in.readParcelable(getClass().getClassLoader());
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Show> CREATOR = new Parcelable.Creator<Show>() {
        public Show createFromParcel(Parcel in) {
            return new Show(in);
        }

        public Show[] newArray(int size) {
            return new Show[size];
        }
    };

}
