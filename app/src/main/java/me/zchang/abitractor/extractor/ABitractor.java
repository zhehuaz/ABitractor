package me.zchang.abitractor.extractor;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.INotificationSideChannel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Created by Administrator on 2015/10/2.
 */
public class ABitractor {
    Bitmap bitmap;

    int bitmapWidth;
    int bitmapHeight;

    int tarWidth;
    int tarHeight;

    public ABitractor() { }
    public ABitractor(Bitmap bitmap) {
        this.bitmap = bitmap;

        if(bitmap != null) {
            bitmapWidth = bitmap.getWidth();
            bitmapHeight = bitmap.getHeight();
        }
    }

    /**
     *
     * @param listener
     * @param sampleLevel
     */
    public void generate(@NonNull final ABitractorAsyncListener listener, final int sampleLevel) {
        //this.sampleLevel = (int)(Math.pow(2.0, (int)(Math.log(sampleLevel) / Math.log(2.d))));
        if (sampleLevel == 1) {
            listener.onGeneated(bitmap, 1.f);
            return;
        }
        if (sampleLevel <= 1 || bitmap == null) {
            listener.onGeneated(null, -1);
            return;
        }

        tarWidth = bitmapWidth / sampleLevel;
        tarHeight = bitmapHeight / sampleLevel;

        if (tarHeight <= 0 || tarWidth <= 0) {
            listener.onGeneated(null, -1);
            return;
        }


        new AsyncTask<Bitmap, Integer, Bitmap>() {

            @Override
            protected Bitmap doInBackground(Bitmap... params) {
                Bitmap srcBitmap = params[0];
                int []generated = new int[tarHeight * tarWidth];
                //Bitmap generatedBitmap = Bitmap.createBitmap(tarWidth, tarHeight, Bitmap.Config.ARGB_8888);
                HashMap<Integer, Integer> colors = new HashMap<>();
                List<Map.Entry<Integer, Integer>> temp;

                int startX, startY;
                int curColor;
                // for one row
                for (int i = 0; i < tarHeight; i++) {
                    // for one column
                    for (int j = 0; j < tarWidth; j++) {
                        // for one block
                        colors.clear();

                        startX = sampleLevel * j;
                        startY = sampleLevel * i;

                        // for one row in a block
                        for (int k = startY; k < startY + sampleLevel && k < bitmapHeight; k++) {
                            // for one column in a block
                            for (int l = startX; l < startX + sampleLevel && l < bitmapWidth; l++) {
                                curColor = srcBitmap.getPixel(l, k);
                                if (colors.containsKey(curColor))
                                    colors.put(curColor, colors.get(curColor) + 1);
                                else
                                    colors.put(curColor, 1);
                            }
                        }
                        temp = new ArrayList<>(colors.entrySet());
                        Collections.sort(temp, new Comparator<Map.Entry<Integer, Integer>>() {
                            @Override
                            public int compare(Map.Entry<Integer, Integer> lhs, Map.Entry<Integer, Integer> rhs) {
                                return (rhs.getValue() - lhs.getValue());
                            }
                        });
                        //generatedBitmap.setPixel(j, i, temp.get(0).getValue());
                        //generated[j * tarWidth + i] = temp.get(0).getKey();
                        generated[i * tarWidth + j] = temp.get(0).getKey();
                    }
                }
                return Bitmap.createBitmap(generated, tarWidth, tarHeight, srcBitmap.getConfig());
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                listener.onGeneated(bitmap, 0);
            }
        }.execute(bitmap);

    }

    public interface ABitractorAsyncListener {
        void onGeneated(Bitmap bitmap, float degree);
    }
}
