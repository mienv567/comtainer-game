package com.qy.ysys.yishengyishi.views.customviews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qy.ysys.yishengyishi.R;
import com.qy.ysys.yishengyishi.utils.ToastUtil;

import java.util.Arrays;

/**
 * Created by TonyChen on 2017/1/7.
 */

public class FramilyTreeContainer extends ViewGroup implements View.OnClickListener, View.OnTouchListener {
    public static String[] names = new String[]{"祖父", "祖母", "叔伯父", "父亲", "母亲", "姑母", "堂姐妹", "堂兄弟", "兄弟", "我", "妻子", "姐妹", "姑表兄弟", "姑表姐妹", "侄女", "侄儿", "儿子", "女儿", "外甥", "外甥女", "孙女", "孙子", "外孙", "外孙女"};
    private static int[] maleIndex = {0, 2, 3, 7, 8, 9, 12, 15, 16, 18, 21, 22};
    private static int[] femaleIndex = {1, 4, 5, 6, 10, 11, 13, 14, 17, 19, 20, 23};

    // positon对应的view里面的子view索引
    public static int[] mapArr = {1, 50, 4, 2, 51, 3, 5, 6, 7, 8, 52, 9, 10, 11, 16, 15, 18, 19, 20, 12, 27, 26, 28, 29};
    private int offsetX;
    private int offsetY;

    //   根据position 检索对应表,获取对应的子view位置
    public static int positionTransformIndex(int positon) {
        for (int i = 0; i < mapArr.length; i++) {
            if (positon == mapArr[i]) {
                return i;
            }
        }
        return -1;
    }


    //   根据position 检索对应表,获取对应的子view位置
    public static int indexTransformPosition(int index) {
        if (index > mapArr.length) {
            throw new IndexOutOfBoundsException();
        }
        return mapArr[index];
    }


    // 获取相对于自己的层级关系
    public static int getRank(int index) {
        if (index <= 1) {
            return 2;
        } else if (index <= 5) {
            return 1;
        } else if (index <= 13) {
            return 0;
        } else if (index <= 19) {
            return -1;
        } else {
            return -2;
        }
    }

    // 获取父节点的性别:0男1女
    public static int getParentGender(int index) {
        if (index == 12 || index == 13 || index == 18 || index == 19 || index == 22 || index == 23) {
            return 1;
        }
        return 0;
    }

    public static int getGender(int index) {
        if (isMale(index)) {
            return 0;
        }
        return 1;
    }


    private Context mContext;
    private int mWidth; // 容器宽
    private int mHeight; // 容器高
    private int childCount; // 子元素个数
    private int paddingTop = 120;
    private int paddingLeft = 10;
    private int paddingRight = paddingLeft;
    private int horizontalSpace = 25;
    private int verticalSpace = 40;
    private int childWidth;
    private int childHeight;
    private int childWidthSum;
    private int childHeightSum;
    private int childMeaureWidth;
    private int childmeasureHeight;
    private int childWidthMiddle;
    private int childHeightMiddle;
    private Paint mPaint;
    private int strokeWidth = 4;
    private String paintColor = "#414753";
    private int brotherHoodVerticalSpace = 12; // 同父母的兄弟姐妹关系水平直线与view的垂直距离
    private int parentChildVerticalSpace = brotherHoodVerticalSpace + 3; // 同父母的兄弟姐妹关系水平直线与view的垂直距离
    private int uncleorauntAndtheyChildVerticalSpace = 10;
    private int offset = 10; // 子view 的中点偏移量

    private int measureWidth; // 原始宽
    private int measureHeight; // 原始高
    private int[] scaleWidths = new int[5]; // 缩放宽度数组
    private int multiple = 0;
    private boolean isFirstMeasure = true;

    private ScaleGestureDetector detector; // 放大缩小手势识别

    private int curX = 0;
    private int curY = 0;
    private int lastX = 0;
    private int lastY = 0;

    private int totalOffX = 0;
    private int totalOffY = 0;

    public ItemOnClickLister getItemOnClickLister() {
        return itemOnClickLister;
    }

    public void setItemOnClickLister(ItemOnClickLister itemOnClickLister) {
        this.itemOnClickLister = itemOnClickLister;
    }

    private ItemOnClickLister itemOnClickLister;

    public interface ItemOnClickLister {
        void onClickIntemView(View v, int index);
    }

    private ScaleGestureDetector scaleGestureDetector;


    public FramilyTreeContainer(Context context) {
        super(context);
        initView(context);
    }

