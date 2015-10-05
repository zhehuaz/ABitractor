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

    private Paint paint;

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
        paint = new Paint();
        paint.setStrokeWidth(2.f);
       // paint.setColor(Color.RED);
        if(dots == null)
            return ;
        for(Map.Entry<Integer, Integer> e : dots.entrySet()) {
            if(e.getKey() <= 500 && e.getKey() >= 0) {
                canvas.drawLine(e.getKey() * 2,
                        300.f,
                        e.getKey() * 2,
                        300 - e.getValue() * 0.1f < 0 ? 0 : 300 - e.getValue() *.1f,
                        paint);
            }
        }
    }

    public void setData(Map<Integer, Integer> dots) {
        this.dots = dots;
        this.invalidate();
    }
}
