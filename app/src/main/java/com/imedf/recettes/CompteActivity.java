package com.imedf.recettes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.imedf.recettes.AsyncTask.AsyncChargementImg;
import com.imedf.recettes.AsyncTask.AsyncEnregistremantImg;
import com.imedf.recettes.firebase.GestionUtilisateur;
import com.imedf.recettes.modele.Utilisateur;

import utils.GestionImage;

public class CompteActivity extends AppCompatActivity {
    private ImageView imgRetour;
    private EditText txtNomCpt, txtPrenomCpt, txtCourrielCpt;
    private TextView txtChangerMotPasse, txtErreur4;
    private ImageView imageProfil, imgProfUpdate;
    private Intent recuIntent, monIntent, retourIntent, galleryInt;
    private Utilisateur user;
    private String newNom, newPrenom;
    private AsyncChargementImg asyncCharImg;
    private AsyncEnregistremantImg asyncEnreImg;
    boolean flag = false;
    ProgressDialog p;

    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private static String userId = auth.getCurrentUser().getUid();
    private static FirebaseDatabase db = FirebaseDatabase.getInstance();
    private static DatabaseReference dbR = db.getReference("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compte);
        getSupportActionBar().hide();
        setWidgets();
        setListeners();
        afficherInfos();
    }

    private void setWidgets() {
        imgRetour = findViewById(R.id.imgRetour);
        txtNomCpt = findViewById(R.id.txtNomCpt);
        txtPrenomCpt = findViewById(R.id.txtPrenomCpt);
        txtCourrielCpt = findViewById(R.id.txtCourrielCpt);
        txtChangerMotPasse = findViewById(R.id.txtChangerMotPasse);
        txtErreur4 = findViewById(R.id.txtErreur4);
        imageProfil = findViewById(R.id.imageProfil);
        imgProfUpdate= findViewById(R.id.imgProfUpdate);

        recuIntent = getIntent();
        user = recuIntent.getParcelableExtra("user");
    }

    private void setListeners() {
        imgRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retourIntent = new Intent();
                retourIntent.putExtra("user",user);
                setResult(Activity.RESULT_OK,retourIntent);
                finish();
            }
        });
        txtChangerMotPasse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monIntent = new Intent(CompteActivity.this, ChangementPasseActivity.class);
                monIntent.putExtra("user",user);
                startActivityIfNeeded(monIntent,502);
            }
        });
        imgProfUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryInt = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityIfNeeded(galleryInt,101);
            }
        });
    }

    private void afficherInfos() {
        txtNomCpt.setText(user.getNom());
        txtPrenomCpt.setText(user.getPrenom());
        txtCourrielCpt.setText(user.getCourriel());

       /* byte[] decodedStr = Base64.decode(user.getImgProfil(),Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedStr,0,decodedStr.length);*/
        imageProfil.setImageBitmap(GestionImage.convertirStringAbitmap(user.getImgProfil()));
    }

    public void onSauvgarder(View view) {
      //  super.recreate();
        Log.v("imed","img1 : ");

        Toast.makeText(CompteActivity.this, "je suis la.", Toast.LENGTH_LONG).show();

        if (asyncEnreImg != null)
            Log.v("imed","img1 : "+asyncEnreImg.getImgStrReslt());
        txtErreur4.setVisibility(TextView.GONE);
        newNom = txtNomCpt.getText().toString();
        newPrenom = txtPrenomCpt.getText().toString();
        if (newNom.equals("")||newPrenom.equals("")){
            txtErreur4.setVisibility(TextView.VISIBLE);
        } else if (!newNom.equals(user.getNom()) || !newPrenom.equals(user.getPrenom())){
            user.setNom(newNom);
            user.setPrenom(newPrenom);
            Log.v("imed","id: "+userId);
            Toast.makeText(CompteActivity.this, "id : "+userId, Toast.LENGTH_LONG).show();
            dbR.child(userId).setValue(user);

            new ExampleAsyncTask().execute(user);
        }
        if (flag) {

            asyncEnreImg = new AsyncEnregistremantImg(CompteActivity.this);
            asyncEnreImg.execute(asyncCharImg.getBitmapReslt());
            user.setImgProfil(asyncEnreImg.getImgStrReslt());
           // GestionUtilisateur.modifierImgProfil(user.getImgProfil());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 502 && resultCode == RESULT_OK){
            user = data.getParcelableExtra("user");
            Toast.makeText(CompteActivity.this, "ok :" + user.toString(), Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_OK){
            asyncCharImg = new AsyncChargementImg(CompteActivity.this, imageProfil);
            asyncCharImg.execute(data.getData());
            flag = true;
        }else {
            Toast.makeText(getApplicationContext(),"Vous n'avez pas choisi d'image", Toast.LENGTH_LONG).show();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////
private class ExampleAsyncTask extends AsyncTask<Utilisateur,String,String> {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        p = new ProgressDialog(CompteActivity.this);
        p.setMessage("Modification en cours ...");
        p.setIndeterminate(false);
        p.setCancelable(false);
        p.show();
    }
    @Override
    protected String doInBackground(Utilisateur... users) {
        GestionUtilisateur.modifierUser(users[0]);
        return "ok";
    }
    @Override
    protected void onPostExecute(String ok) {
        super.onPostExecute(ok);
        p.show();
        Toast.makeText(CompteActivity.this, "Image enregistrée.",Toast.LENGTH_LONG).show();
    }
    }


}