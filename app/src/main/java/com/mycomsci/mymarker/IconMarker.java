package com.mycomsci.mymarker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by root on 9/10/2560.
 */

public class IconMarker {
    Context context;
    Marker m;

    public IconMarker(Context context){
        this.context = context;
    }

    public static IconMarker getInstan(Context context){
        return new IconMarker(context);
    }

    public Marker setMarkerAvatar(Marker m,String url){
        this.m = m;
        LoadBitmap loadBitmap  = new LoadBitmap();
        loadBitmap.execute(url);
        return m;
    }

    public class LoadBitmap extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap b = null;
            try {
                b = BitmapFactory.decodeStream((InputStream) new URL(params[0]).getContent());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap !=null){
                BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(getIcon(bitmap));
                m.setIcon(icon);
            }
        }
    }

    public Bitmap getIcon(Bitmap source){
        if(source!=null) {
            Resources resources = context.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(resources, R.drawable.location_pin);
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }
            float r =(size / 8f);
            Bitmap roundedBitmap = createRoundedRectBitmap(getResizedBitmap(squaredBitmap, bitmap.getWidth()/2), r, r, r, r);
            squaredBitmap.recycle();

            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

            // set default bitmap config if none
            if(bitmapConfig == null)
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;

            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLACK);
            paint.setTextSize((int) (12*scale));
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

            int xx = (bitmap.getWidth()-roundedBitmap.getWidth())/2;
            canvas.drawBitmap(roundedBitmap,xx, 9*scale,paint);
            return bitmap;

        }else
            return source;
    }

    private static Bitmap createRoundedRectBitmap(Bitmap bitmap,
                                                  float topLeftCorner, float topRightCorner,
                                                  float bottomRightCorner, float bottomLeftCorner) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);


        final int color = Color.WHITE;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        Path path = new Path();
        float[] radii = new float[]{
                topLeftCorner, bottomLeftCorner,
                topRightCorner, topRightCorner,
                bottomRightCorner, bottomRightCorner,
                bottomLeftCorner, bottomLeftCorner
        };


        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        path.addRoundRect(rectF, radii, Path.Direction.CW);
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    public static Bitmap getResizedBitmap(Bitmap bm,int w) {
        if(bm==null)return bm;
        int width = bm.getWidth();
        int height = bm.getHeight();

        int newWidth=width,newHeight=height;
        //if(w<newWidth){
        newWidth = w;
        newHeight = newWidth*height/width;
        //}

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap;
        try {
            resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        }catch (IllegalArgumentException e){
            return bm;
        }
        return resizedBitmap;
    }
}
