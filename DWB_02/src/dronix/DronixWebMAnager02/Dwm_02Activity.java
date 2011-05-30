package dronix.DronixWebMAnager02;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Dwm_02Activity extends Activity {
    /** Called when the activity is first created. */
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView view=(TextView) findViewById(R.id.View1);
		 
		Button buttonDWB = (Button) findViewById(R.id.buttonDWB);
		
		 
		if(exec("/system/bin/ps").indexOf("mini_httpd") == -1){
			buttonDWB.setText("START");
			
			view.setText("DronixWebManager:\n disattivo");
			
			buttonDWB.setOnTouchListener(new OnTouchListener() {
					 
					public boolean onTouch(View arg0, MotionEvent arg1) {
						showDialog(DIALOG_PROGRESS_ID);
						 String ipWifi=getIp();
						if(ipWifi.equalsIgnoreCase("NOK")){	
							
										showDialog(DIALOG_NotNet_ID);
								    	
										//dismissDialog(DIALOG_NotNet_ID);

										// TODO Auto-generated method stub
										
									}

							
							
						
			
						return false;
					}

				});
		}
		 else {
			 buttonDWB.setText("STOP");
			 showDialog(DIALOG_PROGRESS_ID);
				if(getIp().equalsIgnoreCase("NOK")){	
					
					showDialog(DIALOG_NotNet_ID);
					
					rootExec("/system/xbin/kill `ps aux | /system/xbin/grep mini_httpd | /system/xbin/awk {'print $1'}`");
					// rootExec("/system/xbin/killall mini_httpd");
					final Toast toast =Toast.makeText(this,"DronixWebManager stop", Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 65);
					toast.setDuration(4000);
					toast.show();
					
					
					finish();
					//dismissDialog(DIALOG_NotNet_ID);

					// TODO Auto-generated method stub
					
				}

			
				else{	 
			view.setText("DronixWebManager:\n attivo on:\n"+getIp());
			
			
			 buttonDWB.setOnTouchListener(new OnTouchListener() {

					public boolean onTouch(View arg0, MotionEvent arg1) {
						showDialog(DIALOG_CONFIRM_ID);

						// TODO Auto-generated method stub
						return false;
					}

				});	 
				}
			  
						 
					    
		
			 }
		 

		

	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onRestart() {
		super.onRestart();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;

		switch (id) {
		case DIALOG_PROGRESS_ID:
			dialog = createProgressDialog();
			break;
		case DIALOG_CONFIRM_ID:
			dialog = createConfirmDialog();
			break;
		case DIALOG_NotNet_ID:
			dialog = createDialogNotNet();
			break;
			
		default:
			dialog = null;
			break;
		}
		return dialog;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		final Toast toast =Toast.makeText(this,"DronixWebManager start on: "+getIp(), Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 65);
		toast.setDuration(4000);
		switch (id) {
		case DIALOG_PROGRESS_ID:{
			final ProgressDialog ref = (ProgressDialog) dialog;
			ref.setProgress(0);
			Thread thread = new Thread(new Runnable() {
				public void run() {
				
					String script = "/system/xbin/mini_httpd -C /system/etc/mini-httpd.conf -r";
					rootExec(script);
					
					dismissDialog(DIALOG_PROGRESS_ID);
					if(!getIp().equalsIgnoreCase("NOK")){toast.show();
				
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finish();
				}}
				
			});
			thread.start();
			}
			break;
		case DIALOG_CONFIRM_ID: {

		}break;

		case DIALOG_NotNet_ID: {

		}break;

		
		}
	}
 
	private ProgressDialog createProgressDialog() {
		ProgressDialog progress = new ProgressDialog(this);
		progress.setTitle("Attendere");
		progress.setMessage("Operazione in corso...");
		progress.setCancelable(false);
		return progress;
	}

	private AlertDialog createConfirmDialog() {
		final Toast toast = Toast.makeText(this, "Dronix Web Manager stop",
				Toast.LENGTH_LONG);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 65);
		toast.setDuration(4000);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Conferma");
		builder.setMessage("Vuoi proseguire con l'operazione?");
		builder.setCancelable(false);
		builder.setPositiveButton("Prosegui",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Confermato!
						dismissDialog(DIALOG_CONFIRM_ID);
						rootExec("/system/xbin/kill `ps aux | /system/xbin/grep mini_httpd | /system/xbin/awk {'print $1'}`");
						// rootExec("/system/xbin/killall mini_httpd");
						try {

							Thread.sleep(2000);

						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						toast.show();
						finish();
					}
				});
		builder.setNegativeButton("Annulla",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Annullato!
						dismissDialog(DIALOG_CONFIRM_ID);
						finish();
					}
				});
		AlertDialog alert = builder.create();
		return alert;

	}

	private AlertDialog createDialogNotNet() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Avviso");
		builder.setMessage("Non sei connesso a nessuna rete");
		builder.setCancelable(false);
		builder.setPositiveButton("Chiudi", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dismissDialog(DIALOG_NotNet_ID);
				
			}
		});
		
		AlertDialog alert = builder.create();
		
		return alert;

	}
	
	public void rootExec(String command) {
		Process process = null;
		DataOutputStream shell = null;

		try {

			process = Runtime.getRuntime().exec("su");
			shell = new DataOutputStream(process.getOutputStream());
			shell.writeBytes(command + " \n");
			shell.writeBytes("exit\n");
			shell.flush();
			process.waitFor();

			process.destroy();

		} catch (Exception e) {
		} finally {
			try {
				if (shell != null) {
					shell.close();
				}
				process.destroy();
			} catch (Exception e) {
			}
		}
	}
	// Executes UNIX command.

    private String getIp() {

    	WifiManager myWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
    	WifiInfo myWifiInfo = myWifiManager.getConnectionInfo();
    	int ipAddress = myWifiInfo.getIpAddress();
    	String ipWifi=android.text.format.Formatter.formatIpAddress(ipAddress);
    	if(!ipWifi.equalsIgnoreCase("0.0.0.0")){
    		return ipWifi;
    		}
    	else {
    		
    		
    		return "NOK";
    	
    	}
    
    
    
    
    }
    private String exec(String command) {
            try {
            	   
                    Process process = Runtime.getRuntime().exec("su -c "+command);
                    BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(process.getInputStream()));
                    int read;
                    char[] buffer = new char[4096];
                    StringBuffer output = new StringBuffer();
                    while ((read = reader.read(buffer)) > 0) {
                            output.append(buffer, 0, read);
                    }
                    reader.close();
                    process.waitFor();
                    return output.toString();
            } catch (IOException e) {
                    throw new RuntimeException(e);
            } catch (InterruptedException e) {
                    throw new RuntimeException(e);
            }
    }
	

	private static final int DIALOG_PROGRESS_ID = 1;
	private static final int DIALOG_CONFIRM_ID = 2;
	private static final int DIALOG_NotNet_ID = 3;
	


}
