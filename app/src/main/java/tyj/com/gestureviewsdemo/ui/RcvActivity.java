package tyj.com.gestureviewsdemo.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alexvasilkov.gestures.animation.ViewPositionAnimator;
import com.alexvasilkov.gestures.transition.GestureTransitions;
import com.alexvasilkov.gestures.transition.ViewsTransitionAnimator;
import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;

import tyj.com.gestureviewsdemo.R;

/**
 * @author ChenYe
 *         created by on 2018/1/17 0017. 08:49
 **/

public class RcvActivity extends Activity {

    private int[] mRes = new int[]{R.mipmap.paint_1, R.mipmap.paint_2, R.mipmap.paint_3
            , R.mipmap.paint_4, R.mipmap.paint_5, R.mipmap.paint_6, R.mipmap.paint_2,
            R.mipmap.paint_6, R.mipmap.paint_2, R.mipmap.paint_2, R.mipmap.paint_2
            , R.mipmap.paint_6, R.mipmap.paint_6};
    private ViewsTransitionAnimator mAnimator;
    private GestureImageView mGiv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rcv);
        mGiv = (GestureImageView) findViewById(R.id.giv);
        RecyclerView mRcv = (RecyclerView) findViewById(R.id.rcv);
        final MyAdapter mAdapter = new MyAdapter(this);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void clickItem(int res, int position, ImageView iv) {
                Glide.with(RcvActivity.this).load(res).into(mGiv);
                mAnimator = GestureTransitions.from(iv).into(mGiv);
                mAnimator.addPositionUpdateListener(positionUpdateListener);
                mAnimator.enter(position,true);
            }
        });
        mRcv.setLayoutManager(new LinearLayoutManager(this));
        mRcv.setAdapter(mAdapter);

        mGiv.getController().getSettings().setMaxZoom(6).setDoubleTapEnabled(false);
        mGiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnimator != null && !mAnimator.isLeaving()) {
                    mAnimator.exit(true);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mAnimator != null && !mAnimator.isLeaving()) {
            mAnimator.exit(true);
        } else {
            super.onBackPressed();
        }
    }

    private ViewPositionAnimator.PositionUpdateListener positionUpdateListener = new ViewPositionAnimator.PositionUpdateListener() {
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

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        private Context mContext;
        private OnItemClickListener mListener;

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
}
