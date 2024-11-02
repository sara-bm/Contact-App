package com.example.tp1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<User> userList;
    private DatabaseHelper db;

    public UserAdapter(Context context, ArrayList<User> userList, DatabaseHelper db) {
        this.context = context;
        this.userList = userList;
        this.db = db;
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //initiate xml layout that includes phone edit delete design
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.textViewName);
        TextView textViewTel = convertView.findViewById(R.id.textViewTel);
        ImageView buttonCall = convertView.findViewById(R.id.buttonCall);
        ImageView buttonEdit = convertView.findViewById(R.id.buttonEdit);
        ImageView buttonDelete = convertView.findViewById(R.id.buttonDelete);

        final User user = userList.get(position);
        textViewName.setText(user.getName());
        textViewTel.setText(user.getTel());

        buttonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + user.getTel()));
                context.startActivity(callIntent);
            }
        });
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Edit this contact")
                        .setMessage("")
                        .setView(LayoutInflater.from(context).inflate(R.layout.edit_contact, parent, false))
                        .show();
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete this user?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteData(String.valueOf(user.getId()));
                                userList.remove(position);
                                //refresh the adapter and update the UI.
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        return convertView;
    }
}

