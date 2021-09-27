package com.imedf.recettes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imedf.recettes.modele.Utilisateur;

public class DemarrageActivity extends AppCompatActivity {
    private Intent monIntent;
    private Utilisateur user;

    private FirebaseAuth auth;
    private FirebaseUser userFb;
    private FirebaseDatabase db;
    private DatabaseReference dbR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demarrage);
        getSupportActionBar().hide();
        setWidgets();
    }

    private void setWidgets() {
        auth = FirebaseAuth.getInstance();
        userFb = auth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        //dbR = db.getReference().child("Users").child(userFb.getUid());
    }

    public void OnDemarrer(View view) {
        try {
            if (userFb != null) {
                Toast.makeText(DemarrageActivity.this, "connecté ", Toast.LENGTH_SHORT).show();
                dbR = db.getReference().child("Users").child(userFb.getUid());
                recupererUstilisateur();
            }
            else if (auth.getInstance().getCurrentUser() == null) {
                Toast.makeText(DemarrageActivity.this, "Non connecté ", Toast.LENGTH_SHORT).show();
                monIntent = new Intent(DemarrageActivity.this, MainActivity.class);
                startActivity(monIntent);
            }
        } catch (Exception ex) {
            Toast.makeText(DemarrageActivity.this, "ex : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean recupererUstilisateur(){
        dbR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user =  snapshot.getValue(Utilisateur.class);
                entrerUtilisateur();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return true;
    }
    private void entrerUtilisateur(){
        monIntent = new Intent(DemarrageActivity.this, EntranceActivity.class);
        monIntent.putExtra("user", user);
        startActivity(monIntent);
    }
}