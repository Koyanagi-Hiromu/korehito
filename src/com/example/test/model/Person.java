package com.example.test.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;

public class Person implements Serializable {
	public String getFirst_met_location() {
		return first_met_location;
	}

	public void setFirst_met_location(String first_met_location) {
		if (first_met_location == null || first_met_location.trim().isEmpty()) {
			first_met_location = "（未登録）";
		}
		this.first_met_location = first_met_location;
	}

	public Date getCreated_date() {
		return created_date;
	}

	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}

	public Date getLast_modified_date() {
		return last_modified_date;
	}

	public void setLast_modified_date(Date last_modified_date) {
		this.last_modified_date = last_modified_date;
	}

	public int get_id() {
		return id;
	}

	public void set_id(int _id) {
		this.id = _id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private static final long serialVersionUID = -1495684503280169545L;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHiragana() {
		return hiragana;
	}

	public void setHiragana(String hiragana) {
		this.hiragana = hiragana;
	}

	public int getQuestion_thrown_count() {
		return question_thrown_count;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		if (profile == null || profile.trim().isEmpty()) {
			profile = "（未登録）";
		}
		this.profile = profile;
	}

	public void setQuestion_thrown_count(int question_thrown_count) {
		this.question_thrown_count = question_thrown_count;
	}

	public int getQuestion_correctly_answer_count() {
		return question_correctly_answer_count;
	}

	public void setQuestion_correctly_answer_count(
			int question_correctly_answer_count) {
		this.question_correctly_answer_count = question_correctly_answer_count;
	}

	public byte getMemorized_state_setted_by_user() {
		return memorized_state_setted_by_user;
	}

	public void setMemorized_state_setted_by_user(
			byte memorized_state_setted_by_user) {
		this.memorized_state_setted_by_user = memorized_state_setted_by_user;
	}

	public void addCollectelyAnsweredCount() {
		setQuestion_correctly_answer_count(getQuestion_correctly_answer_count() + 1);
		addAnsweredCount_CollectllyOrNot();
	}

	public void addAnsweredCount_CollectllyOrNot() {
		setQuestion_thrown_count(getQuestion_thrown_count() + 1);
	}

	public int getPercentOfCollectlyAnswerCount() {
		if (getQuestion_thrown_count() == 0) {
			return 0;
		}
		return 100 * getQuestion_correctly_answer_count()
				/ getQuestion_thrown_count();
	}

	public enum Parameter {
		// パラメータの追加時に順番を変えないこと!
		name(String.class), hiragana(String.class), first_met_location(
				String.class), profile(String.class), created_date(String.class), last_modified_date(
						String.class), id(int.class), question_thrown_count(int.class), question_correctly_answer_count(
								int.class), memorized_state_setted_by_user(int.class)

								;
		public String toUpperString() {
			return toString().toUpperCase(Locale.ENGLISH);
		}

		public final int version;
		public final Class<?> sql_data_type;

		private Parameter(Class<?> clazz) {
			this(clazz, 1);
		}

		private Parameter(Class<?> clazz, int i) {
			sql_data_type = clazz;
			version = i;
		}

		public static String[] valuesToStrings() {
			Parameter[] values = values();
			String[] arr = new String[values.length];
			for (int i = 0; i < arr.length; i++) {
				arr[i] = values[i].toUpperString();
			}
			return arr;
		}
	}

	private String name, hiragana, first_met_location, profile;
	private Date created_date, last_modified_date;
	private int id, question_thrown_count, question_correctly_answer_count;
	private byte memorized_state_setted_by_user;

	/**
	 * Use the member:FLAG in order to complete memorized_stat_setted_by_user
	 * 
	 * @author koyanagihirogayume
	 * 
	 */
	public enum Memorized_state {
		PERFECTLY(2, 5), ALMOST(1, 25), YET(0, 70);
		/**
		 * warning: this is a byte data.
		 */
		public final byte FLAG;
		public final int WEIGHT;

		private Memorized_state(int i, int weight) {
			FLAG = (byte) i;
			WEIGHT = weight;
		}

		public static Memorized_state get(int i) {
			return get((byte) i);
		}

		public static Memorized_state get(byte i) {
			for (Memorized_state memorized_state : values()) {
				if (memorized_state.FLAG == i) {
					return memorized_state;
				}
			}
			return null;
		}
	}

	public static Person createInstance(String name, String hiragana,
			String first_met_location, String profile) throws Exception {
		if (name == null || name.isEmpty()) {
			throw new Exception("名前を登録してください");
		}
		if (hiragana == null || hiragana.isEmpty()) {
			throw new Exception("よみを登録してください");
		}
		if (!hiragana.matches("^[a-zA-Z\\u3040-\\u309F]+$")) {
			throw new Exception("よみは英字とひらがなのみ受け付けています");
		}
		return new Person(name, hiragana, first_met_location, profile);
	}

	private Person(String name, String hiragana, String first_met_location,
			String profile) {
		setName(name);
		setHiragana(hiragana);
		setFirst_met_location(first_met_location);
		setProfile(profile);
		setCreated_date(new Date());
		setLast_modified_date(new Date());
		set_id(-1);
		setQuestion_thrown_count(0);
		setQuestion_correctly_answer_count(0);
		setMemorized_state_setted_by_user(Memorized_state.YET);
	}

	public final static String date_format = "yyyy/MM/dd";

	public Person(Cursor c) {
		int i = 0;
		name = c.getString(i++);
		hiragana = c.getString(i++);
		first_met_location = c.getString(i++);
		profile = c.getString(i++);
		SimpleDateFormat format = new SimpleDateFormat(date_format,
				Locale.ENGLISH);
		format.setLenient(false);
		try {
			created_date = format.parse(c.getString(i++));
		} catch (ParseException e) {
			created_date = new Date();
		}
		try {
			last_modified_date = format.parse(c.getString(i++));
		} catch (ParseException e) {
			last_modified_date = new Date();
		}
		id = c.getInt(i++);
		question_thrown_count = c.getInt(i++);
		question_correctly_answer_count = c.getInt(i++);
		memorized_state_setted_by_user = (byte) c.getInt(i++);
	}

	public void setMemorized_state_setted_by_user(Memorized_state state) {
		setMemorized_state_setted_by_user(state.FLAG);
	}

	public int getShowLevelMax() {
		return 10;
	}

	public int getShowLevelLow() {
		return 1;
	}

	public int getShowLevelMin() {
		return 0;
	}

	public ArrayList<String[]> getProfileList(int show_level) {
		boolean show_all = show_level == getShowLevelMax();
		boolean show_min = show_level == getShowLevelMin();
		boolean show_low = show_level == getShowLevelLow();
		ArrayList<String[]> list = new ArrayList<String[]>();
		String name;
		String[] arr1 = { getName(), getHiragana() };
		String[] arr2 = { "名前", "よみ" };
		for (int i = 0; i < arr1.length; i++) {
			name = arr1[i];
			if (show_low) {
				if (!name.isEmpty()) {
					name = name.charAt(0) + name.replaceAll(".", "？");
					if (name.length() > 1) {
						name = name.substring(0, name.length() - 1);
					}
				}
			} else if (show_min) {
				name = name.replaceAll(".", "？");
			}
			list.add(new String[] { name, arr2[i] });
		}
		list.add(new String[] { getFirst_met_location(), "初めて出会った場所" });
		if (show_all) {
			list.add(new String[] {
					"問題正答率：".concat(
							String.valueOf(getPercentOfCollectlyAnswerCount()))
							.concat("％"),
							"正答：".concat(
									String.valueOf(getQuestion_correctly_answer_count()))
									.concat("　問題数：")
									.concat(String.valueOf(getQuestion_thrown_count())) });
			list.add(new String[] {
					"登録日付：".concat(toStringFromDate(getCreated_date())),
					"修正日付：".concat(toStringFromDate(getLast_modified_date())) });

		}
		// list.add(new String[] { "プロフィール", getProfile() });
		return list;
	}

	@Deprecated
	public String getProfileWithBreaking() {
		return getProfile();
		// StringBuilder sb = new StringBuilder();
		// for (String s : getProfile()) {
		// sb.append(s);
		// sb.append("\n");
		// }
		// return sb.toString();
	}

	public void saveData(ContentValues val) {
		// PARAM_V1
		val.put(Parameter.name.toUpperString(), name);
		val.put(Parameter.hiragana.toUpperString(), hiragana);
		val.put(Parameter.first_met_location.toUpperString(),
				first_met_location);
		val.put(Parameter.profile.toUpperString(), profile);
		// val.put(Parameter_v1.id.toUpperString(), id);
		val.put(Parameter.question_thrown_count.toUpperString(),
				question_thrown_count);
		val.put(Parameter.question_correctly_answer_count.toUpperString(),
				question_correctly_answer_count);
		val.put(Parameter.memorized_state_setted_by_user.toUpperString(),
				memorized_state_setted_by_user);
		val.put(Parameter.created_date.toUpperString(),
				toStringFromDate(created_date));
		val.put(Parameter.last_modified_date.toUpperString(),
				toStringFromDate(last_modified_date));
		// PARAM_V2
		// ... is null
	}

	private String toStringFromDate(Date date) {
		return new SimpleDateFormat(date_format, Locale.ENGLISH).format(date);
	}

	public void upDate() {
		setLast_modified_date(new Date());
	}
}
