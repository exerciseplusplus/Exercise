package com.example.exercise;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;

import java.io.ByteArrayOutputStream;


public class MeFragment extends Fragment{

    private View mParent;
    private FragmentActivity mActivity;
    AVUser user ;

    TextView studentIdLabelText;
    TextView realNameLabelText;
    TextView bodyLabelText;
    TextView numberLabelText;

    TextView studentIdText;
    TextView realNameText;
    TextView bodyText;
    TextView numberText;
    TextView logoutText;

    ImageView avatarImage;
    ImageView hintImage;
    TextView nameText;
    private static final int IMAGE_PICK_REQUEST = 0;
    private static final int CROP_REQUEST = 1;
    /**
     * Create a new instance of DetailsFragment, initialized to show the text at
     * 'index'.
     */
    public static RegisterFragment newInstance(int index) {
        RegisterFragment f = new RegisterFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        f.setArguments(args);
        return f;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container,
                false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = getActivity();
        mParent = getView();
        user= AVUser.getCurrentUser();

        studentIdLabelText =(TextView)mParent.findViewById(R.id.textView_student_id_label);
        realNameLabelText =(TextView)mParent.findViewById(R.id.textView_real_name_label);
        bodyLabelText=(TextView)mParent.findViewById(R.id.textView_body_label);
        numberLabelText=(TextView)mParent.findViewById(R.id.textView_register_num_label);

        studentIdText=(TextView)mParent.findViewById(R.id.textView_student_id);
        realNameText=(TextView)mParent.findViewById(R.id.textView_real_name);
        bodyText=(TextView)mParent.findViewById(R.id.textView_body);
        numberText=(TextView)mParent.findViewById(R.id.textView_register_num);
        logoutText=(TextView)mParent.findViewById(R.id.textView_logout);

        studentIdLabelText.setClickable(true);realNameLabelText.setClickable(true);
        bodyLabelText.setClickable(true);numberLabelText.setClickable(true);
        studentIdText.setClickable(true);realNameText.setClickable(true);
        bodyText.setClickable(true);numberText.setClickable(true);logoutText.setClickable(true);

        avatarImage=(ImageView)mParent.findViewById(R.id.ImageView_info_avatar);
        hintImage=(ImageView)mParent.findViewById(R.id.ImageView_info_hint);
        nameText=(TextView)mParent.findViewById(R.id.TextView_info_name);

        StatusUtils.displayAvatar(user, avatarImage);
        nameText.setText(user.getUsername());
        hintImage.setVisibility(View.VISIBLE);
        avatarImage.setOnClickListener(avatarListener);

        studentIdText.setText(user.get("studentId").toString());
        realNameText.setText(user.get("realName").toString());
        numberText.setText(user.get("registerNumber").toString());


        studentIdLabelText.setOnClickListener(studentIdListener);
        realNameLabelText.setOnClickListener(realNameListener);
        bodyLabelText.setOnClickListener(bodyListener);

        studentIdText.setOnClickListener(studentIdListener);
        realNameText.setOnClickListener(realNameListener);
        bodyText.setOnClickListener(bodyListener);
        logoutText.setOnClickListener(logoutListener);

    }

    OnClickListener avatarListener = new OnClickListener()
    {
        public void onClick(View arg0) {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intent, IMAGE_PICK_REQUEST);
        }
    };

    OnClickListener studentIdListener = new OnClickListener()
    {
        public void onClick(View arg0) {
            Log.d("Register","Hello");
            final EditText inputStudentId = new EditText(mActivity);
            inputStudentId.setFocusable(true);

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(getString(R.string.info_student_id_change)).setIcon(
                    R.drawable.ic_launcher).setView(inputStudentId).setNegativeButton(
                    getString(R.string.cancel), null);
            builder.setPositiveButton(getString(R.string.ok),
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            String studentId = inputStudentId.getText().toString();
                            studentIdText.setText(studentId);
                            Log.d("Info", studentId);
                            user.put("studentId", studentId);
                            user.setFetchWhenSave(true);
                            user.saveInBackground();
                        }
                    });
            builder.show();
        }
    };

    OnClickListener realNameListener = new OnClickListener()
    {
        public void onClick(View arg0) {
            final EditText inputRealName = new EditText(mActivity);
            inputRealName.setFocusable(true);

            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setTitle(getString(R.string.info_student_real_name_change)).setIcon(
                    R.drawable.ic_launcher).setView(inputRealName).setNegativeButton(
                    getString(R.string.cancel), null);
            builder.setPositiveButton(getString(R.string.ok),
                    new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            String realName = inputRealName.getText().toString();
                            realNameText.setText(realName);
                            Log.d("Info", realName);
                            user.put("realName",realName);
                            user.setFetchWhenSave(true);
                            user.saveInBackground();
                        }
                    });
            builder.show();

        }
    };

    OnClickListener bodyListener = new OnClickListener()
    {
        public void onClick(View arg0) {
            Log.d("Register","World");
            Intent intent =new Intent(mActivity,UserInfoActivity.class);
            startActivity(intent);

        }
    };


    OnClickListener logoutListener = new OnClickListener()
    {
        public void onClick(View arg0) {
            AVUser.logOut();
            Intent intent =new Intent(mActivity,LoginActivity.class);
            startActivity(intent);
            mActivity.finish();
        }
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGE_PICK_REQUEST) {
                Uri uri = data.getData();
                startAvatarCrop(mActivity, uri, 200, 200, CROP_REQUEST, getCachePath());
            } else if (requestCode == CROP_REQUEST) {
                final Bitmap bitmap = data.getExtras().getParcelable("data");
                new StatusNetAsyncTask(getContext()) {
                    @Override
                    protected void doInBack() throws Exception {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                        byte[] bytes = stream.toByteArray();
                        final AVFile file = new AVFile("file", bytes);
                        file.save();
                        user.put("avatar", file);
                        user.save();
                    }

                    @Override
                    protected void onPost(Exception e) {
                        if (StatusUtils.filterException(mActivity, e)) {
                            StatusUtils.displayAvatar(user, avatarImage);
                        }
                    }
                }.execute();

            }
        }
    }
    public  Uri startAvatarCrop(Activity activity, Uri uri, int outputX, int outputY,
                                int requestCode, String outputPath) {
        Intent intent = null;
        intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
//    Uri outputUri = Uri.fromFile(new File(outputPath));
//    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        startActivityForResult(intent, requestCode);
        return null;
    }


    public String getCachePath() {
        return getCacheDir() + "tmp";
    }
    public String getCacheDir() {
        return getContext().getCacheDir().getAbsolutePath() + "/";
    }

}
