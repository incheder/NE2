package com.ninetoseven.series.parser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Log;
import android.util.Xml;

import com.ninetoseven.series.model.ListEp;
import com.ninetoseven.series.model.ListEp.Episode;



public class EpisodeListParser {
	//private final static String ITUNES_NAMESPACE="http://www.itunes.com/dtds/podcast-1.0.dtd";
	private String rssUrl;
   // private Item itemActual;
    private ListEp listEp;
    private List<Episode> lista;
    private String temporada;
    Episode ep;
   // ArrayList<Item> listaItems;
    private final static String TAG="NE2";
 
    public EpisodeListParser(String url)
    {
        
            this.rssUrl = url ;
        
       
    }
 
    public ListEp parse ()
    {
    	
        RootElement root = new RootElement("Show");
        Element episodeList = root.getChild("Episodelist");
        Element season = episodeList.getChild("Season");
        Element episode = season.getChild("episode");
        
        root.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
            	 listEp = new ListEp();
            }
        });
        
        season.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
            	 temporada = attrs.getValue("no");
            }
        });
        
        episodeList.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
               lista = new ArrayList<Episode>();
            }
        });
        
        episodeList.setEndElementListener(new EndElementListener() {
			
			@Override
			public void end() {
				listEp.setListaEpisodios(lista);
				
			}
		});
        
        episode.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                ep = new Episode();
            }
        });
        
        episode.setEndElementListener(new EndElementListener() {
			
			@Override
			public void end() {
				ep.setSeason(temporada);
				lista.add(ep);
				
			}
		});
        
        root.getChild("name").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        listEp.setName(body);
                       
                    }
            });
        root.getChild("totalseasons").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        listEp.setTotalseasons(body);
                       
                    }
            });
        episode.getChild("epnum").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        ep.setEpnum(body);
                       
                    }
            });
        episode.getChild("seasonnum").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        ep.setSeasonnum(body);
                       
                    }
            });
        episode.getChild("airdate").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        ep.setAirdate(body);
                       
                    }
            });
        episode.getChild("link").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        ep.setLink(body);
                       
                    }
            });
        episode.getChild("title").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        ep.setTitle(body);
                       
                    }
            });
        episode.getChild("rating").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        ep.setRating(body);
                       
                    }
            });
        episode.getChild("summary").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        ep.setSummary(body);
                       
                    }
            });
        episode.getChild("screencap").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        ep.setScreencap(body);
                       
                    }
            });
       
       
        
 
        try
        {
            Xml.parse(rssUrl,
                    root.getContentHandler());
        }
        catch (Exception ex)
        {
            throw new RuntimeException(ex);
        }

        return listEp;
    }
 
   
    
  
}
