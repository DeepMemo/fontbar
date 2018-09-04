package com.bkvito.beikeshequ.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.fontbarlibrary.R


/**
 * 字体选择的进度条
 */
class FontProgress : View {

    constructor(context: Context) : super(context) {
        FontProgress(context, null)
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.FontProgress)
        lineNum = obtainStyledAttributes.getInt(R.styleable.FontProgress_num, 5)
        obtainStyledAttributes.recycle()
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    fun inintData(lineNum: Int) {
        this.lineNum = lineNum
    }

    // 默认有5段线条，即圆球可以移动的点
    // 不用提供set方法，kotlin直接提供了set方法
    var lineNum = 5
    val paint = Paint()
    val paintText = Paint()
    override fun onDraw(canvas: Canvas?) {
        val lineColor = Color.parseColor("#065991")
        paint.color = lineColor
        // 画笔粗细，直接相关线条宽度
        paint.strokeWidth = 3f
        // 这个View 宽度的比例尺，按照其1/10来画线条的成都
        val scaleX = measuredWidth * 1 / 10f
        val scaleY = measuredHeight * 1 / 2f
        // 整横线居中显示，从第一段开始，到第9段结束，里面有8段
        canvas?.drawLine(
                scaleX,
                scaleY,
                scaleX * 9,
                scaleY, paint)

        // 线段一共长8*scaleX，根据有几个线条就划分成线条-1个线段,
        // 再得到每段线段的长度即为偏移量
        val segment = 8 * scaleX / (lineNum - 1)
        // 因i从0开始，所以减一
        for (i in 0..lineNum - 1) {
            // 开始画线段,一共20像素，以长度中心上下10像素
            canvas?.drawLine(
                    scaleX,
                    scaleY - 10,
                    scaleX,
                    scaleY + 10, paint)
            // 这才是控制线段之间距离的
            canvas?.translate(segment, 0f)
        }
        // 画布还原,使用负方向
        canvas?.translate(-segment * lineNum, 0f)
        // 中心往上100像素开始打字
        paintText.color = lineColor
        paintText.strokeWidth = 5f
        paintText.textAlign = Paint.Align.CENTER
        paintText.textSize = 50f
        paintText.style = Paint.Style.FILL
        paintText.isAntiAlias = true
        // 中间往上100个像素
        canvas?.drawText(
                "A",
                scaleX,
                scaleY - 100,
                paintText)

        // 画布偏移量为整个线段的1/5 ，取消了标准位置
//        paintText.color = ContextCompat.getColor(context, R.color.word_bar)
//        paintText.strokeWidth = 10f
//        canvas?.translate(measuredWidth * 2 / 10f, 0f)
//        canvas?.drawText(
//                "标准",
//                measuredWidth * 1 / 10f,
//                measuredHeight * 1 / 2f - 100,
//                paintText)

        // 画布偏移量为之前的3倍
        paintText.textSize = 60f
        paintText.strokeWidth = 5f
        paintText.color = lineColor
        // 再移动8个scaleX距离
        canvas?.translate(scaleX * 8, 0f)
        canvas?.drawText(
                "A",
                scaleX,
                scaleY - 100,
                paintText)
    }
}