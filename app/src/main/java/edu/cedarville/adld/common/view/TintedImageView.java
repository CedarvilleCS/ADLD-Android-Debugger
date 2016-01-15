package edu.cedarville.adld.common.view;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.ImageView;

import edu.cedarville.adld.R;

public class TintedImageView extends ImageView {

    public TintedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        int color = context.getResources().getColor(R.color.gray_dominant);
        this.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }
}