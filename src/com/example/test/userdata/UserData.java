package com.example.test.userdata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.example.test.Entrance;
import com.example.test.EntryData;
import com.example.test.R;
import com.example.test.UserList;
import com.example.test.model.Person;
import com.example.test.model.Person.Memorized_state;
import com.example.test.sql.PersonStore;

public class UserData extends AbstractUserData {
	public final static String ex_key = "User";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Object obj = getIntent().getSerializableExtra(ex_key);
		if (obj == null || !(obj instanceof Person)) {
			Toast.makeText(UserData.this, "Error@UserData: Personが渡されていません",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		hero = (Person) obj;
		setContentView(R.layout.user_data);
		// setList();
		// setTextView();
		setRadioButton();
		setButton();
	}

	private void setButton() {
		Button b;
		b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserData.this, Entrance.class);
				startActivity(intent);
			}
		});
		b = (Button) findViewById(R.id.button2);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserData.this, EntryData.class);
				intent.putExtra(PersonStore.ex_key, hero);
				startActivity(intent);
			}
		});
		b = (Button) findViewById(R.id.button3);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserData.this, UserList.class);
				startActivity(intent);
			}
		});
		b = (Button) findViewById(R.id.Button01);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserData.this, Question.class);
				intent.putExtra(PersonStore.ex_key, getIntent()
						.getSerializableExtra(PersonStore.ex_key));
				startActivity(intent);
			}
		});
	}

	private void setRadioButton() {
		RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup1);
		int r_id;
		switch (Person.Memorized_state.get(hero
				.getMemorized_state_setted_by_user())) {
				case PERFECTLY:
					r_id = R.id.RadioButton02;
					break;
				case ALMOST:
					r_id = R.id.RadioButton01;
					break;
				case YET:
					r_id = R.id.RadioButton00;
					break;
				default:
					return;
		}
		rg.check(r_id);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				String msg;
				Person.Memorized_state st;
				switch (checkedId) {
				case R.id.RadioButton02:
					msg = "おめでとうございます！　問題にほとんど出ないように設定されました";
					st = Memorized_state.PERFECTLY;
					break;
				case R.id.RadioButton01:
					msg = "問題にそれなりに出されるように設定されました";
					st = Memorized_state.ALMOST;
					break;
				case R.id.RadioButton00:
					msg = "問題によく出されるように設定されました";
					st = Memorized_state.YET;
					break;
				default:
					// どれも選択されていなければidには-1が入ってくる
					return;
				}
				Toast.makeText(UserData.this, msg, Toast.LENGTH_SHORT).show();
				hero.setMemorized_state_setted_by_user(st);
				PersonStore.updateOpenClose(hero, UserData.this);
			}
		});

	}

	@Override
	public int getProfileShowLevel() {
		return hero.getShowLevelMax();
	}
}
