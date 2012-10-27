package jvb.GtalkStatusUpdater;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class GtalkStatusUpdaterActivity extends Activity implements OnClickListener, OnCheckedChangeListener{
	private static Context context;
	private static TextView txtInfo;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        GtalkStatusUpdaterActivity.context = getApplicationContext();
        
        txtInfo = (TextView)findViewById(R.id.txtInfo);
        
        ToggleButton btnToggle = (ToggleButton)findViewById(R.id.toggleButton1);
        btnToggle.setOnClickListener((android.view.View.OnClickListener)this);

        CheckBox chkFullAddress = (CheckBox)findViewById(R.id.chkFullAddress);
        chkFullAddress.setOnCheckedChangeListener((OnCheckedChangeListener)this);
        
        if(GtalkStatusUpdaterLocationListener.active){
        	btnToggle.setChecked(GtalkStatusUpdaterLocationListener.active);
        	chkFullAddress.setChecked(GtalkStatusUpdaterLocationListener.fullAddress);
        	
        	setLocationInfo(GtalkStatusUpdaterLocationListener.currentLocation);
        	
            EditText txtUsername = (EditText)findViewById(R.id.txtUserName);
            txtUsername.setText(GtalkStatusUpdaterLocationListener.username);
            
            EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
            txtPassword.setText(GtalkStatusUpdaterLocationListener.password);
            
            EditText txtServerName = (EditText)findViewById(R.id.txtServerName);
            txtServerName.setText(GtalkStatusUpdaterLocationListener.serverName);
            
            EditText txtPort = (EditText)findViewById(R.id.txtPort);
            txtPort.setText(GtalkStatusUpdaterLocationListener.port);
            
            EditText txtHost = (EditText)findViewById(R.id.txtHost);
            txtHost.setText(GtalkStatusUpdaterLocationListener.host);
        }
    }
    
    
    public void onClick(View v){
    	
		LocationManager locationManger = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

		ToggleButton toggleButton = (ToggleButton)findViewById(R.id.toggleButton1);
		if(toggleButton.isChecked()){
			
	        EditText txtUsername = (EditText)findViewById(R.id.txtUserName);
	        GtalkStatusUpdaterLocationListener.username = txtUsername.getText().toString();
	        
	        EditText txtPassword = (EditText)findViewById(R.id.txtPassword);
	        GtalkStatusUpdaterLocationListener.password = txtPassword.getText().toString();
	        
            EditText txtServerName = (EditText)findViewById(R.id.txtServerName);
            GtalkStatusUpdaterLocationListener.serverName = txtServerName.getText().toString();
            
            EditText txtPort = (EditText)findViewById(R.id.txtPort);
            GtalkStatusUpdaterLocationListener.port = txtPort.getText().toString();
            
            EditText txtHost = (EditText)findViewById(R.id.txtHost);
            GtalkStatusUpdaterLocationListener.host = txtHost.getText().toString();
            
            CheckBox chkFullAddress = (CheckBox)findViewById(R.id.chkFullAddress);
            GtalkStatusUpdaterLocationListener.fullAddress = chkFullAddress.isChecked();

			ConnectionConfiguration config = new ConnectionConfiguration(GtalkStatusUpdaterLocationListener.host, 
					Integer.parseInt(GtalkStatusUpdaterLocationListener.port), 
					GtalkStatusUpdaterLocationListener.serverName);

			GtalkStatusUpdaterLocationListener.connection = new XMPPConnection(config);
			try 
			{
				GtalkStatusUpdaterLocationListener.connection.connect();
				GtalkStatusUpdaterLocationListener.connection.login(GtalkStatusUpdaterLocationListener.username, GtalkStatusUpdaterLocationListener.password);
			} 
			catch (XMPPException e)
			{
				setLocationInfo(e.getMessage());
			} 
			
			locationManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 10, GtalkStatusUpdaterLocationListener.locationListener);
			GtalkStatusUpdaterLocationListener.active = true;
		}
		else
		{
			locationManger.removeUpdates(GtalkStatusUpdaterLocationListener.locationListener);
			GtalkStatusUpdaterLocationListener.active = false;
			GtalkStatusUpdaterLocationListener.currentLocation = "";
			GtalkStatusUpdaterActivity.setLocationInfo("");
			
			if(GtalkStatusUpdaterLocationListener.connection != null
					&& GtalkStatusUpdaterLocationListener.connection.isConnected())
			{
				GtalkStatusUpdaterLocationListener.connection.disconnect();
			}
//			Presence presence = new Presence(Presence.Type.available);
//			
//				presence.setStatus("");
//				presence.setPriority(24);
//				//presence.setMode(Mode.available);
//			    
//				connection.sendPacket(presence);
		}
		
    }
    
    public static void setLocationInfo(String locationInfo){
    	try{
    		txtInfo.setText(locationInfo);
    	}
    	catch (Exception e){
    		
    		
    	}
    }
    
    public static Context getContext(){
    	return context;
    }


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        CheckBox chkFullAddress = (CheckBox)findViewById(R.id.chkFullAddress);
        GtalkStatusUpdaterLocationListener.fullAddress = chkFullAddress.isChecked();
	}
}