package com.ninetoseven.series.parser;

import org.xml.sax.Attributes;

import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import com.ninetoseven.series.model.Show;



public class ShowInfoParser {
	//private final static String ITUNES_NAMESPACE="http://www.itunes.com/dtds/podcast-1.0.dtd";
	private String rssUrl;
   // private Item itemActual;
    private Show show;
   // ArrayList<Item> listaItems;
    //private final static String TAG="NE2";
 
    public ShowInfoParser(String url)
    {
        
            this.rssUrl = url ;
        
       
    }
 
    public Show parse ()
    {
    	
        RootElement root = new RootElement("Showinfo");
       // Element channel = root.getChild("channel");
      //  Element image = channel.getChild(ITUNES_NAMESPACE,"image");
        root.setStartElementListener(new StartElementListener() {
            public void start(Attributes attrs) {
                show = new Show();
            }
        });
        
        root.getChild("showid").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setId(body);
                       
                    }
            });
        root.getChild("showname").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setShowName(body);
                       
                    }
            });
        root.getChild("showlink").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setShowLink(body);
                       
                    }
            });
        root.getChild("seasons").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setSeasons(body);
                       
                    }
            });
        root.getChild("image").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setImage(body);
                       
                    }
            });
        root.getChild("started").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setStarted(body);
                       
                    }
            });
        root.getChild("ended").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setEnded(body);
                       
                    }
            });
        root.getChild("status").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setStatus(body);
                       
                    }
            });
        root.getChild("summary").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setSummary(body);
                       
                    }
            });
        root.getChild("runtime").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setRuntime(body);
                       
                    }
            });
        root.getChild("network").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setNetwork(body);
                       
                    }
            });
        root.getChild("airtime").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setAirtime(body);
                       
                    }
            });
        root.getChild("airday").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setAirday(body);
                       
                    }
            });
        root.getChild("timezone").setEndTextElementListener(
                new EndTextElementListener(){
                    public void end(String body) {
                        show.setTimezone(body);
                       
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

        return show;
    }
 
   
    
  
}
