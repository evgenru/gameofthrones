package ru.skillbranch.gameofthrones.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class AspectRatioImage @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyle: Int = 0) :
    ImageView(context, attrs, defStyle) {


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (drawable!=null && drawable.intrinsicHeight != 0) {
            val ratio = drawable.intrinsicWidth.toDouble() / drawable.intrinsicHeight.toDouble()
            setMeasuredDimension(measuredWidth, (measuredWidth / ratio).toInt())
        }
    }
}