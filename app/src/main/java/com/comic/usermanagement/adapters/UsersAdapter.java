package com.comic.usermanagement.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comic.usermanagement.R;
import com.comic.usermanagement.interfaces.OnUserClickListener;
import com.comic.usermanagement.models.Users;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private ArrayList<Users> usersArrayList;
    private Context context;

    private OnUserClickListener clickListener;

    public UsersAdapter(ArrayList<Users> usersArrayList, Context context,
                        OnUserClickListener userClickListener) {
        this.usersArrayList = usersArrayList;
        this.context = context;
        this.clickListener = userClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.users_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Users users = usersArrayList.get(position);
        Log.e("user id 1", users.id);

        holder.nameTV.setText(users.username);
        holder.dobTV.setText(users.dob);
        holder.addressTV.setText(users.address);
        holder.contactTV.setText(users.contact);
        holder.emailTV.setText(users.email);
        holder.genderTV.setText(users.gender);

        holder.deleteMB.setOnClickListener(view ->
                clickListener.onUserDelete(users));

        holder.editMB.setOnClickListener(
                view -> clickListener.onUserEdit(users));

    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, dobTV, contactTV, genderTV, emailTV, addressTV;
        MaterialButton editMB, deleteMB;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.userNameTV);
            dobTV = itemView.findViewById(R.id.dobTV);
            contactTV = itemView.findViewById(R.id.contactTV);
            genderTV = itemView.findViewById(R.id.genderTV);
            emailTV = itemView.findViewById(R.id.emailTV);
            addressTV = itemView.findViewById(R.id.addressTV);

            editMB = itemView.findViewById(R.id.editUserBTN);
            deleteMB = itemView.findViewById(R.id.deleteUserBTN);
        }
    }
}
