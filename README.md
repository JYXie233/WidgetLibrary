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

### 添加依赖

````
compile 'com.xjy.widget:adapter:0.1.3'
````

### 使用方式
#### 1.自定义Provider，每个Provider可视为一个Item
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

#### 2.自定义HeaderFooter

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

#### 3.在Activity中

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

#### 4.使用ActionLayout 滑动出现自定义布局

````Java
public class MainProvider extends ItemProvider<Model> implements ItemProviderActionHelper {

    public MainProvider() {
        setSpanSize(2);
    }

    @Override
    public int onInflateLayout() {
        return R.layout.item_main;
    }

    @Override
    public void onBindViewHolder(MultipleViewHolder viewHolder, int position, Model item) {
        viewHolder.setText(R.id.textView, item.name);
    }
    //重写onInflateActionLayout返回自定义滑动布局
    @Override
    public int onInflateActionLayout() {
        return R.layout.item_action_view;
    }

    @Override
    public boolean isItemCanSwipe(int position) {
        return false;
    }

    @Override
    public boolean isItemCanMove(int position) {
        return true;
    }

    @Override
    public boolean onItemSwipe(int position) {
        return false;
    }

    @Override
    public boolean onItemMove(int oldPosition, int newPosition) {
        return false;
    }
}
````

##### ```MultipleViewHolder```常用方法

- ```isActionLayoutOpen()``` ActionLayout是否打开

- ```openActionLayout()``` 打开ActionLayout

- ```closeActionLayout()```关闭ActionLayout


##### ```MultipleAdapter```

- ```toggleExpand()``` 打开或关闭 某个Provider
- ```getProviderByPosition()``` 根据position获取当前Provider
- ```setUseDefaultSetting()``` 是否使用默认设置(不需要RecyclerView.setLayoutManager())

##### ```AbsItemProvider```
- ```add()``` ```clear()``` ```addAll()``` ```remove()``` 等等List的操作
- ```reallySize()```返回List的size()，当前Provider为关闭时(expand为false)size()返回0，reallySize()不受影响。
- ```registerHeaderProvider()``` 注册头部
- ```registerFooterProvider()``` 注册尾部
- ```setExpand()``` 展开或收起Provider
- ```setOnClickViewListener(int id)```监听item中子View的点击事件
- ```setOnLongClickViewListener(int id)```监听item中子View的长按事件
- ```setOnProviderClickListener()``` 监听item的点击事件
- ```setOnProviderLongClickListener()``` 监听item的长按事件

#### 各种事件监听
- ```setOnProviderLongClickListener``` 长按事件
- ```setOnProviderClickListener``` 点击事件
- ```setOnClickViewListener``` 监听item中某个View的点击事件
- ```setOnLongClickViewListener``` 监听item中某个View的长按事件

## BounceLayout

*核心代码来自[CanRefresh](https://github.com/canyinghao/CanRefresh)*

- *```BounceLayout```* 上下拉反弹效果

- *```JYRefreshLayout```* 上下拉刷新效果

#### 添加依赖

````
compile 'com.xjy.widget:bounceLayout:0.0.2'
````

#### 使用

##### BounceLayout
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
##### JYRefreshLayout
- ```xml```
````
   <com.xjy.widget.bounce.JYRefreshLayout
        android:id="@+id/refreshLayout"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <android.support.v7.widget.RecyclerView
            android:id="@+id/recyclerView"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:text="Hello World!" />
    </com.xjy.widget.bounce.JYRefreshLayout>
````
- ```Java```
````
JYRefreshLayout refreshLayout = (JYRefreshLayout) findViewById(R.id.refreshLayout);
refreshLayout.autoRefresh();
refreshLayout.setOnRefreshListener(new CanRefreshLayout.OnRefreshListener() {
     @Override
     public void onRefresh() {
     	new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
        	refreshLayout.refreshComplete();
        }
        }, 2000);
        }
});

refreshLayout.setOnLoadMoreListener(new CanRefreshLayout.OnLoadMoreListener() {
	@Override
	public void onLoadMore() {
    	new Handler().postDelayed(new Runnable() {
		@Override
		public void run() {
			refreshLayout.loadMoreComplete();
		}
		}, 2000);
		}
});
````



#### 更多用法请看example