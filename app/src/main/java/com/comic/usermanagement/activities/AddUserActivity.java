package com.comic.usermanagement.activities;

import android.os.Bundle;
import android.util.Log;
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
import com.comic.usermanagement.databinding.ActivityAddUserBinding;
import com.comic.usermanagement.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUserActivity extends AppCompatActivity {

    private ActivityAddUserBinding binding;

    private String radioButtonTxt;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityAddUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Log.e("reference 123", mDatabase.toString());
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

        binding.addUserBTN.setOnClickListener(view -> {
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
            Toast.makeText(this, "Select the gender", Toast.LENGTH_SHORT).show();

        } else {

            String uid = mDatabase.push().getKey();
            writeNewUser(uid, userName, dob, address, contact, email, radioButtonTxt);
        }

    }

    private void writeNewUser(String uid, String userName, String dob, String address, String contact,
                              String email, String gender) {
        Users users = new Users(uid, userName, dob, address, contact, email, gender);

        mDatabase.child("users").child(uid).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(AddUserActivity.this, "Data saved", Toast.LENGTH_SHORT).show();

                    binding.userNameTIET.setText("");
                    binding.dobTIET.setText("");
                    binding.addressTIET.setText("");
                    binding.contactTIET.setText("");
                    binding.emailTIET.setText("");


                } else  {
                    Toast.makeText(AddUserActivity.this, "Data not saved", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void openCalendar() {



    }
}