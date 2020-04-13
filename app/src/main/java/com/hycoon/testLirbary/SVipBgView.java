package com.hycoon.testLirbary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.hycoon.testLirbary.R;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @author:create by Hycoon
 * @time:2019/9/11 14
 * @des：
 */
public class SVipBgView extends View {

    private Paint mPaint;
    private Bitmap mTopBmp;
    private Bitmap mBottomBmp;

    private Rect mTopSrcRect, mBottomSrcRect;
    private RectF mTopBmpDescRect, mBottomDescRect;
    private RectF mBgRect;

    private LinearGradient mLinearGradient;

    private int mColorStart, mColorEnd;

    private int mHeight;

    private int mRadius;
    private int mDp13;

    private TextPaint mTextPaint;
    private int mDp20;
    private int mDp15;
    private String mVipTitle;
    private float titleWidth;
    private float titleHeight;
    private float lineHeight;
    private int mDp1;
    private int mDp8;
    private int mDp2;
    private int mDp6;
    private int mDp10;
    private int mDp12;
    private int mDp60;
    private int mSp13;
    private int mDp35;
    private int mDp25;
    private int mDp17;

    private Drawable buttonBg;
    private Rect mButtonRect;
    private Context mContext;
    private int mDp100;
    private int mDp40;
    private int mDesWidth;

    private int mButtonStartX;
    private int mButtonEndX;
    private int mButtonStartY;
    private int mButtonEndY;
    private String mButtonText = "续费";
    private String mTestEtc = "...";
    private float mTestEtcLength;
    private float introWidth = 500 * 1.0f;
    private String introText = "钟南山:无症状患者大多有传染性 主要来自两处_新闻频道_中华网";
    private String autoText = "病情好转，英首相离开重症监护病房！欧洲多国防控“加码”";
    private float lineBeginX;
    private Rect mAutoTextRect;
    private int mAutoTextStartX;
    private int mAutoTextStartY;
    private int mAutoArrowStartX;
    private int mAutoArrowStartY;
    private Rect mAutoImgRect;
    private Rect mAutoTextRectBound;
    private Bitmap mArrowBitmap;

    private int mCurStrStartY; //当前文字的起始y
    private int mNextStrStarY;//下一个文字的起始y

    private int mCurStrAnimStartY;//当前文字动态改变的y
    private int mNextStrAnimStartY;//下一个文字动态改变的y

    private int mCurShowIndex = 0;//当前显示的列表中的第几个
    private List<String> mStrList,mTempList;

    private int delayTime = 3000;//多久滚动一次

    private int animDuration = 600;//动画持续时间

    private int mAlapha ;//当前的透明度
    private int mArrAlapha;

    private Paint.FontMetricsInt mFontMetricsInt;
    private TextPaint mCurPaint;
    private TextPaint mNexPaint;
    private int mTextSize ;//文字大小
    private Rect mLayoutRect;
    private boolean mIsAnimationing;
    private CustomeAnimation mCustomeAnimation;
    private MHandle mHandle;
    private boolean mNeedRemake = true;//是否需要重新计算显示的字符串
    private int mStartX;//画文字的起始x


    private int mTextColor = Color.parseColor("#A6222222"); //文字颜色
    private int mScrollTextColor ;

    private static final int MSG_STRING_CHANGE = 1;
    private Paint mCurArrPaint;
    private Paint mNextArrPaint;

    private Paint mSplitLinePaint;
    private TextPaint mIntroPaint;
    private Drawable mArrowDrawable;
    private Rect mArrowRect;
    private OnSubmitClickLisenter mOnSubmitClickLisenter;
    private OnScrollClickLisenter mOnScrollClickLisenter;

    public interface  OnSubmitClickLisenter{
        void onClick();
    }

    public interface  OnScrollClickLisenter{
        void onClick();
    }

    public void setOnSubmitClickLisenter(OnSubmitClickLisenter onSubmitClickLisenter){
        mOnSubmitClickLisenter = onSubmitClickLisenter;
    }

