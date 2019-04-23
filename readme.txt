1.关于解决部分版本android上ScrollView和ListView以及ScrollView和RecyclerView嵌套时，
在初次进入时会滚动到ListView和RecyclerView的问题，调用setFocusable使其无法获取焦点可以解决。
2.视频播放需要注意，如果通过前端列表页面跳转至播放页面，会出现权限问题导致无法全屏，
需要通过终端进行中转开启新的webview。



--------------------------------------------------
 
import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.itheima.gridlayout_test.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apple on 2016/10/11.
 * 自定义的可以拖拽的GridLayout
 */
public class DragGridLayout extends GridLayout {
    //列数
    private final int columnCount = 4;

    //是否进行拖拽
    private boolean hasCanDrag;

    //记录拖拽的View
    private View dragView;
    //条目的矩形区域集合
    private List<Rect> rects;

    private List<String> items;
    //从外部设置条目的内容
    public void setItems(List<String> items) {
        this.items = items;
        createItems(items);
    }

    //创建所有的条目
    private void createItems(List<String> items) {
        //创建条目
        for (String item : items) {
            addItem(item);
        }
    }

    public void setHasCanDrag(boolean hasCanDrag) {
        this.hasCanDrag = hasCanDrag;
        if(hasCanDrag){
            setOnDragListener(onDragListener);
        }else{
            setOnDragListener(null);
        }
    }
    private View.OnDragListener onDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            //Log.i(TAG,getDragEventAction(event));
            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_STARTED://按下
                    dragView.setEnabled(false);
                    initRects();
                    break;
                case DragEvent.ACTION_DRAG_LOCATION://移动
                    //获取要交换条目的下标
                    int exchangeItemPosition = getExchangeItemPosition(event);
                    if(exchangeItemPosition > -1 && dragView != getChildAt(exchangeItemPosition)){
                        //移除
                       removeView(dragView);
                        //添加
                        addView(dragView,exchangeItemPosition);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED://弹起
                    dragView.setEnabled(true);
                    break;
            }
            return true;//拖拽事件成功的处理
        }
    };

    //获取要交换条目的下标
    private int getExchangeItemPosition(DragEvent event) {
        for (int i = 0; i < rects.size(); i++) {
            Rect rect = rects.get(i);
            if(rect.contains((int)event.getX(),(int)event.getY())){
                return i;
            }
        }
        return -1;
    }

    public DragGridLayout(Context context) {
        this(context,null);
    }

    public DragGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

