package com.example.test.userdata;

import android.support.v7.app.ActionBarActivity;

import com.example.test.model.Person;

public abstract class AbstractUserData extends ActionBarActivity {
	protected Person hero;

	public Person getHero() {
		return hero;
	}

	public abstract int getProfileShowLevel();

}
