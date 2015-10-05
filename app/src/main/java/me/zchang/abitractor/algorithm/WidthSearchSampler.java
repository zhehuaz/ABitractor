package me.zchang.abitractor.algorithm;

import android.graphics.Bitmap;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/5.
 */
public class WidthSearchSampler implements ABitractor.Sampler {
    Map<Integer, Integer> widthD;

    public WidthSearchSampler() {
        widthD = new HashMap<>();
    }

    @Override
    public int calSampleLevel(Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int lastColor;
        int curColor;
        int curLength;
        widthD.clear();
        for (int i = 0;i < bitmapHeight; i ++) {
            lastColor = -1;
            curLength = 1;
            for (int j = 0;j < bitmapWidth; j ++) {
                curColor = bitmap.getPixel(j, i);
                if(curColor == lastColor) {
                    curLength ++;
                } else {
                    if(!widthD.containsKey(curLength)) {
                        widthD.put(curLength, 1);
                    } else {
                        widthD.put(curLength, widthD.get(curLength) + 1);
                    }
                    curLength = 1;
                }
                lastColor = curColor;
            }
        }
        return 0;
    }

    @Override
    public Map<Integer, Integer> getWidthD() {
        return widthD;
    }
}
