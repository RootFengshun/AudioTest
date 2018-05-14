package com.fengshun.audiotest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by fengshun on 2018/5/9.
 */
public class UnitView extends LinearLayout{
    Button mStartRecord;
    Button mStopRecord;
    Button mStartPlay;
    Button mStopPlay;
    OnClickListener mClickListener;

    public UnitView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.unit_view_layout, this);
        initViews();
    }
    public UnitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.unit_view_layout, this);
        initViews();
    }
    public UnitView(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.unit_view_layout, this);
        initViews();
    }
    private void initViews() {
        mStartRecord = (Button) findViewById(R.id.start_record);
        mStopRecord = (Button) findViewById(R.id.stop_record);
        mStartPlay = (Button) findViewById(R.id.start_play);
        mStopPlay = (Button) findViewById(R.id.stop_play);
    }


    public void setClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
        mStartPlay.setOnClickListener(mClickListener);
        mStopRecord.setOnClickListener(mClickListener);
        mStartRecord.setOnClickListener(mClickListener);
        mStopPlay.setOnClickListener(mClickListener);
    }
}
