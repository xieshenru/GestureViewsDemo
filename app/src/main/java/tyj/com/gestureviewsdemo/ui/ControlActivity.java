package tyj.com.gestureviewsdemo.ui;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;

import com.alexvasilkov.gestures.GestureController;
import com.alexvasilkov.gestures.State;
import com.alexvasilkov.gestures.views.GestureImageView;

import tyj.com.gestureviewsdemo.R;

/**
 * @author ChenYe
 *         created by on 2018/1/19 0019. 09:47
 **/

public class ControlActivity extends Activity {

    private CheckBox mCheckBox;
    private GestureImageView mGiv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        mGiv = (GestureImageView) findViewById(R.id.giv);
        mGiv.getController().getSettings().setMaxZoom(6).setDoubleTapEnabled(false);
        mCheckBox = (CheckBox) findViewById(R.id.control_animate);
    }

    /**
     * 放大
     *
     * @param view
     */
    public void bigger(View view) {
        zoomImage(true, mCheckBox.isChecked());
    }

    /**
     * 缩小
     *
     * @param view
     */
    public void smaller(View view) {
        zoomImage(false, mCheckBox.isChecked());
    }

    /**
     * 旋转
     *
     * @param view
     */
    public void rotate(View view) {
        rotateImage(mCheckBox.isChecked());
    }

    /**
     * 还原
     *
     * @param view
     */
    public void reset(View view) {
        resetImage(mCheckBox.isChecked());
    }

    /**
     * 还原
     *
     * @param animate
     */
    private void resetImage(boolean animate) {
        final GestureController controller = mGiv.getController();

        if (controller.isAnimating()) {
            return; // Waiting for animation end
        }

        if (animate) {
            final State state = controller.getState().copy();
            final PointF pivot = getPivot();

            // Restoring initial image zoom and rotation
            final float minZoom = controller.getStateController().getMinZoom(state);
            state.zoomTo(minZoom, pivot.x, pivot.y);
            state.rotateTo(0f, pivot.x, pivot.y);

            // Animating state changes. Do not forget to make a state's copy prior to any changes.
            controller.setPivot(pivot.x, pivot.y);
            controller.animateStateTo(state);
        } else {
            // Immediately resetting the state
            controller.resetState();
        }
    }

    /**
     * 旋转
     *
     * @param animate
     */
    private void rotateImage(boolean animate) {
        final GestureController controller = mGiv.getController();

        if (controller.isAnimating()) {
            return; // Waiting for animation end
        }

        final State state = controller.getState().copy();
        final PointF pivot = getPivot();

        // Rotating to closest next 90 degree ccw
        float rotation = Math.round(state.getRotation()) % 90f == 0f
                ? state.getRotation() - 90f : (float) Math.floor(state.getRotation() / 90f) * 90f;
        state.rotateTo(rotation, pivot.x, pivot.y);

        if (animate) {
            // Animating state changes. Do not forget to make a state's copy prior to any changes.
            controller.setPivot(pivot.x, pivot.y);
            controller.animateStateTo(state);
        } else {
            // Immediately applying state changes
            controller.getState().set(state);
            controller.updateState();
        }
    }

    /**
     * 缩放
     *
     * @param zoomIn
     * @param animate
     */
    private void zoomImage(boolean zoomIn, boolean animate) {
        final GestureController controller = mGiv.getController();

        if (controller.isAnimating()) {
            return; // Waiting for animation end
        }

        final State state = controller.getState().copy();
        final PointF pivot = getPivot();

        // Zooming the image in or out
        state.zoomBy(zoomIn ? 1.333f : 0.75f, pivot.x, pivot.y);

        if (animate) {
            // Animating state changes. Do not forget to make a state's copy prior to any changes.
            controller.setPivot(pivot.x, pivot.y);
            controller.animateStateTo(state);
        } else {
            // Immediately applying state changes
            controller.getState().set(state);
            controller.updateState();
        }
    }

    private PointF getPivot() {
        // Default pivot point is a view center
        PointF pivot = new PointF();
        pivot.x = 0.5f * mGiv.getController().getSettings().getViewportW();
        pivot.y = 0.5f * mGiv.getController().getSettings().getViewportH();
        return pivot;
    }
}
