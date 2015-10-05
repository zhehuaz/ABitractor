package me.zchang.abitractor.algorithm;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

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
    private Sampler sampler;

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
                if(extractor == null)
                    extractor = new GridExtractorMajority();// default extractor
                return Bitmap.createBitmap(extractor.extractFromBitmap(params[0], sampleLevel),
                        tarWidth,
                        tarHeight,
                        params[0].getConfig());
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                listener.onGenerated(bitmap, 0);
            }
        }.execute(bitmap);
    }

    public void sample(@NonNull final ABitractorAsyncListener listener) {
        new AsyncTask<Bitmap, Integer, Integer>() {

            @Override
            protected Integer doInBackground(Bitmap... params) {
                if(sampler == null) {
                    sampler = new Sampler() {
                        @Override
                        public int calSampleLevel(Bitmap bitmap) {
                            return 1;
                        }
                    };
                }
                return sampler.calSampleLevel(params[0]);
            }

            @Override
            protected void onPostExecute(Integer integer) {
                listener.onSampled(integer);
            }
        }.execute(bitmap);
    }

    public void setExtractor(Extractor extractor) {
        this.extractor = extractor;
    }

    public void setSampler(Sampler sampler) {
        this.sampler = sampler;
    }

    /**
     *
     */
    public interface ABitractorAsyncListener {
        /**
         * Callback when recommended sample level is calculated over.
         * @param sampleLevel The recommended sample level.
         */
        void onSampled(int sampleLevel);

        /**
         * Callback when the sampled bitmap is generated.
         * @param bitmap The bitmap generated.
         * @param degree Similar degree. TODO always 0 at present
         */
        void onGenerated(Bitmap bitmap, float degree);
    }

    interface Extractor {
        int[] extractFromBitmap(Bitmap bitmap, int sampleLevel);
    }

    interface Sampler {
        int calSampleLevel(Bitmap bitmap);
    }
}