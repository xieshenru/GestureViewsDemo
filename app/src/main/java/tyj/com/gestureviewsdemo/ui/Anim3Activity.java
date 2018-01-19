package tyj.com.gestureviewsdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;

import com.alexvasilkov.gestures.animation.ViewPosition;
import com.alexvasilkov.gestures.animation.ViewPositionAnimator;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import tyj.com.gestureviewsdemo.R;

/**
 * @author ChenYe
 *         created by on 2018/1/16 0016. 10:24
 **/

public class Anim3Activity extends Activity {

    private GestureImageView mGiv;
    private View mGb;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_anim3);
        mGiv = (GestureImageView) findViewById(R.id.giv);
        mGb = findViewById(R.id.bg);
        Glide.with(this).load(R.mipmap.paint_1).into(mGiv);
        mGiv.getController().getSettings().setDoubleTapEnabled(false);
        mGiv.getPositionAnimator().addPositionUpdateListener(new ViewPositionAnimator.PositionUpdateListener() {
            @Override
            public void onPositionUpdate(float position, boolean isLeaving) {
                // Exit animation is finished,下面这个mGb和mGic的setVisibility会不断的调用，看能不能进行优化
                boolean isFinished = position == 0f && isLeaving;
                mGb.setAlpha(position);
                mGb.setVisibility(isFinished ? View.INVISIBLE : View.VISIBLE);
                mGiv.setVisibility(isFinished ? View.INVISIBLE : View.VISIBLE);
                if (isFinished) {
                    //已经关闭了,通知上一个界面显示iv
                    MessageEntity entity = MessageEntity.obtianMessage();
                    entity.what = 20;
                    EventBus.getDefault().post(entity);

                    //下面两行代码是为了退出时的流畅效果，避免出现闪烁
                    mGiv.getController().getSettings().disableBounds();
                    mGiv.getPositionAnimator().setState(0f, false, false);

                    finish();
                    overridePendingTransition(0, 0);
                }
            }
        });

        //只有在第一次绘制图像时才开始输入图像动画来防止图片在activity开始时闪烁
        runAfterImageDraw();
    }

    private void enterFullImage(boolean animate) {
        // 播放从提供的位置输入动画
        ViewPosition position = ViewPosition.unpack(getIntent().getStringExtra("position"));
        mGiv.getPositionAnimator().enter(position, animate);
    }

    /**
     * Runs provided action after image is drawn for the first time.
     */
    private void runAfterImageDraw() {

        mGiv.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mGiv.getViewTreeObserver().removeOnPreDrawListener(this);
                // 只有当activity不是从保存状态创建时，才应该播放动画
                enterFullImage(savedInstanceState == null);

                // 为了实现流畅效果，隐藏上一个界面的原始图片
                mGb.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MessageEntity entity = MessageEntity.obtianMessage();
                        entity.what = 21;
                        EventBus.getDefault().post(entity);
                    }
                }, 300);
                return true;
            }
        });
        mGiv.invalidate();
    }

    @Override
    public void onBackPressed() {
        // We should leave full image mode instead of finishing this activity,
        // activity itself should only be finished in the end of the "exit" animation.
        if (!mGiv.getPositionAnimator().isLeaving()) {
            mGiv.getPositionAnimator().exit(true);
        }
    }
}
