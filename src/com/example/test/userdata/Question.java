package com.example.test.userdata;

import java.util.ArrayList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.test.R;
import com.example.test.model.Person;
import com.example.test.sql.PersonStore;

public class Question extends AbstractUserData {
	protected ArrayList<Person> personList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		personList = PersonStore.getAllInList(this);
		if (personList.isEmpty()) {
			Toast.makeText(this, "No Person Data is Entried.",
					Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		initHero();
		setContentView(R.layout.question);
		Fragment newFragment = new UserDataFragment();
		// ActivityにFragmentを登録する。
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		// Layout位置先の指定
		ft.replace(R.id.LinearLayout2, newFragment);
		// Fragmentの変化時のアニメーションを指定
		ft.addToBackStack(null);
		ft.commit();
		initButton();
	}

	private enum Step {
		START, HEAD_LETTER_APPEAR
	}

	private Step step = Step.START;

	private void initButton() {
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				remember(true);
			}
		});
		findViewById(R.id.button2).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (step) {
				case HEAD_LETTER_APPEAR:
					remember(false);
					return;
				case START:
					nextStepTo2();
					return;
				}
			}
		});
	}

	protected void nextStepTo2() {
		step = Step.HEAD_LETTER_APPEAR;
		Fragment newFragment = new UserDataFragment();
		// ActivityにFragmentを登録する。
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		// Layout位置先の指定
		ft.replace(R.id.LinearLayout2, newFragment);
		// Fragmentの変化時のアニメーションを指定
		ft.setTransition(FragmentTransaction.TRANSIT_ENTER_MASK);
		ft.addToBackStack(null);
		ft.commit();
		((Button) findViewById(R.id.button2)).setText("分からん！");
	}

	protected void remember(boolean success) {
		if (success) {
			hero.addCollectelyAnsweredCount();
		} else {
			hero.addAnsweredCount_CollectllyOrNot();
		}
		PersonStore.updateOpenClose(hero, this);
		Intent intent = new Intent(Question.this, UserData.class);
		intent.putExtra(UserData.ex_key, hero);
		intent.putExtra(PersonStore.ex_key, personList);
		startActivity(intent);
		overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
		finish();
	}

	private void initHero() {
		hero = null;
		int total_weight = 0;
		for (Person p : personList) {
			total_weight += Person.Memorized_state.get(p
					.getMemorized_state_setted_by_user()).WEIGHT;
		}
		int r = (int) Math.floor(Math.random() * total_weight);
		int seek = 0;
		for (Person p : personList) {
			seek += Person.Memorized_state.get(p
					.getMemorized_state_setted_by_user()).WEIGHT;
			if (r < seek) {
				hero = p;
				return;
			}
		}
	}

	@Override
	public int getProfileShowLevel() {
		if (step == Step.START) {
			return hero.getShowLevelMin();
		} else {
			return hero.getShowLevelLow();
		}
	}
}
