package me.zchang.abitractor.algorithm;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/5.
 */
public class GridExtractorMajority implements ABitractor.Extractor {
    @Override
    public int[] extractFromBitmap(Bitmap srcBitmap, int sampleLevel) {
        int bitmapHeight = srcBitmap.getHeight();
        int bitmapWidth = srcBitmap.getWidth();
        int tarWidth = bitmapWidth / sampleLevel;
        int tarHeight = bitmapHeight / sampleLevel;

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
                generated[i * tarWidth + j] = temp.get(0).getKey();
            }
        }
        return generated;
    }
}
