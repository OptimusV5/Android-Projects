package com.mid.myplan;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog.OnDateSetListener;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;

import java.io.File;
import java.util.Calendar;

public class EditActivity extends FragmentActivity implements OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private LinearLayout done, del, alarm, fin;
    private TextView subject, content, timeView, dateView;
    private Spinner spinner;
    private String days;
    private String times;
    private String[] spinnerItem = {"学习", "工作", "生活", "娱乐", "运动"};
    private int id;
    int option_;
    int isAlarm_;
    String subject_;
    int class_, olddClass;
    String time_;
    String content_;
    SQLiteDatabase db;
    DBHelper myHelper;
    String table;
    String newtable;
    Bundle returnbundle;
    Bundle bundle;
    Intent intent;
    String oldsubject;
    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";
    private static final int ButtonAlarm = 1;
    final Calendar calendar = Calendar.getInstance();
    final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), false);
    final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        subject = (TextView) findViewById(R.id.editText1);
        content = (TextView) findViewById(R.id.editText2);
        spinner = (Spinner) findViewById(R.id.spinner);
        done = (LinearLayout) findViewById(R.id.layout_done);
        del = (LinearLayout) findViewById(R.id.layout_delete);
        alarm = (LinearLayout) findViewById(R.id.layout_alarm);
        fin = (LinearLayout) findViewById(R.id.layout_finish);
        timeView = (TextView) findViewById(R.id.timeView);
        dateView = (TextView) findViewById(R.id.dateView);

        newtable = "todos";
        intent = getIntent();

        bundle = getIntent().getExtras();
        returnbundle = new Bundle();
        myHelper = new DBHelper(this);
        db = myHelper.getWritableDatabase();
        option_ = bundle.getInt("option");
        isAlarm_ = 0;
        oldsubject = "";
        subject_ = "";

        days = calendar.get(Calendar.YEAR) + "-" + ((calendar.get(Calendar.MONTH) + 1) < 10 ? "0" : "")
                + (calendar.get(Calendar.MONTH) + 1) + "-" + (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "")
                + calendar.get(Calendar.DAY_OF_MONTH);
        times = (calendar.get(Calendar.HOUR_OF_DAY) < 10 ? "0" : "")
                + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                + (calendar.get(Calendar.MINUTE) < 10 ? "0" : "")
                + calendar.get(Calendar.MINUTE);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.spinner_group_layout, spinnerItem);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item_layout);
        spinner.setAdapter(arrayAdapter);

        opt(option_);


        dateView.setText(days);
        timeView.setText(times);

        dateView.setOnClickListener(dateOnClickListener);
        timeView.setOnClickListener(timeOnClickListener);

        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }

    }

    public OnClickListener dateOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            datePickerDialog.setVibrate(false);
            datePickerDialog.setYearRange(1985, 2028);
            datePickerDialog.setCloseOnSingleTapDay(false);
            datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
        }
    };
    public OnClickListener timeOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            timePickerDialog.setVibrate(false);
            timePickerDialog.setCloseOnSingleTapMinute(false);
            timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
        }
    };

    public void opt(int x) {
        if (x == 1) {
            del.setVisibility(View.GONE);
            fin.setVisibility(View.GONE);
            table = "todos";
            id = MainActivity.id;
        } else {
            table = bundle.getString("table");
            subject_ = bundle.getString("subject");
            subject.setText(subject_);
            del.setVisibility(View.VISIBLE);
            Cursor cursor;
            if (table.equals("done")) {
                fin.setVisibility(View.GONE);
                alarm.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
                cursor = db.rawQuery("SELECT * FROM done WHERE subject = ?", new String[]{subject_});
            } else {
                cursor = db.rawQuery("SELECT * FROM todos WHERE subject = ?", new String[]{subject_});
            }
            while (cursor.moveToNext()) {
                id = cursor.getInt(cursor.getColumnIndex("id"));
                class_ = cursor.getInt(cursor.getColumnIndex("class"));
                olddClass = class_;
                spinner.setSelection(class_);
                time_ = cursor.getString(cursor.getColumnIndex("time"));
                isAlarm_ = cursor.getInt(cursor.getColumnIndex("alarm"));
                String[] strings = time_.split(" ");
                days = strings[0];
                times = strings[1];

                content_ = cursor.getString(cursor.getColumnIndex("content"));
                content.setText(content_);
            }
            oldsubject = subject_;
        }
        spinner.setOnItemSelectedListener(mspinner);
        done.setOnClickListener(mdone);
        alarm.setOnClickListener(malarm);
        del.setOnClickListener(mdel);
        fin.setOnClickListener(mfin);
    }

    public void myturn(int x) {
        returnbundle.putInt("position", bundle.getInt("position"));
        returnbundle.putString("table", table);
        returnbundle.putInt("class", class_);
        returnbundle.putString("subject", subject_);
        returnbundle.putString("time", time_);
        returnbundle.putInt("oldClass", olddClass);
        returnbundle.putInt("alarm", isAlarm_);
        if (x != 3) {
            intent.putExtras(returnbundle);
            setResult(RESULT_OK, intent);
        } else {
            Intent it = new Intent(EditActivity.this, MainActivity.class);
            it.putExtras(returnbundle);
            startActivity(it);
        }
        finish();
    }

    View.OnClickListener mdel = new OnClickListener() {
        public void onClick(View v) {
            new AlertDialog.Builder(EditActivity.this, R.style.dialog).setTitle("删除提示框").setMessage("确认删除计划？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            db.delete(table, "subject=?", new String[]{subject_});
                            Intent intent = new Intent(EditActivity.this, CallAlarm.class);
                            PendingIntent sender = PendingIntent.getBroadcast(EditActivity.this, id, intent, 0);
                            //由AlarmManager中移除
                            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                            am.cancel(sender);
                            returnbundle.putInt("option", 4);
                            myturn(option_);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();

        }
    };

    View.OnClickListener malarm = new OnClickListener() {
        public void onClick(View v) {

            final CustomDailog dailog = new CustomDailog(EditActivity.this, getRootLayout(), EditActivity.this);
            dailog.setTitle("设置提醒");
            dailog.setPositiveButton("确定", new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isAlarm_ = 1;
                    if (dailog.setAlarm(id))
                        Toast.makeText(EditActivity.this, "提醒设置成功", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(EditActivity.this, "提醒时间设置错误，请重新设置",Toast.LENGTH_LONG).show();
                    dailog.dismiss();
                }
            });
            dailog.setNegativeButton("取消", new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isAlarm_ = 0;
                    Intent intent = new Intent(EditActivity.this, CallAlarm.class);
                    PendingIntent sender = PendingIntent.getBroadcast(EditActivity.this, id, intent, 0);
                    //由AlarmManager中移除
                    AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                    am.cancel(sender);
                    //以Toast提示已删除设定，并更新显示的闹钟时间
                    Toast.makeText(EditActivity.this, "提醒取消成功", Toast.LENGTH_SHORT).show();
                    dailog.dismiss();
                }
            });
            dailog.show();
        }
    };

    View.OnClickListener mdone = new OnClickListener() {
        public void onClick(View v) {
            ContentValues values = new ContentValues();
            subject_ = subject.getText().toString();
            time_ = days + " " + times;
            class_ = (int) spinner.getSelectedItemId();
            content_ = content.getText().toString();

            values.put("subject", subject_);
            values.put("class", class_);
            values.put("time", time_);
            values.put("content", content_);
            values.put("alarm", isAlarm_);
            if (judge(oldsubject, subject_)) {
                if (option_ == 1) {
                    //table = "todos";
                    db.insert("todos", null, values);
                    returnbundle.putInt("option", 1);
                } else {
                    db.update(table, values, "subject=?", new String[]{oldsubject});
                    returnbundle.putInt("option", 2);
                }
                myturn(option_);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "命名错误", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    };

    View.OnClickListener mfin = new OnClickListener() {
        public void onClick(View v) {
            new AlertDialog.Builder(EditActivity.this, R.style.dialog).setTitle("完成").setMessage("确认移动到完成计划列表？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ContentValues values = new ContentValues();
                            subject_ = subject.getText().toString();
                            time_ = days + " " + times;
                            content_ = content.getText().toString();
                            class_ = (int) spinner.getSelectedItemId();

                            values.put("subject", subject_);
                            values.put("class", class_);
                            values.put("time", time_);
                            values.put("content", content_);
                            values.put("alarm", isAlarm_);
                            if (judge(oldsubject, subject_)) {
                                db.delete("todos", "subject=?", new String[]{oldsubject});
                                db.insert("done", subject_, values);
                                returnbundle.putInt("option", 3);
                                myturn(option_);
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "命名错误", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    };

    Spinner.OnItemSelectedListener mspinner = new OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            class_ = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub

        }

    };

    public boolean judge(String oldsub, String newsub) {
        if (oldsub.equals(newsub) && !oldsub.equals("")) {
            return true;
        } else {
            Cursor cursor1 = db.rawQuery("SELECT * FROM " + "todos" + " WHERE subject = ?", new String[]{newsub});
            Cursor cursor2 = db.rawQuery("SELECT * FROM " + "done" + " WHERE subject = ?", new String[]{newsub});
            return cursor1.getCount() == 0 && cursor2.getCount() == 0 && !newsub.equals("");
        }
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        days = year + "-";
        days += (month + 1) < 10 ? "0" + (month + 1) + "-" : (month + 1) + "-";
        days += day < 10 ? "0" + day : day;
        dateView.setText(days);
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
        times = hour < 10 ? "0" + hour + ":" : hour + ":";
        times += minute < 10 ? "0" + minute : minute;
        timeView.setText(times);
    }

    public View getRootLayout() {
        return ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
    }

    //当设置铃声之后的回调函数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ButtonAlarm:
                Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                //将选择的铃声设置为默认
                if (pickedUri != null) {
                    RingtoneManager.setActualDefaultRingtoneUri(EditActivity.this, RingtoneManager
                            .TYPE_ALARM, pickedUri);
                    CustomDailog.musicView.setText(RingtoneManager.getRingtone(EditActivity.this, pickedUri).getTitle(EditActivity.this));
                }
                break;

            default:
                break;
        }
    }


}