package tyj.com.gestureviewsdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;

import tyj.com.gestureviewsdemo.R;

/**
 * @author ChenYe
 *         created by on 2018/1/15 0015. 16:44
 **/

public class ViewPagerActivity extends Activity {

    private int[] mRes = new int[]{R.mipmap.paint_1, R.mipmap.paint_2, R.mipmap.paint_3
            , R.mipmap.paint_4, R.mipmap.paint_5, R.mipmap.paint_6};
    private ViewPager mViewpager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vp);
        mViewpager = (ViewPager) findViewById(R.id.viewpager);
        mViewpager.setAdapter(new MyViewPager());
    }

    private class MyViewPager extends PagerAdapter {
        @Override
        public int getCount() {
            return mRes.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(container.getContext(), R.layout.item_viewpager, null);
            GestureImageView gi = (GestureImageView) view.findViewById(R.id.item_gi);
            //下面这一行是设置在放大的时候还可不可以滑动viewPager的item
            gi.getController().enableScrollInViewPager(mViewpager);
            gi.getController().getSettings()
                    .setMaxZoom(6f)
                    .setDoubleTapZoom(3f);
            Glide.with(container.getContext()).load(mRes[position]).into(gi);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((View) object));
        }
    }
}
