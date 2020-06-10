package com.mehayou.pressedview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.FloatRange;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TextPressedView extends AppCompatTextView {

    private final static int TARGET_NONE = 0;
    private final static int TARGET_TEXT = 1;
    private final static int TARGET_BACKGROUND = 2;
    private final static int TARGET_COMPOUND_DRAWABLE = 4;

    private int mPressedTarget;

    private final static int MODE_COLOR = 0;
    private final static int MODE_ALPHA = 1;

    private int mPressedMode;
    private int mPressedColor;
    private float mPressedAlpha;

    private int mDefaultTextColor;

    @ColorRes
    private int mRippleColorResources;
    private Drawable mDefaultBackgroundDrawable;
    private Drawable mRippleBackgroundDrawable;

    public TextPressedView(Context context) {
        this(context, null);
    }

    public TextPressedView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextPressedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setDefaultTextColor(getCurrentTextColor());

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TextPressedView);
        int rippleColor = a.getResourceId(R.styleable.TextPressedView_textRippleColor, 0);

        int pressedTarget = a.getInt(R.styleable.TextPressedView_textPressedTarget, TARGET_NONE);

        int pressedMode = a.getInt(R.styleable.TextPressedView_textPressedMode, MODE_COLOR);
        int pressedColor = a.getColor(R.styleable.TextPressedView_textPressedColor, Color.DKGRAY);
        float pressedAlpha = a.getFloat(R.styleable.TextPressedView_textPressedAlpha, 0.5f);

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
     * @param target {TextPressedView.TARGET_NONE} 无效果
     *               {TextPressedView.TARGET_TEXT} 文字按下效果
     *               {TextPressedView.TARGET_BACKGROUND} 背景按下效果
     *               {TextPressedView.TARGET_COMPOUND_DRAWABLE} 图标按下效果
     */
    private void setPressedTarget(int target) {
        this.mPressedTarget = target;
    }

    public int getPressedTarget() {
        return mPressedTarget;
    }

    /**
     * 设置按下效果模式
     *
     * @param mode {TextPressedView.MODE_COLOR} 按下颜色变化
     *             {TextPressedView.MODE_ALPHA} 按下透明度变化
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
     * 记录当前默认文字颜色
     *
     * @param color color
     */
    private void setDefaultTextColor(int color) {
        this.mDefaultTextColor = color;
    }

    public int getDefaultTextColor() {
        return mDefaultTextColor;
    }

    /**
     * 设置水波纹背景效果颜色，模式效果失效
     *
     * @param colorId 颜色资源ID
     */
    public void setRippleColorResources(@ColorRes int colorId) {
        if (this.mRippleColorResources != colorId) {
            this.mRippleColorResources = colorId;
            updateRippleBackgroundDrawable(mDefaultBackgroundDrawable);
        }
    }

    @ColorRes
    public int getRippleColorResources() {
        return mRippleColorResources;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        // 布局编辑模式 && 当前水波纹背景为空
        if (isInEditMode() || mRippleBackgroundDrawable != null) {
            return;
        }

        int pressedTarget = getPressedTarget();

        // 图标支持
        if ((pressedTarget & TARGET_COMPOUND_DRAWABLE) != 0) {
            Drawable[] compoundDrawables = getCompoundDrawables();
            for (Drawable compoundDrawable : compoundDrawables) {
                if (compoundDrawable != null) {
                    if (pressed) {
                        if (getPressedMode() == MODE_COLOR) {
                            compoundDrawable.setColorFilter(getPressedColor(), PorterDuff.Mode.MULTIPLY);
                        } else if (getPressedMode() == MODE_ALPHA) {
                            compoundDrawable.mutate().setAlpha((int) (getPressedAlpha() * 255));
                        }
                    } else {
                        if (getPressedMode() == MODE_COLOR) {
                            compoundDrawable.clearColorFilter();
                        } else if (getPressedMode() == MODE_ALPHA) {
                            compoundDrawable.mutate().setAlpha(255);
                        }
                    }
                }
            }
        }

        // 背景支持
        if ((pressedTarget & TARGET_BACKGROUND) != 0) {
            Drawable background = getBackground();
            if (background != null) {
                if (pressed) {
                    if (getPressedMode() == MODE_COLOR) {
                        background.setColorFilter(getPressedColor(), PorterDuff.Mode.MULTIPLY);
                        invalidate();
                    } else if (getPressedMode() == MODE_ALPHA) {
                        background.mutate().setAlpha((int) (getPressedAlpha() * 255));
                    }
                } else {
                    if (getPressedMode() == MODE_COLOR) {
                        background.clearColorFilter();
                        invalidate();
                    } else if (getPressedMode() == MODE_ALPHA) {
                        background.mutate().setAlpha(255);
                    }
                }
            }
        }

        // 文字支持 && 当前水波纹背景为空
        if ((pressedTarget & TARGET_TEXT) != 0) {
            if (pressed) {
                if (getPressedMode() == MODE_COLOR) {
                    getPaint().setColorFilter(new PorterDuffColorFilter(getPressedColor(), PorterDuff.Mode.MULTIPLY));
                    invalidate();
                } else if (getPressedMode() == MODE_ALPHA) {
                    super.setTextColor(ColorStateList.valueOf(getDefaultTextColor()).withAlpha((int) (getPressedAlpha() * 255)).getDefaultColor());
                }
            } else {
                if (getPressedMode() == MODE_COLOR) {
                    getPaint().setColorFilter(null);
                    invalidate();
                } else if (getPressedMode() == MODE_ALPHA) {
                    super.setTextColor(getDefaultTextColor());
                }
            }
        }
    }

    @Override
    public void setTextColor(int color) {
        int pressedTarget = getPressedTarget();

        if ((pressedTarget & TARGET_TEXT) != 0) {
            // 文字支持时，记录当前文字颜色
            if (isPressed()) {
                return;
            }

            super.setTextColor(color);
            setDefaultTextColor(getCurrentTextColor());
        } else {
            super.setTextColor(color);
        }
    }

    @Override
    public void setTextColor(ColorStateList colors) {
        int pressedTarget = getPressedTarget();

        if ((pressedTarget & TARGET_TEXT) != 0) {
            // 文字支持时，记录当前文字颜色
            if (isPressed()) {
                return;
            }

            super.setTextColor(colors);
            setDefaultTextColor(getCurrentTextColor());
        } else {
            super.setTextColor(colors);
        }
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
        // 初始化背景
        updateRippleBackgroundDrawable(getBackground());
    }

    /**
     * 更新水波纹背景
     *
     * @param background 当前背景
     */
    public void updateRippleBackgroundDrawable(Drawable background) {
        int rippleColorResources = getRippleColorResources();
        if (rippleColorResources > 0
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
}
