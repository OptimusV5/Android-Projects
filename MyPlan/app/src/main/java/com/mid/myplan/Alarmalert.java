package com.mid.myplan;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class Alarmalert extends Activity {

	private static  Alarmalert alarmAlert;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		Intent intent1= new Intent(Alarmalert.this,PlayMusic.class);
		startService(intent1);
        new AlertDialog.Builder(Alarmalert.this,R.style.dialog).setTitle("计划提醒").setMessage("有一项计划需要执行，快去查看吧！")
                .setPositiveButton("关闭提醒", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        Intent intent1 = new Intent(Alarmalert.this, PlayMusic.class);
                        stopService(intent1);
                        Alarmalert.this.finish();
                    }
                }).show();
	}
	
	private static Alarmalert getInstance(){
		return alarmAlert;
	}

}
