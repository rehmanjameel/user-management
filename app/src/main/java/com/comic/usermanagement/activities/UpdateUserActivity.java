package com.comic.usermanagement.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.comic.usermanagement.R;
import com.comic.usermanagement.databinding.ActivityUpdateUserBinding;
import com.comic.usermanagement.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateUserActivity extends AppCompatActivity {

    private ActivityUpdateUserBinding binding;
    private String radioButtonTxt;

    private DatabaseReference mDatabase;
    private Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityUpdateUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        user = (Users) getIntent().getSerializableExtra("user");

        if (user != null) {

            binding.userNameTIET.setText(user.username);
            binding.dobTIET.setText(user.dob);
            binding.addressTIET.setText(user.address);
            binding.contactTIET.setText(user.contact);
            binding.emailTIET.setText(user.email);

//            binding.genderRG.check(user.gender);

            if (user.gender != null) {
                for (int i = 0; i < binding.genderRG.getChildCount(); i++) {
                    // get the position of gender radio group's child
                    View view = binding.genderRG.getChildAt(i);

                    // check if the instance of radio buttons exists or not
                    if (view instanceof RadioButton) {
                        // then create the instance
                        RadioButton rb = (RadioButton) view;
                        if (rb.getText().toString().equals(user.gender)) {
                            rb.setChecked(true);
                            radioButtonTxt = rb.getText().toString();
                            break;
                        }
                    }
                }
            }
        }

        binding.genderRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(@NonNull RadioGroup radioGroup, int checkedId) {

                RadioButton radioButton = radioGroup.findViewById(checkedId);
                if (radioButton != null) {
                    radioButtonTxt = radioButton.getText().toString();
                }
            }
        });


        MaterialDatePicker<Long> datePicker = MaterialDatePicker
                .Builder.datePicker()
                .setTitleText("Choose a date")
                .build();

        // open calendar
        binding.dobTIL.setEndIconOnClickListener(view -> {
            datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
        });

        datePicker.addOnPositiveButtonClickListener(selection -> {
            binding.dobTIET.setText(datePicker.getHeaderText());
        });

        binding.updateUserBTN.setOnClickListener(view -> {
            verifyFields();
        });

    }

    private void verifyFields() {
        String userName = binding.userNameTIET.getText().toString();
        String dob = binding.dobTIET.getText().toString();
        String address = binding.addressTIET.getText().toString();
        String contact = binding.contactTIET.getText().toString();
        String email = binding.emailTIET.getText().toString();

//        Log.e("radiobutton", ",.,." + dob + ",.,.");

        if (userName.isEmpty() && dob.isEmpty() && address.isEmpty()
                && contact.isEmpty() && email.isEmpty() && radioButtonTxt == null) {

            binding.userNameTIET.setError("Field Required");
            binding.dobTIET.setError("Field Required");
            binding.addressTIET.setError("Field Required");
            binding.contactTIET.setError("Field Required");
            binding.emailTIET.setError("Field Required");

            Toast.makeText(this, "Please select the gender", Toast.LENGTH_SHORT).show();
        } else if (userName.isEmpty()) {
            binding.userNameTIET.setError("Field Required");

        } else if (dob.isEmpty()) {
            binding.dobTIET.setError("Field Required");

        } else if (address.isEmpty()) {
            binding.addressTIET.setError("Field Required");

        } else if (contact.isEmpty()) {
            binding.contactTIET.setError("Field Required");

        } else if (email.isEmpty()) {
            binding.emailTIET.setError("Field Required");

        } else if (radioButtonTxt == null) {
            Toast.makeText(this, "Please select the gender", Toast.LENGTH_SHORT).show();

        } else {

            updateUser(user.id, userName, dob, address, contact, email, radioButtonTxt);
        }

    }

    private void updateUser(String uid, String userName, String dob, String address, String contact,
                              String email, String gender) {
        Users users = new Users(uid, userName, dob, address, contact, email, gender);

        mDatabase.child("users").child(uid).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UpdateUserActivity.this, "Data updated", Toast.LENGTH_SHORT).show();

                    finish();

                } else  {
                    Toast.makeText(UpdateUserActivity.this, "Data not updated", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}