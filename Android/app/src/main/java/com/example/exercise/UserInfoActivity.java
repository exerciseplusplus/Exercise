package com.example.exercise;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserInfoActivity extends Activity implements View.OnClickListener {

    @InjectView(R.id.textView_age_label)
    TextView ageLabelView;

    @InjectView(R.id.textView_age)
    TextView ageView;

    @InjectView(R.id.textView_sex_label)
    TextView sexLabelView;

    @InjectView(R.id.textView_sex)
    TextView sexView;

    @InjectView(R.id.textView_email_label)
    TextView emailLabelView;

    @InjectView(R.id.textView_email)
    TextView emailView;

    @InjectView(R.id.textView_phone_number_label)
    TextView phoneLabelView;

    @InjectView(R.id.textView_phone_number)
    TextView phoneView;

    @InjectView(R.id.textView_height_label)
    TextView heightLabelView;

    @InjectView(R.id.textView_height)
    TextView heightView;

    @InjectView(R.id.textView_weight_label)
    TextView weightLabelView;

    @InjectView(R.id.textView_weight)
    TextView weightView;

    AVUser user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        setTitle(R.string.info_student_body);
        user= AVUser.getCurrentUser();
        ButterKnife.inject(this);
        ageLabelView.setClickable(true);ageView.setClickable(true);
        sexLabelView.setClickable(true);sexView.setClickable(true);
        emailLabelView.setClickable(true);emailView.setClickable(true);
        phoneLabelView.setClickable(true);phoneView.setClickable(true);
        heightLabelView.setClickable(true);heightView.setClickable(true);
        weightLabelView.setClickable(true);weightView.setClickable(true);

        ageLabelView.setOnClickListener(this);ageView.setOnClickListener(this);
        sexLabelView.setOnClickListener(this);sexView.setOnClickListener(this);
        emailLabelView.setOnClickListener(this);emailView.setOnClickListener(this);
        phoneLabelView.setOnClickListener(this);phoneView.setOnClickListener(this);
        heightLabelView.setOnClickListener(this);heightView.setOnClickListener(this);
        weightLabelView.setOnClickListener(this);weightView.setOnClickListener(this);

        ageView.setText(user.get("age").toString());
        sexView.setText(user.get("sex").toString());
        if(user.get("email")!=null)
            emailView.setText(user.get("email").toString());
        if(user.get("mobilePhoneNumber")!=null)
            phoneView.setText(user.get("mobilePhoneNumber").toString());
        heightView.setText(user.get("height").toString());
        weightView.setText(user.get("weight").toString());

    }

    @Override
    public void onClick(View v) {
    switch (v.getId())
    {
        case R.id.textView_email_label:
        case R.id.textView_email:
            buildAlert(R.string.info_student_email_change,emailView,"email",1);break;

        case R.id.textView_phone_number_label:
        case R.id.textView_phone_number:
            buildAlert(R.string.info_student_phone_number_change,phoneView,"mobilePhoneNumber",1);break;

        case R.id.textView_sex_label:
        case R.id.textView_sex:
            buildAlert(R.string.info_student_sex_change,sexView,"sex",1);break;

        case R.id.textView_age_label:
        case R.id.textView_age:
            buildAlert(R.string.info_student_age_change,ageView,"age",2);break;

        case R.id.textView_height_label:
        case R.id.textView_height:
            buildAlert(R.string.info_student_height_change,heightView,"height",2);break;

        case R.id.textView_weight_label:
        case R.id.textView_weight:
            buildAlert(R.string.info_student_weight_change,weightView,"weight",2);break;
        default:break;

    }
    }

    public void buildAlert(int titleId,final TextView showInfo,final String key,final int type)
    {
        final EditText inputInfo = new EditText(this);
        inputInfo.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(titleId)).setIcon(
                R.drawable.ic_launcher).setView(inputInfo).setNegativeButton(
                getString(R.string.cancel), null);
        builder.setPositiveButton(getString(R.string.ok),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        String studentInfo = inputInfo.getText().toString();
                        showInfo.setText(studentInfo);
                        Log.d("Info", studentInfo);
                        if(type==1)
                            user.put(key, studentInfo);
                        else
                            user.put(key, Integer.parseInt(studentInfo));
                        user.setFetchWhenSave(true);
                        user.saveInBackground();
                    }
                });
        builder.show();
    }

}
