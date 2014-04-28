package com.ninetoseven.series.parser;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import com.ninetoseven.series.model.Episode;
import com.ninetoseven.series.model.Show;



public class EpisodeInfoParser {
	//private final static String ITUNES_NAMESPACE="http://www.itunes.com/dtds/podcast-1.0.dtd";
	private String rssUrl;
   // private Item itemActual;
    private Show show;
    private Episode episode;
    private Episode latestE,nextE;
    Episode [] arrayE;
   // ArrayList<Item> listaItems;
    private final static String TAG="NE2";
 
    public EpisodeInfoParser(String url)
    {
        
            this.rssUrl = url ;
        
       
    }
 
    public Episode[] parse ()
    {
    	
        RootElement root = new RootElement("show");
        Element latestepisode = root.getChild("latestepisode");
        Element nextepisode = root.getChild("nextepisode");
      //  Element image = channel.getChild(ITUNES_NAMESPACE,"image");
       /* root.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                show = new Show();
            }
        });*/
        
        root.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
            	arrayE = new Episode[2];
            }
        });
        
        latestepisode.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                episode = new Episode();
            }
        });
        
        latestepisode.setEndElementListener(new EndElementListener() {
			
			@Override
			public void end() {
			//	show.setLatestepisode(episode);
				arrayE[0]=episode;
			}
		});
        
        nextepisode.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                episode = new Episode();
            }
        });
        
        nextepisode.setEndElementListener(new EndElementListener() {
			
			@Override
			public void end() {
				//show.setNextepisode(episode);
				arrayE[1]=episode;
			}
		});
        
        
        latestepisode.getChild("number").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                       episode.setNumber(body);
                       
                    }
            });
        latestepisode.getChild("title").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        episode.setTitle(body);
                       
                    }
            });
        latestepisode.getChild("airdate").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        episode.setAirdate(body);
                       
                    }
            });
        latestepisode.getChild("airtime").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        episode.setAirtime(body);
                       
                    }
            });
        nextepisode.getChild("number").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                       episode.setNumber(body);
                       
                    }
            });
        nextepisode.getChild("title").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        episode.setTitle(body);
                       
                    }
            });
        nextepisode.getChild("airdate").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        episode.setAirdate(body);
                       
                    }
            });
        nextepisode.getChild("airtime").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        episode.setAirtime(body);
                       
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

        return arrayE;
    }
 
   
    
  
}
