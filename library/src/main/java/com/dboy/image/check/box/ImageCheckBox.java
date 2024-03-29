package com.dboy.image.check.box;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


/**
 * 图片 CheckBox ，设置选中和未选中图片，还允许设置等待状态。
 */
public class ImageCheckBox extends View implements View.OnClickListener {
    /**
     * 画布范围
     */
    protected final Rect dst = new Rect();
    /**
     * 绘制范围
     */
    protected final Rect src = new Rect();
    /**
     * 绘制 Paint
     */
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 选中状态
     */
    protected boolean isCheck = true;
    /**
     * 中间等待状态
     */
    protected boolean isWait = false;
    /**
     * 控件宽度
     */
    protected int width;
    /**
     * 控件高度
     */
    protected int height;
    /**
     * 回调
     */
    protected CheckBoxChangeListener mCheckBoxChangeListener;
    /**
     * 开启状态图片
     */
    protected int onDrawableId;
    /**
     * 关闭状态图片
     */
    protected int offDrawableId;
    /**
     * 等待状态的图片
     */
    protected int waitDrawableID;
    /**
     * 开启Bitmap
     */
    protected Bitmap mOnBitmap;
    /**
     * 关闭Bitmap
     */
    protected Bitmap mOffBitmap;
    /**
     * 等待Bitmap
     */
    protected Bitmap mWaitBitmap;
    /**
     * 外部onclick
     */
    protected OnClickListener mUserClickListener;

    public ImageCheckBox(Context context) {
        this(context, null);
    }

    public ImageCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.ImageCheckBox);
        onDrawableId = t.getResourceId(R.styleable.ImageCheckBox_onDrawable, -1);
        offDrawableId = t.getResourceId(R.styleable.ImageCheckBox_offDrawable, -1);
        waitDrawableID = t.getResourceId(R.styleable.ImageCheckBox_waitDrawable, -1);
        if (onDrawableId != -1) {
            mOnBitmap = BitmapFactory.decodeResource(getResources(), onDrawableId);
        }
        if (offDrawableId != -1) {
            mOffBitmap = BitmapFactory.decodeResource(getResources(), offDrawableId);
        }
        if (waitDrawableID != -1) {
            mWaitBitmap = BitmapFactory.decodeResource(getResources(), waitDrawableID);
        }
        isCheck = t.getBoolean(R.styleable.ImageCheckBox_checked, isCheck);
        super.setOnClickListener(this);
        t.recycle();
    }

    public int dp2px(final float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        if (onClickListener != this) {
            mUserClickListener = onClickListener;
        }
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
        if (fieldBitmap != null) {
            //图片范围
            src.set(0, 0, fieldBitmap.getWidth(), fieldBitmap.getHeight());
            //取最小高度或宽度 保证比例正确
            int minSize = Math.min(width, height);
            int drawWidth = minSize - Math.max(getPaddingRight(), getPaddingLeft());
            int drawHeight = minSize - Math.max(getPaddingTop(), getPaddingBottom());
            //绘制区域
            dst.set(0, 0, Math.min(drawWidth, drawHeight), Math.min(drawWidth, drawHeight));

            //绘制区域居中
            int centerX = (getWidth() - dst.width()) / 2;
            int centerY = (getHeight() - dst.height()) / 2;
            dst.offset(centerX, centerY);

            canvas.drawBitmap(fieldBitmap, src, dst, paint);
        }
    }

    /**
     * 状态改变事件监听器
     */
    public void setOnCheckBoxChangeListener(CheckBoxChangeListener checkBoxChangeListener) {
        mCheckBoxChangeListener = checkBoxChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = measureSize(widthMeasureSpec);
        height = measureSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    protected int measureSize(int measureSpec) {
        int result = dp2px(40);
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, specSize);
                break;
            default:
        }
        return result;
    }

    /**
     * 是否是等待状态
     */
    public boolean isWait() {
        return isWait;
    }

    /**
     * @param wait 等待状态
     */
    @SuppressWarnings("unused")
    public void setWait(boolean wait) {
        setWait(wait, false);
    }

    /**
     * @param wait           等待状态
     * @param notifyListener 是否通知回调；只有在wait=false时进行判断
     */
    public void setWait(boolean wait, boolean notifyListener) {
        this.isWait = wait;
        if (!isWait) {
            setCheck(isCheck, notifyListener);
        } else {
            if (waitDrawableID == 0 || mWaitBitmap == null) {
                throw new UnsupportedOperationException("没有设置等待状态的图片  app:waitDrawable");
            } else {
                postInvalidateOnAnimation();
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
    @SuppressWarnings("unused")
    public void setCheck(boolean isCheck) {
        setCheck(isCheck, false);
    }

    /**
     * 设置选中状态不通知回调方法
     *
     * @param isCheck        选中状态
     * @param notifyListener 是否通知回调方法
     */
    public void setCheck(boolean isCheck, boolean notifyListener) {
        isWait = false;
        this.isCheck = isCheck;
        if (notifyListener) {
            this.isCheck = !isCheck;
            this.onClick(this);
        } else {
            postInvalidateOnAnimation();
        }
    }

    @Override
    public void onClick(View view) {
        isWait = false;
        isCheck = !isCheck;
        if (mCheckBoxChangeListener != null) {
            mCheckBoxChangeListener.onCheck(isCheck);
        }
        if (mUserClickListener != null) {
            mUserClickListener.onClick(view);
        }
        postInvalidateOnAnimation();
    }

    public interface CheckBoxChangeListener {
        void onCheck(boolean isCheck);
    }
}
