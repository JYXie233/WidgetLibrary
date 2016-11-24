package com.xjy.widget.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: Tom
 * Date: 2016-11-16
 * Time: 11:15
 * FIXME
 */
public class MultipleAdapter extends RecyclerView.Adapter<MultipleViewHolder> implements JYItemTouchHelperAdapter {

    private Map<Integer, AbsBaseProvider> mViewTypeForProviderMap;

    private ArrayList<Integer> mProviderOrderArray;

    private final static int sKeepKey = R.id.item_keep;
    private final static int sKeepYes = 102;
    private final static int sKeepNo = 103;

    private ArrayList<Integer> mHeaderPositions;
    private ArrayList<Integer> mFooterPositions;

    private SparseIntArray mPositionViewTypes;

    private SparseIntArray mPositionHeaderViewTypes;

    private SparseIntArray mPositionSection;

    private int mSpanSize = 1;

    private boolean useDefaultSetting = true;

    private Context mContext;

    private FrameLayout mFrameLayout;

    private AbsHeaderFooterProvider mStickyHeaderProvider;

    private MultipleViewHolder mStickyHeaderHolder;

    public MultipleAdapter(Context context) {
        this.mContext = context;
        mPositionHeaderViewTypes = new SparseIntArray();
        mViewTypeForProviderMap = new HashMap<>();
        mProviderOrderArray = new ArrayList<>();
        mPositionViewTypes = new SparseIntArray();
        mPositionSection = new SparseIntArray();
        mHeaderPositions = new ArrayList<>();
        mFooterPositions = new ArrayList<>();
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                calculationCount();
                super.onChanged();
            }

        });

    }


    @Override
    public MultipleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AbsBaseProvider itemCreator = mViewTypeForProviderMap.get(viewType);
        MultipleViewHolder viewHolder = itemCreator.onCreateViewHolder(parent, viewType);
        viewHolder.itemView.setTag(sKeepKey, sKeepNo);
        if (itemCreator instanceof AbsHeaderFooterProvider) {
            if ((itemCreator).isKeep()) {
                viewHolder.itemView.setTag(sKeepKey, sKeepYes);
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MultipleViewHolder holder, int position) {
        int type = mPositionViewTypes.get(position);
        AbsBaseProvider itemCreator = mViewTypeForProviderMap.get(type);
        int startNum = itemCreator.getStartNum();
        if (mHeaderPositions.contains(position)) {
            AbsHeaderFooterProvider headerFooterProvider = (AbsHeaderFooterProvider) itemCreator;
            int section = headerFooterProvider.getSection(position);
            headerFooterProvider.onBindViewHolder(holder, section, headerFooterProvider.getHeaderData(section));
        } else if (mFooterPositions.contains(position)) {
            AbsHeaderFooterProvider headerFooterProvider = (AbsHeaderFooterProvider) itemCreator;
            int section = headerFooterProvider.getSection(position);
            headerFooterProvider.onBindViewHolder(holder, section, headerFooterProvider.getFooterData(section));
        } else {
            AbsItemProvider provider = (AbsItemProvider) itemCreator;
            provider.onBindViewHolder(holder, position - startNum, provider.get(position - startNum));
        }

    }


    @Override
    public int getItemCount() {
        int count = calculationCount();
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (mPositionViewTypes.size() < position) {
            calculationCount();
        }
        return mPositionViewTypes.get(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        ItemTouchHelper.Callback callback = new JYItemTouchHelperCallback(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            int size = 1;
            for (int i = 0; i < mProviderOrderArray.size(); i++) {
                Integer type = mProviderOrderArray.get(i);
                AbsItemProvider itemProvider = (AbsItemProvider) mViewTypeForProviderMap.get(type);
                int spanSize = itemProvider.getSpanSize();
                if (size % spanSize != 0) {
                    size *= spanSize;
                }
            }
            layoutManager = new GridLayoutManager(mContext, size);
            recyclerView.setLayoutManager(layoutManager);
        }
        if (useDefaultSetting) {
            recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
            if (layoutManager instanceof GridLayoutManager) {
                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                mSpanSize = gridLayoutManager.getSpanCount();
                gridLayoutManager.setSpanSizeLookup(mSpanSizeLookup);
            }
        }

        mFrameLayout = new FrameLayout(mContext);
        mFrameLayout.setTag("Keep it");
        ViewGroup viewGroup = (ViewGroup) recyclerView.getParent();
        viewGroup.addView(mFrameLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                handleScrolled(recyclerView);
            }
        });

    }

    private void handleScrolled(RecyclerView recyclerView) {
        int position = findFirstPosition(recyclerView);
        int headerViewType = mPositionHeaderViewTypes.get(position);
        AbsHeaderFooterProvider absHeaderFooterProvider = (AbsHeaderFooterProvider) mViewTypeForProviderMap.get(headerViewType);

        if (absHeaderFooterProvider != null && absHeaderFooterProvider.isKeep()) {
            mFrameLayout.removeAllViews();
            MultipleViewHolder holder = absHeaderFooterProvider.onCreateViewHolder(recyclerView, absHeaderFooterProvider.onInflateLayout());
            mStickyHeaderHolder = holder;
            mFrameLayout.addView(mStickyHeaderHolder.itemView);
            mStickyHeaderProvider = absHeaderFooterProvider;
            int section = mPositionSection.get(position);

            absHeaderFooterProvider.onBindViewHolder(mStickyHeaderHolder, section, absHeaderFooterProvider.getHeaderData(section));

            mFrameLayout.setVisibility(View.VISIBLE);
            mFrameLayout.bringToFront();
        } else {
            mFrameLayout.setVisibility(View.GONE);
        }

        View transInfoView = recyclerView.findChildViewUnder(
                mFrameLayout.getMeasuredWidth() / 2, mFrameLayout.getMeasuredHeight() + 1);

        if (transInfoView != null && transInfoView.getTag(sKeepKey) != null) {
            int transViewStatus = (int) transInfoView.getTag(sKeepKey);
            int dealtY = transInfoView.getTop() - mFrameLayout.getMeasuredHeight();

            // 如果当前item需要展示StickyLayout，
            // 那么根据这个item的getTop和FakeStickyLayout的高度相差的距离来滚动FakeStickyLayout.
            // 这里有一处需要注意，如果这个item的getTop已经小于0，也就是滚动出了屏幕，
            // 那么我们就要把假的StickyLayout恢复原位，来覆盖住这个item对应的吸顶信息.
            if (transViewStatus == sKeepYes) {
                if (transInfoView.getTop() > 0) {
                    mFrameLayout.setTranslationY(dealtY);
                } else {
                    mFrameLayout.setTranslationY(0);
                }
            } else if (transViewStatus == sKeepNo) {
                // 如果当前item不需要展示StickyLayout，那么就不会引起FakeStickyLayout的滚动.
                mFrameLayout.setTranslationY(0);
            }

        }
    }

    private int findFirstPosition(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            return linearLayoutManager.findFirstVisibleItemPosition();
        } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager linearLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
            return linearLayoutManager.findFirstVisibleItemPosition();
        }
        return 0;
    }

    private GridLayoutManager.SpanSizeLookup mSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
        @Override
        public int getSpanSize(int position) {
            calculationCount();
            int count = 1;
            Integer type = mPositionViewTypes.get(position);
            AbsBaseProvider itemProvider = mViewTypeForProviderMap.get(type);
            if (itemProvider == null) {
                count = 1;
            } else {
                count = mSpanSize / itemProvider.getSpanSize();
            }
            return count;

        }
    };

    private synchronized int calculationCount() {
        mHeaderPositions.clear();
        mFooterPositions.clear();
        int count = 0;
        int startNum = 0;

        for (int i = 0; i < mProviderOrderArray.size(); i++) {
            Integer headerViewType = null;
            Integer type = mProviderOrderArray.get(i);
            AbsItemProvider itemProvider = (AbsItemProvider) mViewTypeForProviderMap.get(type);
            if (itemProvider.getHeaderProvider() != null) {
                int headerType = itemProvider.getHeaderProvider().hashCode();
                headerViewType = headerType;
                mPositionHeaderViewTypes.put(startNum, headerViewType);
                mPositionViewTypes.put(startNum, headerType);
                mHeaderPositions.add(startNum);
                itemProvider.getHeaderProvider().put(startNum, i);
                mPositionSection.put(startNum, i);
                startNum += 1;
                count++;

            }
            int num = itemProvider.size();

            for (int j = 0; j < num; j++) {
                if (headerViewType != null) {
                    mPositionHeaderViewTypes.put(j + startNum, headerViewType);
                }
                mPositionSection.put(j + startNum, i);
                mPositionViewTypes.put(j + startNum, type);
            }

            itemProvider.setStartNum(startNum);

            startNum += num;

            count += num;
            if (itemProvider.getFooterProvider() != null) {
                if (headerViewType != null) {
                    mPositionHeaderViewTypes.put(startNum, headerViewType);
                }
                int footerType = itemProvider.getFooterProvider().hashCode();
                mPositionViewTypes.put(startNum, footerType);
                mFooterPositions.add(startNum);
                itemProvider.getFooterProvider().put(startNum, i);
                mPositionSection.put(startNum, i);
                startNum += 1;
                count++;
            }
        }
        return count;
    }

    public <M> AbsItemProvider<M, MultipleViewHolder> registerProvider(AbsItemProvider<M, MultipleViewHolder> itemProvider) {
        itemProvider.setContext(mContext);
        int type = itemProvider.hashCode();
        mViewTypeForProviderMap.put(type, itemProvider);
        if (itemProvider.getHeaderProvider() != null) {
            itemProvider.getHeaderProvider().setContext(mContext);
            itemProvider.getHeaderProvider().putHeader(itemProvider.getHeaderData(), mProviderOrderArray.size());
            mViewTypeForProviderMap.put(itemProvider.getHeaderProvider().hashCode(), itemProvider.getHeaderProvider());
        }
        if (itemProvider.getFooterProvider() != null) {
            itemProvider.getFooterProvider().setContext(mContext);
            itemProvider.getFooterProvider().putFooter(itemProvider.getFooterData(), mProviderOrderArray.size());
            mViewTypeForProviderMap.put(itemProvider.getFooterProvider().hashCode(), itemProvider.getFooterProvider());
        }
        mProviderOrderArray.add(type);
        return itemProvider;
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        int oldType = mPositionViewTypes.get(fromPosition);
        int newType = mPositionViewTypes.get(toPosition);
        if (mViewTypeForProviderMap.get(oldType) instanceof AbsItemProvider && mViewTypeForProviderMap.get(newType) instanceof AbsItemProvider) {
            AbsItemProvider oldItemProvider = (AbsItemProvider) mViewTypeForProviderMap.get(oldType);
            AbsItemProvider newItemProvider = (AbsItemProvider) mViewTypeForProviderMap.get(newType);
            if (oldItemProvider instanceof ItemProviderActionHelper) {
                boolean handle = ((ItemProviderActionHelper) oldItemProvider).onItemMove(fromPosition - oldItemProvider.getStartNum(), toPosition - oldItemProvider.getStartNum());
                if (!handle) {
                    if (oldItemProvider.equals(newItemProvider)) {
                        int oldPosition = fromPosition - oldItemProvider.getStartNum();
                        int newPosition = toPosition - oldItemProvider.getStartNum();
                        Collections.swap(oldItemProvider.getDataList(), oldPosition, newPosition);
                        notifyItemMoved(fromPosition, toPosition);
                    }
                }
            }
        }
    }

    @Override
    public void onItemDismiss(int position) {
        int type = mPositionViewTypes.get(position);
        AbsItemProvider itemCreator = (AbsItemProvider) mViewTypeForProviderMap.get(type);
        if (itemCreator instanceof ItemProviderActionHelper) {
            boolean handle = ((ItemProviderActionHelper) itemCreator).onItemSwipe(position - itemCreator.getStartNum());
            if (!handle) {
                itemCreator.remove(position - itemCreator.getStartNum());
                notifyItemRangeRemoved(position, 1);
            }
        }
    }

    @Override
    public boolean canSwipe(int position) {
        int type = mPositionViewTypes.get(position);
        if (mViewTypeForProviderMap.get(type) instanceof AbsItemProvider) {
            AbsItemProvider itemCreator = (AbsItemProvider) mViewTypeForProviderMap.get(type);
            if (itemCreator instanceof ItemProviderActionHelper) {
                return ((ItemProviderActionHelper) itemCreator).isItemCanSwipe(position - itemCreator.getStartNum());
            }
        }
        return false;
    }

    @Override
    public boolean canMove(int position) {
        int type = mPositionViewTypes.get(position);
        if (mViewTypeForProviderMap.get(type) instanceof AbsItemProvider) {
            AbsItemProvider itemCreator = (AbsItemProvider) mViewTypeForProviderMap.get(type);
            if (itemCreator instanceof ItemProviderActionHelper) {
                return ((ItemProviderActionHelper) itemCreator).isItemCanMove(position - itemCreator.getStartNum());
            }
        }
        return false;
    }

    public boolean isUseDefaultSetting() {
        return useDefaultSetting;
    }

    public void setUseDefaultSetting(boolean useDefaultSetting) {
        this.useDefaultSetting = useDefaultSetting;
    }

    public <T extends AbsItemProvider> T getProviderBySection(int index) {
        Integer type = mProviderOrderArray.get(index);
        return (T) mViewTypeForProviderMap.get(type);
    }

    public <T extends AbsBaseProvider> T getProviderByPosition(int position) {
        Integer type = mPositionViewTypes.get(position);
        return (T) mViewTypeForProviderMap.get(type);
    }

    public void toggleExpand(int index) {
        Integer type = mProviderOrderArray.get(index);
        AbsItemProvider itemProvider = (AbsItemProvider) mViewTypeForProviderMap.get(type);
        toggleExpand(index, !itemProvider.isExpand());
    }

    public void toggleExpand(int index, boolean toggle) {
        Integer type = mProviderOrderArray.get(index);
        AbsItemProvider itemProvider = (AbsItemProvider) mViewTypeForProviderMap.get(type);
        int num = itemProvider.size();
        itemProvider.setExpand(toggle);

        if (itemProvider.isExpand()) {
            num = itemProvider.size();
            notifyItemRangeInserted(itemProvider.getStartNum(), num);
        } else {
            notifyItemRangeRemoved(itemProvider.getStartNum(), num);
        }
    }

}