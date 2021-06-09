package com.yan.android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: 024347
 * 时间: 2021/6/9
 * 版本:
 * 说明:
 */
public class YanProcessView extends View {
    /**
     * 进程列表
     */
    private final List<String> processList = new ArrayList<>();
    /**
     * 底线画笔
     */
    private Paint linePaint;
    /**
     * 底线颜色
     */
    private int lineColor = Color.parseColor("#FFF4E2");
    /**
     * 底线高度
     */
    private float lineHeight;
    /**
     * 进程半径
     */
    private float processRadius;
    /**
     * 相邻两个进程中心点距离
     */
    private float processSpace;
    /**
     * 默认选中进程下标
     */
    private int selectIndex;
    /**
     * 进程点画笔
     */
    private Paint processPaint;
    /**
     * 文字与进程点间距
     */
    private float processMargin;
    /**
     * 文字画笔
     */
    private Paint textPaint;
    /**
     * 文字大小
     */
    private float textSize;
    /**
     * 选中的颜色
     */
    private int selectColor;
    /**
     * 文字框
     */
    private Rect textRect;

    public YanProcessView(Context context) {
        this(context, null);
    }

    public YanProcessView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YanProcessView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = null;
        try {
            // 获取自定义属性
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.YanProcessView);
            // 获取底线颜色
            lineColor = typedArray.getColor(R.styleable.YanProcessView_yan_process_line_color, Color.parseColor("#FFF4E2"));
            // 获取底线高度
            lineHeight = typedArray.getDimension(R.styleable.YanProcessView_yan_process_line_height, Utils.dip2px(context, 5));
            // 获取进程半径
            processRadius = typedArray.getDimension(R.styleable.YanProcessView_yan_process_radius, Utils.dip2px(context, 5));
            // 获取相邻两个进程中心点距离
            processSpace = typedArray.getDimension(R.styleable.YanProcessView_yan_process_space, Utils.dip2px(context, 80));
            // 获取文字与进程点间距
            processMargin = typedArray.getDimension(R.styleable.YanProcessView_yan_process_margin, 0);
            // 获取默认选中进程下标
            selectIndex = typedArray.getInt(R.styleable.YanProcessView_yan_process_default_select_index, 0);
            // 获取选中的颜色
            selectColor = typedArray.getColor(R.styleable.YanProcessView_yan_process_select_color, Color.RED);
            // 获取文字大小
            textSize = typedArray.getDimension(R.styleable.YanProcessView_yan_process_text_size, Utils.sp2px(context, 18));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (typedArray != null) {
                // 这里记得释放
                typedArray.recycle();
            }
        }
        init();
    }


    private void init() {
        linePaint = new Paint();
        // 设置底线颜色
        linePaint.setColor(lineColor);
        // 抗锯齿
        linePaint.setAntiAlias(true);
        // 设置底线宽度
        linePaint.setStrokeWidth(lineHeight);

        processPaint = new Paint();
        // 抗锯齿
        processPaint.setAntiAlias(true);

        textPaint = new Paint();
        // 设置文字大小
        textPaint.setTextSize(textSize);
        textPaint.setColor(lineColor);

        // 文字框
        textRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float width, height;
        if (processList.size() == 0) {
            // 没有进程点则宽度只有左右padding值
            width = getPaddingLeft() + getPaddingRight();
        } else {
            // 有进程点
            width = 2 * processRadius + (processList.size() - 1) * processSpace + getPaddingLeft() + getPaddingRight();
        }
        if (processList.size() != 0) {
            textPaint.getTextBounds(processList.get(0), 0, processList.get(0).length(), textRect);
        }
        // 高度
        height = Math.max(lineHeight, 2 * processRadius) + getPaddingTop() + getPaddingBottom() + processMargin + textRect.height();
        // 设置控件的宽高
        setMeasuredDimension((int) width, (int) height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int size = processList.size();
        // 起始端点的X坐标
        float lineStartX = processRadius + getPaddingLeft();
        // 起始端点的Y坐标
        float lineStartY = getPaddingTop() + Math.max(processRadius * 2, lineHeight) / 2;
        // 终止端点的X坐标
        float lintEndX = lineStartX + (size != 0 ? (size - 1) * processSpace : 0);
        // 终止端点的Y坐标
        float lineEndY = getPaddingTop() + Math.max(processRadius * 2, lineHeight) / 2;
        // 画底线
        canvas.drawLine(lineStartX, lineStartY, lintEndX, lineEndY, linePaint);
        for (int i = 0; i < processList.size(); i++) {
            if (i == selectIndex) {
                processPaint.setColor(selectColor);
                textPaint.setColor(selectColor);
            } else {
                processPaint.setColor(lineColor);
                textPaint.setColor(lineColor);
            }
            // 画进程点
            float cx = getPaddingLeft() + i * processSpace + processRadius;
            float cy = getPaddingTop() + Math.max(processRadius * 2, lineHeight) / 2;
            canvas.drawCircle(cx, cy, processRadius, processPaint);
            // 画文字
            float textX, textY;
            // 测量文字所占大小
            textPaint.getTextBounds(processList.get(i), 0, processList.get(i).length(), textRect);
            textY = getPaddingTop() + Math.max(2 * processRadius, lineHeight) + processMargin + textRect.height();
            if (i == 0) {
                textX = getPaddingLeft();
            } else if (i == processList.size() - 1) {
                textX = getPaddingLeft() + i * processSpace + processRadius - textRect.width();
            } else {
                textX = getPaddingLeft() + i * processSpace + processRadius - textRect.width() * 1.0f / 2;
            }
            canvas.drawText(processList.get(i), textX, textY, textPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    public void setData(List<String> processList) {
        setData(processList, 0);
    }

    public void setData(List<String> processList, int selectIndex) {
        this.processList.clear();
        if (processList != null) {
            this.processList.addAll(processList);
        }
        this.selectIndex = selectIndex;
        invalidate();
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        invalidate();
    }

}
