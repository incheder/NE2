package com.ninetoseven.series.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {

	
	private static VolleySingleton instance;
	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	
	public VolleySingleton(Context context) {
		requestQueue = Volley.newRequestQueue(context);
		imageLoader = new ImageLoader(requestQueue, new ImageCache() {
			private final LruCache<String,Bitmap> cache = new LruCache<String,Bitmap>(20);
			@Override
			public void putBitmap(String url, Bitmap bitmap) {
				// TODO Auto-generated method stub
				cache.put(url, bitmap);
			}
			
			@Override
			public Bitmap getBitmap(String url) {
				// TODO Auto-generated method stub
				return cache.get(url);
			}
		});
	}
	
	public static VolleySingleton getInstance(Context context)
	{
		if(instance==null)
		{
			instance= new VolleySingleton(context);
		}
		return instance;
		
	}
	
	public RequestQueue getRequestQueue()
	{
		return requestQueue;
	}
	
	public ImageLoader getImageLoader()
	{
		return imageLoader;
	}
}