    public FramilyTreeContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FramilyTreeContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        for (int i = 0; i < 24; i++) {
            ViewGroup itemChildView = (ViewGroup) View.inflate(context, R.layout.item_framilytree, null);
            addView(itemChildView);
            TextView tv = (TextView) itemChildView.getChildAt(1);
            itemChildView.setOnClickListener(this);
            dark(i);
            tv.setText(names[i]);
            Log.i("index = beifen", names[i] + "=" + i);
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(Color.parseColor(paintColor));
        setOnTouchListener(this);

        detector = new ScaleGestureDetector(mContext, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                Log.i("ontouch", scaleFactor + "");

                if (scaleFactor > 1) {
//                    // 放大
                    if (multiple <= 3) {
                        multiple += 1;
                        zoom(scaleWidths[multiple]);
                    }

                } else if (scaleFactor < 1) {
                    // 缩小
                    if (multiple >= 1) {
                        multiple -= 1;
                        zoom(scaleWidths[multiple]);
                    }
                }
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {


            }
        });


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (isFirstMeasure) {
            isFirstMeasure = false;
            measureWidth = mWidth;
            measureHeight = mHeight;
            int measureChildWidth = (mWidth - paddingLeft - paddingRight - 7 * horizontalSpace) / 8;
            scaleWidths = new int[]{measureChildWidth, (int) (1.25 * measureChildWidth), (int) (1.5 * measureChildWidth), (int) (1.75 * measureChildWidth), (int) (2 * measureChildWidth)};
            Log.i("onFirstMeasure", "measureWidth = " + measureWidth + "    measureHeight = " + measureHeight);
        }
        Log.i("onMeasure", "mWidth = " + mWidth + "    mHeight = " + mHeight);
        childCount = getChildCount();
        Log.i("onMeasure", "childCount = " + childCount);

        // 1. 限制子view的大小
        childWidth = (mWidth - paddingLeft - paddingRight - 7 * horizontalSpace) / 8;
        childHeight = childWidth * 4 / 3;
        childHeightMiddle = childHeight / 2;
        childWidthMiddle = childWidth / 2;

