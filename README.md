# WidgetLibrary
自己觉得常用的一些控件简单封装一下。
- ```MultipleAdapter``` RecyclerView Adapter的封装
- ```BounceLayout``` 上下拉刷新加载更多，Bounce效果，自定义头部尾部。在[CanRefresh](https://github.com/canyinghao/CanRefresh)基础上修改，感谢canyinghao.

*在build.gradle中添加*

````
allprojects {
    repositories {
        jcenter()
        maven {
            url 'https://dl.bintray.com/tmwin/maven'
        }
    }
}
````

## MultipleAdapter
这是一个Recyclerview Adapter的封装，使用于Adapter中有多种不同Item。
- 方便定义多种类型的Item
- 支持Section
- 支持滑动删除，拖动等
- 添加头部尾部
- 头部固定
- 后续将添加更多功能

#### import

````
compile 'com.xjy.widget:adapter:0.1.0'
````

#### 自定义Provider，每个Provider可视为一个Item
````
public class HomeItemProvider extends ItemProvider<Model> implements ItemProviderActionHelper{
    @Override
    public int onInflateLayout() {
        return R.layout.item_home_item;
    }

    @Override
    public void onBindViewHolder(MultipleViewHolder viewHolder, int position, Model item) {
        viewHolder.setText(R.id.textView, item.name);
    }

    //以下必须implements ItemProviderActionHelper

    //是否可以滑动删除
    @Override
    public boolean isItemCanSwipe(int position) {
        return false;
    }

    //是否可以拖动
    @Override
    public boolean isItemCanMove(int position) {
        return true;
    }

    @Override
    public boolean onItemSwipe(int position) {
        //返回true表示自己处理这个事件
        return false;//返回false表示不处理，MutilpleAdapter自动删除对应数据，更新界面
    }

    @Override
    public boolean onItemMove(int oldPosition, int newPosition) {
        //返回true表示自己处理这个事件
        return false;//返回false表示MutilpleAdapter处理，自动交换数据和更新界面
    }
}
````

#### 自定义HeaderFooter

````
    public class MyHeader extends AbsHeaderFooterProvider<String>{

        @Override
        public int onInflateLayout() {
            return 0;
        }

        @Override
        public void onBindViewHolder(MultipleViewHolder viewHolder, int position, String item) {

        }

        @Override
        public boolean isKeep() {
        //设置滑动到顶部时固定，暂时只支持垂直方向
             return true;
        }
    }
````

#### 使用

````
    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    MultipleAdapter multipleAdapter = new MultipleAdapter(this);
    recyclerview.setAdapter(multipleAdapter);

    HomeItemProvider provider = new HomeItemProvider();
    //将Provider注册到Adapter
    multipleAdapter.registerProvider(provider);
    //设置每行显示4列，此处不需要设置Layoutanager
    provider.setSpanSize(4);
    MyHeader header = MyHeader();
    provider.registerHeaderProvider("header", header);
    //如需使用自己的LayoutManager，请设置setUseDefaultSetting(false);
    //展开或收起一个Provider, section代表第几个Provider
    multipleAdapter.toggleExpand(section);

````

#### 各种事件监听
- setOnProviderLongClickListener 长按事件
- setOnProviderClickListener 点击事件
- setOnClickViewListener 监听item中某个View的点击事件
- setOnLongClickViewListener 监听item中某个View的长按事件

## BounceLayout

*核心代码来自[CanRefresh](https://github.com/canyinghao/CanRefresh)*

- *```BounceLayout```* 上下拉反弹效果

- *```JYRefreshLayout```* 上下拉刷新效果

#### import

````
compile 'com.xjy.widget:bounceLayout:0.0.1'
````

#### 使用

````
  <com.xjy.widget.bounce.BounceLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"/>

    </com.xjy.widget.bounce.BounceLayout>
````


