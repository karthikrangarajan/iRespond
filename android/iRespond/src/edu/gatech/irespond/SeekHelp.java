package edu.gatech.irespond;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class SeekHelp extends Activity implements OnClickListener {

	SMSObject smsO;
	EditText et;
	Button bt;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.seekhelp);

		this.smsO = new SMSObject();
		smsO.type = 0; // THIS IS SEEK HELP QUERY

		et = (EditText) findViewById(R.id.edit1);
		bt = (Button) findViewById(R.id.button1);
		bt.setOnClickListener(this);

		Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
				this, R.array.catgegories_array,
				android.R.layout.simple_spinner_item);
		adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner1.setAdapter(adapter1);

		Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
				this, R.array.priorities, android.R.layout.simple_spinner_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);

		spinner1.setOnItemSelectedListener(new MyOnItemSelectedListener());
		spinner2.setOnItemSelectedListener(new MyOnItemSelectedListener());

	}

	public class MyOnItemSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> parent, View view, int pos,
				long id) {
			// Toast.makeText(
			// parent.getContext(),
			// "The selection is "
			// + parent.getItemAtPosition(pos).toString(),
			// Toast.LENGTH_LONG).show();

			switch (parent.getId()) {
			case R.id.spinner1: {
				smsO.category = pos;
				Integer temp = pos;
				Log.v("pos: ", temp.toString());
				break;
			}
			case R.id.spinner2: {
				Integer temp = pos;
				Log.v("pos: ", temp.toString());
				smsO.priority = pos;
				break;
			}
			}

			Long currentTime = System.currentTimeMillis();
			Log.v("", currentTime.toString());

			Log.v("smsO:-  ", smsO.toString());

		}

		public void onNothingSelected(AdapterView parent) {
			// Do nothing.
		}
	}

	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button1:
			Intent i = new Intent(this, SendAndConfirm.class);
			smsO.msg = et.getText().toString();
			i.putExtra("sms", smsO);
			startActivity(i);
			break;
		}

	}

}
