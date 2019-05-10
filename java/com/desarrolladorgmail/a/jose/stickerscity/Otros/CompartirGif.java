package com.desarrolladorgmail.a.jose.stickerscity.Otros;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CompartirGif {
    private Context TheThis;
    private String carpeta = "StickerCity";
    private String fichero = "sticker";
    private boolean escritura;
    private boolean lectura;

   public void comprartirImagen(Context context, Bitmap ImageToSave){
            String CurrentDateAndTime = getCurrentDateAndTime();
            //creo ruta de donde se guardan las imagenes
       String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+System.getProperty("file.separator")+carpeta;
       File dir = new File(file_path);
       if (!dir.exists()) {
           dir.mkdirs();
       }
            File file = new File( dir,fichero + CurrentDateAndTime + ".gif");// crea el fichero pa la imagen
            try {
                file.createNewFile();
                FileOutputStream fOut = new FileOutputStream(file);
                ImageToSave.compress(Bitmap.CompressFormat.WEBP, 85, fOut);
                fOut.flush();
                fOut.close();
                file.setReadable(true, false);
                Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(file.getAbsolutePath()));
                intent.setPackage("com.whatsapp");
                intent.setType("text/plain");
                context.startActivity(intent);
            }

            catch(Exception e) {
                Toast.makeText(context, "¡No se ha podido compartir el gif!", Toast.LENGTH_SHORT).show();
                Log.i("Error shared",e.getMessage());
            }



    }
    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