//        android:columnCount="4"
//        android:animateLayoutChanges="true"
        //设置列数
        setColumnCount(columnCount);
        //开启动画
        setLayoutTransition(new LayoutTransition());
    }

    //初始化条目的矩形区域
    private void initRects() {
        rects = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            //创建矩形
            Rect rect = new Rect(view.getLeft(),view.getTop(),view.getRight(),view.getBottom());
            rects.add(rect);
        }
    }

    //SparseArray<String> 相当于hashmap,但更高效，谷歌官方推荐
    static SparseArray<String> dragEventType = new SparseArray<String>();
    static{
        dragEventType.put(DragEvent.ACTION_DRAG_STARTED, "STARTED");
        dragEventType.put(DragEvent.ACTION_DRAG_ENDED, "ENDED");
        dragEventType.put(DragEvent.ACTION_DRAG_ENTERED, "ENTERED");
        dragEventType.put(DragEvent.ACTION_DRAG_EXITED, "EXITED");
        dragEventType.put(DragEvent.ACTION_DRAG_LOCATION, "LOCATION");
        dragEventType.put(DragEvent.ACTION_DROP, "DROP");
    }

    /*
    DragEvent.ACTION_DRAG_STARTED 手指按下
    DragEvent.ACTION_DRAG_ENTERED  手指按下|从GridLayout外部进入
    DragEvent.ACTION_DRAG_LOCATION 在Gridlayout的区间内移动
    DragEvent.ACTION_DRAG_EXITED   离开GridLayout的区间
    DragEvent.ACTION_DROP      松手
    DragEvent.ACTION_DRAG_ENDED  松手

     */
    public static String getDragEventAction(DragEvent de){
        return dragEventType.get(de.getAction());
    }



    private int margin = 5;

    public void addItem(String content){
        //添加条目
        TextView tv = new TextView(getContext());
        //设置布局参数
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        params.width = getResources().getDisplayMetrics().widthPixels/4 - margin*2;
        //设置外边距
        params.setMargins(margin,margin,margin,margin);
        tv.setLayoutParams(params);
        //设置内边距
        tv.setPadding(0,margin,0,margin);
        tv.setGravity(Gravity.CENTER);
        tv.setText(content);
        //设置背景资源
        tv.setBackgroundResource(R.drawable.item_bg);
        addView(tv,0);

        if(hasCanDrag){
            //给条目设置长按点击事件
            tv.setOnLongClickListener(onLongClickListener);
        }else{
            //给条目设置长按点击事件
            tv.setOnLongClickListener(null);
        }
        //设置条目的点击事件
        tv.setOnClickListener(onClickListener);
    }

    private OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if(onDragItemClickListener != null){
                onDragItemClickListener.onDragItemClick((TextView) v);
            }
        }
    };

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            dragView = v;
            //产生浮动的阴影效果
            //①传递数据  ② 产生阴影的    ③ 传递数据  ④ 状态
            v.startDrag(null,new View.DragShadowBuilder(v),null,0);
            return true;//处理长按事件
        }
    };

    //接口
    public interface OnDragItemClickListener{
        public void onDragItemClick(TextView tv);
    }

    private OnDragItemClickListener onDragItemClickListener;

    public void setOnDragItemClickListener(OnDragItemClickListener onDragItemClickListener) {
        this.onDragItemClickListener = onDragItemClickListener;
    }
}


