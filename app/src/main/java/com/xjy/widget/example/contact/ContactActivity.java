package com.xjy.widget.example.contact;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.xjy.widget.adapter.AbsHeaderFooterProvider;
import com.xjy.widget.adapter.AbsItemProvider;
import com.xjy.widget.adapter.ItemProviderActionHelper;
import com.xjy.widget.adapter.MultipleAdapter;
import com.xjy.widget.adapter.MultipleViewHolder;
import com.xjy.widget.adapter.OnProviderItemClickListener;
import com.xjy.widget.example.R;


public class ContactActivity extends AppCompatActivity {
    private AbsHeaderFooterProvider<String> headerFooterProvider;
    private MultipleAdapter multipleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        multipleAdapter = new MultipleAdapter(this);

        recyclerView.setAdapter(multipleAdapter);

        headerFooterProvider = new AbsHeaderFooterProvider<String>() {
            @Override
            public int onInflateLayout() {
                return R.layout.item_contact;
            }

            @Override
            public void onBindViewHolder(MultipleViewHolder viewHolder, int position, String item) {
                viewHolder.setText(R.id.textView, "header:" + position);
            }
        };

        headerFooterProvider.setOnProviderClickListener(new OnProviderItemClickListener<AbsHeaderFooterProvider<String>>() {
            @Override
            public void onProviderClick(AbsHeaderFooterProvider<String> provider, MultipleViewHolder viewHolder, View view, int position) {
                if (multipleAdapter.getProviderBySection(position).reallySize() == 0) {
                    installData(position);
                } else {
                    multipleAdapter.toggleExpand(position);
                }
            }

        });



        headerFooterProvider.setKeep(true);


        for (int i = 0; i < 8; i++) {
            generateData(i);
        }

        multipleAdapter.notifyDataSetChanged();
    }

    private void installData(final int position) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载数据");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ContactProvider contactProvider = multipleAdapter.getProviderBySection(position);
                for (int i = 0; i < position + 1; i++)
                    contactProvider.add("Contact Position:" + position + " Contact:" + i);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        multipleAdapter.toggleExpand(position, true);
                    }
                });
            }
        }).start();
    }


    private void generateData(int position) {
        ContactProvider contactProvider = new ContactProvider();

        contactProvider.registerHeaderProvider("position" + position, headerFooterProvider);

        for (int i = 0; i < position + 2; i++)
            contactProvider.add("Contact Position:" + position + " Contact:" + i);
        multipleAdapter.registerProvider(contactProvider);
    }

    private class ContactProvider extends AbsItemProvider<String, MultipleViewHolder> implements ItemProviderActionHelper {

        @Override
        public int onInflateLayout() {
            return R.layout.item_simple;
        }

        @Override
        public void onBindViewHolder(MultipleViewHolder viewHolder, int position, String item) {
            viewHolder.setText(R.id.textView, item);
        }

        @Override
        public boolean isItemCanSwipe(int position) {
            return true;
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

        @Override
        public int getSpanSize() {
            return super.getSpanSize();
        }
    }

}
