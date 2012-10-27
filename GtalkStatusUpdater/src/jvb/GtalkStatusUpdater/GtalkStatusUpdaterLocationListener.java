package jvb.GtalkStatusUpdater;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GtalkStatusUpdaterLocationListener implements LocationListener {
	public static  LocationListener locationListener = new GtalkStatusUpdaterLocationListener();
	public static Boolean active = false;
	public static String currentLocation;
	public static String username;
	public static String password;
	public static String serverName;
	public static String host;
	public static String port;
	public static Boolean fullAddress = false;
	public static Boolean autoReconnect = true;
	public static XMPPConnection connection;
	
	@Override
	public void onLocationChanged(Location location)
	{
		try 
		{
//			ConnectionConfiguration config = new ConnectionConfiguration(host, Integer.parseInt(port), serverName);
//			XMPPConnection connection = new XMPPConnection(config);
			if(!connection.isConnected() && autoReconnect)
			{
				connection.connect();
				connection.login(username, password);
			}
			Presence presence = new Presence(Presence.Type.available);
			Geocoder geocoder = new Geocoder(GtalkStatusUpdaterActivity.getContext(),
					Locale.getDefault());
			List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),
					location.getLongitude(), 1);
			
			if(addresses.size() >0)
			{
				String locationInfo = "";

				if(fullAddress && addresses.get(0).getMaxAddressLineIndex()>-1)
				{
					for(int index=0; index <addresses.get(0).getMaxAddressLineIndex() + 1; index++)
					{
						if(locationInfo.length()>0){
							locationInfo += ", ";
						}
						locationInfo += addresses.get(0).getAddressLine(index);
					}
				}
				else
				{
					if(addresses.get(0).getSubLocality() != null)
					{
						locationInfo += addresses.get(0).getSubLocality() + ", ";
					}
					if(addresses.get(0).getLocality() != null)
					{
						locationInfo += addresses.get(0).getLocality() + ", ";
					}
					
					if(addresses.get(0).getAdminArea() != null)
					{
						locationInfo +=	 addresses.get(0).getAdminArea();
					}
				}
					
				GtalkStatusUpdaterActivity.setLocationInfo(locationInfo);
				if(currentLocation != locationInfo)
				{
					currentLocation = locationInfo;
					presence.setStatus(locationInfo);
					presence.setPriority(24);
					//presence.setMode(Mode.available);
				    
					connection.sendPacket(presence);
					//connection.disconnect();
				}
			}
			
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block 
			GtalkStatusUpdaterActivity.setLocationInfo(e.getMessage());
		} 
		catch (Exception e) 
		{
			GtalkStatusUpdaterActivity.setLocationInfo(e.getMessage());
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