----------------------------------------------

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import com.itheima.gridlayout_test.view.DragGridLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private GridLayout gridLayout;

    //记录拖拽的View
    private View dragView;
    //条目的矩形区域集合
    private List<Rect> rects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化GridLayout对象
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        //设置拖拽的监听
//        gridLayout.setOnDragListener(onDragListener);


        final DragGridLayout dragGridLayout = (DragGridLayout) findViewById(R.id.dragGridLayout);
        dragGridLayout.setHasCanDrag(true);
        List<String> items = new ArrayList<>();
        items.add("北京");
        items.add("上海");
        items.add("深圳");
        items.add("广州");
        items.add("武汉");
        items.add("济南");
        items.add("重庆");
        items.add("长沙");
        items.add("大连");
        items.add("南京");
        items.add("石家庄");
        dragGridLayout.setItems(items);

        final DragGridLayout dragGridLayout2 = (DragGridLayout) findViewById(R.id.dragGridLayout2);
        List<String> items2 = new ArrayList<>();
        items2.add("日本");
        items2.add("东京");
        items2.add("韩国");
        items2.add("新加坡");
        items2.add("纽约");
        items2.add("常德");
        items2.add("广西");
        items2.add("哈尔滨");
        items2.add("南昌");
        items2.add("无锡");
        items2.add("海南");
        dragGridLayout2.setItems(items2);
        dragGridLayout2.setHasCanDrag(false);

        //设置条目点击监听
        dragGridLayout.setOnDragItemClickListener(new DragGridLayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                //移除点击的条目，把条目添加到下面的Gridlayout
                dragGridLayout.removeView(tv);//移除是需要时间,不能直接添加
                dragGridLayout2.addItem(tv.getText().toString());
            }
        });

        dragGridLayout2.setOnDragItemClickListener(new DragGridLayout.OnDragItemClickListener() {
            @Override
            public void onDragItemClick(TextView tv) {
                //移除点击的条目，把条目添加到上面的Gridlayout
                dragGridLayout2.removeView(tv);//移除是需要时间,不能直接添加
                dragGridLayout.addItem(tv.getText().toString());
            }
        });
    }

    private View.OnDragListener onDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            //Log.i(TAG,getDragEventAction(event));
            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_STARTED://按下
                    dragView.setEnabled(false);
                    initRects();
                    break;
                case DragEvent.ACTION_DRAG_LOCATION://移动
                    //获取要交换条目的下标
                    int exchangeItemPosition = getExchangeItemPosition(event);
                    if(exchangeItemPosition > -1 && dragView != gridLayout.getChildAt(exchangeItemPosition)){
                        //移除
                        gridLayout.removeView(dragView);
                        //添加
                        gridLayout.addView(dragView,exchangeItemPosition);
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED://弹起
                    dragView.setEnabled(true);
                    break;
            }
            return true;//拖拽事件成功的处理
        }
    };

    //获取要交换条目的下标
    private int getExchangeItemPosition(DragEvent event) {
        for (int i = 0; i < rects.size(); i++) {
            Rect rect = rects.get(i);
            if(rect.contains((int)event.getX(),(int)event.getY())){
                return i;
            }
        }
        return -1;
    }

    //初始化条目的矩形区域
    private void initRects() {
        rects = new ArrayList<>();
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View view = gridLayout.getChildAt(i);
            //创建矩形
            Rect rect = new Rect(view.getLeft(),view.getTop(),view.getRight(),view.getBottom());
            rects.add(rect);
        }
    }

    //SparseArray<String> 相当于hashmap,但更高效，谷歌官方推荐
    static SparseArray<String> dragEventType = new SparseArray<String>();
    static{
        dragEventType.put(DragEvent.ACTION_DRAG_STARTED, "STARTED");
        dragEventType.put(DragEvent.ACTION_DRAG_ENDED, "ENDED");
        dragEventType.put(DragEvent.ACTION_DRAG_ENTERED, "ENTERED");
        dragEventType.put(DragEvent.ACTION_DRAG_EXITED, "EXITED");
        dragEventType.put(DragEvent.ACTION_DRAG_LOCATION, "LOCATION");
        dragEventType.put(DragEvent.ACTION_DROP, "DROP");
    }

    /*
    DragEvent.ACTION_DRAG_STARTED 手指按下
    DragEvent.ACTION_DRAG_ENTERED  手指按下|从GridLayout外部进入
    DragEvent.ACTION_DRAG_LOCATION 在Gridlayout的区间内移动
    DragEvent.ACTION_DRAG_EXITED   离开GridLayout的区间
    DragEvent.ACTION_DROP      松手
    DragEvent.ACTION_DRAG_ENDED  松手

     */
    public static String getDragEventAction(DragEvent de){
        return dragEventType.get(de.getAction());
    }



    private int index = 0;
    private int margin = 5;
    public void addItem(View view){
        //添加条目
        TextView tv = new TextView(this);
        //设置布局参数
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = GridLayout.LayoutParams.WRAP_CONTENT;
        //动态计算每个item的宽度
        params.width = getResources().getDisplayMetrics().widthPixels/4 - margin*2;
        //设置外边距
        params.setMargins(margin,margin,margin,margin);
        tv.setLayoutParams(params);
        //设置内边距
        tv.setPadding(0,margin,0,margin);
        tv.setGravity(Gravity.CENTER);
        tv.setText(index+"");
        //设置背景资源
        tv.setBackgroundResource(R.drawable.item_bg);
        index++;
        gridLayout.addView(tv,0);
        //给条目设置长按点击事件
        tv.setOnLongClickListener(onLongClickListener);
    }

    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            dragView = v;
            //产生浮动的阴影效果
            //①传递数据  ② 产生阴影的    ③ 传递数据  ④ 状态
            v.startDrag(null,new View.DragShadowBuilder(v),null,0);
            return true;//处理长按事件
        }
    };
}


---------------------------------------
