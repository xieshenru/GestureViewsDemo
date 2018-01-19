package tyj.com.gestureviewsdemo.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
 *         created by on 2018/1/17 0017. 10:15
 **/

public class RcvWithVpActivity extends Activity {

    private int[] mRes = new int[]{R.mipmap.paint_1, R.mipmap.paint_2, R.mipmap.paint_3
            , R.mipmap.paint_4, R.mipmap.paint_5, R.mipmap.paint_6, R.mipmap.paint_2,
            R.mipmap.paint_6, R.mipmap.paint_2, R.mipmap.paint_2, R.mipmap.paint_2
            , R.mipmap.paint_6, R.mipmap.paint_6};
    private ViewsTransitionAnimator mAnimator;
    private ViewPager mViewPager;
    private RecyclerView mRcv;
    private MyAdapter mAdapter;
    private MyViewPagerAdapter mViAdapter;
    private View mBg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcv_vp);
        mRcv = (RecyclerView) findViewById(R.id.rcv);
        mViewPager = (ViewPager) findViewById(R.id.vp);
        mBg = findViewById(R.id.bg);
        mAdapter = new MyAdapter(this);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void clickItem(int res, int position, ImageView iv) {
                mAnimator.enter(position, true);
            }
        });
        mRcv.setLayoutManager(new LinearLayoutManager(this));
        mRcv.setAdapter(mAdapter);
        final SimpleTracker listTracker = new SimpleTracker() {

            @Override
            public View getViewAt(int position) {
                return mAdapter.getIv(position);
            }
        };
        mViAdapter = new MyViewPagerAdapter();

        final SimpleTracker pagerTracker = new SimpleTracker() {

            @Override
            public View getViewAt(int position) {
                return mViAdapter.getItemView(position);
            }
        };

        mAnimator = GestureTransitions.from(mRcv, listTracker).into(mViewPager, pagerTracker);
        mAnimator.addPositionUpdateListener(new ViewPositionAnimator.PositionUpdateListener() {
            @Override
            public void onPositionUpdate(float position, boolean isLeaving) {
                mBg.setVisibility(position == 0f ? View.INVISIBLE : View.VISIBLE);
                mBg.setAlpha(position);
            }
        });
        mViewPager.setAdapter(mViAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mAnimator != null && !mAnimator.isLeaving()) {
            mAnimator.exit(true);
        } else {
            super.onBackPressed();
        }
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        private Context mContext;
        private OnItemClickListener mListener;
        private Map<Integer, ImageView> mCaches = new HashMap<>();

        public MyAdapter(Context context) {
            this.mContext = context;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(mInflater.inflate(R.layout.item_lv, null));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.updateView(position);
        }

        @Override
        public int getItemCount() {
            return mRes.length;
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mListener = listener;
        }

        public View getIv(int position) {
            return mCaches.get(position);
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView mIv;

            public ViewHolder(View itemView) {
                super(itemView);
                mIv = itemView.findViewById(R.id.item_giv);
            }

            public void updateView(final int position) {

                Glide.with(mContext)
                        .load(mRes[position])
                        .into(mIv);
                mCaches.put(position, mIv);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mListener != null) {
                            mListener.clickItem(mRes[position], position, mIv);
                        }
                    }
                });
            }
        }
    }

    public interface OnItemClickListener {

        void clickItem(int res, int position, ImageView iv);
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
