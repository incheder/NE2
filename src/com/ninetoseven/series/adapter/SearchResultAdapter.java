package com.ninetoseven.series.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ninetoseven.series.R;
import com.ninetoseven.series.model.Episode;
import com.ninetoseven.series.model.Show;

public class SearchResultAdapter extends BaseAdapter {

	private Context context;
	private List<Show> sList;
	Random r = new Random();
	static class ViewHolder {
		ImageView ivShow;
		TextView tvShowName;
		

	}

	public SearchResultAdapter(Context context, List<Show> eList) {
		this.context = context;
		this.sList = eList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return sList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return sList.get(position);
	}

	public long getItemId(int position) {
		return Long.valueOf(sList.get(position).getId());

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.item_show,
					parent, false);

			holder = new ViewHolder();
			holder.ivShow = (ImageView) convertView
					.findViewById(R.id.ivSearchShow);
			holder.tvShowName = (TextView) convertView
					.findViewById(R.id.tvSearchShowName);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//holder.ivShow.setImageDrawable(null);// para evitar que muestra la
												// imagen anterior en el caso de
												// que reuse el view
		// Util.imageLoader.displayImage("file:///"+Util.getColumna("cache",
		// context, lista.get(position).getId(),"Podcast"), holder.ivPod);
	
		if(r.nextInt(2)==1)
		{
			holder.ivShow.setImageResource(R.drawable.show);
		}
		else
		{
			holder.ivShow.setImageResource(R.drawable.show2);
		}
		
		holder.tvShowName.setText(sList.get(position).getShowName());
		

		return convertView;
	}
}
