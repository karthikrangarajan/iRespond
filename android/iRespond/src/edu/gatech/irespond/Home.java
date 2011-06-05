package edu.gatech.irespond;

import android.app.TabActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TabHost;

public class Home extends TabActivity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TabHost host = getTabHost();
		host.addTab(host.newTabSpec("Seek Help").setIndicator("Seek Help").setContent(new Intent(this, SeekHelp.class)));
		host.addTab(host.newTabSpec("Respond").setIndicator("Respond").setContent(new Intent(this, Respond.class)));
        
        
    }
}