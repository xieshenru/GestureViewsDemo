package tyj.com.gestureviewsdemo.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alexvasilkov.gestures.animation.ViewPositionAnimator;
import com.alexvasilkov.gestures.transition.GestureTransitions;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.transition.tracker.SimpleTracker;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.Map;

import tyj.com.gestureviewsdemo.R;

/**
 * @author ChenYe
 *         created by on 2018/1/16 0016. 15:48
 *         ListView 跟 ViewPager 结合使用，需要记录轨迹。目前有些图片不自动填充
 **/

public class LvWithVpActivity extends Activity {

    private int[] mRes = new int[]{R.mipmap.paint_1, R.mipmap.paint_2, R.mipmap.paint_3
            , R.mipmap.paint_4, R.mipmap.paint_5, R.mipmap.paint_6, R.mipmap.paint_2,
            R.mipmap.paint_6, R.mipmap.paint_2, R.mipmap.paint_2, R.mipmap.paint_2
            , R.mipmap.paint_6, R.mipmap.paint_6};
    private ListView mListView;
    private ViewPager mViewPager;
    private MyAdapter mLvAdapter;
    private MyViewPagerAdapter mVpAdapter;
    private ViewsTransitionAnimator mAnimator;
    private View mBg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv_vp);
        mListView = (ListView) findViewById(R.id.lv);
        mBg = findViewById(R.id.bg);
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mLvAdapter = new MyAdapter(this);
        mListView.setAdapter(mLvAdapter);
        mVpAdapter = new MyViewPagerAdapter();
        mViewPager.setAdapter(mVpAdapter);
        final SimpleTracker listTracker = new SimpleTracker() {
            @Override
            public View getViewAt(int position) {
                View itemView = mListView.getChildAt(position - mListView.getFirstVisiblePosition());
                return itemView == null ? null : mLvAdapter.getImageView(itemView);
            }
        };

        final SimpleTracker pagerTracker = new SimpleTracker() {
            @Override
            public View getViewAt(int position) {
                return mVpAdapter.getItemView(position);
            }
        };

        mAnimator = GestureTransitions.from(mListView, listTracker).into(mViewPager, pagerTracker);
        mAnimator.addPositionUpdateListener(new ViewPositionAnimator.PositionUpdateListener() {
            @Override
            public void onPositionUpdate(float position, boolean isLeaving) {
                mBg.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
                mBg.setAlpha(position);
            }
        });

        mLvAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void clickItem(int pic, int position, ImageView iv) {
                mAnimator.enter(position, true);
            }
        });
    }


    @Override
    public void onBackPressed() {
        // We should leave full image mode instead of closing the screen
        if (mAnimator != null && !mAnimator.isLeaving()) {
            mAnimator.exit(true);
        } else {
            super.onBackPressed();
        }
    }

    private class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private OnItemClickListener mListener;

        public MyAdapter(Context context) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mRes.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_lv, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.updateView(position, mContext);
            return convertView;
        }

        public ImageView getImageView(View itemView) {
            ViewHolder holder = new ViewHolder(itemView);
            return holder == null ? null : holder.iv;
        }

        private class ViewHolder {
            private ImageView iv;
            private RelativeLayout mBg;

            public ViewHolder(View view) {
                iv = view.findViewById(R.id.item_giv);
                mBg = view.findViewById(R.id.item_bg);
            }

            public void updateView(final int position, Context context) {
                Glide.with(context)
                        .load(mRes[position])
                        .into(iv);
                mBg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.clickItem(mRes[position], position, iv);
                        }
                    }
                });
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mListener = listener;
        }
    }

    public interface OnItemClickListener {
        void clickItem(int pic, int position, ImageView iv);
    }

    private class MyViewPagerAdapter extends PagerAdapter {

        private Map<Integer, View> mCaches = new HashMap<>();

        @Override
        public int getCount() {
            return mRes.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = View.inflate(container.getContext(), R.layout.item_viewpager, null);
            GestureImageView gi = (GestureImageView) view.findViewById(R.id.item_gi);
            //下面这一行是设置在放大的时候还可不可以滑动viewPager的item
            gi.getController().enableScrollInViewPager(mViewPager);
            Glide.with(container.getContext()).load(mRes[position]).into(gi);
            container.addView(view);
            mCaches.put(position, gi);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(((View) object));
            mCaches.remove(position);
        }

        public View getItemView(int position) {
            return mCaches.get(position);
        }
    }
}
