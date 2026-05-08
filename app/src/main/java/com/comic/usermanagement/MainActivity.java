package com.comic.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.comic.usermanagement.activities.AddUserActivity;
import com.comic.usermanagement.activities.UpdateUserActivity;
import com.comic.usermanagement.adapters.UsersAdapter;
import com.comic.usermanagement.databinding.ActivityMainBinding;
import com.comic.usermanagement.interfaces.OnUserClickListener;
import com.comic.usermanagement.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatabaseReference mDatabase;

    private UsersAdapter usersAdapter;
    private ArrayList<Users> usersArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        binding.addUserFAB.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddUserActivity.class);
            startActivity(intent);
        });

        if (mDatabase != null) {
            fetchUsers();
        }
    }

//    private void fetchUsers() {
//
//        usersArrayList.clear();
//        ValueEventListener valueEventListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//
//                    Users users = dataSnapshot.getValue(Users.class);
////                    Log.e("user id", dataSnapshot.getKey().toString());
//
////                    usersArrayList.add(users);
//
////                    Log.e("snapshotss", Objects.requireNonNull(snapshot.getKey()));
//                    if (users != null) {
//                        String id = dataSnapshot.getKey();
//                        String address = users.address;
//                        String name = users.username;
//                        String contact = users.contact;
//                        String email = users.email;
//                        String gender = users.gender;
//                        String dob = users.dob;
//
//                        usersArrayList.add(
//                                new Users(id, name, dob, address, contact, email, gender)
//                        );
////                        Log.e("uaddress", "address" + address);
////                    Log.e("uname", name);
////                    Log.e("uemail", email);
//                    }
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MainActivity.this, "onCancel " + error.toException(), Toast.LENGTH_SHORT).show();
//                Log.w("TAG", "loadPost:onCancelled", error.toException());
//            }
//        };
//
//       mDatabase.addValueEventListener(valueEventListener);
//
//        binding.usersRV
//                .setLayoutManager(new LinearLayoutManager(MainActivity.this));
//
//        usersAdapter = new UsersAdapter(usersArrayList,
//                MainActivity.this, new OnUserClickListener() {
//            @Override
//            public void onUserEdit(Users users) {
//
//            }
//
//            @Override
//            public void onUserDelete(Users users) {
//                DatabaseReference reference = FirebaseDatabase.getInstance()
//                        .getReference("users").child(users.id);
//
//                reference.removeValue()
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Toast.makeText(MainActivity.this,
//                                            users.username + " is deleted successfully!",
//                                            Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(MainActivity.this, "User not deleted", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });
//
//        binding.usersRV.setAdapter(usersAdapter);
//    }

//private ArrayList<Users> usersArrayList = new ArrayList<>();
//    private UsersAdapter usersAdapter;
//    private DatabaseReference mDatabase;
    private ChildEventListener childEventListener;

    private void fetchUsers() {

        // Setup RecyclerView & Adapter ONLY ONCE
        usersAdapter = new UsersAdapter(usersArrayList,
                MainActivity.this, new OnUserClickListener() {

            @Override
            public void onUserEdit(Users users) {
                // handle edit
                Intent intent = new Intent(MainActivity.this, UpdateUserActivity.class);
                intent.putExtra("user", users);
                startActivity(intent);
            }

            @Override
            public void onUserDelete(Users users) {
                DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReference("users").child(users.id);

                reference.removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this,
                                users.username + " deleted!",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this,
                                "User not deleted",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        binding.usersRV.setLayoutManager(new LinearLayoutManager(this));
        binding.usersRV.setAdapter(usersAdapter);

        // Initialize database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        // Clear list once before attaching listener
        usersArrayList.clear();

        // Use ChildEventListener (BEST approach)
        childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Users user = snapshot.getValue(Users.class);

                if (user != null) {
                    user.id = snapshot.getKey(); // ensure ID is set
                    usersArrayList.add(user);
                    usersAdapter.notifyItemInserted(usersArrayList.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                Users updatedUser = snapshot.getValue(Users.class);

                if (updatedUser != null) {
                    String id = snapshot.getKey();

                    for (int i = 0; i < usersArrayList.size(); i++) {
                        if (usersArrayList.get(i).id.equals(id)) {
                            updatedUser.id = id;
                            usersArrayList.set(i, updatedUser);
                            usersAdapter.notifyItemChanged(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                String id = snapshot.getKey();

                for (int i = 0; i < usersArrayList.size(); i++) {
                    if (usersArrayList.get(i).id.equals(id)) {
                        usersArrayList.remove(i);
                        usersAdapter.notifyItemRemoved(i);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
                // usually not needed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,
                        "Error: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        };

        //  Attach listener once
        mDatabase.addChildEventListener(childEventListener);
    }
}