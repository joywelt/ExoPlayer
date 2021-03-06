/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer.demo;

import com.google.android.exoplayer.demo.Samples.Sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An activity for selecting from a number of samples.
 */
public class SampleChooserActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.prefButton) {
            Intent i = new Intent(this, AddPreference.class);
            startActivityForResult(i, 1);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_chooser_activity);
        try {
            PlayerActivity newActivity = new PlayerActivity();
            newActivity.new MyAsyncTask().execute("http://localhost:8080/Route_Receiver/Receiver_App/setIP.php", "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
//    final List<SampleGroup> sampleGroups = new ArrayList<>();
//    SampleGroup group = new SampleGroup("YouTube DASH");
//    group.addAll(Samples.YOUTUBE_DASH_MP4);
//    group.addAll(Samples.YOUTUBE_DASH_WEBM);
//    sampleGroups.add(group);
//    group = new SampleGroup("Widevine DASH Policy Tests (GTS)");
//    group.addAll(Samples.WIDEVINE_GTS);
//    sampleGroups.add(group);
//    group = new SampleGroup("Widevine HDCP Capabilities Tests");
//    group.addAll(Samples.WIDEVINE_HDCP);
//    sampleGroups.add(group);
//    group = new SampleGroup("Widevine DASH: MP4,H264");
//    group.addAll(Samples.WIDEVINE_H264_MP4_CLEAR);
//    group.addAll(Samples.WIDEVINE_H264_MP4_SECURE);
//    sampleGroups.add(group);
//    group = new SampleGroup("Widevine DASH: WebM,VP9");
//    group.addAll(Samples.WIDEVINE_VP9_WEBM_CLEAR);
//    group.addAll(Samples.WIDEVINE_VP9_WEBM_SECURE);
//    sampleGroups.add(group);
//    group = new SampleGroup("Widevine DASH: MP4,H265");
//    group.addAll(Samples.WIDEVINE_H265_MP4_CLEAR);
//    group.addAll(Samples.WIDEVINE_H265_MP4_SECURE);
//    sampleGroups.add(group);
//    group = new SampleGroup("SmoothStreaming");
//    group.addAll(Samples.SMOOTHSTREAMING);
//    sampleGroups.add(group);
//    group = new SampleGroup("HLS");
//    group.addAll(Samples.HLS);
//    sampleGroups.add(group);
//    group = new SampleGroup("Misc");
//    group.addAll(Samples.MISC);
//    sampleGroups.add(group);
//    ExpandableListView sampleList = (ExpandableListView) findViewById(R.id.sample_list);
//    sampleList.setAdapter(new SampleAdapter(this, sampleGroups));
//    sampleList.setOnChildClickListener(new OnChildClickListener() {
//      @Override
//      public boolean onChildClick(ExpandableListView parent, View view, int groupPosition,
//                                  int childPosition, long id) {
//        onSampleSelected(sampleGroups.get(groupPosition).samples.get(childPosition));
//        return true;
//      }
//    });

        final ImageView button1 = (ImageView) findViewById(R.id.imageView1);
        final ImageView button2 = (ImageView) findViewById(R.id.imageView2);
        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onSampleSelected(Samples.test[0]);
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onSampleSelected(Samples.test[1]);
            }
        });
    }

    private void onSampleSelected(Sample sample) {
        Intent mpdIntent = new Intent(this, PlayerActivity.class)
                .setData(Uri.parse(sample.uri))
                .putExtra(PlayerActivity.CONTENT_ID_EXTRA, sample.contentId)
                .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, sample.type)
                .putExtra(PlayerActivity.PROVIDER_EXTRA, sample.provider)
                .putExtra(PlayerActivity.CHANNEL_NUM, sample.uri.contains("DASH_Content1")?"1":"2");
        startActivity(mpdIntent);
    }

    private static final class SampleAdapter extends BaseExpandableListAdapter {

        private final Context context;
        private final List<SampleGroup> sampleGroups;

        public SampleAdapter(Context context, List<SampleGroup> sampleGroups) {
            this.context = context;
            this.sampleGroups = sampleGroups;
        }

        @Override
        public Sample getChild(int groupPosition, int childPosition) {
            return getGroup(groupPosition).samples.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent,
                        false);
            }
            ((TextView) view).setText(getChild(groupPosition, childPosition).name);
            return view;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return getGroup(groupPosition).samples.size();
        }

        @Override
        public SampleGroup getGroup(int groupPosition) {
            return sampleGroups.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.sample_chooser_inline_header, parent,
                        false);
            }
            ((TextView) view).setText(getGroup(groupPosition).title);
            return view;
        }

        @Override
        public int getGroupCount() {
            return sampleGroups.size();
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    private static final class SampleGroup {

        public final String title;
        public final List<Sample> samples;

        public SampleGroup(String title) {
            this.title = title;
            this.samples = new ArrayList<>();
        }

        public void addAll(Sample[] samples) {
            Collections.addAll(this.samples, samples);
        }

    }

}
