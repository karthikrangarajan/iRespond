package edu.gatech.irespond;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class Receive extends BroadcastReceiver {
	
	private final String MY_DATABASE_NAME = "ir_SMS_DB";
	private final String MY_DATABASE_TABLE = "messages";	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			
	
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				str += "SMS from " + msgs[i].getOriginatingAddress();
				str += " :";
				str += msgs[i].getMessageBody().toString();
				str += "\n";
			}
			// ---display the new SMS message---
			Log.v("MSG generated", str);
			//Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
			
			//Intent i = new Intent(this, Lastpage.class);
			//Log.v("Entering last Page with msg: ",msgs.toString());
			Intent i = new Intent(context, Lastpage.class);
			i.putExtra("msg", str);
			i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
			
		}
	}
}