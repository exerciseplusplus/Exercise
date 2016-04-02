package com.example.exercise;

//import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SaveCallback;

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
import android.widget.Toast;

import java.util.List;

public class LoginActivity extends Activity {


	Button loginButton;
	Button registerButton;
	Button forgetButton;
	EditText userNameEditText;
	EditText userPasswordEditText;
	private ProgressDialog progressDialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		setTitle(R.string.login_button_login);
		
	
/*		AVObject testObject = new AVObject("TestObject");
        testObject.put("words","Hello World!");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    Log.d("saved","success!");
                }
            }
        });
        */

//		AVAnalytics.trackAppOpened(getIntent());
		loginButton = (Button) findViewById(R.id.button_login);
		registerButton = (Button) findViewById(R.id.button_register);
		forgetButton = (Button) findViewById(R.id.button_forget_password);
		userNameEditText = (EditText) findViewById(R.id.editText_userName);
		userPasswordEditText = (EditText) findViewById(R.id.editText_userPassword);
		loginButton.setOnClickListener(loginListener);
		registerButton.setOnClickListener(registerListener);
		forgetButton.setOnClickListener(forgetListener);


	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	OnClickListener loginListener = new OnClickListener() {
		public void onClick(View arg0) {

			AVUser.logInInBackground("keven", "521521", new LogInCallback() {
				public void done(AVUser user, AVException e) {
					if (e == null) {
						Log.d("Login", "Login successfully");

					}
				}
			});
	      String username = userNameEditText.getText().toString();
	      String password = userPasswordEditText.getText().toString();
		  if (!password.isEmpty()) {
				if (!username.isEmpty()) {
					AVUser.logInInBackground(username,password,new LogInCallback() {
						public void done(AVUser user, AVException e) {
							if (e==null)
							{
								progressDialogShow();
								Log.d("Login", "Login successfully");
								//showLogin(R.string.dialog_message_title, R.string.dialog_text_wait);
								Toast.makeText(LoginActivity.this,  R.string.dialog_text_wait, Toast.LENGTH_SHORT).show();
								Intent intent =new Intent(LoginActivity.this,MainActivity.class);
								startActivity(intent);
								finish();
							}
							else
							{
								showLogin(R.string.dialog_message_title,R.string.error_login_failed);
							}
						}
					});
				}
				else {
					showLogin(R.string.dialog_message_title,R.string.error_register_user_name_null);
				}
		  }
			else{
			  showLogin(R.string.dialog_message_title,R.string.error_register_password_null);
		  }
		}
	};
	@SuppressWarnings({ "unchecked", "rawtypes" })
	OnClickListener registerListener = new OnClickListener() {
		public void onClick(View arg0) {
			Intent intent =new Intent(LoginActivity.this,RegisterActivity.class);
			startActivity(intent);
		}
	};
	@SuppressWarnings({ "unchecked", "rawtypes" })
	OnClickListener forgetListener = new OnClickListener() {
		public void onClick(View arg0) {
			Intent intent =new Intent(LoginActivity.this,ForgetPasswdActivity.class);
			startActivity(intent);
		}
	     
	};
	
	
	
	private void showLogin(int title,int message) {
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
	
	private void progressDialogShow() {
		progressDialog = ProgressDialog
				.show(this,
						this.getResources().getText(
								R.string.dialog_message_title),
						this.getResources().getText(
								R.string.dialog_text_wait), true, false);
		progressDialog.dismiss();
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