        childMeaureWidth = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        childmeasureHeight = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
        Log.i("onMeasure", "ChildView childMeaureWidth&model = " + childWidth + "childmeasureHeight&model" + childmeasureHeight);

        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            childView.measure(childMeaureWidth, childmeasureHeight);
            Log.i("onMeasure", "ChildView childMeaureWidth = " + childView.getMeasuredWidth() + "  childmeasureHeight = " + childView.getMeasuredHeight());
        }

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);


        setMeasuredDimension(mWidth, mHeight);
        Log.i("onMeasure", "container mWidth = " + mWidth + "  mHeight = " + mHeight);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < childCount; i++) {
            if (!isLeader(i)) {
                View subView = getChildAt(i);
                int nl = (int) (getChildAt(i - 1).getX() + childWidth + horizontalSpace);
                int nt = (int) getChildAt(i - 1).getY();
                int nr = nl + childWidth;
                int nb = nt + childHeight;
                subView.layout(nl, nt, nr, nb);
                Log.i("noleader", "index =" + i + " ==> l=" + nl + "  _t" + nt + "  _r=" + nr + "  _b=" + nb);
            }

        }

    }

    public static boolean isMale(int index) {
        return Arrays.binarySearch(maleIndex, index) >= 0;
    }


    private boolean isLeader(int index) {
        RelativeLayout subView = (RelativeLayout) getChildAt(index);

        if (index == 0) {
            int ml = paddingLeft + 3 * (childWidth + horizontalSpace);
            int mt = paddingTop;
            int mr = ml + childWidth;
            int mb = mt + childHeight;
            subView.layout(ml, mt, mr, mb);
            Log.i("isLeader", "index = 0 ==> l=" + ml + "  _t" + mt + "  _r=" + mr + "  _b=" + mb);
            return true;
        } else if (index == 2) {
            int ml = paddingLeft + 2 * (childWidth + horizontalSpace);
            int mt = paddingTop + (childHeight + verticalSpace) * 1;
            int mr = ml + childWidth;
            int mb = mt + childHeight;
            subView.layout(ml, mt, mr, mb);
            Log.i("isLeader", "index = 2 ==> l=" + ml + "  _t" + mt + "  _r=" + mr + "  _b=" + mb);
            return true;
        } else if (index == 6) {
            int ml = paddingLeft;
            int mt = paddingTop + (childHeight + verticalSpace) * 2;
            int mr = ml + childWidth;
            int mb = mt + childHeight;
            subView.layout(ml, mt, mr, mb);
            Log.i("isLeader", "index = 6 ==> l=" + ml + "  _t" + mt + "  _r=" + mr + "  _b=" + mb);
            return true;
        } else if (index == 14) {
            int ml = paddingLeft + 1 * (childWidth + horizontalSpace);
            int mt = paddingTop + (childHeight + verticalSpace) * 3;
            int mr = ml + childWidth;
            int mb = mt + childHeight;
            subView.layout(ml, mt, mr, mb);
            Log.i("isLeader", "index = 14 ==> l=" + ml + "  _t" + mt + "  _r=" + mr + "  _b=" + mb);
            return true;
        } else if (index == 20) {
            int ml = paddingLeft + 2 * (childWidth + horizontalSpace);
            int mt = paddingTop + (childHeight + verticalSpace) * 4;
            int mr = ml + childWidth;
            int mb = mt + childHeight;
            subView.layout(ml, mt, mr, mb);
            Log.i("isLeader", "index = 20 ==> l=" + ml + "  _t" + mt + "  _r=" + mr + "  _b=" + mb);
            return true;
        }
        return false;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("onDraw", "start");
        // 2.set夫妻关系划线,index 0,1;3,4;9,10是夫妻关系,
        drawMarriageBonds(0, 1, canvas);
        drawMarriageBonds(3, 4, canvas);
        drawMarriageBonds(9, 10, canvas);

        // 兄弟姐妹关系 (6,7);(12,13),(14,15),(16,17),(18,19),(20,21);(22,23)
        drawBrotherHood(6, 7, canvas);
        drawBrotherHood(12, 13, canvas);
        drawBrotherHood(14, 15, canvas);
        drawBrotherHood(16, 17, canvas);
        drawBrotherHood(18, 19, canvas);
        drawBrotherHood(20, 21, canvas);
        drawBrotherHood(22, 23, canvas);

        // 祖父被与父辈,父辈与我
        drawParentChild(2, 3, 5, canvas);
        drawParentChild(8, 9, 11, canvas);

        // 我与子女的竖线
        drawMeAndChild(canvas);

        // 子女关系竖线偏左的(8,14,15),(16,20,21),所谓的偏左右是竖线在父节点的左右来划分的
        drawInclinedLeft(8, 14, 15, canvas);
        drawInclinedLeft(16, 20, 21, canvas);

        // 子女关系竖线偏右的(11,18,19),(17,22,23)
        drawInclinedRight(11, 18, 19, canvas);
        drawInclinedRight(17, 22, 23, canvas);

        // 叔伯父与他的子女
        drawUncleAndHisChild(canvas);

        // 姑母与她的子女
        drawAuntAndHisChild(canvas);

        Log.d("onDraw", "end");
    }

    private void drawAuntAndHisChild(Canvas canvas) {
        // 水平直线
        float startX = getChildAt(5).getX() + childWidthMiddle - offset;
        float startY = getChildAt(5).getY() + childHeight + uncleorauntAndtheyChildVerticalSpace;
        float stopX = getChildAt(12).getX() + childWidth + horizontalSpace / 2;
        float stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        // 垂直竖线
        canvas.drawLine(startX, startY, startX, startY - uncleorauntAndtheyChildVerticalSpace, mPaint);
        canvas.drawLine(stopX, stopY, stopX, verticalSpace - uncleorauntAndtheyChildVerticalSpace - parentChildVerticalSpace + stopY, mPaint);


    }

    private void drawUncleAndHisChild(Canvas canvas) {
        // 水平直线
        float startX = getChildAt(2).getX() + childWidthMiddle - offset;
        float startY = getChildAt(2).getY() + childHeight + uncleorauntAndtheyChildVerticalSpace;
        float stopX = getChildAt(6).getX() + childWidth + horizontalSpace / 2;
        float stopY = startY;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

        // 垂直竖线
        canvas.drawLine(startX, startY, startX, startY - uncleorauntAndtheyChildVerticalSpace, mPaint);
        canvas.drawLine(stopX, stopY, stopX, verticalSpace - uncleorauntAndtheyChildVerticalSpace - parentChildVerticalSpace + stopY, mPaint);


    }

    private void drawInclinedRight(int motherIndex, int daughterIndex, int sonIndex, Canvas canvas) {
        float startX = getChildAt(motherIndex).getX() + childWidthMiddle + offset;
        float startY = getChildAt(motherIndex).getY() + childHeight;

        float stopX = startX;
        float stopY = startY + verticalSpace - brotherHoodVerticalSpace;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

    }

    private void drawInclinedLeft(int fatherIndex, int daughterIndex, int sonIndex, Canvas canvas) {
        float startX = getChildAt(fatherIndex).getX() + childWidthMiddle - offset;
        float startY = getChildAt(fatherIndex).getY() + childHeight;

        float stopX = startX;
        float stopY = startY + verticalSpace - brotherHoodVerticalSpace;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
    }


    // 我和子女的竖线
    private void drawMeAndChild(Canvas canvas) {
        // 1行2行水平线
        float startX = getChildAt(9).getX() + childWidth;
        float startY = getChildAt(9).getY() + childHeightMiddle;

        float stopX = getChildAt(10).getX();
        float stopY = getChildAt(10).getY() + childHeightMiddle;
        canvas.drawLine((startX + stopX) / 2, startY, (startX + stopX) / 2, stopY + childHeightMiddle + verticalSpace - brotherHoodVerticalSpace, mPaint);
    }

    /**
     * 第一行第二行,第二行中父亲与我兄弟姐妹关系
     *
     * @param canvas
     */
    private void drawParentChild(int bortherIndex, int myIndex, int sisterIndex, Canvas canvas) {
        // 1行2行水平线
        float startX = getChildAt(bortherIndex).getX() + childWidthMiddle;
        float startY = getChildAt(bortherIndex).getY() - parentChildVerticalSpace;

        float stopX = (int) (getChildAt(sisterIndex).getX() + childWidthMiddle);
        float stopY = (int) (getChildAt(sisterIndex).getY() - parentChildVerticalSpace);
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);

