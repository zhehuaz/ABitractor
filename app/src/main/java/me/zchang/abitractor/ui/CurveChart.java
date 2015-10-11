package me.zchang.abitractor.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/11.
 */
public class CurveChart extends View {
    Paint paint = new Paint();
    Map<Integer, Float> dots;
    int sampleLevel = 0;
    float degree;

    public CurveChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        dots = new HashMap<>();
    }

    public CurveChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dots = new HashMap<>();

    }

    public CurveChart(Context context) {
        super(context);
        dots = new HashMap<>();
    }

    public CurveChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        dots = new HashMap<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(0, 0, 500, 0, paint);
        int lastKey = 0;
        float lastValue = 0;
        for(Map.Entry e : dots.entrySet()) {
            canvas.drawCircle((int) e.getKey() * 5, 120 - (float)e.getValue() * 100, 2.f, paint);
            //canvas.drawLine( lastKey * 5, 120 - lastValue * 100, (int) e.getKey() * 5, 120 - (float)e.getValue() * 100, paint);
            //lastKey = (int)e.getKey();
            //lastValue = (float)e.getValue();
        }
    }

    public void drawDot(int sampleLevel, float degree) {
        this.sampleLevel = sampleLevel;
        this.degree = degree;
        invalidate();
        dots.put(sampleLevel, degree);
    }

    public void clear() {
        dots.clear();
        sampleLevel = 0;
        degree = 0;
        invalidate();
    }
}
