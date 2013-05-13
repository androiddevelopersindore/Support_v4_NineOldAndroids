
package com.supportanimator.sample;

import java.util.ArrayList;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PagerActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewPager pager = new ViewPager(this);
        pager.setPageTransformer(false, new FlipPageTransformer());
        pager.setId(99);
        setContentView(pager);
        MyAdapter adapter = new MyAdapter(getSupportFragmentManager());
        adapter.addPage("Fragment #1", Color.RED);
        adapter.addPage("Fragment #2", Color.BLUE);
        adapter.addPage("Fragment #3", Color.GREEN);
        pager.setAdapter(adapter);
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo {
            private final String text;
            private final int color;

            TabInfo(String text, int color) {
                this.text=text;
                this.color=color;
            }
        }
        
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addPage(String text, int color) {
            mTabs.add(new TabInfo(text, color));
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
            TabInfo info = mTabs.get(position);
            Bundle args = new Bundle();
            args.putString("text", info.text);
            args.putInt("color", info.color);
            TextFragment frag = new TextFragment();
            frag.setArguments(args);
            return frag;
        }
    }
    
    public static class TextFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.text_fragment, container, false);
            view.setBackgroundColor(getArguments().getInt("color"));
            TextView text = (TextView)view.findViewById(R.id.text1);
            text.setText(getArguments().getString("text"));
            return view;
        }
    }
}
