package com.antonionicolaspina.animatedcountertextview;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

public final class AnimatedCounterTextView extends TextView implements Runnable, ValueAnimator.AnimatorUpdateListener {
  private int animationDuration = 300;
  private int currentValue;
  private int destinationValue;

  public AnimatedCounterTextView(Context context) {
    super(context);
    init(context, null);
  }

  public AnimatedCounterTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public AnimatedCounterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public AnimatedCounterTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init(context, attrs);
  }

  protected void init(Context context, AttributeSet attrsSet) {
    if (null != attrsSet) {
      TypedArray attrs = context.getTheme().obtainStyledAttributes(attrsSet, R.styleable.AnimatedCounterTextView, 0, 0);

      try {
        animationDuration = attrs.getInteger(R.styleable.AnimatedCounterTextView_duration, animationDuration);
        destinationValue  = attrs.getInteger(R.styleable.AnimatedCounterTextView_value, destinationValue);
      } finally {
        attrs.recycle();
      }
    }

    if (isInEditMode()) {
      setCurrent(destinationValue);
    } else {
      setValue(destinationValue);
    }
  }

  @Override
  public void run() {
    ValueAnimator animator = ValueAnimator.ofInt(currentValue, destinationValue);
    animator.setDuration(animationDuration);
    animator.addUpdateListener(this);
    animator.start();
  }

  @Override
  public void onAnimationUpdate(ValueAnimator valueAnimator) {
    setCurrent((int) valueAnimator.getAnimatedValue());
  }

  protected void setCurrent(int value) {
    currentValue = value;
    setText(String.valueOf(currentValue));
  }

  /**************
   *** Public ***
   **************/

  /**
   * Set the destination value applying the animation.
   * @param destinationValue
   */
  public void setValue(int destinationValue) {
    this.destinationValue = destinationValue;

    setCurrent(currentValue);
    if (currentValue != destinationValue) {
      post(this);
    }
  }
}
