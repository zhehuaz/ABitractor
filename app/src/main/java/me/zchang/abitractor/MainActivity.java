package me.zchang.abitractor;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.Map;

import me.zchang.abitractor.algorithm.ABitractor;
import me.zchang.abitractor.algorithm.GridExtractorMajority;
import me.zchang.abitractor.algorithm.WidthSearchSampler;
import me.zchang.abitractor.ui.CurveChart;

public class MainActivity extends AppCompatActivity implements ABitractor.ABitractorAsyncListener{
    public final static int REQUEST_SELECT_IMAGE = 0x1;

    private ImageView srcImage;
    private ImageView dstImage;
    private ImageView libImage;
    private ImageView markedImage;
    private SeekBar sampleBar;
    private TextView sampleText;
    private ProgressBar progressBar;
    private Button extractButton;
//    private Chart chart;
    private CurveChart cvChart;
    private TextView degreeText;

    private String selectImagePath;
    private Bitmap selectImage;
    private Bitmap sampledImage;

    private int sampleTime;

    private ABitractor aBitractor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sampleTime = 8;
        srcImage = (ImageView) findViewById(R.id.iv_src);
        dstImage = (ImageView) findViewById(R.id.iv_des);
        libImage = (ImageView) findViewById(R.id.iv_lib);
        markedImage = (ImageView) findViewById(R.id.iv_mark);
        sampleBar = (SeekBar) findViewById(R.id.sb_sample);
        sampleText = (TextView) findViewById(R.id.tv_sample);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        extractButton = (Button) findViewById(R.id.bt_extract);
        cvChart = (CurveChart) findViewById(R.id.cc_curve);
//        chart = (Chart) findViewById(R.id.ct_statics);
        degreeText = (TextView) findViewById(R.id.tv_degree);
        sampleBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //sampleTime = (int) Math.pow(2, progress + 1);
                sampleTime = progress + 1;
                sampleText.setText(sampleTime + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //sampleTime = (int)Math.pow(2, sampleBar.getProgress() + 1);

        srcImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, MainActivity.REQUEST_SELECT_IMAGE);
            }
        });

        extractButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aBitractor != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    aBitractor.asyncGenerate(MainActivity.this, sampleTime);
                    aBitractor.asyncSample(MainActivity.this);
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = sampleTime;
//                    sampledImage = BitmapFactory.decodeFile(selectImagePath, options);
//                    libImage.setImageBitmap(sampledImage);
                    extractButton.setClickable(false);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MainActivity.REQUEST_SELECT_IMAGE && resultCode == RESULT_OK) {
            Uri selectImageUri = data.getData();
            Cursor cursor = getContentResolver().query(selectImageUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            cursor.moveToFirst();
            selectImagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();

            // origin image
            selectImage = BitmapFactory.decodeFile(selectImagePath);
            aBitractor = new ABitractor(selectImage);
            aBitractor.setExtractor(new GridExtractorMajority());
            aBitractor.setSampler(new WidthSearchSampler());
            srcImage.setImageURI(Uri.fromFile(new File(selectImagePath)));

            sampleTime = 8;
            cvChart.clear();
        }
    }


    @Override
    public void onSampled(int sampleLevel, Map<Integer, Integer> widthD) {
//        if(widthD != null)
//            chart.setData(widthD);
    }

    @Override
    public void onGenerated(Bitmap bitmap, Bitmap GridMarkedBmp, float degree) {
        progressBar.setVisibility(View.INVISIBLE);
        extractButton.setClickable(true);
        if(bitmap != null) {
            dstImage.setImageBitmap(bitmap);
            sampleBar.setProgress(sampleTime - 1);
        }

        markedImage.setImageBitmap(GridMarkedBmp);
        degreeText.setText(degree + "");

        cvChart.drawDot(sampleTime, degree);
        if(sampleTime < 100) {
            aBitractor.asyncGenerate(this, ++sampleTime);
        }
        //                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = sampleTime;
//                selectImage = BitmapFactory.decodeFile(selectImagePath, options);
//                dstImage.setImageBitmap(selectImage);
    }
}
