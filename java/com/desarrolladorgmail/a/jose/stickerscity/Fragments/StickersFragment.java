package com.desarrolladorgmail.a.jose.stickerscity.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.desarrolladorgmail.a.jose.stickerscity.Otros.CompartirGif;
import com.desarrolladorgmail.a.jose.stickerscity.Otros.GuardarGifs;
import com.desarrolladorgmail.a.jose.stickerscity.POJOS.Guardado;
import com.desarrolladorgmail.a.jose.stickerscity.POJOS.Sticker;
import com.desarrolladorgmail.a.jose.stickerscity.R;
import com.desarrolladorgmail.a.jose.stickerscity.Adapter.StickerAdapter;
import com.desarrolladorgmail.a.jose.stickerscity.API.StickerViewModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import static android.content.Context.CONNECTIVITY_SERVICE;


public class StickersFragment extends Fragment implements StickerAdapter.OnButtonClikedListener {
    RecyclerView recyclerView;
    StickerAdapter adapter;
    List<Sticker> stickerList;
    StickerViewModel model;
    String palabra;
    View vista;
    TextView textView;
    ImageView imageView;
    ProgressBar progressBar;
    private InterstitialAd mInterstitialAd;
    public void setPalabra(String palabra){
        palabra=palabra.replace(" ","+");
        this.palabra=palabra;
    }
    public StickersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         vista=inflater.inflate(R.layout.fragment_stickers, container, false);
        progressBar=vista.findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);
        if(getResources().getConfiguration().orientation!=Configuration.ORIENTATION_PORTRAIT){
            vista.setBackgroundResource(R.drawable.fondo_inferior);
        }
        stickerList= new ArrayList<Sticker>();
        recyclerView= vista.findViewById(R.id.recycler_sticker);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        model=ViewModelProviders.of(this).get(StickerViewModel.class);
        adapter=new StickerAdapter(getContext(),stickerList,this,getActivity());
        recyclerView.setAdapter(adapter);
        ConnectivityManager connectivityManager=(ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info= connectivityManager.getActiveNetworkInfo();

        textView = vista.findViewById(R.id.error);
        boolean  isConnected= info!=null && info.isConnected();
        if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
            if(isConnected ){
                mostrarStickers();
            }else{
                progressBar=vista.findViewById(R.id.progress);
                progressBar.setVisibility(View.GONE);
                imageView= vista.findViewById(R.id.lobo);
                imageView.setImageResource(R.drawable.lobo);
                textView.setText(getString(R.string.sin_conexion));
            }
        }
        return vista;
    }

    public void mostrarStickers(){
        progressBar=vista.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);
        final  ImageView imageView=vista.findViewById(R.id.lobo);
        model.getStickers(palabra).observe(this, new Observer<List<Sticker>>() {
            @Override
            public void onChanged(@Nullable List<Sticker> products) {
                progressBar=vista.findViewById(R.id.progress);
                progressBar.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();
                stickerList.clear();
                if(products.size()==0 || products==null){

                    imageView.setImageResource(R.drawable.stich);
                    imageView.setVisibility(View.VISIBLE);
                    textView.setText(getString(R.string.sin_coincidencias));
                }else{imageView.setVisibility(View.GONE); textView.setText("");}
                stickerList.addAll(products);
            }
        });
    }

    public void cambiarBusqueda(String busqueda){
        this.palabra=busqueda;
        mostrarStickers();
    }

    @Override
    public void onButtonCliked(View view,final Sticker sticker) {
        if(view.getId()==R.id.icono_corazon) {
            ImageView imageView = view.findViewById(R.id.icono_corazon);
            if (sticker.isFavorito()) {
                imageView.setImageResource(R.drawable.favorito_vacio_24dp);
                sticker.setFavorito(false);
                model.deleteSticker(sticker);

            }else{
                imageView.setImageResource(R.drawable.favorite_red_24dp);
                sticker.setFavorito(true);
                model.addSticker(sticker);
            }
        }else if (view.getId() == R.id.icono_descarga) {
            new DescargarImagenAsync().execute(sticker.getUrl());
            Guardado guardado = new Guardado(sticker.getUrl(), sticker.isFavorito());
            model.addStickerSave(guardado);
        } else {
            mInterstitialAd = new InterstitialAd(getContext());
            mInterstitialAd.setAdUnitId("");
            AdRequest adRequest= new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
            mInterstitialAd.setAdListener(new AdListener(){
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mInterstitialAd.show();

                }


            });
            new CompartirImagenAsync().execute(sticker.getUrl());
        }

    }

    public class CompartirImagenAsync extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap imagen = descargarImagen(url);
            return imagen;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            if(bitmap!=null){
                CompartirGif guardarGifse = new CompartirGif();
                guardarGifse.comprartirImagen(getContext(), bitmap);
            }else{ Toast.makeText(getContext(), "¡No se ha podido acceder el gif para descargarlo!", Toast.LENGTH_SHORT).show();}

        }
    }
    public class DescargarImagenAsync extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap imagen = descargarImagen(url);
            return imagen;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null){
                GuardarGifs guardarGifse = new GuardarGifs();
                guardarGifse.guardarImagen(getContext(), bitmap);
            }else{ Toast.makeText(getContext(), "¡No se ha podido acceder el gif para descargarlo!", Toast.LENGTH_SHORT).show();}

        }
    }

    private Bitmap descargarImagen (String imageHttpAddress){
        URL imageUrl = null;
        Bitmap imagen = null;
        try{
            imageUrl = new URL(imageHttpAddress);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            imagen = BitmapFactory.decodeStream(conn.getInputStream());
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return imagen;
    }

}
