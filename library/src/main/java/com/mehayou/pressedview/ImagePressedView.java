package com.mehayou.pressedview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class ImagePressedView extends AppCompatImageView {

    private final static int TARGET_NONE = 0;
    private final static int TARGET_SRC = 1;
    private final static int TARGET_BACKGROUND = 2;

    private int mPressedTarget;

    private final static int MODE_COLOR = 0;
    private final static int MODE_ALPHA = 1;

    private int mPressedMode;
    private int mPressedColor;
    private float mPressedAlpha;

    @ColorRes
    private int mRippleColorResources;
    private Drawable mDefaultImageDrawable;
    private Drawable mDefaultBackgroundDrawable;
    private Drawable mRippleImageDrawable;
    private Drawable mRippleBackgroundDrawable;

    public ImagePressedView(Context context) {
        this(context, null);
    }

    public ImagePressedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImagePressedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ImagePressedView);
        int rippleColor = a.getResourceId(R.styleable.ImagePressedView_imageRippleColor, 0);

        int pressedTarget = a.getInt(R.styleable.ImagePressedView_imagePressedTarget, TARGET_NONE);

        int pressedMode = a.getInt(R.styleable.ImagePressedView_imagePressedMode, MODE_COLOR);
        int pressedColor = a.getColor(R.styleable.ImagePressedView_imagePressedColor, Color.DKGRAY);
        float pressedAlpha = a.getFloat(R.styleable.ImagePressedView_imagePressedAlpha, 0.5f);

        setPressedTarget(pressedTarget);

        setPressedMode(pressedMode);
        setPressedColor(pressedColor);
        setPressedAlpha(pressedAlpha);

        setRippleColorResources(rippleColor);
        a.recycle();
    }

    /**
     * 设置按下效果目标
     *
     * @param target {@link ImagePressedView.TARGET_NONE} 无效果
     *               {@link ImagePressedView.TARGET_SRC} 图片按下效果
     *               {@link ImagePressedView.TARGET_BACKGROUND} 背景按下效果
     */
    public void setPressedTarget(int target) {
        this.mPressedTarget = target;
    }

    public int getPressedTarget() {
        return mPressedTarget;
    }

    /**
     * 设置按下效果模式
     *
     * @param mode {@link ImagePressedView.MODE_COLOR} 按下颜色变化
     *             {@link ImagePressedView.MODE_ALPHA} 按下透明度变化
     */
    private void setPressedMode(int mode) {
        this.mPressedMode = mode;
    }

    public int getPressedMode() {
        return mPressedMode;
    }

    /**
     * 设置颜色，当模式为颜色变化
     *
     * @param color 颜色值
     */
    public void setPressedColor(int color) {
        this.mPressedColor = color;
    }

    /**
     * 设置颜色，当模式为颜色变化
     *
     * @param colorId 颜色资源ID
     */
    public void setPressedColorResources(@ColorRes int colorId) {
        setPressedColor(getResources().getColor(colorId));
    }

    public int getPressedColor() {
        return mPressedColor;
    }

    /**
     * 设置颜色，当模式为透明度变化
     *
     * @param alpha 透明度
     */
    public void setPressedAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        this.mPressedAlpha = alpha;
    }

    public float getPressedAlpha() {
        return mPressedAlpha;
    }

    /**
     * 设置水波纹效果颜色，模式效果失效
     *
     * @param colorId 颜色资源ID
     */
    public void setRippleColorResources(@ColorRes int colorId) {
        if (this.mRippleColorResources != colorId) {
            this.mRippleColorResources = colorId;
            updateRippleBackgroundDrawable(mDefaultBackgroundDrawable);
            updateRippleImageDrawable(mDefaultImageDrawable);
        }
    }

    @ColorRes
    public int getRippleColorResources() {
        return mRippleColorResources;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (isInEditMode() || mRippleImageDrawable != null || mRippleBackgroundDrawable != null) {
            return;
        }

        int pressedTarget = getPressedTarget();

        // 背景支持
        if ((pressedTarget & TARGET_BACKGROUND) != 0) {
            Drawable background = getBackground();
            if (background != null) {
                if (pressed) {
                    if (getPressedMode() == MODE_COLOR) {
                        background.setColorFilter(getPressedColor(), PorterDuff.Mode.MULTIPLY);
                        invalidate();
                    } else if (getPressedMode() == MODE_ALPHA) {
                        background.setAlpha((int) (getPressedAlpha() * 255));
                    }
                } else {
                    if (getPressedMode() == MODE_COLOR) {
                        background.clearColorFilter();
                        invalidate();
                    } else if (getPressedMode() == MODE_ALPHA) {
                        background.setAlpha(255);
                    }
                }
            }
        }

        // 图片支持
        if ((pressedTarget & TARGET_SRC) != 0) {
            Drawable drawable = getDrawable();
            if (drawable != null) {
                if (pressed) {
                    if (getPressedMode() == MODE_COLOR) {
                        setColorFilter(getPressedColor(), PorterDuff.Mode.MULTIPLY);
                    } else if (getPressedMode() == MODE_ALPHA) {
                        drawable.setAlpha((int) (getPressedAlpha() * 255));
                    }
                } else {
                    if (getPressedMode() == MODE_COLOR) {
                        clearColorFilter();
                    } else if (getPressedMode() == MODE_ALPHA) {
                        drawable.setAlpha(255);
                    }
                }
            }
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        if (isPressed()) {
            return;
        }
        super.setImageDrawable(drawable);
        // 图片支持时，记录当前图片
        mDefaultImageDrawable = getDrawable();
        updateRippleImageDrawable(mDefaultImageDrawable);
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        if (isPressed()) {
            return;
        }
        super.setBackgroundDrawable(background);
        // 背景支持时，记录当前背景
        mDefaultBackgroundDrawable = getBackground();
        updateRippleBackgroundDrawable(mDefaultBackgroundDrawable);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 初始化背景&图片
        updateRippleBackgroundDrawable(getBackground());
        updateRippleImageDrawable(getDrawable());
    }

    /**
     * 添加水波纹背景
     *
     * @param background 当前背景
     */
    public void updateRippleBackgroundDrawable(Drawable background) {
        int rippleColorResources = getRippleColorResources();
        if ((getPressedTarget() & TARGET_BACKGROUND) != 0 && rippleColorResources > 0
                && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
                && mRippleBackgroundDrawable != getBackground()) {
            // 背景支持 && API21以上 && 水波纹有颜色 && 当前不是水波纹背景
            ColorStateList stateList = getResources().getColorStateList(rippleColorResources);
            if (background != null) {
                mRippleBackgroundDrawable = new RippleDrawable(stateList, background, null);
            }
            super.setBackgroundDrawable(mRippleBackgroundDrawable);
        }
    }

    /**
     * 添加水波纹图片
     *
     * @param drawable 当前图片
     */
    public void updateRippleImageDrawable(Drawable drawable) {
        int rippleColorResources = getRippleColorResources();
        if ((getPressedTarget() & TARGET_SRC) != 0 && rippleColorResources > 0
                && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP
                && mRippleImageDrawable != getDrawable()) {
            // 背景支持 && API21以上 && 水波纹有颜色 && 当前不是水波纹图片
            ColorStateList stateList = getResources().getColorStateList(rippleColorResources);
            if (drawable != null) {
                mRippleImageDrawable = new RippleDrawable(stateList, drawable, null);
            }
            super.setImageDrawable(mRippleImageDrawable);
        }
    }

}
