package com.ninetoseven.series.parser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import com.ninetoseven.series.model.Show;



public class ShowSearchParser {
	private String rssUrl;
    private List<Show> showList;
	private Show show;
    private final static String TAG="NE2";
 
    public ShowSearchParser(String url)
    {
        
            this.rssUrl = url ;
        
       
    }
 
    public List<Show> parse ()
    {
    	
        RootElement root = new RootElement("Results");
        Element showE = root.getChild("show");
        
        root.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                showList = new ArrayList<Show>();
            }
        });
        
        showE.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                show = new Show();
            }
        });
        
        showE.setEndElementListener(new EndElementListener() {
			
			@Override
			public void end() {
				showList.add(show);
				
			}
		});
        
        showE.getChild("showid").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setId(body);
                       
                    }
            });
        showE.getChild("name").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setShowName(body);
                       
                    }
            });
        showE.getChild("link").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setShowLink(body);
                       
                    }
            });
        showE.getChild("seasons").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setSeasons(body);
                       
                    }
            });
     
        showE.getChild("started").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setStarted(body);
                       
                    }
            });
        showE.getChild("ended").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setEnded(body);
                       
                    }
            });
        showE.getChild("status").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setStatus(body);
                       
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

        return showList;
    }
 
   
    
  
}
