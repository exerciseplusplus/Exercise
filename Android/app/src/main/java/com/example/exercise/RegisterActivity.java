package com.example.exercise;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
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

public class RegisterActivity extends Activity {


	Button registerButton;
	Button phoneButton;
	EditText userNameEditText;
	EditText userEmailEditText;
	EditText userPasswordEditText;
	EditText userPasswordEditTextAgain;
	EditText userPhoneEditText;
	EditText userPhoneCheckcodeEditText;
	
	Boolean isCheck=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		AVAnalytics.trackAppOpened(getIntent());

	
		registerButton = (Button) findViewById(R.id.button_i_need_register);
		phoneButton = (Button) findViewById(R.id.button_checkcode);
		
		userNameEditText = (EditText) findViewById(R.id.editText_register_userName);
		userEmailEditText = (EditText) findViewById(R.id.editText_register_email);
		userPasswordEditText = (EditText) findViewById(R.id.editText_register_userPassword);
		userPasswordEditTextAgain = (EditText) findViewById(R.id.editText_register_userPassword_again);
		userPhoneEditText = (EditText) findViewById(R.id.editText_register_phone);
		userPhoneCheckcodeEditText = (EditText) findViewById(R.id.editText_register_checkcode);
		
		phoneButton.setOnClickListener(phoneListener);
		registerButton.setOnClickListener(registerListener);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	OnClickListener registerListener = new OnClickListener() {
		public void onClick(View arg0) {
	      String userName = userNameEditText.getText().toString();
	      String userEmail = userEmailEditText.getText().toString();
	      String password = userPasswordEditText.getText().toString();
	      String passwordAgain = userPasswordEditTextAgain.getText().toString();
	      String userPhone = userPhoneEditText.getText().toString();
	      String userPhoneCheckcode = userPhoneCheckcodeEditText.getText().toString();
    	  Log.d("Login",userPhone);
    	  Log.d("Login",userPhoneCheckcode);
	      
	      if (password.equals(passwordAgain)){

				if (!password.isEmpty()) {
					if (!userName.isEmpty()) {
						if (!userEmail.isEmpty()) {
							//verifyCode(userPhoneCheckcode,userPhone);
							Signup(userName,userEmail,password,userPhone);
						
							
						} else {
							showSignup(R.string.dialog_message_title,R.string.error_register_email_address_null);
						}
					} else {
						showSignup(R.string.dialog_message_title,R.string.error_register_user_name_null);
					}
				} else {
					showSignup(R.string.dialog_message_title,R.string.error_register_password_null);
				}
			} else {
				showSignup(R.string.dialog_message_title,R.string.error_register_password_not_equals);
			}
		}
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	OnClickListener phoneListener = new OnClickListener() {
		public void onClick(View arg0) {
	      String userPhone = userPhoneEditText.getText().toString();
	    
    	  Log.d("Login",userPhone);
    	  try {
              AVOSCloud.requestSMSCode(userPhone, "Exercise+", "Register", 10);
            } catch (AVException e) {
              e.printStackTrace();
            }
	      
		}
	};
	
	public void Signup(String userName,String userEmail,String password,String phone)
	{
	    AVUser user = new AVUser();
	    user.setUsername(userName);
	    user.setPassword(password);
	    user.setEmail(userEmail);
	    user.setMobilePhoneNumber(phone);
	    user.signUpInBackground(new SignUpCallback()
	    {

			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if (e==null)
				showSignup(R.string.dialog_message_title,R.string.success_register_success);
				else
				{
					switch(e.getCode())
					{
						case 202:
							showSignup(R.string.dialog_message_title,R.string.error_register_user_name_repeat);
			              break;
			            case 203:
			            	showSignup(R.string.dialog_message_title,R.string.error_register_email_repeat);
			              break;
			            default:
			            	showSignup(R.string.dialog_message_title,R.string.error_network);

			              break;
					
					}
				}
			}
	    	
	    });
	}
	
	
	  private void verifyCode(String code,String phone) {
		    AVOSCloud.verifySMSCodeInBackground(code, phone,
		        new AVMobilePhoneVerifyCallback() {
		          @Override
		          public void done(AVException e) {
		            if (e == null) {
		              isCheck=true;
		            } else {
		            	showSignup(R.string.dialog_message_title,R.string.error_register_checkcode);
		            }
		          }
		        });
		  }
	
	private void showSignup(int title,int message) {
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
