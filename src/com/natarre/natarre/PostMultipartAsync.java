package com.natarre.natarre;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PostMultipartAsync {
	private PostAsyncTask postAsyncTask;
	private PostMListener postMListener;
	private File[] files;
	
	public interface PostMListener {
		public void postCallback(JsonObject result);
	}
	
	public PostMultipartAsync(PostMListener postMListener) {
		this.postMListener = postMListener;
		postAsyncTask = new PostAsyncTask();
	}
	
	public void setFiles(File...files) {
		this.files = files;
	}
	
	public void execute(String...strings) { 
		postAsyncTask.execute(strings);		
	}

	public class PostAsyncTask extends AsyncTask<String, Integer, JsonObject> {

		@Override
		protected JsonObject doInBackground(String... strings) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(strings[0]);
			try {
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				for (int i = 1; i < strings.length; i += 2)
					entity.addPart(new FormBodyPart(strings[i], new StringBody(strings[i+1])));
				//for (File f : files)
				File f = files[0];
				entity.addPart(new FormBodyPart("audio", new FileBody(f)));
				httppost.setEntity(entity);
				//Log.i("NATARRE", convertStreamToString(entity.getContent()));
				HttpResponse response = httpclient.execute(httppost);
				try {
					String jsonResponse = convertStreamToString(response
							.getEntity().getContent());
					Log.i("NATARRE", jsonResponse);
					JsonParser jsonParser = new JsonParser();
					JsonObject returnObj = jsonParser.parse(jsonResponse)
							.getAsJsonObject();
					response.getEntity().consumeContent();
					return returnObj;
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(JsonObject result) {
			PostMultipartAsync.this.postMListener.postCallback(result);
		}
	}

	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		while ((line = reader.readLine()) != null) {
			sb.append(line); 
		}

		is.close();

		return sb.toString();
	}
}
