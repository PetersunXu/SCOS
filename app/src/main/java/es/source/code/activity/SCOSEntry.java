package es.source.code.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import es.source.code.service.UpdateService;


public class SCOSEntry extends AppCompatActivity implements View.OnTouchListener,GestureDetector.OnGestureListener{

    private RelativeLayout rl;
    private GestureDetector gd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);

        rl=(RelativeLayout)findViewById(R.id.relativelayout);
        rl.setOnTouchListener(this);
        rl.setLongClickable(true);
        gd=new GestureDetector((GestureDetector.OnGestureListener)this);

    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }


    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1,MotionEvent e2,float velocityX,float velocityY) {
        final int FLING_MIN_DISTANCE=100;
        final int FLING_MIN_VELOCITY=200;

        if(e1.getX() - e2.getX() > FLING_MIN_DISTANCE && Math.abs(velocityX) > FLING_MIN_VELOCITY)
        {
            Intent intent = new Intent(SCOSEntry.this,MainScreen.class);
            Bundle bundle = new Bundle();
            bundle.putString("Extra_data","FromEntry");
            intent.putExtras(bundle);
            startActivity(intent);
            Intent intent1 = new Intent(getApplicationContext(), UpdateService.class);
            startService(intent1);
        }

        return false;

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        return gd.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }
}
