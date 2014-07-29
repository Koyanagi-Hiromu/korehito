package com.example.test;

import java.util.Locale;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.test.model.RestTask;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SelectMap extends ActionBarActivity {
	private String location;
	private static GoogleMap mGoogleMap = null;
	private static MarkerOptions mMyMarkerOptions = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_map);
		mGoogleMap = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map)).getMap();
		if (mGoogleMap != null) {
			mGoogleMap
			.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
				@Override
				public void onMapClick(LatLng latLng) {
					mMyMarkerOptions = new MarkerOptions();
					mMyMarkerOptions.position(latLng);

					// 古いピンを消去する
					mGoogleMap.clear();
					// タップした位置にピンを立てる
					mGoogleMap.addMarker(mMyMarkerOptions);

					// 逆ジオコーディングでピンを立てた位置の住所を取得する
					requestReverseGeocode(latLng.latitude,
							latLng.longitude);
				}
			});
		}
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(SelectMap.this, EntryData.class);
				intent.putExtra(EntryData.ex_name,
						getIntent().getStringExtra(EntryData.ex_name));
				intent.putExtra(EntryData.ex_hiragana, getIntent()
						.getStringExtra(EntryData.ex_hiragana));
				if (location != null) {
					intent.putExtra(EntryData.ex_location, location);
				} else {
					intent.putExtra(EntryData.ex_location, getIntent()
							.getStringExtra(EntryData.ex_location));
				}
				intent.putExtra(EntryData.ex_profile, getIntent()
						.getStringExtra(EntryData.ex_profile));
				startActivity(intent);
				finish();
			}
		});
		location = getIntent().getStringExtra(EntryData.ex_location);
		showTextView("初めて出会った地点をタップしてください");
	}

	private String REVERSE_GEOCODE = "REVERSE_GEOCODE";

	@Override
	protected void onResume() {
		super.onResume();

		// 逆ジオコーディングのレシーバ登録
		registerReceiver(reverseGeoCodeReceiver, new IntentFilter(
				REVERSE_GEOCODE));
	}

	@Override
	protected void onPause() {
		super.onPause();

		// 逆ジオコーディングのレシーバ解除
		unregisterReceiver(reverseGeoCodeReceiver);
	}

	/**
	 * 逆ジオコーディング
	 * 
	 * @param latitude
	 *            緯度
	 * @param longitude
	 *            軽度
	 */
	private void requestReverseGeocode(double latitude, double longitude) {

		// Googleの逆ジオコーディング用URL(結果はJSONで受け取る)
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.encodedAuthority("maps.googleapis.com");
		builder.path("/maps/api/geocode/json");
		builder.appendQueryParameter("latlng", latitude + "," + longitude);
		builder.appendQueryParameter("sensor", "true");
		builder.appendQueryParameter("language", Locale.getDefault()
				.getLanguage());
		HttpGet request = new HttpGet(builder.build().toString());

		try {
			// REST API非同期アクセスをリクエスト
			// リクエストの結果はreverseGeoCodeReceiverで受け取る
			RestTask task = new RestTask(this, REVERSE_GEOCODE);
			task.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 逆ジオコーディングレシーバ
	 */
	private BroadcastReceiver reverseGeoCodeReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String responseJSON = intent.getStringExtra(RestTask.HTTP_RESPONSE);

			// 取得結果を解析
			parseReverseGeoCodeJSON(responseJSON);
		}
	};

	// 逆ジオコーディングで取得したJSONデータの解析
	private void parseReverseGeoCodeJSON(String str) {
		if (str == null) {
			showTextView("通信エラー", false);
			return;
		}
		try {
			String address = "";
			JSONObject rootObject = new JSONObject(str);
			JSONArray eventArray = rootObject.getJSONArray("results");

			for (int i = 0; i < eventArray.length(); i++) {
				JSONObject jsonObject = eventArray.getJSONObject(i);
				address = jsonObject.getString("formatted_address");

				if (!address.equals("")) {
					showTextView(address, true);
					return;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		showTextView("通信エラー", false);
	}

	private void showTextView(String text, boolean json_connection_sucess) {
		if (json_connection_sucess) {
			location = text;
		}
		showTextView(text);
	}

	private void showTextView(String text) {
		((TextView) findViewById(R.id.textView1)).setText(text);
	}
}
