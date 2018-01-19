package tyj.com.gestureviewsdemo.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import tyj.com.gestureviewsdemo.R;

/**
 * @author ChenYe
 *         created by on 2018/1/17 0017. 11:08
 **/

public class CropResultActivity extends Activity {

    private GestureImageView mGiv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_result);
        mGiv = (GestureImageView) findViewById(R.id.giv);
        Glide.with(this)
                .load(getIntent().getStringExtra("cropPath"))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mGiv);
        mGiv.getController().getSettings().setMaxZoom(6).setDoubleTapEnabled(false);
    }
}
