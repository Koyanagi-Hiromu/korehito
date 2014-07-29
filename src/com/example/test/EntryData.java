package com.example.test;

import java.io.Serializable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test.model.Person;
import com.example.test.sql.PersonStore;
import com.example.test.userdata.UserData;

public class EntryData extends ActionBarActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		setContentView(R.layout.entry);
		Serializable obj = getIntent().getSerializableExtra(PersonStore.ex_key);
		if (obj != null && obj instanceof Person) {
			hero = (Person) obj;
		}
		findViewById(R.id.editText1).requestFocus();
		initButtons();
		initEditText();
	}

	private void initButtons() {
		Button b;
		b = (Button) findViewById(R.id.button2);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				initTexts();
				Intent intent = new Intent(EntryData.this, SelectMap.class);
				intent.putExtra(ex_name, name);
				intent.putExtra(ex_hiragana, hiragana);
				intent.putExtra(ex_location, first_met_location);
				intent.putExtra(ex_profile, profile);
				startActivity(intent);
			}
		});
		b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PersonStore store = new PersonStore(EntryData.this);
				Person p = createPerson();
				if (p == null) {
					Toast.makeText(EntryData.this, "登録に失敗しました",
							Toast.LENGTH_SHORT).show();
				} else {
					nextPage(p, store);
				}
				store.close();
			}
		});
		if (hero != null) {
			b.setText("修正");
			b.requestFocus();
			b = (Button) findViewById(R.id.button3);
			b.setVisibility(View.VISIBLE);
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					new AlertDialog.Builder(EntryData.this)
					.setTitle("警告")
					.setMessage("削除した場合はデータの復元が出来ませんが本当によろしいですか？")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {
							delete();
						}

					})
					.setNegativeButton("CANCEL",
							new DialogInterface.OnClickListener() {
						@Override
						public void onClick(
								DialogInterface dialog,
								int which) {

						}
					}).show();
				}
			});
		}
	}

	private void delete() {
		PersonStore store = new PersonStore(EntryData.this);
		store.delete(hero);
		Intent intent = new Intent(EntryData.this, UserList.class);
		intent.setFlags(1);
		startActivity(intent);
		store.close();
		Toast.makeText(EntryData.this, "データを削除しました", Toast.LENGTH_SHORT).show();
		finish();
	}

	private Person hero = null;

	protected void nextPage(Person p, PersonStore store) {
		if (hero != null) {
			store.update(hero);
			Toast.makeText(EntryData.this, "更新しました", Toast.LENGTH_SHORT).show();
		} else {
			store.add(p);
			Toast.makeText(EntryData.this, "登録しました", Toast.LENGTH_SHORT).show();
		}
		Intent intent = new Intent(EntryData.this, UserData.class);
		intent.putExtra(UserData.ex_key, p);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}

	public static String ex_name = "name", ex_hiragana = "hiragana",
			ex_location = "first_met_location", ex_profile = "profile";
	private String name, hiragana, first_met_location, profile;

	private void initEditText() {
		int[] ids = new int[] { R.id.editText1, R.id.EditText01,
				R.id.editText3, R.id.editText2 };
		String[] arr;
		if (hero != null) {
			arr = new String[] { hero.getName(), hero.getHiragana(),
					hero.getFirst_met_location(), hero.getProfile() };
		} else {
			arr = new String[] { ex_name, ex_hiragana, ex_location, ex_profile };
			for (int i = 0; i < arr.length; i++) {
				arr[i] = getIntent().getStringExtra(arr[i]);
			}
		}
		for (int i = 0; i < ids.length; i++) {
			String s = arr[i];
			if (s != null) {
				if (i != 3) {
					s = s.replaceAll("\n", "");
				}
				((EditText) findViewById(ids[i])).setText(s);
			}
		}
	}

	private void initTexts() {
		name = ((EditText) findViewById(R.id.editText1)).getText().toString();
		hiragana = ((EditText) findViewById(R.id.EditText01)).getText()
				.toString();
		first_met_location = ((EditText) findViewById(R.id.editText3))
				.getText().toString();
		profile = ((EditText) findViewById(R.id.editText2)).getText()
				.toString();
	}

	protected Person createPerson() {
		initTexts();
		if (hero != null) {
			hero.setName(name);
			hero.setHiragana(hiragana);
			hero.setFirst_met_location(first_met_location);
			hero.setProfile(profile);
			hero.upDate();
			return hero;
		} else {
			try {
				return Person.createInstance(name, hiragana,
						first_met_location, profile);
			} catch (Exception e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
				return null;
			}
		}
	}

}
