package com.desarrolladorgmail.a.jose.stickerscity.Activities;


import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.desarrolladorgmail.a.jose.stickerscity.Fragments.StickersFragment;
import com.desarrolladorgmail.a.jose.stickerscity.R;

public class MostrarStickers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_stickers);
        String palabra=getIntent().getStringExtra("palabra");// obtengo la palabra que ha introducido el ususario a buscar
        setupActionBar();// Metodo para poner la flecha de volver hacia atras
        FragmentManager fragmentManager=getSupportFragmentManager();

        StickersFragment sf=new StickersFragment();// Creo una instancia de la clase StickerFragment
        sf.setPalabra(palabra);//le paso la palabra
        /*Reemplazo el contenido del activity por el del fragment donde se muestran todos los stickers*/
        fragmentManager.beginTransaction()
        .replace(R.id.frame_muestra,sf)
        .commit();

    }

    public void setupActionBar(){
        ActionBar actionBar= getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


    }
}
