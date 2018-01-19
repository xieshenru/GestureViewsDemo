package tyj.com.gestureviewsdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.alexvasilkov.gestures.views.GestureFrameLayout;

import tyj.com.gestureviewsdemo.R;

/**
 * @author ChenYe
 *         created by on 2018/1/19 0019. 10:23
 *         这个支持缩放的layout可以放在viewpager里面使用
 **/

public class ZoomLayoutActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom_layout);
        GestureFrameLayout gfl = (GestureFrameLayout) findViewById(R.id.gfl);
    }

    public void test(View view) {
        Toast.makeText(this, "点中按钮了", Toast.LENGTH_SHORT).show();
    }
}
