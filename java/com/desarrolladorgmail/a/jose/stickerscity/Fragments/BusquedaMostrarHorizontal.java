package com.desarrolladorgmail.a.jose.stickerscity.Fragments;


import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desarrolladorgmail.a.jose.stickerscity.Fragments.BusquedaFragment;
import com.desarrolladorgmail.a.jose.stickerscity.Fragments.StickersFragment;
import com.desarrolladorgmail.a.jose.stickerscity.R;


public class BusquedaMostrarHorizontal extends Fragment  implements BusquedaFragment.EnviarBusqueda{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_busqueda_mostrar_horizontal, container, false);
        if(getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT){
            getChildFragmentManager().beginTransaction().replace(R.id.mostrarFragment,new BusquedaFragment()).commit();
        }

        return view;
    }


    @SuppressLint("LongLogTag")
    @Override
    public void onChange(String text) {
        StickersFragment stickersFragment= (StickersFragment) getChildFragmentManager().findFragmentById(R.id.fragment2);
        if(stickersFragment!=null){
            stickersFragment.cambiarBusqueda(text);

        }else Log.i("El fragment StickerFragment ","Es nulo no esta creado debidamente");
    }
}
