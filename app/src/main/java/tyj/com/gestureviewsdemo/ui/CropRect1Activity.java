package tyj.com.gestureviewsdemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alexvasilkov.gestures.commons.CropAreaView;
import com.alexvasilkov.gestures.views.GestureImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

import tyj.com.gestureviewsdemo.R;

/**
 * @author ChenYe
 *         created by on 2018/1/17 0017. 10:57
 *         记得给sd权限
 **/

public class CropRect1Activity extends Activity {

    /**
     * 设置最大多少行多少列
     */
    private static final int MAX_GRID_RULES = 5;
    /**
     * 设置当前多少行多少列,会在个基础上+1
     */
    private int gridRulesCount = 2;
    private CropAreaView mCav;
    private GestureImageView mGiv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_rect_1);
        mGiv = (GestureImageView) findViewById(R.id.giv);
        mCav = (CropAreaView) findViewById(R.id.cav);
        mGiv.getController().getSettings().setMaxZoom(6f).setDoubleTapEnabled(false).setRotationEnabled(true);

        mCav.setImageView(mGiv);
        mCav.setRulesCount(gridRulesCount, gridRulesCount);
        mCav.setAspect(16 / 9);
        //设置是否是圆形
        mCav.setRounded(false);
        mCav.update(true);
    }

    /**
     * 设置分割线
     *
     * @param view
     */
    public void fgx(View view) {
        gridRulesCount = (gridRulesCount + 1) % (MAX_GRID_RULES + 1);
        mCav.setRulesCount(gridRulesCount, gridRulesCount);
    }

    /**
     * 还原
     *
     * @param view
     */
    public void hy(View view) {
        mGiv.getController().resetState();
    }

    /**
     * 确定,因为startActivity传递数据有限制，所以这里我把裁剪之后的数据
     * 临时的存在文件里里面，然后把路径传递过去，在另外一个界面把字符串
     * 取出来然后Glide加载出来。
     *
     * @param view
     */
    public void qd(View view) {
//        记得给sd权限
        Bitmap bitmap = mGiv.crop();
        Log.e("Bitmap", FormatFileSize(bitmap.getAllocationByteCount()));
        if (bitmap != null) {
            Intent intent = new Intent(this, CropResultActivity.class);
            intent.putExtra("cropPath",saveBitmap(bitmap));
            startActivity(intent);
            // 不可以 592900  614656 571536
            //可以 498436 432964
        } else {
            Toast.makeText(this, "裁剪结果为空", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveBitmap(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(), "temCrop.jpg");
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private String FormatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * 小长方形
     *
     * @param view
     */
    public void shape1(View view) {
        mCav.setAspect(16f / 9f);
        mCav.setRounded(false);
        mCav.update(true);
    }

    /**
     * 大长方形
     *
     * @param view
     */
    public void shape2(View view) {
        mCav.setAspect(1f);
        mCav.setRounded(false);
        mCav.update(true);
    }

    /**
     * 切换到正方形
     *
     * @param view
     */
    public void shape3(View view) {
        mCav.setAspect(CropAreaView.ORIGINAL_ASPECT);
        mCav.setRounded(false);
        mCav.update(true);
    }

    /**
     * 切换到圆形
     *
     * @param view
     */
    public void shape4(View view) {
        mCav.setAspect(1f);
        mCav.setRounded(true);
        mCav.update(true);
    }
}
