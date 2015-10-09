package me.zchang.abitractor.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.INotificationSideChannel;
import android.util.AttributeSet;
import android.view.View;

import java.util.Map;

/**
 * Created by Administrator on 2015/10/5.
 */
public class Chart extends View {

    private Paint paint = new Paint();

    private Map<Integer, Integer> dots;
    public Chart(Context context) {
        super(context);
        paint = new Paint();
    }

    public Chart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Chart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //paint = new Paint();
        paint.setStrokeWidth(2.f);
       // paint.setColor(Color.RED);
        if(dots == null)
            return ;
        for(Map.Entry<Integer, Integer> e : dots.entrySet()) {
            if(e.getKey() <= 500 && e.getKey() >= 0) {
                canvas.drawLine(e.getKey() * 2,
                        1000.f,
                        e.getKey() * 2,
                        1000 - e.getValue() * 0.1f < 0 ? 0 : 1000 - e.getValue() *.03f,
                        paint);
                paint.setTextSize(20.f);
                canvas.drawText(e.getKey() + "", e.getKey() * 2, 1010, paint);
                canvas.drawText(e.getValue() + "", e.getKey() * 2,1000 - e.getValue() * 0.1f < 0 ? 0 : 1000 - e.getValue() *.03f, paint);
            }
        }
    }

    public void setData(Map<Integer, Integer> dots) {
        this.dots = dots;
        this.invalidate();
    }
}
