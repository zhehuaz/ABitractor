package me.zchang.abitractor.extractor;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/2.
 */
public class ABitractor {
    Bitmap bitmap;

    int bitmapWidth;
    int bitmapHeight;

    //int tarWidth;
    //int tarHeight;

    private Extractor extractor;

    public ABitractor() { }
    public ABitractor(Bitmap bitmap) {
        this.bitmap = bitmap;

        if(bitmap != null) {
            bitmapWidth = bitmap.getWidth();
            bitmapHeight = bitmap.getHeight();
        }
    }

    /**
     * Asynchronously generate a sampled picture.
     * @param listener Listener that listens to the generation result.
     * @param sampleLevel Sample level.
     */
    public void generate(@NonNull final ABitractorAsyncListener listener, final int sampleLevel) {
        //this.sampleLevel = (int)(Math.pow(2.0, (int)(Math.log(sampleLevel) / Math.log(2.d))));
        if (sampleLevel == 1) {
            listener.onGenerated(bitmap, 1.f);
            return;
        }
        if (sampleLevel <= 1 || bitmap == null) {
            listener.onGenerated(null, -1);
            return;
        }

        final int tarWidth = bitmapWidth / sampleLevel;
        final int tarHeight = bitmapHeight / sampleLevel;

        if (tarHeight <= 0 || tarWidth <= 0) {
            listener.onGenerated(null, -1);
            return;
        }

        new AsyncTask<Bitmap, Integer, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Bitmap... params) {
                if(extractor != null)
                    return Bitmap.createBitmap(extractor.extractFromBitmap(params[0], tarWidth, tarHeight, sampleLevel),
                            tarWidth,
                            tarHeight,
                            params[0].getConfig());
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                listener.onGenerated(bitmap, 0);
            }
        }.execute(bitmap);
    }

    public void setExtractor(Extractor extractor) {
        this.extractor = extractor;
    }

    public interface ABitractorAsyncListener {
        void onGenerated(Bitmap bitmap, float degree);
    }

    interface Extractor {
        int[] extractFromBitmap(Bitmap bitmap, int tarWidth, int tarHeight, int sampleLevel);
    }
}
