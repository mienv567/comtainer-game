package com.fanwe.live.adapter;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fanwe.library.adapter.SDSimpleAdapter;
import com.fanwe.library.utils.SDCollectionUtil;
import com.fanwe.library.utils.SDResourcesUtil;
import com.fanwe.library.utils.SDToast;
import com.fanwe.live.R;
import com.fanwe.live.event.ELiveSongDownload;
import com.fanwe.live.model.LiveSongModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class LiveSongListAdapter extends SDSimpleAdapter<LiveSongModel>
{
	private OnXItemClickListener mListener;

	public LiveSongListAdapter(Activity activity) {
		this(new ArrayList<LiveSongModel>(), activity);
	}
	
	public LiveSongListAdapter(List<LiveSongModel> listModel, Activity activity)
	{
		super(listModel, activity);
	}
	
	public void setXItemClickListener(OnXItemClickListener listener) {
		mListener = listener;
	}

	@Override
	public int getLayoutId(int position, View convertView, ViewGroup parent)
	{
		return R.layout.item_live_song_list;
	}

	@Override
	public void bindData(final int position, final View convertView, final ViewGroup parent, final LiveSongModel model)
	{
		View view = get(R.id.v_status_line, convertView);
		TextView tvNameTextView = get(R.id.tv_name, convertView);
		TextView tvSinger = get(R.id.tv_singer, convertView);
		TextView tvDuring = get(R.id.tv_during, convertView);
		TextView tvStatus = get(R.id.tv_status, convertView);
		ProgressBar pbBar = get(R.id.pb_download, convertView);
		
		tvNameTextView.setText(model.getAudio_name());
		tvSinger.setText(model.getArtist_name());
		if (model.getTime_len() > 0) {
			DecimalFormat df = new DecimalFormat("#0.00");  
			df.format(model.getTime_len()/60.0);
			tvDuring.setText( df.format(model.getTime_len()/60.0) + "");
		}else {
			tvDuring.setText("");
		}
		
		
		if (model.isCached()) {
			view.setBackgroundColor(mActivity.getResources().getColor(R.color.live_song_status_download_line)); 
		}else {
			view.setBackgroundColor(mActivity.getResources().getColor(R.color.live_song_status_line));
		}
		if (model.isCached() && model.getStatus() != 1) {
			tvStatus.setVisibility(View.VISIBLE);
			tvStatus.setText(R.string.choose);
			tvStatus.setBackgroundResource(R.drawable.live_song_select_shape);
			tvStatus.setTextColor(mActivity.getResources().getColor(R.color.live_song_select_text));
			pbBar.setVisibility(View.INVISIBLE);
		} else if (model.getStatus() == 1) {
			tvStatus.setVisibility(View.INVISIBLE);
			pbBar.setVisibility(View.VISIBLE);
			pbBar.setProgress(model.getProgress());
		}
		else {
			tvStatus.setText(R.string.download);
			tvStatus.setVisibility(View.VISIBLE);
			pbBar.setVisibility(View.INVISIBLE);
			tvStatus.setBackgroundResource(R.drawable.live_song_download_shape);
			tvStatus.setTextColor(mActivity.getResources().getColor(R.color.live_song_download_text));
		}
		
		convertView.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				if (mListener != null) {
					mListener.onXItemLongClick(position, convertView, parent, model);
				}
				return false;
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (mListener != null) {
					mListener.onXItemClick(position, convertView, parent, model);
				}
			}
		});
	}

	@Override
	public void updateData(List<LiveSongModel> listModel) {
		
		super.updateData(updateState(listModel));
	}
	
	protected List<LiveSongModel> updateState(List<LiveSongModel> listModel) {
		if (SDCollectionUtil.isEmpty(listModel)) {
			return listModel;
		}
		for (int i = 0; i < listModel.size(); i++) {
			LiveSongModel model = listModel.get(i);
			model.setCached(model.isMusicExist());
		}
		return listModel;
	}
	@Override
	public void appendData(List<LiveSongModel> listModel) {
		super.appendData(updateState(listModel));
	}
	public void addFirst(LiveSongModel model){
		listModel.add(0, model);
		notifyDataSetChanged();
	}
	
	
	protected LiveSongModel findItem(int audioId) {
		for (int i = 0; i < listModel.size(); i++) {
			if (listModel.get(i).getAudio_id() == audioId) {
				return listModel.get(i);
			}
		}
		return null;
	}
	public void updateSongItem(ELiveSongDownload eventDownload) {
		LiveSongModel model = findItem(eventDownload.songModel.getAudio_id());
		if (model == null) {
			if (eventDownload.songModel.getStatus() == 2) {
				eventDownload.songModel.setCached(model.isMusicExist());
				addFirst(eventDownload.songModel);
			}
			return;
		}
		
		model.setAudio_link(eventDownload.songModel.getAudio_link());
		model.setProgress(eventDownload.songModel.getProgress());
		switch (eventDownload.songModel.getStatus()) {
		case -1: // 下载失败
			model.setProgress(0);
			model.setStatus(0);
			SDToast.showToast(SDResourcesUtil.getString(R.string.download_song_fail));
			break;
		case 0: // 普通状态
			model.setProgress(0);
			model.setStatus(0);
			break;
		case 1: // 下载中
			model.setProgress(eventDownload.songModel.getProgress());
			model.setStatus(1);
			break;
		case 2:
			model.setProgress(eventDownload.songModel.getProgress());
			model.setCached(model.isMusicExist());
			model.setTime_len(eventDownload.songModel.getTime_len());
			model.setLrc_content(eventDownload.songModel.getLrc_content());
			model.setAudio_link(eventDownload.songModel.getAudio_link());
			model.setStatus(2);
			break;
		default:
			break;
		}
		notifyDataSetChanged();
	}
	
	public static interface OnXItemClickListener
	{
		void onXItemLongClick(int position, View convertView, ViewGroup parent, LiveSongModel model);
		void onXItemClick(int position, View convertView, ViewGroup parent, LiveSongModel model);
	}
}
