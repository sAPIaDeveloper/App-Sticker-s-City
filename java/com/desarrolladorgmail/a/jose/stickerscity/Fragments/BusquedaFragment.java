package com.desarrolladorgmail.a.jose.stickerscity.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.desarrolladorgmail.a.jose.stickerscity.Otros.CompartirGif;
import com.desarrolladorgmail.a.jose.stickerscity.Otros.GuardarGifs;
import com.desarrolladorgmail.a.jose.stickerscity.Activities.MostrarStickers;
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


public class BusquedaFragment extends Fragment implements StickerAdapter.OnButtonClikedListener {
    EnviarBusqueda callback;
    RecyclerView recyclerView;
    StickerAdapter adapter;
    List<Sticker> stickerList;
    StickerViewModel model;
    private InterstitialAd mInterstitialAd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view= inflater.inflate(R.layout.fragment_busqueda, container, false);

        Button button= view.findViewById(R.id.boton);
        final EditText editText= view.findViewById(R.id.palabra);

        stickerList= new ArrayList<Sticker>();
        recyclerView= view.findViewById(R.id.ultimoDescargado);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));// Indico como debe mostrarse los sticker
        //creo una instancia del viewmodel
        model=ViewModelProviders.of(this).get(StickerViewModel.class);
        adapter=new StickerAdapter(getContext(),stickerList,this,getActivity());// creo el adapter
        recyclerView.setAdapter(adapter);
        //Llamo al metodo para obtener los gif guardados
        model.getGifGuardado().observe(this, new Observer<List<Sticker>>() {
            @Override
            public void onChanged(@Nullable List<Sticker> products) {
                adapter.notifyDataSetChanged();// si hay cambios en el adapter lo notifica
                stickerList.clear();
                stickerList.addAll(products);
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                String palabra=editText.getText().toString();
                if(getFragmentManager().findFragmentById(R.id.fragment2)==null || getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
                    Intent intent=new Intent(getActivity(),MostrarStickers.class);
                    intent.putExtra("palabra",palabra);
                    startActivity(intent);
                }else callback.onChange(palabra);

            }
        });
        return  view;

    }
    //Lo implemento para poder pasarle informacion al otro fragment
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callback = (EnviarBusqueda) getParentFragment();
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString()+ " Debería implementar el interfaz EnviarBusqueda");
        }
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
    /*
    * Metodo para descargar la imagen y guardalo en un Bitmap
    * */
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

    public interface EnviarBusqueda {
        void onChange(String text);
    }




}
