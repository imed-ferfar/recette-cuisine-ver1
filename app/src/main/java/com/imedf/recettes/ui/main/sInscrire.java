package com.imedf.recettes.ui.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.imedf.recettes.daoSqlite.UserDbAdapter;
import com.imedf.recettes.MainActivity;
import com.imedf.recettes.R;
import com.imedf.recettes.firebase.GestionUtilisateur;
import com.imedf.recettes.modele.Utilisateur;

import utils.GestionImage;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link sInscrire#newInstance} factory method to
 * create an instance of this fragment.
 */
public class sInscrire extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private Button btnInscrire;
    private EditText txtNom, txtPrenom, txtCourrielNew, txtMotPasseNew, txtMotPasseConfirm;
    private String nom, prenom, courriel="", motPasse, motPasseConfirm;
    private TextView txtErreur, txtCompteCree;
    private Utilisateur user;
    private UserDbAdapter userDbAdapter;
    private Intent monIntent;
    private FirebaseAuth auth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public sInscrire() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment sInscrire.
     */
    // TODO: Rename and change types and number of parameters
    public static sInscrire newInstance(String param1, String param2) {
        sInscrire fragment = new sInscrire();
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
        View view = inflater.inflate(R.layout.fragment_s_inscrire,container,false);
        setWidjets(view);

        btnInscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtErreur.setVisibility(TextView.GONE);
                txtCompteCree.setVisibility(TextView.GONE);
                if (validerData()) {
                    try {
                        CreerUtilisateur(courriel,motPasse);
                        // Toast.makeText(getActivity(), "ok : " + user.toString(), Toast.LENGTH_SHORT).show();
                    }catch (Exception ex){
                        Toast.makeText(getActivity(), "ex : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    txtErreur.setVisibility(TextView.VISIBLE);
                   // Toast.makeText(getActivity(), "Erreur de saisi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtCompteCree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    monIntent = new Intent(getActivity(), MainActivity.class);
                    monIntent.putExtra("courriel", user.getCourriel());
                    monIntent.putExtra("motPasse", user.getCourriel());
                    startActivity(monIntent);
                } catch (Exception ex){
                    Toast.makeText(getActivity(), "ex : "+ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void setWidjets(View view) {
        btnInscrire = (Button) view.findViewById(R.id.btnInscrire);
        txtNom = (EditText) view.findViewById(R.id.txtNom);
        txtPrenom = (EditText) view.findViewById(R.id.txtPrenom);
        txtCourrielNew = (EditText) view.findViewById(R.id.txtCourrielNew);
        txtMotPasseNew = (EditText) view.findViewById(R.id.txtMotPasseNew);
        txtMotPasseConfirm = (EditText) view.findViewById(R.id.txtMotPasseConfirm);
        txtErreur = (TextView) view.findViewById(R.id.txtErreur);
        txtCompteCree = (TextView) view.findViewById(R.id.txtCompteCree);

        auth = FirebaseAuth.getInstance();
    }


    private boolean validerData(){
        nom = txtNom.getText().toString();
        prenom = txtPrenom.getText().toString();
        courriel = txtCourrielNew.getText().toString();
        motPasse = txtMotPasseNew.getText().toString();
        motPasseConfirm = txtMotPasseConfirm.getText().toString();
        if (nom.equals("")||prenom.equals("")||courriel.equals("")||motPasse.equals("")) {
            txtErreur.setText("Veuillez compléter les champs de saisie!");
            return false;
        }
        if (!courriel.contains("@") || !Patterns.EMAIL_ADDRESS.matcher(courriel).matches()){
            txtErreur.setText("Le courriel saisi n'est pas valide!");
            return  false;
        }
        if (!motPasse.equals(motPasseConfirm)) {
            txtErreur.setText("Mot de passe non identique!");
            return false;
        }
        if (motPasse.length() < 7) {
            txtErreur.setText("Le mot de passe doit avoir 7 caractères au minimum!");
            return false;
        }
        user = new Utilisateur(nom,prenom,courriel,motPasse);
        return true;
    }



    private void CreerUtilisateur(String courriel, String motPasse) {
        auth.createUserWithEmailAndPassword(courriel, motPasse)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            validerResultat(task.getException().getClass().toString());
                            txtErreur.setVisibility(TextView.VISIBLE);
                        } else {
                            try {
                                Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.imgprofil) ;
                               /* ByteArrayOutputStream monByte = new ByteArrayOutputStream();
                                bmp.compress(Bitmap.CompressFormat.PNG, 100, monByte);
                                bmp.recycle();
                                byte[] byteArray = monByte.toByteArray();
                                String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);*/
                                user.setImgProfil(GestionImage.convertirBitmapEnString(bmp));
                                GestionUtilisateur.enregistrerUser(user);
                            }catch (Exception ex){
                                Toast.makeText(getActivity(), "ex : " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            txtCompteCree.setVisibility(TextView.VISIBLE);
                            btnInscrire.setVisibility(Button.GONE);
                            // enregistrerUser();
                        }
                    }
                });
    }

    private void validerResultat(String result){
        if (result.contains("FirebaseAuthUserCollisionException")) {
            txtErreur.setText("Échec d’authentification .\nCourriel déja utilisé");
            return;
        }
        else if (result.contains("FirebaseAuthInvalidCredentialsException")) {
            txtErreur.setText("Échec d’authentification .\nCourriel non valide!");
            return;
        }
        else if (result.contains("FirebaseNetworkException")) {
            txtErreur.setText("Échec d’authentification .\nProblème de connexion internet!");
            return ;
        }
        txtErreur.setText("Échec d’authentification .");
    }


}