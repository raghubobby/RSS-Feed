package com.rss.rtabdil.pcapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.rss.rtabdil.pcapp.R;
import com.rss.rtabdil.pcapp.adapter.RSSFeedAdapter;
import com.rss.rtabdil.pcapp.beans.RSSFeedDetails;
import com.rss.rtabdil.pcapp.helper.ApplicationHelper;
import com.rss.rtabdil.pcapp.ui.ApplicationDesign;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    Context mContext = this;
    ProgressDialog pd;
    FloatingActionButton mButton;
    RSSFeedAdapter adapter;
    ArrayList<RSSFeedDetails> rssFeedDetails;
    protected ApplicationHelper mApplicationHelper;
    protected ApplicationDesign mApplicationDesign;
    private static final String TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApplicationHelper = new ApplicationHelper(this);
        mApplicationDesign = new ApplicationDesign(this);
        CoordinatorLayout corLayout = new CoordinatorLayout(this);

        LinearLayout lrLayout = new LinearLayout(mContext);
        lrLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lrLayout.setLayoutParams(params);

        mRecyclerView = new RecyclerView(this);
        mRecyclerView.setLayoutParams(params);
        mRecyclerView.setId(R.id.recyclerP);

        Toolbar toolbar = mApplicationDesign.createToolbar(this);
        //Setting Title for the Toolbar
        toolbar.setTitle("Research and Insights");


        mButton = createFAB();
        corLayout.addView(mButton);
        lrLayout.addView(toolbar);
        lrLayout.addView(mRecyclerView);
        corLayout.addView(lrLayout);

        setContentView(corLayout);


        if (mApplicationHelper.isConnected(getApplicationContext())) {
            mRecyclerView = (RecyclerView) findViewById(R.id.recyclerP);
            ReadRSSFeed readFeedDetails = new ReadRSSFeed(this, mRecyclerView);
            readFeedDetails.execute();
            //Getting the details from async task
            try {
                rssFeedDetails = readFeedDetails.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            //CustomAdapter
            adapter = new RSSFeedAdapter(mContext, rssFeedDetails);
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, 50, true));
            GridLayoutManager gridLayoutManager;
            /**
             * This method call is to check if the connection is a tablet and accordingly setting the span as 3
             */


            if (mApplicationHelper.isTablet(getApplicationContext())) {
                Log.d(TAG, "Hi Bobby I am in Tablet");
                gridLayoutManager = new GridLayoutManager(mContext, 3);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                    @Override
                    public int getSpanSize(int position) {
                        if (position == 0) {
                            return 3;
                        } else {
                            return 1;
                        }
                    }
                });
            } else {

                gridLayoutManager = new GridLayoutManager(mContext, 2);
                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                    @Override
                    public int getSpanSize(int position) {
                        if (position == 0) {
                            return 2;
                        } else {
                            return 1;
                        }
                    }
                });
            }
            mRecyclerView.setLayoutManager(gridLayoutManager);
            mRecyclerView.addItemDecoration(new Spacing(20));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(adapter);
            pd.dismiss();
        } else {
            showConnectionDialog();
        }
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }
    }


    public class Spacing extends RecyclerView.ItemDecoration {
        int space;

        public Spacing(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildLayoutPosition(view) > 0) {
                outRect.left = space;
                outRect.bottom = space;
                outRect.right = space;
            } else {
                outRect.bottom = space;
            }
        }

    }

    /**
     * Shows dialog if not connected to internet
     */
    private void showConnectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to connect to WiFi?")
                .setTitle("Internet access Not Found");

        // Add the buttons
        builder.setPositiveButton("Connect to WiFi", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        builder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                finish();
            }
        });
        // Set other dialog properties
        //Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public FloatingActionButton createFAB() {
        mButton = new FloatingActionButton(this);
        mButton.setId(R.id.fabProgrammatically);
        CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        mButton.setLayoutParams(lp);
        mButton.setSize(FloatingActionButton.SIZE_NORMAL);
        mButton.setImageResource(R.drawable.refresh);
        mButton.setBackgroundColor(Color
                .BLUE);
        //To understand that the refresh button has been clicked
        mButton.setRippleColor(Color.BLUE);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** * Calling the Refresh method*/
                refreshRSSFeed();
            }
        });
        return mButton;
    }

    public void refreshRSSFeed() {
        ReadRSSFeed readRssFeed = new ReadRSSFeed(this, mRecyclerView);
        /**
         * Updating the Content once the referesh icon is clicked
         */
        readRssFeed = new ReadRSSFeed(this, mRecyclerView);
        readRssFeed.execute();
        try {
            rssFeedDetails = readRssFeed.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        adapter = new RSSFeedAdapter(mContext, rssFeedDetails);
        mRecyclerView.setAdapter(adapter);
        pd.dismiss();
    }

    public class ReadRSSFeed extends AsyncTask<Void, Void, ArrayList<RSSFeedDetails>> {
        Context context;
        String address = "https://blog.personalcapital.com/feed/?cat=3,891,890,68,284";
        RecyclerView recyclerView;
        URL url;

        @Override
        protected void onCancelled(ArrayList<RSSFeedDetails> rssFeedItems) {
            super.onCancelled(rssFeedItems);
        }

        public ReadRSSFeed(Context context, RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            this.context = context;
            pd = new ProgressDialog(context);
            pd.setMessage("Loading RSS feed...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            rssFeedDetails = parseXml(getData());
            return rssFeedDetails;
        }

        /**
         * Parsing xml feed data
         *
         * @param data
         */
        private ArrayList parseXml(Document data) {
            if (data != null) {
                rssFeedDetails = new ArrayList<>();
                Element rootElement = data.getDocumentElement();
                Node channel = rootElement.getChildNodes().item(1);
                NodeList items = channel.getChildNodes();
                for (int i = 0; i < items.getLength(); i++) {
                    Node currentchild = items.item(i);
                    if (currentchild.getNodeName().equalsIgnoreCase("item")) {
                        RSSFeedDetails item = new RSSFeedDetails();
                        NodeList itemchilds = currentchild.getChildNodes();
                        for (int j = 0; j < itemchilds.getLength(); j++) {
                            Node current = itemchilds.item(j);
                            if (current.getNodeName().equalsIgnoreCase("title")) {
                                item.setTitle(current.getTextContent());
                            } else if (current.getNodeName().equalsIgnoreCase("description")) {
                                item.setDescription(current.getTextContent());
                            } else if (current.getNodeName().equalsIgnoreCase("pubDate")) {
                                item.setPubDate(current.getTextContent());
                            } else if (current.getNodeName().equalsIgnoreCase("link")) {
                                item.setLink(current.getTextContent());
                            } else if (current.getNodeName().equalsIgnoreCase("media:content")) {
                                String imageUrl = current.getAttributes().item(0).getTextContent();
                                item.setImageUrl(imageUrl);

                            }

                        }
                        rssFeedDetails.add(item);
                    }
                }


            }
            return rssFeedDetails;

        }

        /**
         * Getting document from URL
         *
         * @return
         */
        public Document getData() {
            try {
                url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(inputStream);
                return document;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        /**
         * RecyclerView item decoration - give equal margin around grid item
         */
        private ArrayList result(ArrayList myValue) {
            //handle value
            return myValue;
        }

    }


}
