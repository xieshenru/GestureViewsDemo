package tyj.com.gestureviewsdemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alexvasilkov.gestures.animation.ViewPosition;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import tyj.com.gestureviewsdemo.R;

/**
 * @author ChenYe
 *         created by on 2018/1/16 0016. 10:24
 *         这个界面是跟Anim3Activity一起用的，来实现跳转界面的共享动画效果的。
 **/

public class Anim2Activity extends Activity {

    private ImageView mIv1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim2);
        EventBus.getDefault().register(this);
        mIv1 = (ImageView) findViewById(R.id.anim2_iv);
        mIv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPosition viewPosition = ViewPosition.from(mIv1);
                Intent intent = new Intent(Anim2Activity.this, Anim3Activity.class);
                intent.putExtra("position", viewPosition.pack());
                startActivity(intent);
                //为了视觉效果，关闭动画
                overridePendingTransition(0, 0);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiverMsg(MessageEntity msg) {
        if (msg.what == 20) {
            Log.e("Anim2Activity", "接收到了GestureView退出动画完毕的消息");
            mIv1.setVisibility(View.VISIBLE);
        } else if (msg.what == 21) {
            Log.e("Anim2Activity", "接收到了GestureView入场动画完毕的消息");
            mIv1.setVisibility(View.GONE);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
