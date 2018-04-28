package com.restfulexample.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.restfulexample.Helper.CallWebservice;
import com.restfulexample.Activity.MainActivity;
import com.restfulexample.Model.UserDetail;
import com.restfulexample.R;

import java.util.ArrayList;

/**
 * @author siddhesh gawde trainer at suven consultant
 * @since 2016
 */
public class UserAdapter extends ArrayAdapter<UserDetail> {
    private final Context context;
    private ArrayList<UserDetail> lstUserDetails;
    DeleteTask deleteTask;
    //constructor here

    /**
     * @param context
     * @param objects
     */
    public UserAdapter(Context context,
                       ArrayList<UserDetail> objects) {
        super(context, R.layout.user_list, objects);
        this.context = context;
        this.lstUserDetails = objects;
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflating view using layout id
        if (convertView == null) {
            //set custom view here..
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.user_list, null);

        }
        try {

            //get list object like this
            final UserDetail userDetail = lstUserDetails.get(position);
            //initiaze the view
            TextView tvName = (TextView) convertView
                    .findViewById(R.id.tvName);

            TextView tvProfession = (TextView) convertView
                    .findViewById(R.id.tvProfession);

            TextView tvSrNo = (TextView) convertView
                    .findViewById(R.id.tvSrNo);

            ImageView ivEdit = (ImageView) convertView
                    .findViewById(R.id.ivEdit);
            //set the values here
            tvSrNo.setText((position + 1) + ".");
            tvName.setText(userDetail.getName());
            tvProfession.setText(userDetail.getProfession());
            //setting onclick listner here
            ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.etName.setText(userDetail.getName());
                    MainActivity.etProfession.setText(userDetail.getProfession());
                    MainActivity.btnAdd.setText("UPDATE");
                    MainActivity.tvHiddenId.setText(String.valueOf(userDetail.getId()));
                }
            });


            ImageView ivDelete = (ImageView) convertView
                    .findViewById(R.id.ivDelete);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showPopupForDeleteUser(userDetail, null,
                            "Are you sure you want to delete " + userDetail.getName() + " user?");
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


        return convertView;
    }

    /**
     * @param userDetail
     * @param title
     * @param msg
     */
    private void showPopupForDeleteUser(final UserDetail userDetail, String title, String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context);
         alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            deleteTask = new DeleteTask();
                            deleteTask.execute(userDetail);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    /**
     * Async task to delete the records
     */
    class DeleteTask extends AsyncTask<UserDetail, Void, ArrayList<UserDetail>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // showing cancelable progress dialog
            try {
                MainActivity.pd = new ProgressDialog(context);
                MainActivity.pd.setMessage("Deleting record\nPlease wait...");
                MainActivity.pd.setCanceledOnTouchOutside(false);
                MainActivity.pd.setCancelable(false);
                MainActivity.pd.setIndeterminate(true);
                // on cancel event cancel currently running AsyncTask
                MainActivity.pd.setButton(DialogInterface.BUTTON_NEGATIVE,
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                if (deleteTask != null) {
                                    dialog.cancel();
                                    deleteTask.cancel(true);
                                    deleteTask = null;
                                }
                            }
                        });
                MainActivity.pd.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected ArrayList<UserDetail> doInBackground(UserDetail... request) {
            return CallWebservice.deleteUser(request[0]);
        }


        @Override
        protected void onPostExecute(ArrayList<UserDetail> lstResult) {
            super.onPostExecute(lstResult);
            //get result and refresh the list view if valid
            if (lstResult != null && !lstResult.isEmpty()) {
                MainActivity.lstUsers = lstResult;
                MainActivity.tvNoRecords.setVisibility(View.GONE);
                MainActivity.lvUsers.setVisibility(View.VISIBLE);
                MainActivity.bindUsers();
            } else {
                MainActivity.tvNoRecords.setVisibility(View.VISIBLE);
                MainActivity.lvUsers.setVisibility(View.GONE);
            }

            //hide the progress dialog
            if (MainActivity.pd != null && MainActivity.pd.isShowing()) {
                MainActivity.pd.dismiss();
                MainActivity.pd = null;
            }
            //cancel the task
            if (deleteTask != null) {
                deleteTask.cancel(true);
                deleteTask = null;
            }


        }
    }
}
