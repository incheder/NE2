package com.ninetoseven.series.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ninetoseven.series.R;
import com.ninetoseven.series.model.Episode;
import com.ninetoseven.series.util.VolleySingleton;

public class NewEpisodeAdapter extends BaseAdapter {

	private static final String TAG = "NE2";
	private Context context;
	private List<Episode> eList;
	Random r = new Random();
	static class ViewHolder {
		NetworkImageView ivShow;
		TextView tvShowName;
		TextView tvEpisode;
		TextView tvEpisodeName;
		TextView tvDate;
		TextView tvTime;

	}

	public NewEpisodeAdapter(Context context, List<Episode> eList) {
		this.context = context;
		this.eList = eList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return eList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return eList.get(position);
	}

	public long getItemId(int position) {
		return position;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {

			convertView = inflater.inflate(R.layout.item_nuevo_episodio,
					parent, false);

			holder = new ViewHolder();
			holder.ivShow = (NetworkImageView) convertView
					.findViewById(R.id.ivItemShow);
			holder.tvShowName = (TextView) convertView
					.findViewById(R.id.tvItemShowName);
			holder.tvEpisode = (TextView) convertView
					.findViewById(R.id.tvItemEpisode);
			holder.tvEpisodeName = (TextView) convertView
					.findViewById(R.id.tvItemEpisodeName);
			holder.tvDate = (TextView) convertView
					.findViewById(R.id.tvItemDate);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tvItemTime);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//holder.ivShow.setImageDrawable(null);// para evitar que muestra la
												// imagen anterior en el caso de
												// que reuse el view
		// Util.imageLoader.displayImage("file:///"+Util.getColumna("cache",
		// context, lista.get(position).getId(),"Podcast"), holder.ivPod);
		Log.d(TAG, "image: "+eList.get(position).getImage());
		ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
		holder.ivShow.setImageUrl(eList.get(position).getImage(), imageLoader);
	
	/*	if(r.nextInt(2)==1)
		{
			holder.ivShow.setImageResource(R.drawable.show);
		}
		else
		{
			holder.ivShow.setImageResource(R.drawable.show2);
		}
		*/
		holder.tvShowName.setText(eList.get(position).getShowName());
		holder.tvEpisode.setText(eList.get(position).getNumber());
		holder.tvEpisodeName.setText(eList.get(position).getTitle());
		holder.tvDate.setText(eList.get(position).getAirdate());
		holder.tvTime.setText(eList.get(position).getAirtime());

		return convertView;
	}
}
