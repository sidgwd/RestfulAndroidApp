package com.restfulexample.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.restfulexample.Adapter.UserAdapter;
import com.restfulexample.Utill.AppConfig;
import com.restfulexample.Helper.CallWebservice;
import com.restfulexample.Model.UserDetail;
import com.restfulexample.R;

import java.util.ArrayList;

/**
 * @author siddhesh gawde trainer at suven consultant
 * @since 2016
 */
public class MainActivity extends Activity {
    //declare the variables here
    public static ListView lvUsers;
    public static UserAdapter userAdapter;
    public static ArrayList<UserDetail> lstUsers;
    public static UserTask usersTask;
    public static ProgressDialog pd;
    public static UserDetail userDetail;
    public static EditText etName, etProfession;
    public static Button btnAdd;
    public static TextView tvNoRecords;
    public static TextView tvHiddenId;
    private static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //set Actionbar color
        getActionBar().setBackgroundDrawable(new ColorDrawable(0xff00DDED));
        //initialize the veriable here(registration)
        lstUsers = new ArrayList<UserDetail>();
        etName = (EditText) findViewById(R.id.etName);
        etProfession = (EditText) findViewById(R.id.etProfession);
        tvNoRecords = (TextView) findViewById(R.id.tvNoRecords);
        tvHiddenId = (TextView) findViewById(R.id.tvHiddenId);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        lvUsers = (ListView) findViewById(R.id.lvUsers);
        //call async task
        usersTask = new UserTask(AppConfig.VIEW_ALL_USERS);
        usersTask.execute();


    }

    /**
     * @param view
     */
    public void onAddClick(View view) {
        //check if the form is valid or not by using isValid Method
        if (isValid()) {
            //initialize the object
            userDetail = new UserDetail();
            //set the values here
            userDetail.setName(etName.getText().toString());
            userDetail.setProfession(etProfession.getText().toString());
            if (((Button) view).getText().equals("ADD")) {
                //call async task
                usersTask = new UserTask(AppConfig.CREATE_USERS);
                usersTask.execute();
            } else if (((Button) view).getText().equals("UPDATE")) {
                //we need ids to update the specific records here
                userDetail.setId(Integer.parseInt(tvHiddenId.getText().toString()));
                //call async task
                usersTask = new UserTask(AppConfig.UPDATE_USERS);
                usersTask.execute();
            }


            clearFields();
        }

    }

    private boolean isValid() {
        int error = 0;

        if (etName.getText().toString().isEmpty()) {
            etName.setError("Please enter the name of user");
            if (error == 0) {
                etName.requestFocus();
            }
            //increment
            error++;
        }


        if (etProfession.getText().toString().isEmpty()) {
            etProfession.setError("Please enter the name of user");
            if (error == 0) {
                etProfession.requestFocus();
            }
            //increment
            error++;
        }

        return error == 0;

    }


    public static void bindUsers() {
        //bind the list here
        userAdapter = new UserAdapter(context, lstUsers);
        lvUsers.setAdapter(userAdapter);
    }

    private void clearFields() {
        //clear fields here
        btnAdd.setText("ADD");
        etName.setText("");
        etProfession.setText("");
        tvHiddenId.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_home, menu);


        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh) {
            clearFields();
            usersTask = new UserTask(AppConfig.VIEW_ALL_USERS);
            usersTask.execute();
        }
        return super.onOptionsItemSelected(item);
    }

    class UserTask extends AsyncTask<Void, Void, ArrayList<UserDetail>> {

        String opertaionType = "";

        UserTask(String opertaionType) {
            this.opertaionType = opertaionType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showing cancelable progress dialog
            try {
                pd = new ProgressDialog(MainActivity.this);
                pd.setMessage("Requesting\nPlease wait...");
                pd.setCanceledOnTouchOutside(false);
                pd.setCancelable(false);
                pd.setIndeterminate(true);
                // on cancel event cancel currently running AsyncTask
                pd.setButton(DialogInterface.BUTTON_NEGATIVE,
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (usersTask != null) {
                                    dialog.cancel();
                                    usersTask.cancel(true);
                                    usersTask = null;
                                }
                            }
                        });
                pd.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ArrayList<UserDetail> doInBackground(Void... request) {

            if (opertaionType.equals(AppConfig.VIEW_ALL_USERS)) {
                return CallWebservice.viewUserList();
            } else if (opertaionType.equals(AppConfig.CREATE_USERS)) {
                return CallWebservice.createUser(userDetail);
            } else if (opertaionType.equals(AppConfig.UPDATE_USERS)) {
                return CallWebservice.updateUser(userDetail);
            }
            return null;

        }


        @Override
        protected void onPostExecute(ArrayList<UserDetail> result) {
            super.onPostExecute(result);

            if (result != null && !result.isEmpty()) {
                lstUsers = result;
                tvNoRecords.setVisibility(View.GONE);
                lvUsers.setVisibility(View.VISIBLE);
                bindUsers();
            } else {
                tvNoRecords.setVisibility(View.VISIBLE);
                lvUsers.setVisibility(View.GONE);
            }

////////////////this part is optional
            // Check if no view has focus:
            View view = MainActivity.this.getCurrentFocus();
            //hide keyboard
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
//////////////////////////////////
            //dispose the async task
            if (usersTask != null) {
                usersTask.cancel(true);
                usersTask = null;
            }
            //hide progress dialog here
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }
        }
    }


}
