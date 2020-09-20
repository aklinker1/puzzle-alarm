package io.aklinker1.alarm.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import io.aklinker1.alarm.R
import java.util.*
import kotlin.collections.HashMap
import kotlin.concurrent.schedule
import kotlin.math.floor
import kotlin.random.Random

class GridHighlightPuzzleView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    interface OnPassListener {
        fun onPass()
    }

    companion object {
        const val STATE_PREVIEW = 0
        const val STATE_IN_PROGRESS = 1
        const val STATE_FAILED = 2
        const val STATE_PASSED = 3
    }

    private val mGapSizeDp = 4f
    private val mSquareRadiusDp = 4f
    private var mState = STATE_PREVIEW
    private val mRandom = Random(System.currentTimeMillis())
    private var mGridSize: Int
    private var mHighlightCount: Int
    private var mActiveTimer: Timer? = null
    private var mCorrectSquares = HashMap<Int, Boolean>()
    private var mSelectedSquares = HashMap<Int, Boolean>()
    private var mPressedIndex: Int? = null
    private var mCorrectCount = 0

    private val mDeselectedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) resources.getColor(
                R.color.gridDeselected,
                context.theme
            ) else resources.getColor(R.color.gridDeselected)
    }
    private val mCorrectPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) resources.getColor(
                R.color.gridCorrect,
                context.theme
            ) else resources.getColor(R.color.gridCorrect)
    }
    private val mHighlightedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) resources.getColor(
                R.color.colorPrimary,
                context.theme
            ) else resources.getColor(R.color.colorPrimary)
    }
    private val mWrongPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) resources.getColor(
                R.color.gridWrong,
                context.theme
            ) else resources.getColor(R.color.gridWrong)
    }
    private val mPressedPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) resources.getColor(
                R.color.gridPress,
                context.theme
            ) else resources.getColor(R.color.gridPress)
    }

    var gridSize: Int
        get() = mGridSize
        set(value) {
            val prevValue = mGridSize
            mGridSize = value
            if (prevValue != value) {
                reset()
            }
        }
    var highlightCount: Int
        get() = mHighlightCount
        set(value) {
            val prevValue = mHighlightCount
            mHighlightCount = value
            if (prevValue != value) {
                reset()
            }
        }

    var onPassListener: OnPassListener? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.GridHighlightPuzzleView,
            0, 0
        ).apply {
            try {
                mGridSize = getInteger(R.styleable.GridHighlightPuzzleView_size, 5)
                mHighlightCount = getInteger(R.styleable.GridHighlightPuzzleView_highlightCount, 9)
            } finally {
                recycle()
            }
        }

        reset()
        setOnTouchListener { _, event ->
            performClick()
            if (mState != STATE_IN_PROGRESS) return@setOnTouchListener true

            val clickX = event.x
            val clickY = event.y
            val squareSize = this@GridHighlightPuzzleView.width / mGridSize

            val x = floor(clickX / squareSize).toInt()
            val y = floor(clickY / squareSize).toInt()
            val index = x + (y * mGridSize)

            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val prevValue = mPressedIndex
                    mPressedIndex = index
                    if (prevValue != mPressedIndex) {
                        invalidate()
                        requestLayout()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (mSelectedSquares[index] != true) {
                        mSelectedSquares[index] = true
                        onPressIndex(index)
                    }
                    mPressedIndex = null
                    invalidate()
                    requestLayout()
                }
                MotionEvent.ACTION_CANCEL -> {
                    mPressedIndex = null
                    invalidate()
                    requestLayout()
                }
            }

            return@setOnTouchListener true
        }
    }

    private fun dpToPx(dp: Float): Float =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val gapSize = dpToPx(mGapSizeDp)
        val squareRadius = dpToPx(mSquareRadiusDp)

        canvas.apply {
            val squareCount = mGridSize * mGridSize - 1
            val gapCount = mGridSize - 1
            val squareSize =
                (this@GridHighlightPuzzleView.width - gapCount * gapSize) / mGridSize
            val isPreview = mState == STATE_PREVIEW
            val isInProgress = mState == STATE_IN_PROGRESS
            val isPassed = mState == STATE_PASSED

            for (xIndex in 0..squareCount) {
                for (yIndex in 0..squareCount) {
                    val x = xIndex * squareSize + xIndex * gapSize
                    val y = yIndex * squareSize + yIndex * gapSize
                    val index = xIndex + (yIndex * mGridSize)
                    val isCorrect = mCorrectSquares[index] == true
                    val isHighlighted = mSelectedSquares[index] == true
                    val paint: Paint = if (isPreview) {
                        if (isCorrect) mCorrectPaint else mDeselectedPaint
                    } else if (isInProgress) {
                        if (isHighlighted) mHighlightedPaint else mDeselectedPaint
                    } else if (isPassed) {
                        mCorrectPaint
                    } else {
                        if (isCorrect && isHighlighted) mHighlightedPaint
                        else if (isCorrect) mCorrectPaint
                        else if (isHighlighted) mWrongPaint
                        else mDeselectedPaint
                    }
                    drawRoundRect(
                        x,
                        y,
                        x + squareSize,
                        y + squareSize,
                        squareRadius,
                        squareRadius,
                        paint
                    )
                    if (index == mPressedIndex) {
                        drawRoundRect(
                            x,
                            y,
                            x + squareSize,
                            y + squareSize,
                            squareRadius,
                            squareRadius,
                            mPressedPaint
                        )
                    }
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = measuredWidth
        setMeasuredDimension(size, size)
    }

    fun reset() {
        mState = STATE_PREVIEW
        computeCorrectSquares()
        mPressedIndex = null
        mCorrectCount = 0
        invalidate()
        requestLayout()

        mActiveTimer?.cancel()
        mActiveTimer = Timer().run {
            schedule(5000L) {
                mState = STATE_IN_PROGRESS
                mActiveTimer = null
                post {
                    invalidate()
                    requestLayout()
                }
            }
            this
        }
    }

    private fun fail() {
        mState = STATE_FAILED
        invalidate()
        requestLayout()

        mActiveTimer?.cancel()
        mActiveTimer = Timer().run {
            schedule(3000L) {
                mActiveTimer = null
                post {
                    reset()
                }
            }
            this
        }
    }

    private fun pass() {
        mState = STATE_PASSED
        invalidate()
        requestLayout()

        onPassListener?.onPass()
    }

    private fun computeCorrectSquares() {
        mSelectedSquares = HashMap(highlightCount)
        mCorrectSquares = HashMap(highlightCount)
        var correctCount = 0
        val maxIndex = mGridSize * mGridSize
        while (correctCount != mHighlightCount) {
            val index = mRandom.nextInt(maxIndex)
            if (mCorrectSquares[index] != true) {
                mCorrectSquares[index] = true
                correctCount++
            }
        }
    }

    private fun onPressIndex(index: Int) {
        val correct = mCorrectSquares[index] == true

        if (!correct) {
            fail()
        } else {
            mCorrectCount++
            if (mCorrectCount == mHighlightCount) pass()
        }

    }

}