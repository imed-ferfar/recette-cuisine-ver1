package com.imedf.recettes.firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.imedf.recettes.modele.Utilisateur;

public class GestionUtilisateur {

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static DatabaseReference dbR = db.getReference("Users");
    private static String userId = auth.getCurrentUser().getUid();

    public static String CrrerUtilisateur(String courriel, String motPasse){
        auth = FirebaseAuth.getInstance();
        String[] result = {};
        auth.createUserWithEmailAndPassword(courriel, motPasse)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            result[0] = (task.getException().getClass().toString());
                        } else {
                            result[0] = "true";
                           // enregistrerUser();
                        }
                    }
                });
        return result[0];
    }
    public static void enregistrerUser(Utilisateur utilisateur){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbR;
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();

        utilisateur.setId(userId);
        dbR = db.getReference().child("Users").child(userId);
        dbR.setValue(utilisateur);
    }

    public static void modifierUser(Utilisateur user){
        dbR.child(userId).child("nom").setValue(user.getNom());
        dbR.child(userId).child("prenom").setValue(user.getPrenom());

       /* db.goOffline();
        dbR.onDisconnect();*/
    }
    public static void modifierMotPasse(String motPasse){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();;
        DatabaseReference dbR;
        FirebaseUser userFb = auth.getCurrentUser();
        String userId = userFb.getUid();
        dbR = db.getReference().child("Users").child(userId).child("motPasse");
        dbR.setValue(motPasse);
        db.goOffline();
        dbR.onDisconnect();
    }
    public static void modifierImgProfil(String imgProfil){
        Log.v("imed",imgProfil);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase db = FirebaseDatabase.getInstance();;
        DatabaseReference dbR;
        FirebaseUser userFb = auth.getCurrentUser();
        String userId = userFb.getUid();
        dbR = db.getReference().child("Users").child(userId).child("motPasse");
        dbR.setValue(imgProfil);
        db.goOffline();
        dbR.onDisconnect();
    }



}
