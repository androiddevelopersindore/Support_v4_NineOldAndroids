package android.support.v4.app;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.supportanimator.supportv4.R;

/**
 * 
 */
public class ActionBarSherlockDrawerToggleImpl {
	private static final String TAG = "ActionBarSherlockDrawerToggleImpl";

	private static final int[] THEME_ATTRS = new int[] {
		R.attr.homeAsUpIndicator
	};

	public static Drawable getThemeUpIndicator(Activity activity) {
		final TypedArray a = activity.obtainStyledAttributes(THEME_ATTRS);
		final Drawable result = a.getDrawable(0);
		a.recycle();
		return result;
	}

	public static Object setActionBarUpIndicator(Object info, Activity activity, Drawable drawable, int contentDescRes) {
		if (info == null) info = new SetIndicatorInfo(activity);

		final SetIndicatorInfo sii = (SetIndicatorInfo) info;
		if (sii.upIndicatorView != null) {
			sii.upIndicatorView.setImageDrawable(drawable);
			sii.upIndicatorView.setContentDescription(activity.getResources().getString(contentDescRes));
		} else {
			Log.w(TAG, "Couldn't set home-as-up indicator");
		}
		return info;
	}

	public static Object setActionBarDescription(Object info, Activity activity, int contentDescRes) {
		if (info == null) info = new SetIndicatorInfo(activity);

		final SetIndicatorInfo sii = (SetIndicatorInfo) info;
		if (sii.upIndicatorView != null) {
			sii.upIndicatorView.setContentDescription(activity.getResources().getString(contentDescRes));
		} else {
			Log.w(TAG, "Couldn't set home-as-up indicator");
		}
		return info;
	}

	private static class SetIndicatorInfo {
		public ImageView upIndicatorView;

		SetIndicatorInfo(Activity activity) {
			final View home = activity.findViewById(R.id.abs__up);
			if (home instanceof ImageView) {
				upIndicatorView = (ImageView) home;
			}
		}
	}
}
