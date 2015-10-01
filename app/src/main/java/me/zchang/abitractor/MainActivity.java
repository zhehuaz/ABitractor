package me.zchang.abitractor;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public final static int REQUEST_SELECT_IMAGE = 0x1;

    private ImageView srcImage;
    private ImageView dstImage;
    private SeekBar sampleBar;
    private TextView sampleText;

    private String selectImagePath;
    private Bitmap selectImage;

    private int sampleTime = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        srcImage = (ImageView) findViewById(R.id.iv_src);
        dstImage = (ImageView) findViewById(R.id.iv_des);
        sampleBar = (SeekBar) findViewById(R.id.sb_sample);
        sampleText = (TextView) findViewById(R.id.tv_sample);
        sampleBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sampleTime = (int) Math.pow(2, progress + 1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = sampleTime;
                selectImage = BitmapFactory.decodeFile(selectImagePath, options);
                dstImage.setImageBitmap(selectImage);

                sampleText.setText(((int) Math.pow(2, progress + 1)) + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sampleTime = (int)Math.pow(2, sampleBar.getProgress() + 1);

        srcImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, MainActivity.REQUEST_SELECT_IMAGE);
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

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleTime;
            selectImage = BitmapFactory.decodeFile(selectImagePath, options);

            srcImage.setImageURI(Uri.fromFile(new File(selectImagePath)));
            dstImage.setImageBitmap(selectImage);
            //srcImage.setBackground(null);
//            ViewGroup.LayoutParams params = dstImage.getLayoutParams();
//            params.width = dstImage.getWidth() / 8;
//            params.height = dstImage.getHeight() / 8;
//            dstImage.setLayoutParams(params);
        }
    }
}
