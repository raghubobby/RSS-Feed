package com.rss.rtabdil.pcapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rss.rtabdil.pcapp.R;
import com.rss.rtabdil.pcapp.WebView;
import com.rss.rtabdil.pcapp.activity.MainActivity;
import com.rss.rtabdil.pcapp.beans.RSSFeedDetails;
import com.rss.rtabdil.pcapp.helper.ApplicationHelper;

import java.util.ArrayList;

/**
 * Created by rtabdil on 3/18/17.
 */

public class RSSFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MAIN_ARTICLE = 0;
    public static final int GROUP_ARTICLES = 1;
    ArrayList<RSSFeedDetails> feedItems = null;
    Context mContext;
    MainActivity mainActivity = new MainActivity();

    public RSSFeedAdapter(Context context, ArrayList<RSSFeedDetails> feedItems) {
        this.feedItems = feedItems;
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case GROUP_ARTICLES:
                view = cardViewOtherArticles();
                return new GroupViewHolder(view);
            case MAIN_ARTICLE:
                view = cardViewFirstArticle();
                return new FirstHeaderArticleViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RSSFeedDetails current = feedItems.get(position);

        if (getItemCount() > 0) {
            if (current != null) {
                switch (getItemViewType(position)) {
                    case GROUP_ARTICLES:
                        ((GroupViewHolder) holder).image.setImageBitmap(null);
                        ((GroupViewHolder) holder).title.setText(current.getTitle());
                        //Image Loading us done with the help of Glide
                        Glide.with(mContext).load(current.getImageUrl()).into(((GroupViewHolder) holder).image);
                        ((GroupViewHolder) holder).cView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, WebView.class);
                                String link = current.getLink();
                                intent.putExtra("link", link);
                                intent.putExtra("title", current.getTitle());
                                mContext.startActivity(intent);
                            }
                        });
                        break;
                    case MAIN_ARTICLE:
                        ((FirstHeaderArticleViewHolder) holder).image.setImageBitmap(null);

                        ((FirstHeaderArticleViewHolder) holder).title.setText(current.getTitle());
                        //Image Loading with Glide
                        Glide.with(mContext).load(current.getImageUrl()).into(((FirstHeaderArticleViewHolder) holder).image);
                        String desciptionPubDate = current.getPubDate().substring(0, 17) + " - " + current.getDescription().replaceAll("\\<.*?>", "");
                        ((FirstHeaderArticleViewHolder) holder).descriptionPubDate.setText(desciptionPubDate);
                        ((FirstHeaderArticleViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, WebView.class);
                                intent.putExtra("link", current.getLink());
                                intent.putExtra("title", current.getTitle());
                                mContext.startActivity(intent);
                            }
                        });
                        break;

                }
            }

        }

    }

    /**
     * Differentiating First article and other articles by getting Item view type
     *
     * @param position
     * @return
     */

    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);
        if (position == 0) {
            return MAIN_ARTICLE;
        } else {
            return GROUP_ARTICLES;
        }
    }

    @Override
    public int getItemCount() {
        if (feedItems != null) {
            return feedItems.size();
        } else {
            return 0;
        }

    }

    /**
     * Custom View Holder for Second Article
     */

    public class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        CardView cView;

        public GroupViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.imageP);
            image.setImageResource(R.drawable.loading);
            title = (TextView) itemView.findViewById(R.id.titleP);
            cView = (CardView) itemView.findViewById(R.id.cardP);

        }
    }

    /**
     * Custom View Holder for First Article
     */
    public class FirstHeaderArticleViewHolder extends RecyclerView.ViewHolder {
        TextView title, descriptionPubDate;
        ImageView image;
        CardView cardView;

        public FirstHeaderArticleViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.titlePfirst);
            descriptionPubDate = (TextView) itemView.findViewById(R.id.descriptionPfirst);
            image = (ImageView) itemView.findViewById(R.id.imagePfirst);
            image.setImageResource(R.drawable.loading);
            cardView = (CardView) itemView.findViewById(R.id.cardPfirst);

        }
    }


    /**
     * @return cardView for first article
     */

    public View cardViewFirstArticle() {

        LinearLayout rootLinearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rootLinearLayout.setOrientation(LinearLayout.VERTICAL);

        CardView cViewFirst = new CardView(mContext);
        cViewFirst.setId(R.id.cardPfirst);
        cViewFirst.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.cardview_shadow_end_color));
        cViewFirst.setPreventCornerOverlap(false);
        cViewFirst.setUseCompatPadding(true);

        LinearLayout lrLayout = new LinearLayout(mContext);
        lrLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams childParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rootLinearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ImageView imageFirstArticle = new ImageView(mContext);
        imageFirstArticle.setImageResource(R.drawable.loading);
        imageFirstArticle.setId(R.id.imagePfirst);
        imageFirstArticle.setScaleType(ImageView.ScaleType.FIT_XY);

        imageFirstArticle.setLayoutParams(imageParams);
        if (new ApplicationHelper(mContext).isTablet(mContext)) {
            imageFirstArticle.getLayoutParams().height = dpTopxs(400);
        } else {
            imageFirstArticle.getLayoutParams().height = dpTopxs(200);
        }

        TextView titleFirstArticle = new TextView(mContext);
        titleFirstArticle.setId(R.id.titlePfirst);
        titleFirstArticle.setEllipsize(TextUtils.TruncateAt.END);
        titleFirstArticle.setLines(1);
        titleFirstArticle.setTextColor(Color.BLACK);
        titleFirstArticle.setPadding(20, 30, 20, 0);
        titleFirstArticle.setLayoutParams(childParams);

        TextView descriptionPubDate = new TextView(mContext);
        descriptionPubDate.setId(R.id.descriptionPfirst);
        descriptionPubDate.setEllipsize(TextUtils.TruncateAt.END);
        descriptionPubDate.setLines(2);
        descriptionPubDate.setTextColor(Color.BLACK);
        descriptionPubDate.setPadding(20, 30, 20, 30);
        descriptionPubDate.setTypeface(Typeface.SANS_SERIF);

        descriptionPubDate.setLayoutParams(rootParams);

        TextView previousArticles = new TextView(mContext);
        previousArticles.setText("Previous Articles");
        previousArticles.setPadding(20, 30, 20, 0);
        previousArticles.setTextColor(Color.BLACK);
        previousArticles.setTypeface(Typeface.SANS_SERIF);
        previousArticles.setLayoutParams(rootParams);
        previousArticles.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);


        lrLayout.addView(imageFirstArticle);
        lrLayout.addView(titleFirstArticle);
        lrLayout.addView(descriptionPubDate);
        cViewFirst.addView(lrLayout);
        rootLinearLayout.addView(cViewFirst);
        rootLinearLayout.addView(previousArticles);

        return rootLinearLayout;
    }

    /**
     * @return cardView for other Articles
     */

    public View cardViewOtherArticles() {
        CardView cView = new CardView(mContext);
        cView.setId(R.id.cardP);
        cView.setPreventCornerOverlap(false);
        cView.setUseCompatPadding(true);

        LinearLayout lrLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lrLayout.setOrientation(LinearLayout.VERTICAL);

        ImageView imageView = new ImageView(mContext);
        imageView.setId(R.id.imageP);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params2);
        imageView.setImageResource(R.drawable.loading);
        imageView.getLayoutParams().height = dpTopxs(120);
        ;

        TextView tView = new TextView(mContext);
        tView.setTextColor(Color.BLACK);
        tView.setId(R.id.titleP);
        tView.setLines(2);
        tView.setPadding(20, 0, 20, 0);
        tView.setEllipsize(TextUtils.TruncateAt.END);
        tView.setTypeface(Typeface.SANS_SERIF);
        tView.setLayoutParams(params);

        lrLayout.addView(imageView);
        lrLayout.addView(tView);
        cView.addView(lrLayout);

        return cView;
    }

    public int dpTopxs(int dps) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

}
