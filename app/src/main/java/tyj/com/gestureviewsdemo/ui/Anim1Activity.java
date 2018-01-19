package tyj.com.gestureviewsdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alexvasilkov.gestures.animation.ViewPositionAnimator;
import com.alexvasilkov.gestures.transition.GestureTransitions;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;

import tyj.com.gestureviewsdemo.R;

/**
 * @author ChenYe
 *         created by on 2018/1/16 0016. 09:40
 *         在当前界面里面的“过渡动画”,如果想要单击图片就“复原”的话，我建议屏蔽调双击方法功能，
 *         否则里面会做一系列的判断，然后就会导致单击“复原”的时候会有停顿的感觉。GestureImageView
 *         默认是支持双击放大的功能的。
 **/

public class Anim1Activity extends Activity {

    private GestureImageView mGiv;
    private ImageView mSourceIv;
    private ViewsTransitionAnimator mAnimator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim1);
        mGiv = (GestureImageView) findViewById(R.id.target_giv);
        mSourceIv = (ImageView) findViewById(R.id.source_iv);
        mSourceIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mGiv.getDrawable() == null) {
                    mGiv.setImageDrawable(mSourceIv.getDrawable());
                }

                mGiv.getController().resetState();
                mGiv.getController().getSettings().setMaxZoom(6).setDoubleTapEnabled(false);
                //下面这行是默认打开的双击放大功能的，即使没设置setDoubleTapZoom也是放开的。
//                mGiv.getController().getSettings().setMaxZoom(6).setDoubleTapZoom(4);
                mAnimator.enterSingle(true);
                Glide.with(Anim1Activity.this).load(R.mipmap.paint_4).into(mGiv);
            }
        });

        mAnimator = GestureTransitions.from(mSourceIv).into(mGiv);
        mAnimator.addPositionUpdateListener(new ViewPositionAnimator.PositionUpdateListener() {
            @Override
            public void onPositionUpdate(float position, boolean isLeaving) {
                //下面两行是设置背景淡入淡出的
//                background.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
//                background.setAlpha(position);
                int visibility = mGiv.getVisibility();
                boolean b = position == 0f && isLeaving;
//                mGiv.setVisibility(b ? View.INVISIBLE : View.VISIBLE);
                //我下面这个写法是比上面哪行代码做了一个优化，但是如果要用我这种写法，记得在xml里面
                //mGiv一开始是invisible而不是gone，因为下面的判断是用INVISIBLE做判断的
                if (b && visibility == View.VISIBLE) {
                    Log.e("Anim", "隐藏");
                    mGiv.setVisibility(View.INVISIBLE);
                } else if (!b && visibility == View.INVISIBLE) {
                    mGiv.setVisibility(View.VISIBLE);
                    Log.e("Anim", "显示");
                }
            }
        });

        mGiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mAnimator.isLeaving()) {
                    mAnimator.exit(true);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mAnimator.isLeaving()) {
            mAnimator.exit(true);
        } else {
            super.onBackPressed();
        }
    }
}
