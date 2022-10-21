package io.dourl.mqtt.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.TypedValue
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.dourl.mqtt.bean.BaseIndexPinyinBean
import io.dourl.mqtt.helper.IIndexBarDataHelper

/**
 * File description.
 * 首字母分类-ItemDecoration
 * @author dourl
 * @date 2022/10/8
 */
class LetterCategoryDecoration(context: Context) : RecyclerView.ItemDecoration() {

    val COLOR_TITLE_BG = Color.parseColor("#FFDFDFDF")
    val COLOR_TITLE_FONT = Color.parseColor("#FF999999")

    val mPaint: Paint = Paint()
    val mBounds: Rect = Rect()

    private var mTitleHeight: Float = 0.0f
    private var mTitleFontSize: Float = 0.0f
    private var mHeaderViewCount = 0

    init {
        mTitleHeight = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            24.0f,
            context.resources.displayMetrics
        )
        mTitleFontSize = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            14f,
            context.resources.displayMetrics
        )

        mPaint.textSize = mTitleFontSize
        mPaint.isAntiAlias = true
    }

    var mData : ArrayList<out ILetterCategoryInterface> = ArrayList()
    /**
     * 该方法是 绘制 每一个item的内容
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            var position = params.viewLayoutPosition
            position -= mHeaderViewCount
            //pos为1，size为1，1>0? true
            if (mData.isEmpty() ||
                position > (mData.size.minus(1))
                || position < 0
                || mData[position].isShowLetterItem() != true
            ) {
                continue  //跳过
            }
            //Rv的item position在重置时可能为-1.保险点判断一下吧
            if (position > -1) {
                if (position == 0) { //等于0肯定要有title的
                    drawItemLetterArea(c, left, right, child, params, position)
                } else { //其他的通过判断
                    if (mData[position].getLetter().isNotEmpty() == true
                        && !(mData[position].getLetter().equals(mData.get(position - 1).getLetter()))
                    ) {
                        //不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                        drawItemLetterArea(c, left, right, child, params, position)
                    } else {
                        //none
                    }
                }
            }
        }
    }


    /**
     * 在当前item区域 追加首字母区域
     */
    private fun drawItemLetterArea(c: Canvas, left: Int, right: Int, child: View, params: RecyclerView.LayoutParams, position: Int
    ) {
        mPaint.setColor(COLOR_TITLE_BG)
        c.drawRect(left.toFloat(), child.top - params.topMargin - mTitleHeight,
            right.toFloat(), (child.top - params.topMargin).toFloat(), mPaint
        )
        mPaint.setColor(COLOR_TITLE_FONT)
        val itemLetter: String = mData[position].getLetter()
        mPaint.getTextBounds(itemLetter,0,itemLetter.length,mBounds)
        c.drawText(itemLetter,child.paddingStart.toFloat(),child.top -params.topMargin - (mTitleHeight - mBounds.height())/2,mPaint)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state) // 0000

        var params = view.layoutParams as RecyclerView.LayoutParams
        var position =params.viewLayoutPosition
        position -= mHeaderViewCount

        if (mData.isEmpty() || position > mData.size -1){
            return
        }
        if (position >-1){
            val letterCategoryInterface = mData[position]
            if (letterCategoryInterface.isShowLetterItem() && !letterCategoryInterface.getLetter().isEmpty()){
                if (position == 0){ //第一种情况
                    outRect.set(0, mTitleHeight.toInt(),0,0)
                }else{
                    if (!letterCategoryInterface.getLetter().equals(mData[position-1].getLetter())){
                        outRect.set(0, mTitleHeight.toInt(),0,0)
                    }
                }
            }

        }

    }

    /**
     *  最后调用绘制在recyclerView最上层
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)

        val layoutManager = parent.layoutManager as LinearLayoutManager
        var position  =layoutManager.findFirstVisibleItemPosition()
        position -= mHeaderViewCount

        if (mData.isEmpty() ||
            position > (mData.size.minus(1))
            || position < 0
            || mData[position]?.isShowLetterItem() != true   // 不需要显示首字模的
        ) {
            return  //跳过
        }


        var child = parent.findViewHolderForLayoutPosition(position+mHeaderViewCount)?.itemView
        mPaint.color = COLOR_TITLE_BG
        c.drawRect(
            parent.paddingLeft.toFloat(),
            parent.paddingTop.toFloat(),
            (parent.right - parent.paddingRight).toFloat(), parent.paddingTop + mTitleHeight, mPaint
        )

        val letterCategoryInterface = mData[position]
        val letter = letterCategoryInterface.getLetter()

        mPaint.setColor(COLOR_TITLE_FONT);
        mPaint.getTextBounds(letter, 0, letter!!.length, mBounds);
        c.drawText(letter!!, child!!.paddingStart.toFloat(),
            parent.getPaddingTop() + mTitleHeight - (mTitleHeight / 2 - mBounds.height() / 2),
            mPaint);

    }

}



