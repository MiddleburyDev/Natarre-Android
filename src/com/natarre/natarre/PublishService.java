package com.natarre.natarre;

import static com.natarre.natarre.Constants.UPLOAD_POSTFIX;
import static com.natarre.natarre.Constants.URL_PREFIX_DEBUG;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;

import com.google.gson.JsonObject;
import com.natarre.natarre.PostMultipartAsync.PostMListener;

public class PublishService extends Service implements PostMListener {
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {	
		// TODO: check action??
		Bundle extras = intent.getExtras();
		String audioPath = extras.getString("AUDIO_PATH");
		String title = extras.getString("TITLE");
		String content = extras.getString("CONTENT");
		upload(audioPath, title, content);
		return START_STICKY;
	}

	public void upload(String audioPath, String title, String content) {
		String url = URL_PREFIX_DEBUG + UPLOAD_POSTFIX;
		File audio = new File(audioPath);
		SharedPreferences prefs = getSharedPreferences("natarre_prefs", MODE_PRIVATE);
		String email = prefs.getString("email", null);
		String token = Util.md5(prefs.getString("email", null)+"Te99y");
		String prompt_ID = "0";
		PostMultipartAsync postMultipartAsync = new PostMultipartAsync(this);
		postMultipartAsync.setFiles(audio);
		postMultipartAsync.execute(url, "email", email, "token", token, "title", title, "content", content);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void postCallback(JsonObject result) {
		stopSelf();
	}
}
