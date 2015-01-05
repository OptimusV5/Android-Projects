package com.mid.myplan;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;

public class CustomDailog {
    
    private View mParent;
    private PopupWindow mPopupWindow; 
    private LinearLayout mRootLayout;
    private LayoutParams mLayoutParams;
    public String days,times;
    public TextView timeView, dateView;
    public static TextView musicView;
    public Context context;
    public int year,month,day,hour,minute;
    private final String setAlarmFloder = "/system/media/audio/";
    final Calendar calendar = Calendar.getInstance();
    //PopupWindow������һ��ParentView�����Ա�������������
    public CustomDailog(final Context context, View parent,final Activity activity) {
        mParent = parent;
        this.context = context;
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        minute = calendar.get(Calendar.MINUTE);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        //���ز����ļ�
        mRootLayout = (LinearLayout)mInflater.inflate(R.layout.alarm, null);
        mLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        days = calendar.get(Calendar.YEAR) + "-" + ((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" : "")
                + (calendar.get(Calendar.MONTH) + 1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "")
                + calendar.get(Calendar.DAY_OF_MONTH);
        times = (calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "")
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + (calendar.get(Calendar.MINUTE) < 10 ? "0" : "")
                + calendar.get(Calendar.MINUTE);
        timeView = (TextView) mRootLayout.findViewById(R.id.alarmTime);
        dateView = (TextView) mRootLayout.findViewById(R.id.alarmDate);
        musicView = (TextView) mRootLayout.findViewById(R.id.music);
        dateView.setText(days);
        timeView.setText(times);
        timeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 取得按下按钮的时间作为TimePickerDialog的默认值
                calendar.setTimeInMillis(System.currentTimeMillis());
                //定义获取时间
                int mHour = calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute = calendar.get(Calendar.MINUTE);
                //跳出TimePickerDialog来设定时间
                new TimePickerDialog(context,R.style.AppTheme_Dialog,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view,int hourOfDay, int minute) {
                                CustomDailog.this.hour = hourOfDay;
                                CustomDailog.this.minute = minute;
                                timeView.setText(format(hourOfDay) + ":" + format(minute));
                            }
                        },mHour, mMinute, true).show();
            }
        });
        dateView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //定义获取时间
                calendar.setTimeInMillis(System.currentTimeMillis());
                int mYear = calendar.get(Calendar.YEAR);
                int mMouth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                //跳出TimePickerDialog来设定时间
                new DatePickerDialog(context,R.style.AppTheme_Dialog,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        CustomDailog.this.year = year;
                        CustomDailog.this.month = monthOfYear;
                        CustomDailog.this.day = dayOfMonth;
                        dateView.setText(year + "-" + format(monthOfYear + 1) + "-" + format(dayOfMonth));
                    }
                },mYear,mMouth,mDay).show();

            }
        });
        musicView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bFolder(setAlarmFloder)) {
                    //打开系统铃声设置
                    Intent intent =  new  Intent (RingtoneManager
                            .ACTION_RINGTONE_PICKER);
                    //设置铃声类型和title
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                    intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置闹铃声音");
                    //设置完成后,返回当前的activity
                    activity.startActivityForResult(intent, 1);
                }
            }
        });
    }    
    
    //����Dailog�ı���
    public void setTitle(String title) {
        TextView mTitle = (TextView)mRootLayout.findViewById(R.id.CustomDlgTitle);
        mTitle.setText(title);
    }
    
    
    //����Dailog�ġ�ȷ������ť
    public void setPositiveButton(String text,OnClickListener listener ) {
        final Button buttonOK = (Button)mRootLayout.findViewById(R.id.CustomDlgButtonOK); 
        buttonOK.setText(text);
        buttonOK.setOnClickListener(listener);
        buttonOK.setVisibility(View.VISIBLE);
    }
    
    //����Dailog�ġ�ȡ������ť
    public void setNegativeButton(String text,OnClickListener listener ) {
        final Button buttonCancel = (Button)mRootLayout.findViewById(R.id.CustomDlgButtonCancel);
        buttonCancel.setText(text);
        buttonCancel.setOnClickListener(listener);
        buttonCancel.setVisibility(View.VISIBLE);
    }
    public boolean setAlarm(int id) {
        // 取得设定后的时间 秒跟毫秒设为0
        calendar.setTimeInMillis(System.currentTimeMillis());
        long t1 = calendar.getTimeInMillis();
        calendar.set(year,month,day,hour,minute);
        long t2 = calendar.getTimeInMillis();
        if (t2 < t1)
            return false;
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //定闹钟设定时间到时要执行CallAlarm.class
        Intent intent  = new Intent(context,CallAlarm.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //获取AlarmManager
        //AlarmManager.RTC_WAKEUP设定服务在系统休眠时同样会执行
        //
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //set()设定的PendingIntent只会执行一次
        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),sender);
        return true;
    }
    //����Dailog�ĳ���
    public void setLayoutParams(int width, int height) {
        mLayoutParams.width  = width;
        mLayoutParams.height = height;
    }
    //检测是否存在指定的文件夹,如果不存在,则创建
    private boolean bFolder (String serbFolder){
        boolean btmp =false;
        File f = new File(serbFolder);
        if (!f.exists()) {
            if (f.mkdirs()) {
                btmp = true;
            }else {
                btmp = false;
            }
        }else {
            btmp =true;
        }
        return btmp;
    }
    private String format(int x)
    {
        String s = "" + x;
        if (s.length() == 1)
            s = "0" + s;
        return s;
    }
    //��ʾDailog
    public void show() {
    
        if(mPopupWindow == null) {
            mPopupWindow = new PopupWindow(mRootLayout, mLayoutParams.width,mLayoutParams.height);
            mPopupWindow.setFocusable(true);
        }
        mPopupWindow.showAtLocation(mParent, Gravity.CENTER, Gravity.CENTER, Gravity.CENTER);
    }
    
    //ȡ��Dailog����ʾ
    public void dismiss() {
        if(mPopupWindow == null) {
            return;
        }
        mPopupWindow.dismiss();
    }

}
