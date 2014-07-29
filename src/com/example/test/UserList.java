package com.example.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test.model.Person;
import com.example.test.sql.PersonStore;
import com.example.test.userdata.UserData;

public class UserList extends ActionBarActivity {

	private ArrayList<Person> personList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		personList = PersonStore.getAllInList(this);
		if (personList.isEmpty()) {
			Toast.makeText(this, "No Person Data is Entried.",
					Toast.LENGTH_SHORT).show();
			if (getIntent().getFlags() == 1) {
				startActivity(new Intent(this, Entrance.class));
			}
			finish();

			return;
		}
		setContentView(R.layout.user_list);
		setListView();
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(UserList.this, Entrance.class));
			}
		});
		StringBuilder sb = new StringBuilder();
		sb.append("【登録件数：");
		sb.append(personList.size());
		sb.append("件】　【あいうえお順】");
		((TextView) findViewById(R.id.textView1)).setText(sb.toString());
	}

	private void setListView() {
		final String A = "main", B = "sub";
		Collections.sort(personList, new Comparator<Person>() {

			@Override
			public int compare(Person lhs, Person rhs) {
				int d = lhs.getHiragana().compareTo(rhs.getHiragana());
				if (d == 0) {
					return lhs.getName().compareTo(rhs.getName());
				} else {
					return d;
				}
			}
		});
		final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (int i = 0; i < personList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			Person person = personList.get(i);
			StringBuilder sb = new StringBuilder();
			sb.append("ID@");
			int id = person.get_id();
			while (id < 1000) {
				sb.append("0");
				id *= 10;
			}
			sb.append(person.get_id());
			sb.append(" ");
			sb.append(person.getHiragana());
			map.put(A, person.getName());
			map.put(B, sb.toString());
			list.add(map);
		}

		SimpleAdapter adapter = new SimpleAdapter(this, list,
				android.R.layout.simple_expandable_list_item_2, new String[] {
				A, B }, new int[] { android.R.id.text1,
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
				Intent intent = new Intent(UserList.this, UserData.class);
				intent.putExtra(UserData.ex_key, personList.get((int) id));
				intent.putExtra(PersonStore.ex_key, personList);
				startActivity(intent);
				finish();
			}
		});
	}
}
