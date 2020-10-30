package com.dboy.image.check.box;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


public class ImageCheckBox extends View {
    //画布范围
    private final Rect dst = new Rect();
    //绘制范围
    private final Rect src = new Rect();
    /**
     * 选中状态
     */
    boolean isCheck = true;
    /**
     * 中间等待状态
     */
    boolean isWait = false;
    private int width;
    private int height;
    private Paint checkPaint;
    /**
     * 不允许外部调用setOnClickListener
     */
    private boolean doNotOtherSetOnclick = false;
    /**
     * 回调
     */
    private OnCheckListener mOnCheckListener;
    /**
     * 点击事件监听器
     */
    private final OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            isWait = false;
            isCheck = !isCheck;
            if (mOnCheckListener != null) {
                mOnCheckListener.onCheck(isCheck);
            }
            invalidate();
        }
    };
    //开启状态图片
    private int onDrawableId;
    //关闭状态图片
    private int offDrawableId;
    //等待桩体的图片
    private int waitDrawableID;
    private Bitmap mOnBitmap;
    private Bitmap mOffBitmap;
    private Bitmap mWaitBitmap;

    public ImageCheckBox(Context context) {
        this(context, null);
    }

    public ImageCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.ImageCheckBox);
            onDrawableId = t.getResourceId(R.styleable.ImageCheckBox_onDrawable, R.mipmap.ic_image_check_on);
            offDrawableId = t.getResourceId(R.styleable.ImageCheckBox_offDrawable, R.mipmap.ic_image_check_off);
            waitDrawableID = t.getResourceId(R.styleable.ImageCheckBox_waitDrawable, R.mipmap.ic_image_wait);

            mOnBitmap = BitmapFactory.decodeResource(getResources(), onDrawableId);
            mOffBitmap = BitmapFactory.decodeResource(getResources(), offDrawableId);
            mWaitBitmap = BitmapFactory.decodeResource(getResources(), waitDrawableID);

            isCheck = t.getBoolean(R.styleable.ImageCheckBox_checked, false);
            this.setOnClickListener(mOnClickListener);
            doNotOtherSetOnclick = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        init();
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        if (doNotOtherSetOnclick) {
            return;
        }
        super.setOnClickListener(onClickListener);
    }

    private void init() {
        checkPaint = new Paint();
        checkPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Bitmap fieldBitmap;
        //是否是等待状态
        if (!isWait) {
            if (isCheck) {
                fieldBitmap = mOnBitmap;
            } else {
                fieldBitmap = mOffBitmap;
            }
        } else {
            fieldBitmap = mWaitBitmap;
        }
        //资源绘制范围
        src.set(0, 0, fieldBitmap.getWidth(), fieldBitmap.getHeight());
        //画布范围
        dst.set(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(), height - getPaddingBottom());

        canvas.drawBitmap(fieldBitmap, src, dst, checkPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);

    }

    /**
     * 状态改变事件监听器
     */
    public void setOnCheckListener(OnCheckListener onCheckListener) {
        mOnCheckListener = onCheckListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = measureWidth(widthMeasureSpec);
        height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int result = 200;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 200;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    public boolean isWait() {
        return isWait;
    }

    /**
     * @param wait 等待状态
     */
    public void setWait(boolean wait) {
        this.isWait = wait;
        if (!isWait) {
            setCheck(true);
        } else {
            if (waitDrawableID == 0) {
                throw new UnsupportedOperationException("没有设置等待状态的图片  app:waitDrawable");
            } else {
                invalidate();
            }
        }
    }

    public boolean isCheck() {
        return isCheck;
    }

    /**
     * 设置选中状态不通知回调方法
     *
     * @param isCheck 选中状态
     */
    public void setCheck(boolean isCheck) {
        isWait = false;
        this.isCheck = isCheck;
        invalidate();
    }

    /**
     * 设置选中状态并通知回调
     *
     * @param isCheck 选中状态
     */
    public void setCheckChangeListener(boolean isCheck) {
        this.isCheck = !isCheck;
        mOnClickListener.onClick(this);
    }

    public interface OnCheckListener {
        void onCheck(boolean isCheck);
    }
}
