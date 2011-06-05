package edu.gatech.irespond;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Respond extends Activity implements OnClickListener {

	Button bt;
	SMSObject smsO;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.respond);

		bt = (Button) findViewById(R.id.button2);
		bt.setOnClickListener(this);

		this.smsO = new SMSObject();
		smsO.type = 1; // THIS IS I'll RESPOND QUERY

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button2:

			Intent i = new Intent(this, SendAndConfirm.class);
			smsO.msg = "";
			i.putExtra("sms", smsO);
			startActivity(i);
			break;

		}

	}
}
