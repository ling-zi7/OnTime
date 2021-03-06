package com.example.ontime;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ontime.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a class that implements RiderProfile class, shows rider's profile
 */
public class RiderProfile extends AppCompatActivity {

    Button editButton;
    EditText profileNameEditText;
    EditText profilePasswordEditText;
    EditText profileEmailEditText;
    EditText profilePhoneNumberEditText;

    FirebaseFirestore db;

    private String userName;
    private String userPhone;
    private String userEmail;
    private String userPassword;
    private String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        editButton = findViewById(R.id.edit_button);

        profileNameEditText = findViewById(R.id.profile_name_text);
        profilePasswordEditText = findViewById(R.id.profile_password_text);
        profileEmailEditText = findViewById(R.id.profile_email_text);
        profilePhoneNumberEditText = findViewById(R.id.profile_phonenumber_text);

        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Riders");
        userName = getIntent().getStringExtra("username");
        final DocumentReference user = db.collection("Riders").document(userName);
        user.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            nickname = documentSnapshot.getString("nickname");
                            userPhone = documentSnapshot.getString("phone number");
                            userEmail = documentSnapshot.getString("email");
                            userPassword = documentSnapshot.getString("password");

                            profileNameEditText.setText(nickname);
                            profilePhoneNumberEditText.setText(userPhone);
                            profileEmailEditText.setText(userEmail);
                            profilePasswordEditText.setText(userPassword);
                        }else{
                            Toast.makeText(RiderProfile.this,"user not exist",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RiderProfile.this,"Error",Toast.LENGTH_SHORT).show();
                    }
                });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

    }
    private void saveUserInformation() {
        nickname = profileNameEditText.getText().toString();
        userPhone = profilePhoneNumberEditText.getText().toString();
        userEmail = profileEmailEditText.getText().toString();
        userPassword = profilePasswordEditText.getText().toString();
        db = FirebaseFirestore.getInstance();

        final CollectionReference collectionReference = db.collection("Riders");
        Map<String,Object> data = new HashMap<>();
        if (nickname.length() == 0) {
            Toast.makeText(RiderProfile.this, "Please enter your username", Toast.LENGTH_LONG).show();
        } else if (userEmail.length() == 0 ) {
            Toast.makeText(RiderProfile.this, "Please valid Email Address", Toast.LENGTH_LONG).show();
        } else if (userPhone.length() == 0 || userPhone.length() > 10 || userPhone.length() < 10) {
            Toast.makeText(RiderProfile.this, "Please enter valid Phone Number", Toast.LENGTH_LONG).show();
        } else if (userPassword.length() == 0) {
            Toast.makeText(RiderProfile.this, "Please Set your Password", Toast.LENGTH_LONG).show();
        } else if (nickname.length() > 0 && userPassword.length() > 0 && userEmail.length() > 0 && userPhone.length() > 0){
            data.put("nickname",nickname);
            data.put("password", userPassword);
            data.put("email", userEmail);
            data.put("phone number", userPhone);
            collectionReference
                    .document(userName)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(RiderProfile.this,"Data add successful", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RiderProfile.this, "Data add failed" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}

