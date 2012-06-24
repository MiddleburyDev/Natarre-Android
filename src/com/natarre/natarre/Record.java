package com.natarre.natarre;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Record extends Activity {
	// TODO: Big bug when record button used twice.

	private MediaRecorder mediaRecorder;
	private String fileDir;	
	private String currentFilePath;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record);

		fileDir = getFilesDir().getAbsolutePath();
		configureMediaRecorder();
		
		final ToggleButton recordButton = (ToggleButton) findViewById(R.id.record_button);
		recordButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			private boolean usedOnce = false;
			
			@Override			
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
				if (isChecked) {
					if(!usedOnce) {
						mediaRecorder.start();
						usedOnce = true;
					} else {
						mediaRecorder.reset();
						configureMediaRecorder();
					}
				} else {
					mediaRecorder.stop();
					Toast.makeText(Record.this, "Recording saved! Record again to overwrite.", Toast.LENGTH_SHORT);
				}
			}
		});
		
		final Button recordNextButton = (Button) findViewById(R.id.record_next_button);
		recordNextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent recordInfoIntent = new Intent(Record.this, RecordInfo.class);
				recordInfoIntent.putExtra("FILE", currentFilePath);
				startActivity(recordInfoIntent);
			}
		});
	}

	private void configureMediaRecorder() {
		currentFilePath = getFilePath();
		Log.i("NATARRE", currentFilePath);
		mediaRecorder = new MediaRecorder();
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
		mediaRecorder.setOutputFile(currentFilePath);
		try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getFilePath() {
		return fileDir+"/"+getDateTime()+".m4a";
	}
	private static final String getDateTime() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_hh:mm:ss");
		return df.format(new Date());
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mediaRecorder.release();
		mediaRecorder = null;
	}

}
