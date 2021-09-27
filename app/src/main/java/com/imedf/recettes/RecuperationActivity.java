package com.imedf.recettes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperationActivity extends AppCompatActivity {
    private TextView textRetour,txtIntrouvable,txtMsgMotPasse;
    private EditText txtCourrielRecup;
    private String courriel;
    private Intent retourIntent;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperation);
        getSupportActionBar().hide();
        setWidgets();
        setListeners();
    }

    private void setWidgets() {
        textRetour = findViewById(R.id.txtRetour);
        txtIntrouvable = findViewById(R.id.txtIntrouvable);
        txtMsgMotPasse = findViewById(R.id.txtMsgMotPasse);
        txtCourrielRecup = findViewById(R.id.txtCourrielRecup);

        auth= FirebaseAuth.getInstance();
    }

    private void setListeners() {
        textRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // retourIntent = new Intent();
               // setResult(Activity.RESULT_OK,retourIntent);
                finish();
            }
        });
    }

    public void onRecupurer(View view) {
        txtIntrouvable.setVisibility(TextView.GONE);
        txtMsgMotPasse.setVisibility(TextView.GONE);
        courriel = txtCourrielRecup.getText().toString();
        if (validerdata())
            reinitialiserMotPasse(courriel);
    }

    private boolean validerdata(){
        if (courriel.equals("")){
            txtIntrouvable.setText("Tapez votre adresse courriel svp");
            txtIntrouvable.setVisibility(TextView.VISIBLE);
            return  false;
        }
        if (!courriel.contains("@") || !Patterns.EMAIL_ADDRESS.matcher(courriel).matches()){
            txtIntrouvable.setText("Le courriel saisi n'est pas valide!");
            txtIntrouvable.setVisibility(TextView.VISIBLE);
            return  false;
        }
        return true;
    }

    private void reinitialiserMotPasse(String courriel){
        auth.sendPasswordResetEmail(courriel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    validerResultat("true");
                   /* Toast.makeText(RecuperationActivity.this,
                            "Les instructions ont été envoyées pour le reset!", Toast.LENGTH_SHORT).show();*/
                } else {
                    validerResultat(task.getException().getClass().toString());
                   /* Toast.makeText(RecuperationActivity.this,"Erreur lors de l'envoi des instructions! :\n"
                   +task.getException().getClass(),Toast.LENGTH_SHORT).show();*/
                }
            }
        });
    }

    private void validerResultat(String result){
        if (result.equals("true")) {
            txtMsgMotPasse.setText("Un courriel de réinitialisation est envoyé à votre adresse.");
            txtMsgMotPasse.setVisibility(TextView.VISIBLE);
            return;
        }
        if (result.contains("FirebaseAuthInvalidUserException")) {
            txtIntrouvable.setText("Aucun compte n'est enregistré avec ce courriel!");
            txtIntrouvable.setVisibility(TextView.VISIBLE);
            return;
        }
        else if (result.contains("FirebaseNetworkException")) {
            txtIntrouvable.setText("Problème de connexion internet!");
            txtIntrouvable.setVisibility(TextView.VISIBLE);
            return ;
        }
        txtIntrouvable.setText("Erreur d’authentification!\nAutre!");
        txtIntrouvable.setVisibility(TextView.VISIBLE);
    }

}