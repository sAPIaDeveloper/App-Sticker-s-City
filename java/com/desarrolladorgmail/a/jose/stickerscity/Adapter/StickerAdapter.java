package com.desarrolladorgmail.a.jose.stickerscity.Adapter;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.desarrolladorgmail.a.jose.stickerscity.POJOS.Sticker;
import com.desarrolladorgmail.a.jose.stickerscity.R;
import com.desarrolladorgmail.a.jose.stickerscity.API.StickerViewModel;

import java.util.List;

public class StickerAdapter  extends  RecyclerView.Adapter<StickerAdapter.ViewHolder>  {
    @NonNull
    Context context;
    private List<Sticker> listaStickers;
    private OnButtonClikedListener listener;
    StickerViewModel model;


    public StickerAdapter(@NonNull Context context, List<Sticker> listaStickers, OnButtonClikedListener listener, Activity activity) {//Constructor donde recibe los datos
        this.context = context;
        this.listaStickers = listaStickers;
        this.listener = listener;
        model=ViewModelProviders.of((FragmentActivity) activity).get(StickerViewModel.class);

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {// Metodo que se encarga de inflar el sticker con sus iconos
       View view= LayoutInflater.from(context).inflate(R.layout.items_row_gridlayout,viewGroup,false);
       ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final StickerAdapter.ViewHolder viewHolder, final int i) {
        final Sticker stick= listaStickers.get(i);// Obtengo el sticker de la lista
        Uri uri= Uri.parse(stick.getUrl());// Obtengo la url para mostrar el sticker
        Glide.with(context).load(uri).into(viewHolder.imagen_sticker);// Al elemento que contiene la referencia del la imagen le pongo la imagen
        if(stick.isFavorito()){ //Compruebo si el sticker esta en favorito
            viewHolder.icono_corazon.setImageResource(R.drawable.favorite_red_24dp);//Si esta en favorito le pongo el corazon rojo
        }else{ viewHolder.icono_corazon.setImageResource(R.drawable.favorito_vacio_24dp);}//Si no el vacio
        //Le pongo los iconos tanto el de descargar como el de compartir
        viewHolder.icono_descarga.setImageResource(R.drawable.ic_file_download_black_24dp);
        viewHolder.icono_compartir.setImageResource(R.drawable.ic_share_black_24dp);
        /*
        * Estos metodos capturan si se ha pulsado alguno de los tres iconos para poder implementarle sus funcionalidades
         */
        viewHolder.icono_descarga.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                listener.onButtonCliked(v,listaStickers.get(i));
            }
        });

        viewHolder.icono_corazon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                listener.onButtonCliked(v,listaStickers.get(i));
            }
        });
        viewHolder.icono_compartir.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                listener.onButtonCliked(v,listaStickers.get(i));
            }
        });
    }

    //Interfaz para poder capturar el evento del click
    public interface OnButtonClikedListener{
        void onButtonCliked(View view, Sticker sticker);
    }

    @Override
    public int getItemCount() {return listaStickers.size();}//Devuelve el numero de sticker que contiene la lista

    // Clase que inicializa todos los objetos que se visualizan en el item_row
    public class ViewHolder extends  RecyclerView.ViewHolder  {
        ImageView imagen_sticker;
        ImageView icono_corazon;
        ImageView icono_descarga;
        ImageView icono_compartir;
        public ViewHolder(View view) {
            super(view);
            imagen_sticker=view.findViewById(R.id.imagen);
            icono_corazon=view.findViewById(R.id.icono_corazon);
            icono_descarga=view.findViewById(R.id.icono_descarga);
            icono_compartir=view.findViewById(R.id.icono_compartir);

        }
    }
}
