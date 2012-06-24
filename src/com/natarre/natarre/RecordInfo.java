package com.natarre.natarre;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class RecordInfo extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.record_info);
		
		final Button publish_button = (Button) findViewById(R.id.publish_button);
		publish_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				publish();
			}
		});
	}
	
	private void publish() {
		String audioPath = getIntent().getExtras().getString("FILE");
		String title = ((EditText) findViewById(R.id.title_field)).getEditableText().toString();
		String content = ((EditText) findViewById(R.id.content_field)).getEditableText().toString();
		Intent publishServiceIntent = new Intent(RecordInfo.this, PublishService.class);
		publishServiceIntent.putExtra("AUDIO_PATH", audioPath);		
		publishServiceIntent.putExtra("TITLE", title);
		publishServiceIntent.putExtra("CONTENT", content);		
		startService(publishServiceIntent);
	}
	
	
}
