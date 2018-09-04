package com.bkvito.beikeshequ.ui.view

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import com.example.fontbarlibrary.R


/**
 * 字体选择的进度条的ViewGroup，只拿来存放FontProgress和FontCircle两个自定义view
 */
class FontProgressBar : FrameLayout, OnTouchListener {

    constructor(context: Context, lineNum: Int = 5, locate: Int = 1) : super(context) {
        this.lineNum = lineNum
        this.locate = locate
        initView()
    }

    // 圆球所在第几个线段的位置，因不知有几个线条，都默认从1开始
    var locate = 1
    // 默认有几个线条
    internal var lineNum = 5

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        val obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.FontProgressBar)
        lineNum = obtainStyledAttributes.getInt(R.styleable.FontProgressBar_lineNum, 5)
        locate = obtainStyledAttributes.getInt(R.styleable.FontProgressBar_locate, 1)
        obtainStyledAttributes.recycle()
        initView()
    }

    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr)

    lateinit var fontCircle: FontCircle

    fun initView() {
        // 默认有5个线段
        val progress = FontProgress(context)
        progress.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        progress.lineNum = lineNum
        addView(progress)
        fontCircle = FontCircle(context)
        fontCircle.setOnTouchListener(this)
        fontCircle.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        addView(fontCircle)
        setBackgroundResource(android.R.color.white)
    }

    // 比例尺单位
    var scaleDistance = 0f
    // 线段长度
    var segmentDistance = 0f

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        // 必须在onMeasure以后，不然measuredWidth为0，以父布局的1/10为最小单位
        scaleDistance = measuredWidth * 1 / 10f
        segmentDistance = scaleDistance * 8 / (lineNum - 1)
        // 第一个子View 是进度条
        val child = getChildAt(0)
        // 因为是match_parent 所以从左上角开始
        child.layout(0, 0, measuredWidth, measuredHeight)
        // 得到圆球
        val circle = getChildAt(1)
        // 左边距离父布局的距离
        val left = (scaleDistance + (locate - 1) * segmentDistance - 40 + 0.5).toInt()
        // 第一段为空距离，第二段为每条线段的长度*线条的位置，第二条线段才有距离
        // 第三段为，减去circle的半径加阴影让圆心在线条上
        // 右边距离父布局的距离
        val right = (scaleDistance + (locate - 1) * segmentDistance + 40 + 0.5).toInt()
        circle.layout(
                left,
                measuredHeight * 1 / 2 - 40,
                right,
                measuredHeight * 1 / 2 + 40)
    }

    var currentX = 0f
    var startX = 0f
    override fun onTouch(v: View, event: MotionEvent): Boolean {

        val translationX = v.translationX
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // v本身的偏移量,移动以后变成最新的偏移量
                currentX = translationX
                // 手指触发的原始点
                startX = event.rawX
            }
            MotionEvent.ACTION_MOVE -> {
                // 手指活动距离
                val distance = event.rawX - startX
                // 滑动的距离+之前的偏移量，得到实际偏移量
                val endDistance = distance + currentX
                // 起始位置不一样，可滑动范围不一样，比如在圆球在中间-2segmentDistance---（2segmentDistance）
                // locate之前的都是负的，第二个位置才有一个线段所以减一
                // 而locate之后才是正的,所以用总共的线条数前去当前位置
                val start = (-(locate - 1)) * segmentDistance
                val end = (lineNum - locate) * segmentDistance
                if (endDistance in start ..end) {
                    v.translationX = endDistance
                }
            }
            MotionEvent.ACTION_UP -> {
                // 得到滑动的线段值整数，为一个线段,不能直接加0.5必须根据滑动的方向，得到正负号
                val num = ((translationX / segmentDistance) + 0.5 * (translationX / Math.abs(translationX))).toInt()
                // 滑动的都是线段的值，num带正负，和当前位置相加，得到对应位置
                setAni(num * segmentDistance, num + locate)
            }
        }
        return true
    }

    private fun setAni(moveX: Float, choose: Int) {
        // 偏移量到偏移量的值啊
        val animator = ObjectAnimator.ofFloat(
                fontCircle, "TranslationX", fontCircle.translationX, moveX)
        animator.duration = 300
        animator.start()
        // 回调
        callBack(choose)
    }

    var callBack: (choose: Int) -> Unit = {}
    fun addCallBack(method: (choose: Int) -> Unit) {
        callBack = method
    }
}