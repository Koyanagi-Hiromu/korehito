package com.example.test.userdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.test.R;
import com.example.test.model.Person;

public class UserDataFragment extends Fragment {
	public final static String ex_key = "Hero";
	private Person hero;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.user_data_fragment,
				container, false);
		hero = ((AbstractUserData) getActivity()).getHero();
		setList(rootView);
		setTextView(rootView);
		return rootView;
	}

	private void setTextView(View rootView) {
		TextView textView = (TextView) rootView.findViewById(R.id.textView3);
		textView.setText(hero.getProfile());
	}

	private void setList(View rootView) {
		final List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		final String A = "main", B = "sub";
		ArrayList<String[]> profileList = hero
				.getProfileList(((AbstractUserData) getActivity())
						.getProfileShowLevel());
		for (int i = 0; i < profileList.size(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(A, profileList.get(i)[0]);
			map.put(B, profileList.get(i)[1]);
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), list,
				android.R.layout.simple_expandable_list_item_2, new String[] {
			A, B }, new int[] { android.R.id.text1,
			android.R.id.text2 });

		// アイテムを追加します
		ListView listView = (ListView) rootView.findViewById(R.id.listView1);
		// アダプターを設定します
		listView.setAdapter(adapter);

	}
}
