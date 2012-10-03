package sample.application.memopad;

import java.text.DateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Selection;
import android.view.Menu;
import android.widget.EditText;

public class MemopadActiity extends Activity {

	@Override
	protected void onStop() {
		super.onStop();
		EditText et = (EditText) findViewById(R.id.editText1);
		SharedPreferences pref = getSharedPreferences("MemoPrefs", MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("memo", et.getText().toString());
		editor.putInt("cursor", Selection.getSelectionStart(et.getText()));
		editor.commit();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		EditText et = (EditText) findViewById(R.id.editText1);
		SharedPreferences pref = this.getSharedPreferences("MemoPrefs",
				MODE_PRIVATE);
		et.setText(pref.getString("memo", ""));
		et.setSelection(pref.getInt("cursor", 0));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	void saveMemo() {
		EditText et = (EditText) this.findViewById(R.id.editText1);
		String title;
		String memo = et.getText().toString();

		if (memo.trim().length() > 0) {
			if (memo.indexOf("\n") == -1) {
				title = memo.substring(0, Math.min(memo.length(), 20));
			} else {
				title = memo.substring(0, Math.min(memo.indexOf("\n"), 20));
			}
			String ts = DateFormat.getDateTimeInstance().format(new Date());
			MemoDBHelper memos = new MemoDBHelper(this);
			SQLiteDatabase db = memos.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("title", title + "\n" + ts);
			values.put("memo", memo);
			db.insertOrThrow("memoDB", null, values);
			memos.close();
		}
	}
}
