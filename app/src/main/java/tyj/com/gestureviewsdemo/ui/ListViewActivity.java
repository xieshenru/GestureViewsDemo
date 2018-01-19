package tyj.com.gestureviewsdemo.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;

import tyj.com.gestureviewsdemo.R;

/**
 * @author ChenYe
 *         created by on 2018/1/16 0016. 09:33
 **/

public class ListViewActivity extends Activity {
    private int[] mRes = new int[]{R.mipmap.paint_1, R.mipmap.paint_2, R.mipmap.paint_3
            , R.mipmap.paint_4, R.mipmap.paint_5, R.mipmap.paint_6, R.mipmap.paint_2,
            R.mipmap.paint_6, R.mipmap.paint_2, R.mipmap.paint_2, R.mipmap.paint_2
            , R.mipmap.paint_6, R.mipmap.paint_6};
    private ListView mListView;
    private MyAdapter mAdapter;
    private ViewsTransitionAnimator mAnimator;
    private GestureImageView mGiv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lv);
        mListView = (ListView) findViewById(R.id.lv);
        mAdapter = new MyAdapter(this);
        mListView.setAdapter(mAdapter);
        mGiv = (GestureImageView) findViewById(R.id.giv);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void clickItem(int pic, int position, ImageView iv) {
                Glide.with(ListViewActivity.this).load(pic).into(mGiv);
                mAnimator = GestureTransitions.from(iv).into(mGiv);
                mAnimator.addPositionUpdateListener(mPositionListener);
                mAnimator.enter(position, true);
            }
        });
        mGiv.getController().getSettings().setDoubleTapEnabled(false);
        mGiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnimator != null && !mAnimator.isLeaving()) {
                    mAnimator.exit(true);
                }
            }
        });
    }

    private ViewPositionAnimator.PositionUpdateListener mPositionListener = new ViewPositionAnimator.PositionUpdateListener() {
        @Override
        public void onPositionUpdate(float position, boolean isLeaving) {
            int visibility = mGiv.getVisibility();
            boolean b = position == 0f && isLeaving;
            if (b && visibility == View.VISIBLE) {
                Log.e("Anim", "隐藏");
                mGiv.setVisibility(View.INVISIBLE);
            } else if (!b && visibility == View.INVISIBLE) {
                mGiv.setVisibility(View.VISIBLE);
                Log.e("Anim", "显示");
            }
        }
    };

    @Override
    public void onBackPressed() {
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
}
