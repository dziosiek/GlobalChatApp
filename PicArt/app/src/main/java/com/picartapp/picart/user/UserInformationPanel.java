package com.picartapp.picart.user;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.picartapp.picart.R;
import com.squareup.picasso.Picasso;

public class UserInformationPanel extends AppCompatActivity {
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    ImageButton avatarButton;
    Button saveButton;
    private StorageReference mStorage;
    private static final int GALLERY_INTENT = 2;
    User user;
    EditText firstname, lastname, date;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information_panel);
        saveButton = (Button) findViewById(R.id.save);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname= (EditText) findViewById(R.id.lastname);
        date = (EditText) findViewById(R.id.date);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mStorage = FirebaseStorage.getInstance().getReference();
        avatarButton = (ImageButton) findViewById(R.id.user_info);

        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);


        avatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setFirstname(firstname.getText().toString().trim());
                user.setLastname(lastname.getText().toString().trim());
                user.setDate(date.getText().toString().trim());

                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton = (RadioButton) findViewById(selectedId);
                user.setSex(radioSexButton.getText().toString().trim());
                Toast.makeText(getApplicationContext(), "Information changed", Toast.LENGTH_SHORT).show();

                mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).setValue(user);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();

            StorageReference filepath = mStorage.child("ProfilePhoto").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(),"Upload Done",Toast.LENGTH_SHORT).show();

                    Uri downloadUri = taskSnapshot.getDownloadUrl();
                    Log.i("downloadURI",""+ downloadUri);

                    Picasso.get().load(downloadUri).fit().centerCrop().into(avatarButton);


                }

            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        StorageReference filepath = mStorage.child("ProfilePhoto").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(avatarButton);

            }
        });




        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                user = dataSnapshot.getValue(User.class);
//                Log.i("User info:",""+user.getFirstname()+" "+user.getLastname());
            if(user!=null){
                if(user.getFirstname()!=null)firstname.setText(user.getFirstname());
                if(user.getLastname()!=null)lastname.setText(user.getLastname());
                if(user.getDate()!=null)date.setText(user.getDate());
            }
            else user = new User();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("LOG onCancelled:", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        mDatabase.child("users").child(FirebaseAuth.getInstance().getUid()).addValueEventListener(postListener);


    }

}
