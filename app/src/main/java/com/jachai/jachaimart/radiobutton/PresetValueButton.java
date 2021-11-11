package com.jachai.jachaimart.radiobutton;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.jachai.jachaimart.R;

import java.util.ArrayList;


public class PresetValueButton extends RelativeLayout implements RadioCheckable {
    // Views
    private TextView mValueTextView;

    // Constants
    public static final int DEFAULT_TEXT_COLOR = Color.TRANSPARENT;

    // Attribute Variables
    private String mValue;
    private String mImageUrl;
    private int mValueTextColor;

    private int mPressedTextColor;

    // Variables
    private Drawable mInitialBackgroundDrawable;
    private OnClickListener mOnClickListener;
    private OnTouchListener mOnTouchListener;
    private boolean mChecked;
    private ArrayList<OnCheckedChangeListener> mOnCheckedChangeListeners = new ArrayList<>();


    //================================================================================
    // Constructors
    //================================================================================

    public PresetValueButton(Context context) {
        super(context);
        setupView();
    }

    public PresetValueButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs);
        setupView();
    }

    public PresetValueButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(attrs);
        setupView();
    }

    public PresetValueButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttributes(attrs);
        setupView();
    }

    //================================================================================
    // Init & inflate methods
    //================================================================================

    private void parseAttributes(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.PresetValueButton, 0, 0);
        Resources resources = getContext().getResources();
        try {
            mValue = a.getString(R.styleable.PresetValueButton_presetButtonValueText);
            mImageUrl = a.getString(R.styleable.PresetValueButton_presetButtonUnitText);
            mValueTextColor = a.getColor(R.styleable.PresetValueButton_presetButtonValueTextColor, resources.getColor(R.color.black));
            mPressedTextColor = a.getColor(R.styleable.PresetValueButton_presetButtonPressedTextColor, Color.WHITE);
        } finally {
            a.recycle();
        }
    }

    // Template method
    private void setupView() {
        inflateView();
        bindView();
        setCustomTouchListener();
    }

    protected void inflateView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.custom_button_one_field, this, true);
        mValueTextView = (TextView) findViewById(R.id.cod);
        mInitialBackgroundDrawable = getBackground();
    }

    protected void bindView() {
        if (mValueTextColor != DEFAULT_TEXT_COLOR) {
            mValueTextView.setTextColor(mValueTextColor);
        }


        setBackgroundDrawable(mInitialBackgroundDrawable);
        //setBackgroundResource(R.drawable.background_custom_radio_buttons_selected_state);
        mValueTextView.setText(mValue);
    }

    //================================================================================
    // Overriding default behavior
    //================================================================================

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
    }

    protected void setCustomTouchListener() {
        super.setOnTouchListener(new TouchListener());
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    public OnTouchListener getOnTouchListener() {
        return mOnTouchListener;
    }

    private void onTouchDown(MotionEvent motionEvent) {
        setChecked(true);
    }

    private void onTouchUp(MotionEvent motionEvent) {
        // Handle user defined click listeners
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
        }
    }
    //================================================================================
    // Public methods
    //================================================================================

    public void setCheckedState() {
        setBackgroundDrawable(mInitialBackgroundDrawable);
        //setBackgroundResource(R.drawable.background_shape_preset_button__pressed);
        //setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));

        mValueTextView.setBackgroundResource(R.drawable.background_custom_radio_buttons_selected_state);
    }

    public void setNormalState() {

        setBackgroundDrawable(mInitialBackgroundDrawable);

        //backView.setBackgroundResource(R.drawable.background_custom_radio_buttons_selected_state);
        mValueTextView.setBackgroundColor(getResources().getColor(R.color.white));
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
        mValueTextView.setText(mValue);
    }




    //================================================================================
    // Checkable implementation
    //================================================================================

    @Override
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            if (!mOnCheckedChangeListeners.isEmpty()) {
                for (int i = 0; i < mOnCheckedChangeListeners.size(); i++) {
                    mOnCheckedChangeListeners.get(i).onCheckedChanged(this, mChecked);
                }
            }
            if (mChecked) {
                setCheckedState();
            } else {
                setNormalState();
            }
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    public void addOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListeners.add(onCheckedChangeListener);
    }

    @Override
    public void removeOnCheckChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListeners.remove(onCheckedChangeListener);
    }

    //================================================================================
    // Inner classes
    //================================================================================
    private final class TouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onTouchDown(event);
                    break;
                case MotionEvent.ACTION_UP:
                    onTouchUp(event);
                    break;
            }
            if (mOnTouchListener != null) {
                mOnTouchListener.onTouch(v, event);
            }
            return true;
        }
    }
}
