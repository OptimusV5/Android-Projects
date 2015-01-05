package com.mid.myplan;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.MediaStore.Audio.Media;
import android.widget.Toast;

public class PlayMusic extends Service {

	private MediaPlayer myMediaPlayer;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


//MediaPlayer mp = new MediaPlayer();
//mp.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
//mp.prepare();
//mp.start();
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
//		myMediaPlayer = MediaPlayer.create(this, R.raw.tx);
		myMediaPlayer = new MediaPlayer();
		try {
			Uri alarm = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			myMediaPlayer = MediaPlayer.create(getApplicationContext(), alarm);
//			mp.start();
//			myMediaPlayer.setDataSource(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
			myMediaPlayer.prepare();
		} catch(Exception e) {
			
		}
		
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		myMediaPlayer.stop();
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		myMediaPlayer.start();
		super.onStart(intent, startId);
	}

}
