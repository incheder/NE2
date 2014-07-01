package com.ninetoseven.series.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;


public class ListEp implements Parcelable{
	
	private String name;

	private String totalseasons;

	private ArrayList<Episode> listaEpisodios;
	
	public ListEp() {
		// TODO Auto-generated constructor stub
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTotalseasons() {
		return totalseasons;
	}
	public void setTotalseasons(String totalseasons) {
		this.totalseasons = totalseasons;
	}	

	public ArrayList<Episode> getListaEpisodios() {
		return listaEpisodios;
	}

	public void setListaEpisodios(ArrayList<Episode> listaEpisodios) {
		this.listaEpisodios = listaEpisodios;
	}
	

	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(totalseasons);
		dest.writeList(listaEpisodios);
	}
	
	public ListEp(Parcel in) {
		name = in.readString(); 
		totalseasons = in.readString();
		in.readList(listaEpisodios,List.class.getClassLoader());
	}
	
	// this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<ListEp> CREATOR = new Parcelable.Creator<ListEp>() {
        public ListEp createFromParcel(Parcel in) {
            return new ListEp(in);
        }

        public ListEp[] newArray(int size) {
            return new ListEp[size];
        }
    };
	
	
	public static class Episode implements Parcelable
	{
		
		private String epnum;
		
		private String seasonnum;
		
		private String airdate;
		
		private String link;
		
		private String title;
		
		private String summary;
		
		private String prodnum;
		
		private String rating;
		
		private String screencap;
		
		private String season;
		
		public Episode() {
			// TODO Auto-generated constructor stub
		}
		
		public String getEpnum() {
			return epnum;
		}
		public void setEpnum(String epnum) {
			this.epnum = epnum;
		}
		public String getSeasonnum() {
			return seasonnum;
		}
		public void setSeasonnum(String seasonnum) {
			this.seasonnum = seasonnum;
		}
		public String getAirdate() {
			return airdate;
		}
		public void setAirdate(String airdate) {
			this.airdate = airdate;
		}
		public String getLink() {
			return link;
		}
		public void setLink(String link) {
			this.link = link;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getSummary() {
			return summary;
		}
		public void setSummary(String summary) {
			this.summary = summary;
		}
		public String getProdnum() {
			return prodnum;
		}
		public void setProdnum(String prodnum) {
			this.prodnum = prodnum;
		}
		public String getRating() {
			return rating;
		}
		public void setRating(String rating) {
			this.rating = rating;
		}
		public String getScreencap() {
			return screencap;
		}
		public void setScreencap(String screencap) {
			this.screencap = screencap;
		}
		public String getSeason() {
			return season;
		}
		public void setSeason(String season) {
			this.season = season;
		}
		@Override
		public int describeContents() {
			return 0;
		}
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(epnum);
			dest.writeString(seasonnum);
			dest.writeString(airdate);
			dest.writeString(link);
			dest.writeString(title);
			dest.writeString(summary);
			dest.writeString(prodnum);
			dest.writeString(rating);
			dest.writeString(screencap);
			dest.writeString(season);
			
		}
		
		public Episode(Parcel in) {
			epnum = in.readString();
			seasonnum = in.readString();
			airdate = in.readString();
			link = in.readString();
			title = in.readString();
			summary = in.readString();
			prodnum = in.readString();
			rating = in.readString();
			screencap = in.readString();
			season = in.readString();
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


}
