package com.natarre.natarre;

import static com.natarre.natarre.Constants.LOGIN_POSTFIX;
import static com.natarre.natarre.Constants.URL_PREFIX_DEBUG;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.natarre.natarre.PostAsync.PostListener;

public class Login extends Activity implements PostListener {

	ProgressDialog dialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Button loginButton = (Button) findViewById(R.id.login_button);
		loginButton.setOnClickListener(login);
	}

	private OnClickListener login = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String email = ((EditText) findViewById(R.id.email_field))
					.getEditableText().toString();
			String hashedPass = Util.md5(((EditText) findViewById(R.id.pass_field))
					.getEditableText().toString());
			String token = Util.md5(email + "Te99y");
			SharedPreferences prefs = getSharedPreferences("natarre_prefs", MODE_PRIVATE);
			Editor editor = prefs.edit();
			editor.putString("email", email);
			editor.apply();
			
			String url = URL_PREFIX_DEBUG + LOGIN_POSTFIX;
			Login.this.dialog = ProgressDialog.show(Login.this, "", "Logging you in...", true);
			PostAsync postAsync = new PostAsync(Login.this);
			postAsync.execute(url, "email", email, "password", hashedPass,"token", token);
		}
	};

	private OnClickListener register = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent registerIntent = new Intent(Login.this, Register.class);
			startActivity(registerIntent);
			finish();
		}
	};

	@Override
	public void postCallback(JsonObject result) {
		Log.i("NATARRE", "postCallback()");
		dialog.dismiss();
		if (!((JsonPrimitive) result.get("error_present")).getAsBoolean()
				&& result.has("user_id") && result.has("token")) {
			SharedPreferences settings = getSharedPreferences("natarre_prefs",
					MODE_PRIVATE);
			Editor editor = settings.edit();
			editor.putString("user_id",
					((JsonPrimitive) result.get("user_id")).getAsString());
			editor.putString("token",
					((JsonPrimitive) result.get("token")).getAsString());
			editor.apply();
			// clear your session, remove preferences, etc
			Intent intent = new Intent(getBaseContext(), Home.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else {
			// error?
		}		

	}
}
