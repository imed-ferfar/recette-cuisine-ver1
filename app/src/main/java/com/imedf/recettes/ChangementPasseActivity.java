package com.imedf.recettes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.imedf.recettes.firebase.GestionUtilisateur;
import com.imedf.recettes.modele.Utilisateur;

public class ChangementPasseActivity extends AppCompatActivity {
    private ImageView imgRetour2;
    private TextView txtErreur5, txtErreur6, txtConfirmer;
    private EditText txtMotPasseAncien, txtMotPasseNouveau, txtMotPasseNouvConf;
    private Intent recuIntent, retourIntent;
    private String motPasse, nouveauPasse, confirmPass;
    private Utilisateur user;
    private FirebaseUser userFb;
    private AuthCredential credential;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changement_passe);
        getSupportActionBar().hide();
        setWidgets();
        setListeners();
    }
    private void setWidgets() {
        imgRetour2 = findViewById(R.id.imgRetour2);
        txtErreur5 = findViewById(R.id.txtErreur5);
        txtErreur6 = findViewById(R.id.txtErreur6);
        txtConfirmer = findViewById(R.id.txtConfirmer);
        txtMotPasseAncien = findViewById(R.id.txtMotPasseAncien);
        txtMotPasseNouveau = findViewById(R.id.txtMotPasseNouveau);
        txtMotPasseNouvConf = findViewById(R.id.txtMotPasseNouvConf);

        recuIntent = getIntent();
        user = recuIntent.getParcelableExtra("user");
    }
    private void setListeners() {
        imgRetour2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retourIntent = new Intent();
                retourIntent.putExtra("user",user);
                setResult(Activity.RESULT_OK,retourIntent);
                finish();
            }
        });
    }

    public void onChanger(View view) {
        txtErreur5.setVisibility(TextView.GONE);
        txtErreur6.setVisibility(TextView.GONE);
        txtConfirmer.setVisibility(TextView.GONE);
        if (validerData()){
            modifierMotPasse();
        }
    }


    private void modifierMotPasse() {
        userFb = FirebaseAuth.getInstance().getCurrentUser();
        credential = EmailAuthProvider.getCredential(userFb.getEmail(), motPasse);
        userFb.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            userFb.updatePassword(nouveauPasse).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        validerResultat("true");
                                        GestionUtilisateur.modifierMotPasse(nouveauPasse);
                                        Toast.makeText(ChangementPasseActivity.this,
                                                "Mot de passe modifié.",Toast.LENGTH_SHORT).show();
                                    } else {
                                        validerResultat(task.getException().getClass().toString());
                                        Toast.makeText(ChangementPasseActivity.this,
                                                "Erreur modification d emot de passe."+task.getException().getClass(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            validerResultat(task.getException().getClass().toString());
                        }
                    }
                });
    }
    private void validerResultat(String result){
        if (result.equals("true")) {
            user.setMotPasse(nouveauPasse);
            txtConfirmer.setText("Mot de passe modifié avec succès.");
            txtConfirmer.setVisibility(TextView.VISIBLE);
            return;
        }
        if (result.contains("FirebaseAuthInvalidCredentialsException")) {
            txtErreur5.setText("Échec d’authentification .\n" +
                    "Le mot de passe est incorrect!");
            txtErreur5.setVisibility(TextView.VISIBLE);
            return;
        }
        else if (result.contains("FirebaseNetworkException")) {
            txtErreur6.setText("Échec d’authentification .\nProblème de connexion internet!");
            txtErreur6.setVisibility(TextView.VISIBLE);
            return ;
        }
        txtErreur6.setText("Erreur d’authentification!\nAutre!");
        txtErreur6.setVisibility(TextView.VISIBLE);
    }

    private boolean validerData() {
        motPasse = txtMotPasseAncien.getText().toString();
        nouveauPasse = txtMotPasseNouveau.getText().toString();
        confirmPass = txtMotPasseNouvConf.getText().toString();
        if (motPasse.equals("")){
            txtErreur5.setText("Entrez votre mot de passe actuel!");
            txtErreur5.setVisibility(TextView.VISIBLE);
            return false;
        }
        if (nouveauPasse.equals("")){
            txtErreur6.setText("Tapez un nouveau mot de passe!");
            txtErreur6.setVisibility(TextView.VISIBLE);
            return  false;
        }
        if (nouveauPasse.length() < 7){
            txtErreur6.setText("Le mot de passe doit avoir 7 caractères au minimum!");
            txtErreur6.setVisibility(TextView.VISIBLE);
            return  false;
        }
        if (!nouveauPasse.equals(confirmPass)){
            txtErreur6.setText("Mot de passe non identique!");
            txtErreur6.setVisibility(TextView.VISIBLE);
            return  false;
        }
        return true;
    }
}