//        // 父辈与子辈长竖线
        canvas.drawLine((startX + stopX) / 2, startY, (startX + stopX) / 2, startY - childHeightMiddle - verticalSpace + parentChildVerticalSpace, mPaint);

        // 父辈与子辈短竖线
        canvas.drawLine(startX, startY, startX, startY + parentChildVerticalSpace, mPaint);
        float middleX = getChildAt(myIndex).getX() + childWidthMiddle;
        float middleY = getChildAt(myIndex).getY() - parentChildVerticalSpace;
        canvas.drawLine(middleX, middleY, middleX, middleY + parentChildVerticalSpace, mPaint);
        canvas.drawLine(stopX, stopY, stopX, stopY + parentChildVerticalSpace, mPaint);

    }

    /**
     * 同一个父母亲所形成的兄弟姐妹关系
     *
     * @param maleIndex
     * @param femaleIndex
     * @param canvas
     */
    private void drawBrotherHood(int maleIndex, int femaleIndex, Canvas canvas) {
        float startX = getChildAt(maleIndex).getX() + childWidthMiddle;
        float startY = getChildAt(maleIndex).getY() - brotherHoodVerticalSpace;

        float stopX = getChildAt(femaleIndex).getX() + childWidthMiddle;
        float stopY = getChildAt(femaleIndex).getY() - brotherHoodVerticalSpace;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint); // 先画水平线

        canvas.drawLine(startX, startY, startX, startY + brotherHoodVerticalSpace, mPaint);
        canvas.drawLine(stopX, stopY, stopX, stopY + brotherHoodVerticalSpace, mPaint);

        Log.i("drawBrotherHood", "drawBrotherHood male index = " + maleIndex + " female index = " + femaleIndex + " startX=" + startX + " startY=" + startY + " stopX=" + stopX + " stopY=" + stopY);


    }

    /**
     * 绘制夫妻关系
     *
     * @param maleIndex
     * @param femaleIndex
     * @param canvas
     */
    private void drawMarriageBonds(int maleIndex, int femaleIndex, Canvas canvas) {
        float startX = getChildAt(maleIndex).getX() + childWidth;
        float startY = getChildAt(maleIndex).getY() + childHeightMiddle;

        float stopX = getChildAt(femaleIndex).getX();
        float stopY = getChildAt(femaleIndex).getY() + childHeightMiddle;
        canvas.drawLine(startX, startY, stopX, stopY, mPaint);
        Log.i("drawMarriageBonds", "male index = " + maleIndex + " female index = " + femaleIndex + " startX=" + startX + " startY=" + startY + " stopX=" + stopX + " stopY=" + stopY);
    }


    @Override
    public void onClick(View v) {
        Log.d("onClick", "onClick index = " + this.indexOfChild(v));
        if (null != itemOnClickLister) {
            itemOnClickLister.onClickIntemView(v, this.indexOfChild(v));
        }
    }

    private float touchDownX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getPointerCount() >= 2) {
            return true;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDownX = ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(touchDownX - ev.getX()) >= ViewConfiguration.get(
                        getContext()).getScaledTouchSlop()) {
                    return true;
                }
            case MotionEvent.ACTION_UP:
                break;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //记录触摸点的坐标
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                //计算偏移量
                offsetX = x - lastX;
                offsetY = y - lastY;

                if (multiple != 0) { // 如果是没有放大就不让其滑动了
                    // 限制触摸滑动的宽度度
                    int finalX = -offsetX - totalOffX;
                    if (finalX < paddingLeft) {
                        finalX = paddingLeft;
                        offsetX = -totalOffX - finalX;
                    } else if (finalX > mWidth - measureWidth) {
                        finalX = mWidth - measureWidth;
                        offsetX = -totalOffX - finalX;
                    }
                    // 限制触摸滑动的高度
                    int finalY = -offsetY - totalOffY;
                    if (finalY < paddingTop) {
                        finalY = paddingTop;
                        offsetY = -totalOffY - finalY;
                    } else if (finalY > mHeight - measureHeight) {
                        finalY = mHeight - measureHeight;
                        offsetY = -totalOffY - finalY;
                    }


                    scrollTo(finalX, finalY);
                    Log.i("scrollTo", " 执行socrllto( "
                            + (-offsetX - totalOffX) + " ," + (-offsetY - totalOffY)
                            + ")" + " mMeasureWidth=" + measureWidth);
                } else {
                    scrollTo(0, 0);
                    totalOffX = 0;
                    totalOffY = 0;
                }
                Log.i("scrollTo", "offsetX=" + offsetX + " offsetY=" + offsetY + " totalOffX="
                        + totalOffX + " totalOffY=" + totalOffY + "  -offsetX-totalOffX="
                        + (-offsetX - totalOffX) + " -offsetY-totalOffY=" + (-offsetY - totalOffY)
                        + " mWidth=" + mWidth + " mHeight=" + mHeight + " childWdith=" + childWidth
                        + " childHeight=" + childHeight + " multiple=" + multiple);
                break;
            case MotionEvent.ACTION_UP:
                totalOffX += offsetX;
                totalOffY += offsetY;
                break;
        }

        return detector.onTouchEvent(event);
    }

    public void reset() {
        zoom(scaleWidths[0]);
    }

    private void zoom(int width) {
        childWidth = width;
        childHeight = childWidth * 4 / 3;
        mWidth = paddingLeft + paddingRight + 8 * childWidth + 7 * horizontalSpace;
        mHeight = paddingTop + 5 * childHeight + 4 * verticalSpace;
        childWidthSum = mWidth;
        childHeightSum = mHeight;
        Log.i("zoom", "call onmeasure mWidth = " + mWidth + "  mHeight = " + mHeight);
        onMeasure(MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY));
        onLayout(true, 0, 0, mWidth, mHeight);
    }

    public void light(int index) {
        Log.i("light", "index=" + index);
        ViewGroup childView = (ViewGroup) getChildAt(index);
        ImageView iv = (ImageView) childView.getChildAt(0);
        TextView tv_name = (TextView) childView.getChildAt(1);
        if (isMale(index)) {
            iv.setImageResource(R.mipmap.ic_male_liang);
            tv_name.setBackgroundColor(Color.parseColor("#128bd6"));
            childView.setBackgroundColor(Color.parseColor("#ffffff"));
        } else {
            iv.setImageResource(R.mipmap.ic_female_liang);
            tv_name.setBackgroundColor(Color.parseColor("#d31b08"));
            childView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    public void dark(int index) {
        ViewGroup childView = (ViewGroup) getChildAt(index);
        ImageView iv = (ImageView) childView.getChildAt(0);
        TextView tv_name = (TextView) childView.getChildAt(1);
        if (isMale(index)) {
            iv.setImageResource(R.mipmap.ic_male_head);
            childView.setBackgroundColor(Color.parseColor("#fffafa"));
        } else {
            iv.setImageResource(R.mipmap.ic_female_head);
            childView.setBackgroundColor(Color.parseColor("#ffcec3"));
        }
        tv_name.setBackgroundColor(Color.parseColor("#88000000"));
    }
}
