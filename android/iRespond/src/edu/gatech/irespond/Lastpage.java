package edu.gatech.irespond;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class Lastpage extends Activity {

	DBHelper dbh;
	private static final String TABLE_NAME = "messages";

	TextView tv1, tv2, tv3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.last);

		tv1 = (TextView) findViewById(R.id.prioText);
		tv2 = (TextView) findViewById(R.id.cateText);
		tv3 = (TextView) findViewById(R.id.msgText);

		doProcess();
	}

	/*
	 * @Override public void onResume() { super.onResume(); doProcess(); }
	 */

	void doProcess() {

		dbh = new DBHelper(this);

		Intent i = getIntent();
		String msgIn = i.getExtras().getString("msg").split(":")[1];

		try {
			// EXEC THE MSG PARSING LOGIC
			if (msgIn != null) {
				if (msgIn.charAt(4) == '1') {
					// this is the last message
					// Pull all from DB
					Log.v("lastpage: ", "Last message found- " + msgIn);
					String completeMsg = lastMsgInSeqFound(msgIn);
					Log.v("COMPLETE MSG:", completeMsg);
					renderMessage(completeMsg);
				} else {
					// Add it to DB
					Log.v("lastpage: ", "Non Last message found- " + msgIn);
					addMsg(msgIn);
				}
			}
		} finally {
			Log.v("","");
			dbh.close();
		}

	}

	void renderMessage(String xmsg) {

		int priority = Integer.parseInt(xmsg.substring(0, 1));
		int category = Integer.parseInt(xmsg.substring(1, 2));
		String actualMsg = xmsg.substring(2);

		String prStr = "";
		switch (priority) {
		case 0:
			prStr = "Low";
			break;
		case 1:
			prStr = "Medium";
			break;
		case 2:
			prStr = "High";
			break;
		}

		String ctStr = "";
		switch (category) {
		case 0:
			ctStr = "Medical";
			break;
		case 1:
			ctStr = "Water";
			break;
		case 2:
			ctStr = "Food item";
			break;
		case 3:
			ctStr = "Shelter/Clothes";
			break;
		case 4:
			ctStr = "Emergency";
			break;
		case 5:
			ctStr = "Others";
			break;
		}
		
		tv1.setText(prStr);
		tv2.setText(ctStr);
		tv3.setText(actualMsg);
		
	}

	private void addMsg(String xmsg) {
		// Insert a new record into the Events data source.

		SQLiteDatabase db = dbh.getWritableDatabase();
		ContentValues values = new ContentValues();
		Log.v("-->", xmsg);
		values.put("M_ID", Integer.parseInt(xmsg.substring(0, 2))); // msg id ==
		Log.v("values: ", xmsg.substring(0, 2)); // [0,1]
		values.put("SEQ", Integer.parseInt(xmsg.substring(2, 4))); // seq
		Log.v("values: ", xmsg.substring(2, 4));
		values.put("MSG", xmsg.substring(5)); // msg
		Log.v("values: ", xmsg.substring(5));
		db.insertOrThrow(TABLE_NAME, null, values);

	}

	private String lastMsgInSeqFound(String xmsg) {

		int mid = Integer.parseInt(xmsg.substring(0, 2));
		int seq = Integer.parseInt(xmsg.substring(2, 3));
		String last_msg = xmsg.substring(5);

		final String[] FROM = { "MSG" };
		SQLiteDatabase db = dbh.getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, FROM, "M_ID =" + mid, null, null,
				null, "SEQ"); // select msg from table order by seq
		startManagingCursor(cursor);

		StringBuilder completemsg = new StringBuilder("");

		while (cursor.moveToNext()) {
			// Could use getColumnIndexOrThrow() to get indexes
			String title = cursor.getString(0);
			completemsg.append(title);
		}
		completemsg.append(last_msg);
		return completemsg.toString();
	}

}
