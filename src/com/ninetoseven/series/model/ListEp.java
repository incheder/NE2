package com.ninetoseven.series.model;

import java.util.List;


public class ListEp {
	
	private String name;

	private String totalseasons;

	private List<Episode> listaEpisodios;
	
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

	public List<Episode> getListaEpisodios() {
		return listaEpisodios;
	}

	public void setListaEpisodios(List<Episode> listaEpisodios) {
		this.listaEpisodios = listaEpisodios;
	}
	
	
	public static class Episode
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
		
		
	}
}
