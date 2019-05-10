package com.desarrolladorgmail.a.jose.stickerscity.Otros;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GuardarGifs {

    private Context TheThis;
    private String carpeta = "StickerCity";
    private String fichero = "gif";
    private boolean escritura;
    private boolean lectura;

    public void comprobarAlmacenamiento(){
        String estado = Environment.getExternalStorageState();

        if (estado.equals(Environment.MEDIA_MOUNTED))
        {
            Log.i("Ficheros", "Tarjeta montada");
            lectura = true;
            escritura = true;
        }
        else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
        {
            Log.i("Ficheros", "Tarjeta montada para solo lectura");
            lectura = true;
            escritura = false;
        }
        else
        {
            Log.i("Ficheros", "Tarjeta desmontada");
            lectura = false;
            escritura = false;
        }

    }


    public void guardarImagen(Context context, Bitmap ImageToSave) {
        comprobarAlmacenamiento();
        if(lectura && escritura ){
            TheThis = context;
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+System.getProperty("file.separator")+carpeta;
            String CurrentDateAndTime = getCurrentDateAndTime();
            File dir = new File(file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, fichero + CurrentDateAndTime + ".gif");
            try {
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(file);
                ImageToSave.compress(Bitmap.CompressFormat.WEBP, 85, fOut);
                fOut.flush();
                fOut.close();
                MakeSureFileWasCreatedThenMakeAvabile(file);
                Toast.makeText(TheThis, "Gif guardado en la galería.", Toast.LENGTH_SHORT).show();
            }

            catch(Exception e) {
                Toast.makeText(TheThis, "¡No se ha podido guardar el gif!", Toast.LENGTH_SHORT).show();
            }

        }else{Toast.makeText(TheThis, "Existe problemas con tu almacenamiento externo", Toast.LENGTH_SHORT).show();}
    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }
    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

}