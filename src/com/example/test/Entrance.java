package com.example.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.test.sql.PersonStore;
import com.example.test.userdata.Question;

public class Entrance extends ActionBarActivity {
	// リストに設定するメインテキスト
	private final String[] mainText = new String[] { "問題", "登録者一覧", "新規登録" };
	// リストに設定するサブテキスト
	private final String[] subText = new String[] { "ひとの名前を覚えてるかチェック！",
			"あいうえお順です", "大切なお友達を登録しよう" };
	private final int question = 0, user_list = 1, resist = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entrance);
		final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < mainText.length; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("main", mainText[i]);
			map.put("sub", subText[i]);
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list,
				android.R.layout.simple_expandable_list_item_2, new String[] {
				"main", "sub" }, new int[] { android.R.id.text1,
				android.R.id.text2 });

		// アイテムを追加します
		ListView listView = (ListView) findViewById(R.id.listView1);
		// アダプターを設定します
		listView.setAdapter(adapter);
		// リストビューのアイテムがクリックされた時に呼び出されるコールバックリスナーを登録します
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (position) {
				case question:
					intent = new Intent(Entrance.this, Question.class);
					intent.putExtra(PersonStore.ex_key, getIntent()
							.getSerializableExtra(PersonStore.ex_key));

					startActivity(intent);
					break;
				case user_list:
					intent = new Intent(Entrance.this, UserList.class);
					intent.putExtra(PersonStore.ex_key, getIntent()
							.getSerializableExtra(PersonStore.ex_key));
					startActivity(intent);
					break;
				case resist:
					intent = new Intent(Entrance.this, EntryData.class);
					startActivity(intent);
					break;
				}
			}
		});
	}
}
