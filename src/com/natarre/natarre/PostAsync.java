package com.natarre.natarre;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PostAsync {
	private PostListener postListener;
	private PostAsyncTask postAsyncTask;

	public interface PostListener {
		public void postCallback(JsonObject result);
	}

	public PostAsync(PostListener postListener) {
		this.postListener = postListener;
		postAsyncTask = new PostAsyncTask();
	}

	public void execute(String... strings) {
		postAsyncTask.execute(strings);
	}

	public class PostAsyncTask extends AsyncTask<String, Integer, JsonObject> {

		@Override
		protected JsonObject doInBackground(String... strings) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(strings[0]); // first string is url
															// postfix
			try {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				for (int i = 1; i < strings.length; i += 2)
					// must give odd number of params
					nameValuePairs.add(new BasicNameValuePair(strings[i], strings[i + 1]));
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs);
				Log.i("NATARRE", convertStreamToString(entity.getContent()));
				httppost.setEntity(entity);
				HttpResponse response = httpclient.execute(httppost);
				Log.i("NATARRE", "response received.");

				try {
					String jsonResponse = convertStreamToString(response.getEntity().getContent());
					JsonParser jsonParser = new JsonParser();
					JsonObject returnObj = jsonParser.parse(jsonResponse).getAsJsonObject();
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
			Log.i("NATARRE", PostAsync.this.postListener.toString());
			PostAsync.this.postListener.postCallback(result);
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
