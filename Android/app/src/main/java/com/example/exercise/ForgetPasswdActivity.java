package com.example.exercise;

//import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ForgetPasswdActivity extends Activity {


	Button forgetButton;
	EditText userEmailEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forget_password);
		setTitle(R.string.find_password);
//		AVAnalytics.trackAppOpened(getIntent());

	
		forgetButton = (Button) findViewById(R.id.button_find_password);
		userEmailEditText = (EditText) findViewById(R.id.editText_forget_password_email);
		forgetButton.setOnClickListener(registerListener);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	OnClickListener registerListener = new OnClickListener() {
		public void onClick(View arg0) {

	      String userEmail = userEmailEditText.getText().toString();
	      if(userEmail!=null)
	      {
	    	  AVUser.requestPasswordResetInBackground(userEmail,new RequestPasswordResetCallback()
	    	  {

				@Override
				public void done(AVException e) {
					// TODO Auto-generated method stub
					if (e==null)
					{
						showForget(R.string.dialog_message_title,R.string.forget_email_send);
						Intent intent =new Intent(ForgetPasswdActivity.this,LoginActivity.class);
						startActivity(intent);
						finish();
						
					}
					else
						showForget(R.string.dialog_message_title,R.string.error_forget_email);
					
				}
	    		  
	    	  });
	      }
	      else{
	    	  showForget(R.string.dialog_message_title,R.string.error_forget_email_null);
	      }
	     
		}
	};


	
	private void showForget(int title,int message) {
		new AlertDialog.Builder(this)
				.setTitle(this.getResources().getString(title))
				.setMessage(this.getResources().getString(message))
				.setNegativeButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