    public void setOnScrollClickLisenter(OnScrollClickLisenter onScrollClickLisenter){
        mOnScrollClickLisenter = onScrollClickLisenter;
    }

    private static class MHandle extends Handler {

        private WeakReference<SVipBgView> mView;

        public MHandle(SVipBgView autoScrollTextView) {
            super();
            mView = new WeakReference<>(autoScrollTextView);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_STRING_CHANGE:
                    SVipBgView autoScrollTextView = mView.get();
                    if (autoScrollTextView != null) {
                        autoScrollTextView.startAnimation(autoScrollTextView.mCustomeAnimation);
                        sendEmptyMessageDelayed(MSG_STRING_CHANGE, autoScrollTextView.delayTime);
                    }
            }
        }

    };


    public SVipBgView(Context context) {
        this(context, null);
        mContext = context;
    }

    public SVipBgView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SVipBgView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mRadius = Util.dipToPixel(context, 12);
        mDp60 = Util.dipToPixel(context, 60);
        mDp40 = Util.dipToPixel(context, 40);
        mDp35 = Util.dipToPixel(context, 35);
        mDp25 = Util.dipToPixel(context, 25);
        mDp20 = Util.dipToPixel(context, 20);
        mDp17 = Util.dipToPixel(context, 17);
        mDp15 = Util.dipToPixel(context, 15);
        mDp12 = Util.dipToPixel(context, 12);
        mDp10 = Util.dipToPixel(context, 10);
        mDp8 = Util.dipToPixel(context, 8);
        mDp6 = Util.dipToPixel(context, 6);
        mDp2 = Util.dipToPixel(context, 2);
        mDp1 = Util.dipToPixel(context, 1);

        mSp13 = Util.spToPixel(context,13);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mDesWidth = Util.getScreenWidth(context);
        mScrollTextColor = context.getResources().getColor(R.color.mine_svip_text_color65);

        mTopBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.texture);
        mBottomBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_radian);

        mTopSrcRect = new Rect();
        mBottomSrcRect = new Rect(0, 0, mBottomBmp.getWidth(), mBottomBmp.getHeight());
        mTopBmpDescRect = new RectF();
        mBottomDescRect = new RectF();
        mBgRect = new RectF();

        //设置标题文字画笔
        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setColor(context.getResources().getColor(R.color.mine_svip_text_color));
        mTextPaint.setTextSize(mSp13);
        mTextPaint.setFakeBoldText(true);

        //分割线画笔
        mSplitLinePaint = new Paint();
        mSplitLinePaint.setAntiAlias(true);
        mSplitLinePaint.setTextSize(mSp13);
        mSplitLinePaint.setFakeBoldText(true);
        mSplitLinePaint.setDither(true);
        mSplitLinePaint.setColor(context.getResources().getColor(R.color.mine_svip_divider_color_gold));

        //简介文字画笔
        mIntroPaint = new TextPaint();
        mIntroPaint.setAntiAlias(true);
        mIntroPaint.setDither(true);
        mIntroPaint.setColor(context.getResources().getColor(R.color.mine_svip_text_color));
        mIntroPaint.setTextSize(mSp13);


        mCurArrPaint = new Paint();
        mCurArrPaint.setAntiAlias(true);
        mCurArrPaint.setDither(true);
        mNextArrPaint = new Paint();
        mNextArrPaint.setAntiAlias(true);
        mNextArrPaint.setDither(true);

        //设置右侧按钮
        mButtonStartX =  mDesWidth - mDp60 - mDp20 - mDp15;
        mButtonEndX = mDesWidth - mDp20 - mDp15;
        mButtonStartY = mDp8;
        mButtonEndY = mDp12 + mDp20;
        buttonBg = ContextCompat.getDrawable(getContext(), R.drawable.buttonbg);
        mArrowDrawable = ContextCompat.getDrawable(getContext(), R.mipmap.arrow_next_3);
        mArrowDrawable.setColorFilter(context.getResources().getColor(R.color.mine_svip_text_color), PorterDuff.Mode.SRC_ATOP);
        mButtonRect = new Rect(mButtonStartX, mButtonStartY, mButtonEndX, mButtonEndY);
        mArrowRect = new Rect(mButtonStartX, mButtonStartY, mButtonEndX, mButtonEndY);


        //滚动文字rect
        mAutoTextStartX = mDesWidth - (mDp20 + mDp15) * 2 - mDp12;
        mAutoTextStartY =  225 - mButtonEndY - mDp8 ;
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        //文本所在行的行高
        lineHeight = fm.bottom - fm.top + fm.leading;
        mAutoTextRect = new Rect(mDp20 + mDp15,mAutoTextStartY,mAutoTextStartX,mAutoTextStartY + (int)lineHeight );
        mAutoTextRectBound = new Rect();

        mAutoArrowStartX = mDesWidth - (mDp20 + mDp15) * 2 - mDp12;
        mAutoArrowStartY =  225 - mButtonEndY + mDp8;
        mAutoImgRect = new Rect(mDp20 + mDp15,mAutoTextStartY,mAutoTextStartX,mAutoTextStartY + (int)lineHeight );

        Bitmap  mArrowBitmapSrc = BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_circle_arrow);
        mArrowBitmap = Util.tintBitmap(mArrowBitmapSrc,mScrollTextColor);


        mCurPaint = new TextPaint();
        mCurPaint.setTextSize(mSp13);
        mCurPaint.setColor(mScrollTextColor);
        mCurPaint.setAntiAlias(true);
        mFontMetricsInt = mCurPaint.getFontMetricsInt();

        mNexPaint = new TextPaint();
        mNexPaint.setTextSize(mSp13);
        mNexPaint.setColor(mScrollTextColor);
        mNexPaint.setAntiAlias(true);

        mAlapha = mCurPaint.getAlpha();
        mArrAlapha = mCurArrPaint.getAlpha();
        mLayoutRect = new Rect();

        mStrList = new ArrayList<>();
        mTempList = new ArrayList<>();
        mCustomeAnimation = new CustomeAnimation();
        mCustomeAnimation.setDuration(animDuration);
        mCustomeAnimation.setInterpolator(new LinearInterpolator());
        mCustomeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsAnimationing = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                resetDrawHeight();
                refreshIndex();
                resetPaint();
                mIsAnimationing = false;
                invalidate();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mHandle = new MHandle(this);
        mStartX = mDp20 + mDp15;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mHeight <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        } else {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            mCurStrStartY = mDp10 + mHeight/2 + (mFontMetricsInt.descent - mFontMetricsInt.ascent)/2 - mFontMetricsInt.descent ;
            mNextStrStarY = mDp10 + mHeight/2 + (mFontMetricsInt.descent - mFontMetricsInt.ascent)/2 - mFontMetricsInt.descent + mHeight/6;
            mCurStrAnimStartY = mCurStrStartY;
            mNextStrAnimStartY = mNextStrStarY;
            setMeasuredDimension(width, mHeight);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画卡片背景
        drawableCardBackground(canvas);
        //画左上角文字
        drawTitleText(canvas);
        //画分割线
        drawSplitLine(canvas);
        //画按钮
        drawButton(canvas);
        //画按钮和分割线之间的文字
        drawIntroText(canvas);
        //画滚动文字
        drawAutoText(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int startX = (int)event.getX();
        int startY = (int)event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(mButtonStartX < startX && startX < mButtonEndX
                  && mButtonStartY < startY && startY < mButtonEndY ){ //若果点击续费按钮区域
                    if(mOnSubmitClickLisenter != null ){
                        mOnSubmitClickLisenter.onClick();
                    }
                }

                if(startY > mAutoTextStartY){
                    if(mOnScrollClickLisenter != null){
                        mOnScrollClickLisenter.onClick();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        //返回true表示不消耗此事件，事件继续传递，返回flase表示事件消耗
        return true;

    }

    /**
     * 绘制滚动文字
     *
     * @param canvas
     */
    private void drawAutoText(Canvas canvas){
        if (canvas == null) {
            return;
        }
        canvas.save();
        canvas.translate(mDp35  ,mAutoTextStartY);
        String curStr = getCuStr();
        if (!TextUtils.isEmpty(curStr)) {
            canvas.drawText(curStr, 0, curStr.length(), 0, mCurStrAnimStartY - mAutoTextStartY, mCurPaint);
            canvas.drawBitmap(mArrowBitmap,mCurPaint.measureText(curStr),mCurStrAnimStartY - mAutoTextStartY- mDp10,mCurArrPaint);
        }

        String nexStr = getNexStr();
        if (!TextUtils.isEmpty(nexStr) && mIsAnimationing) {
            canvas.drawText(nexStr, 0, nexStr.length(), 0, mNextStrAnimStartY  - mAutoTextStartY, mNexPaint);
            canvas.drawBitmap(mArrowBitmap, mNexPaint.measureText(nexStr) ,(int)mNextStrAnimStartY  - mAutoTextStartY - mDp10,mNextArrPaint);
        }
        canvas.restore();

    }

    /**
     *  画按钮分割线之间的文字
     *  超过部分打点
     *  @param canvas
     */
    private void drawIntroText(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        //分割线和按钮之间文字
        mTestEtcLength = mIntroPaint.measureText(mTestEtc);
        introWidth = mDesWidth - (mDp35 * 2 + titleWidth + mDp8 * 3 + mDp1 + mDp60);
        canvas.save();
        canvas.drawText(getFinalString(introText),lineBeginX + mDp8 , mDp25, mIntroPaint);
        canvas.restore();
    }


    /**
     * 得到最终合适长度的字符串
     * @param string
     * @return
     */
    private String getFinalString(String string) {
        if (TextUtils.isEmpty(string)) {
            return string;
        }
        int breakIndex = mTextPaint.breakText(string, true, (int) introWidth, null);
        boolean hasCut = false;
        while (breakIndex < string.length()){
            hasCut = true;
            string  = string.substring(0,breakIndex);
            breakIndex = mTextPaint.breakText(string, true, (int) introWidth - mTestEtcLength, null);
        }
        if (hasCut) {
            string += mTestEtc;
        }
        return string;
    }

    /**
     * 处理书籍字符串
     *
     * @param bookName
     * @return
     */
    private String getBookNameFinalString(String bookName){
        if (TextUtils.isEmpty(bookName)) {
            return bookName;
        }
        String subfixString = "》开通会员免费读";
        String prefixString = "《" + bookName;

        float subfixWidth = mTextPaint.measureText(subfixString);
        float defaultWidth = mDesWidth - mDp35 * 2 - mDp15 - subfixWidth - mDp8;
        int breakIndex = mTextPaint.breakText(prefixString, true, (int) defaultWidth, null);
        boolean hasCut = false;
        while (breakIndex < prefixString.length()){
            hasCut = true;
            prefixString  = prefixString.substring(0,breakIndex);
            breakIndex = mTextPaint.breakText(bookName, true, (int) defaultWidth - mTestEtcLength, null);
        }
        if (hasCut) {
            prefixString += mTestEtc;
        }
        return prefixString + subfixString;
    }

    /**
     * 画右侧按钮
     *
     * @param canvas
     */
    private void drawButton(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        canvas.save();
        buttonBg.setBounds(mButtonRect);
        buttonBg.draw(canvas);
        float mBtTextWidth = mTextPaint.measureText(mButtonText);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        //文本所在行的行高
        float mBtTextHeight  =  fm.bottom - fm.top + fm.leading;
        float mBtBeginX = mButtonStartX + (mDp60 - mBtTextWidth) /2*1.0f - mDp2;
        float mBtBeginY = mDp8 /2 + mDp12 * 2.0f  - (mDp12 * 2.0f  - mBtTextHeight) /2*1.0f;
        canvas.drawText(mButtonText,mBtBeginX,mBtBeginY,mTextPaint);
        mArrowRect.set((int)(mBtBeginX + mBtTextWidth + mDp2) , mButtonStartY + (mButtonEndY - mButtonStartY - mDp8)/2  ,
                (int)(mBtBeginX + mBtTextWidth + mDp2 * 3),mButtonStartY + (mButtonEndY - mButtonStartY - mDp8)/2 + mDp8 );
        mArrowDrawable.setBounds(mArrowRect);
        mArrowDrawable.draw(canvas);

        canvas.restore();
    }

    /**
     * 画标题右边的分割线
     *
     * @param canvas
     */
    private void drawSplitLine(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        lineBeginX = mDp35 + titleWidth + mDp8;
        float lineBeginY = mDp17;
        canvas.save();
        mSplitLinePaint.setStrokeWidth(mDp1);
        canvas.drawLine(lineBeginX, lineBeginY, lineBeginX, lineBeginY + mDp8, mSplitLinePaint);
        canvas.restore();
    }

    /**
     * 画左上角标题
     *
     * @param canvas
     */
    private void drawTitleText(Canvas canvas) {
        if (canvas == null) {
            return;
        }
        canvas.save();
        canvas.drawText(mVipTitle,mDp35,mDp25,mTextPaint);
        canvas.restore();
    }


    /**
     * 画卡片背景
     *
     * @param canvas
     */
    private void drawableCardBackground(Canvas canvas){
        if (mLinearGradient == null) {
            mLinearGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0, mColorStart, mColorEnd, Shader.TileMode.CLAMP);
        }
        canvas.save();
        mPaint.setShader(mLinearGradient);
        mBgRect.set(mDp20, 0, getMeasuredWidth() - mDp20, getMeasuredHeight() + mRadius);
        canvas.drawRoundRect(mBgRect, mRadius, mRadius, mPaint);

        float topBmpHeight = mTopBmp.getWidth() * mHeight * 1.0f / mBgRect.width();
        mTopSrcRect.set(0, 0, mTopBmp.getWidth(), (int) topBmpHeight);
        mTopBmpDescRect.set(mDp20, 0, getMeasuredWidth() - mDp20, getMeasuredHeight());
        canvas.drawBitmap(mTopBmp, mTopSrcRect, mTopBmpDescRect, null);

        float bottomBmpHeight = mBottomSrcRect.height() * getMeasuredWidth() * 1.0f / mBottomSrcRect.width();
        mBottomDescRect.set(0, getMeasuredHeight() - bottomBmpHeight, getMeasuredWidth(), getMeasuredHeight());
        canvas.drawBitmap(mBottomBmp, mBottomSrcRect, mBottomDescRect, null);
        canvas.clipRect(0, 0, getMeasuredWidth(), getMeasuredHeight());
        canvas.restore();
    }

    /**
     * 设置文字标题
     *
     * @param titleText
     */
    public void setTitleText(String titleText) {
        mVipTitle = titleText;
        //需要测量文本宽度
        titleWidth = mTextPaint.measureText(mVipTitle);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        //文本自身的高度
        titleHeight = fm.descent - fm.ascent;
        //文本所在行的行高
        lineHeight = fm.bottom - fm.top + fm.leading;
        requestLayout();

    }

    /**
     * 设置高度
     *
     * @param height
     */
    public void setHeight(int height) {
        this.mHeight = height;
        requestLayout();
    }

    /**
     *
     * 设置背景渐变颜色
     * @param colorStart
     * @param colorEnd
     */
    public void resetLinearGradient(int colorStart, int colorEnd) {
        if (this.mColorStart == colorStart && this.mColorEnd == colorEnd) {
            return;
        }
        this.mColorStart = colorStart;
        this.mColorEnd = colorEnd;
        this.mLinearGradient = null;
        invalidate();
    }

    public void setStrList(List<String> strList) {
        if (strList == null || strList.size() ==0) {
            return;
        }
        if (mStrList == null){
            mStrList = new ArrayList<>();
        }
        mStrList.clear();
        mStrList.addAll(strList);
        mNeedRemake = true;
        remakeStrList(strList);
        if (mStrList != null && mStrList.size() != 0 ) {
            requestLayout();
            if (mStrList.size() == 1) {
                mCurShowIndex = 0;
                stopAnimation();
                invalidate();
            }else {
                startAnimation();
            }
        }
    }


    public void startAnimation(){
        if (mHandle != null && canAnimation() && !mIsAnimationing) {
            if (!mHandle.hasMessages(MSG_STRING_CHANGE)) {
                mHandle.sendEmptyMessageDelayed(MSG_STRING_CHANGE,delayTime);
            }
        }
    }


    public void stopAnimation(){
        if (mHandle != null) {
            mHandle.removeMessages(MSG_STRING_CHANGE);
        }
    }

    private boolean canAnimation(){
        boolean canAnimation = false;
        if (mStrList != null && mStrList.size() > 1 ) {
            canAnimation = true;
        }
        return canAnimation;
    }

    private void remakeStrList(List<String> list) {

        if (list != null && list.size() >0 && mNeedRemake) {
            mTempList.clear();
            for (String str : list) {
                if (!TextUtils.isEmpty(str)) {
                    mTempList.add(getBookNameFinalString(str));
                }
            }
        }
        mStrList.clear();
        mStrList.addAll(mTempList);
        mNeedRemake = false;
    }

    public String getCuStr(){
        if (mStrList != null && mStrList.size() != 0 && mCurShowIndex< mStrList.size()) {
            return mStrList.get(mCurShowIndex);
        }
        return "";
    }

    public String getNexStr(){
        if (mStrList != null && mStrList.size() != 0 ) {
            if (mCurShowIndex + 1 >= mStrList.size()) {
                return mStrList.get(0);
            }else {
                return mStrList.get(mCurShowIndex +1 );
            }
        }
        return "";
    }


    private void resetPaint() {
        mCurPaint.setAlpha(mAlapha);
        mNexPaint.setAlpha(mAlapha);
        mCurArrPaint.setAlpha(mArrAlapha);
        mNextArrPaint.setAlpha(mArrAlapha);

    }

    private void resetDrawHeight() {
        mCurStrAnimStartY = mCurStrStartY;
        mNextStrAnimStartY = mNextStrStarY;
    }
    private void refreshIndex() {
        if (mStrList == null) {
            return;
        }
        if (mCurShowIndex >= mStrList.size() -1) {
            mCurShowIndex = 0;
        }else {
            mCurShowIndex += 1;
        }
    }

    /**
     *
     * 动画类
     *
     */
    class CustomeAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (!mIsAnimationing) {
                return;
            }
            mCurStrAnimStartY = mCurStrStartY;
            mNextStrAnimStartY = mNextStrStarY;
            mCurStrAnimStartY -= mHeight / 6 * interpolatedTime;
            mNextStrAnimStartY -= mHeight / 6 * interpolatedTime;
            if(1-interpolatedTime * 3 > 0){
                mCurPaint.setAlpha((int) ((1-interpolatedTime* 3) * (mAlapha)));
                mCurArrPaint.setAlpha((int) ((1-interpolatedTime * 3) * (mArrAlapha)));
            }else{
                mCurPaint.setAlpha(0);
                mCurArrPaint.setAlpha(0);
            }

            mNexPaint.setAlpha((int) ((interpolatedTime) * mAlapha));
            mNextArrPaint.setAlpha((int) ((interpolatedTime) * mArrAlapha));
            invalidate();
        }
    }

}
