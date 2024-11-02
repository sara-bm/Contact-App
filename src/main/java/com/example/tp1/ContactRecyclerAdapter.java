package com.example.tp1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ContactRecyclerAdapter extends RecyclerView.Adapter< ContactRecyclerAdapter.MyViewHolder> implements Filterable{
    Context con;
    ArrayList<User>data;

    public void setDataFull(ArrayList<User> dataFull) {
        this.dataFull = dataFull;
    }

    ArrayList<User> dataFull;
    DatabaseHelper db;
    public ContactRecyclerAdapter(Context con, ArrayList<User> data, DatabaseHelper db) {
        this.con = con;
        this.data = data;
        this.dataFull = new ArrayList<>(data);
        this.db = db;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf=LayoutInflater.from(con);
        View v=inf.inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User c=data.get(position);
        holder.textViewName.setText(c.getName());
        holder.textViewTel.setText(c.getTel());
        holder.textViewNickname.setText(c.getNickname());
//        holder.buttonCall.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                callIntent.setData(Uri.parse("tel:" + c.getTel()));
//                con.startActivity(callIntent);
//            }
//        });
        holder.buttonCall.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(con, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // Request permission if not already granted
                ActivityCompat.requestPermissions((Activity) con, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
            } else {
                // Permission is granted, make the call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + c.getTel()));
                con.startActivity(callIntent);
            }
        });
//        holder.buttonEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(con, EditContact.class);
//                intent.putExtra("USER_ID", String.valueOf(c.getId()));
//                intent.putExtra("USER_NAME", c.getName());
//                intent.putExtra("USER_TEL", c.getTel());
//                con.startActivity(intent);
//            }
//        });
        holder.buttonEdit.setOnClickListener(v -> showEditDialog(holder.getAdapterPosition(), c));
        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(con)
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete this user?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                db.deleteData(String.valueOf(c.getId()));
                                data.remove(data.indexOf(c));
                                dataFull.remove(dataFull.indexOf(c));
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
    }
    private void showEditDialog(int position, User user) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(con);
        dialogBuilder.setTitle("Edit Contact");

        // Inflate the custom layout for editing
        View dialogView = LayoutInflater.from(con).inflate(R.layout.edit_contact, null);
        dialogBuilder.setView(dialogView);

        EditText editName = dialogView.findViewById(R.id.editNameM);
        EditText editTel = dialogView.findViewById(R.id.editTelM);
        EditText editNickname = dialogView.findViewById(R.id.editNicknameM);

        // Set current user data in fields
        editName.setText(user.getName());
        editTel.setText(user.getTel());
        editNickname.setText(user.getNickname());

        dialogBuilder.setPositiveButton("Save", (dialog, which) -> {
            String newName = editName.getText().toString().trim();
            String newTel = editTel.getText().toString().trim();
            String newNickname = editNickname.getText().toString().trim();

            // Validate input
            if (newName.isEmpty() || newNickname.isEmpty() || newTel.isEmpty() || newTel.length() != 8 || !newTel.matches("\\d+")) {
                Toast.makeText(con, "Please enter valid name and an 8-digit phone number", Toast.LENGTH_SHORT).show();
                return;
            }
            // Update database and list
            db.updateData(String.valueOf(user.getId()), newName, newTel,newNickname);
            user.setName(newName);
            user.setTel(newTel);
            user.setNickname(newNickname);
            notifyItemChanged(position);
        });

        dialogBuilder.setNegativeButton("Cancel", null);
        dialogBuilder.create().show();
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewTel;
        TextView textViewNickname;
        ImageView buttonCall;
        ImageView buttonEdit;
        ImageView buttonDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewTel = itemView.findViewById(R.id.textViewTel);
            textViewNickname = itemView.findViewById(R.id.textViewNickname);
            buttonCall = itemView.findViewById(R.id.buttonCall);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
//            buttonCall.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int index=getAdapterPosition();
//                    User user=data.get(index);
//                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
//                    callIntent.setData(Uri.parse("tel:" + user.getTel()));
//
//                }
//            });
//            buttonEdit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int index=getAdapterPosition();
//                    User user=data.get(index);
//                    Intent intent = new Intent(con, EditContact.class);
//                    intent.putExtra("USER_ID", String.valueOf(user.getId()));
//                    intent.putExtra("USER_NAME", user.getName());
//                    intent.putExtra("USER_TEL", user.getTel());
//                    con.startActivity(intent);
//                }
//            });
//            buttonDelete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int index=getAdapterPosition();
//                    User user=data.get(index);
//                    new AlertDialog.Builder(con)
//                            .setTitle("Delete Confirmation")
//                            .setMessage("Are you sure you want to delete this user?")
//                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int which) {
//                                    db.deleteData(String.valueOf(user.getId()));
//                                    data.remove(index);
//                                    notifyDataSetChanged();
//                                }
//                            })
//                            .setNegativeButton("No", null)
//                            .show();
//                }
//            });
        }
    }
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                ArrayList<User> filteredList = new ArrayList<>();

                if (charString.isEmpty()) {
                    filteredList.addAll(dataFull); // No filter applied, return full list
                } else {
                    for (User user : dataFull) {
                        if (user.getName().toLowerCase().contains(charString.toLowerCase()) ||
                                user.getTel().contains(charString) || // You can also search by telephone number
                                user.getNickname().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(user);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                Log.d("Filtering", "Results found: " + dataFull.size());
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data.clear();
                data.addAll((ArrayList<User>) results.values);
                notifyDataSetChanged();
            }
        };
    }
}
