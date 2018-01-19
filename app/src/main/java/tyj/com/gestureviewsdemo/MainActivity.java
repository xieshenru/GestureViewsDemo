package tyj.com.gestureviewsdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import tyj.com.gestureviewsdemo.ui.Anim1Activity;
import tyj.com.gestureviewsdemo.ui.Anim2Activity;
import tyj.com.gestureviewsdemo.ui.ControlActivity;
import tyj.com.gestureviewsdemo.ui.CropRect1Activity;
import tyj.com.gestureviewsdemo.ui.ListViewActivity;
import tyj.com.gestureviewsdemo.ui.LvWithVpActivity;
import tyj.com.gestureviewsdemo.ui.RcvActivity;
import tyj.com.gestureviewsdemo.ui.RcvWithVpActivity;
import tyj.com.gestureviewsdemo.ui.ScaleActivity;
import tyj.com.gestureviewsdemo.ui.ViewPagerActivity;
import tyj.com.gestureviewsdemo.ui.ZoomLayoutActivity;

/**
 * @author ChenYe
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 只支持缩放
     *
     * @param view
     */
    public void onlyScale(View view) {
        startActivity(new Intent(this, ScaleActivity.class));
    }

    /**
     * 在ViewPager里面的使用
     *
     * @param view
     */
    public void onViewPager(View view) {
        startActivity(new Intent(this, ViewPagerActivity.class));
    }

    /**
     * 在ListView上面的应用
     *
     * @param view
     */
    public void onListView(View view) {
        startActivity(new Intent(this, ListViewActivity.class));
    }

    /**
     * ListView与ViewPager结合使用
     *
     * @param view
     */
    public void onLvWithVp(View view) {
        startActivity(new Intent(this, LvWithVpActivity.class));
    }

    /**
     * 在RecyclerView上面的应用
     *
     * @param view
     */
    public void onRecyclerView(View view) {
        startActivity(new Intent(this, RcvActivity.class));
    }

    /**
     * 在RecyclerView上面的应用
     *
     * @param view
     */
    public void onRcvWithVp(View view) {
        startActivity(new Intent(this, RcvWithVpActivity.class));
    }

    /**
     * 在同一个界面内的“过渡动画”
     *
     * @param view
     */
    public void shareEle1(View view) {
        startActivity(new Intent(this, Anim1Activity.class));
    }

    /**
     * 跳转界面的过渡动画
     *
     * @param view
     */
    public void shareEle2(View view) {
        startActivity(new Intent(this, Anim2Activity.class));
    }

    /**
     * 裁剪1
     *
     * @param view
     */
    public void crop1(View view) {
        startActivity(new Intent(this, CropRect1Activity.class));
    }


    /**
     * 从外部控制缩放、旋转、还原
     *
     * @param view
     */
    public void control(View view) {
        startActivity(new Intent(this, ControlActivity.class));
    }

    /**
     * 支持缩放的layout
     *
     * @param view
     */
    public void zoomLayout(View view) {
        startActivity(new Intent(this, ZoomLayoutActivity.class));
    }
}
