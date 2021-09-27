package com.imedf.recettes.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imedf.recettes.daoSqlite.UserDbAdapter;
import com.imedf.recettes.EntranceActivity;
import com.imedf.recettes.daoSqlite.LoginDbAdapter;
import com.imedf.recettes.R;
import com.imedf.recettes.RecuperationActivity;
import com.imedf.recettes.modele.Utilisateur;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link seConnecter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class seConnecter extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnConnecter;
    private TextView txtPasseOublie, txtErreurSaisi, txtErreurAuth;
    private EditText txtCourriel, txtMotPasse;
    private String courriel, motPasse;
    private Utilisateur user;
    private UserDbAdapter userDbAdapter;

    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference dbR;

    private Intent monIntent;
    private LoginDbAdapter logDbAdapter;



    public seConnecter() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment seConnecter.
     */
    // TODO: Rename and change types and number of parameters
    public static seConnecter newInstance(String param1, String param2) {
        seConnecter fragment = new seConnecter();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_se_connecter,container,false);
        setWidgets(view);
        setListeners();
        txtCourriel.setText(mParam1);
        txtMotPasse.setText(mParam2);
        return view;
    }

    private void setWidgets(View view) {
        btnConnecter = (Button) view.findViewById(R.id.btnConnecter);
        txtCourriel = (EditText) view.findViewById(R.id.txtCourriel);
        txtMotPasse = (EditText) view.findViewById(R.id.txtMotPasse);
        txtErreurSaisi = (TextView) view.findViewById(R.id.txtErreurSaisi);
        txtErreurAuth = (TextView) view.findViewById(R.id.txtErreurAuth);
        txtPasseOublie = (TextView) view.findViewById(R.id.txtPasseOublie);

        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
    }

    private void setListeners() {
        btnConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtErreurSaisi.setVisibility(TextView.GONE);
                txtErreurAuth.setVisibility(TextView.GONE);
                if (validerData()) {
                    authentifierUtilisateur(courriel,motPasse);
                } else {
                   // txtErreurSaisi.setText("Erreur de saisie. Le courriel et le mot de passe sont obligatoires pour l'authentification!");
                    txtErreurSaisi.setVisibility(TextView.VISIBLE);
                    Toast.makeText(getActivity(), "Erreur de saisie!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        txtPasseOublie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getActivity(), "TESTING TextView txtPasseOublie",Toast.LENGTH_SHORT).show();
                monIntent = new Intent(getActivity(), RecuperationActivity.class);
                startActivityForResult(monIntent,401);

            }
        });
    }

    private void souvgarderLogin(Utilisateur user) {

        try {
            logDbAdapter = new LoginDbAdapter(getActivity());
            logDbAdapter.viderLogin();
            logDbAdapter.ajouterLogin(user);
        } catch (Exception ex){
            Toast.makeText(getActivity(), "ex : "+ex.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }



    private boolean validerData() {
        courriel = txtCourriel.getText().toString();
        motPasse = txtMotPasse.getText().toString();

        if (courriel.equals("")) {
            txtErreurSaisi.setText("Le courriel est obligatoire pour l'authentification!");
            return  false;
        }
        if (!courriel.contains("@") || !Patterns.EMAIL_ADDRESS.matcher(courriel).matches()){
            txtErreurSaisi.setText("Le courriel saisi n'est pas valide!");
            return  false;
        }
        if (motPasse.equals("")) {
            txtErreurSaisi.setText("Le mot de passe sont obligatoire pour l'authentification!");
            return  false;
        }
        if (motPasse.trim().length() < 7) {
            txtErreurSaisi.setText("Le mot de passe doit avoir 7 caractères au moins!");
            return  false;
        }
        user = new Utilisateur(courriel,motPasse);
        return true;
    }
/*
    private boolean authentifierUser(){
        userDbAdapter = new UserDbAdapter(getActivity());
        user = userDbAdapter.rechercherUtilisateur(user);
        if (user != null)
            if (user.getMotPasse().equals(motPasse))
                return true;
            else{
                txtErreurAuth.setText("Erreur d'authentification, vérifiez vos informations de se connexion!");
                txtErreurAuth.setVisibility(TextView.VISIBLE);
            }
        else {
            txtErreurAuth.setText("Aucun compte n’est enregistré avec le courriel saisi!");
            txtErreurAuth.setVisibility(TextView.VISIBLE);
        }
        return  false;
    }*/


    private void authentifierUtilisateur(String courriel, String motPasse) {
        try {
            auth.signInWithEmailAndPassword(courriel, motPasse)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Log.d(TAG, "signInWithEmail:success");
                                //Toast.makeText(getActivity(), "Authentication validate.",Toast.LENGTH_SHORT).show();
                                FirebaseUser userFb = auth.getCurrentUser();
                                dbR = db.getReference().child("Users").child(userFb.getUid());
                                recupererUstilisateur();

                            } else {
                                validerResultat(task.getException().getClass().toString());
                                txtErreurAuth.setVisibility(TextView.VISIBLE);
                             /*   Toast.makeText(getActivity(), "Authentication failed."+task.getException().getClass(),
                                        Toast.LENGTH_SHORT).show();*/
                            }
                        }
                    });
        }
        catch (Exception ex){
            Toast.makeText(getActivity(), "erreur\n."+ex.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void entrerUtilisateur(){
        monIntent = new Intent(getActivity(), EntranceActivity.class);
        monIntent.putExtra("user", user);
        startActivity(monIntent);
    }
    private void validerResultat(String result){
        if (result.contains("FirebaseAuthInvalidUserException")) {
            txtErreurAuth.setText("Échec d’authentification .\n" +
                    "Aucun compte n’est enregistré avec le courriel saisi!");
            return;
        }
        else if (result.contains("FirebaseAuthInvalidCredentialsException")) {
            txtErreurAuth.setText("Erreur d'authentification, vérifiez vos informations de se connexion!");
            return;
        }
        else if (result.contains("FirebaseNetworkException")) {
            txtErreurAuth.setText("Échec d’authentification .\nProblème de connexion internet!");
            return ;
        }
        txtErreurAuth.setText("Erreur d’authentification!\nAutre!");
    }


    private boolean recupererUstilisateur(){
        dbR.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user =  snapshot.getValue(Utilisateur.class);
                //Toast.makeText(getActivity(), "la cle est : " + " ,"+ user.toString(), Toast.LENGTH_SHORT).show();
                entrerUtilisateur();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return true;
    }

}