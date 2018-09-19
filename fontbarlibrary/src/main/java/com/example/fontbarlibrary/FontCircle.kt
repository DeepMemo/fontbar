package com.example.fontbarlibrary

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View

/**
 * 字体滚动条的圆球，用于拖动，单独提出来画
 */
class FontCircle : View {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    val circlePaint = Paint()
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 画圆球
        circlePaint.color = ContextCompat.getColor(context, android.R.color.white)
        // 只填充内部
        circlePaint.style = Paint.Style.FILL
        // 画笔的宽度
        circlePaint.strokeWidth = 20f
        // 抗锯齿
        circlePaint.isAntiAlias = true
        // 关闭硬件加速,setShadowLayer 才会有效。
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        // 画阴影10像素
        circlePaint.setShadowLayer(
                10f,
                0f,
                0f,
                ContextCompat.getColor(context, android.R.color.darker_gray))
        // 圆球一共就40x40像素大小,包含10像素的阴影，位置是在FontBar里面定位的
        canvas?.drawCircle(
                40f,
                40f,
                30f, circlePaint)

    }
}