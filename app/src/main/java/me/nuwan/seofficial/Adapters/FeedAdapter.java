package me.nuwan.seofficial.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Model.Common;
import me.nuwan.seofficial.Model.Feed;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;
import me.nuwan.seofficial.Widgets.PostDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater inflater;
    private List<Feed> data;

    public FeedAdapter(Context context, List<Feed> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.adapter_feed, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final MyHolder myHolder = (MyHolder) holder;
        Feed current = data.get(position);

        final String title = current.getTitle();
        final String author = current.getBy();
        final String description = current.getDesc();
        final int imageResId = current.getImage();
        final String type = current.getType();
        final String time = current.getTime();
        final String uid = current.getUid().split(",")[0];
        final String pid = current.getUid().split(",")[1];

        myHolder.feedTitle.setText(title);
        myHolder.feedAuthor.setText(author);
        myHolder.feedImage.setImageResource(imageResId);
        myHolder.feedTime.setText(
                Common.dateTimeFormat.format(
                        new Date(Long.parseLong(time)
                        )
                )
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            myHolder.feedDesc.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
        } else {
            myHolder.feedDesc.setText(Html.fromHtml(description));
        }

        try {
            myHolder.feedDesc.setMovementMethod(LinkMovementMethod.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!uid.equals(User.currentUser.getSno())) {
            myHolder.feedPopUpBtn.setVisibility(View.GONE);
        }

        myHolder.feedPopUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.feed_post_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.feedPostEdit:
                                PostDialog dialog = new PostDialog(view.getContext(),PostDialog.EDIT_POST,pid,title,description,type);
                                return true;

                            case R.id.feedPostDelete:
                                new AlertDialog.Builder(view.getContext())
                                        .setTitle("Delete")
                                        .setMessage("Are you sure you want to delete?")
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                FirebaseDB.getFeedReference().child(pid).removeValue();
                                            }})
                                        .setNegativeButton(android.R.string.no, null).show();

                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView feedTitle, feedDesc, feedAuthor, feedTime;
        ImageView feedImage, feedPopUpBtn;

        MyHolder(final View itemView) {
            super(itemView);
            feedTitle = itemView.findViewById(R.id.feedTitle);
            feedDesc = itemView.findViewById(R.id.feedDesc);
            feedAuthor = itemView.findViewById(R.id.feedAuthor);
            feedImage = itemView.findViewById(R.id.feedImage);
            feedTime = itemView.findViewById(R.id.feedTime);
            feedPopUpBtn = itemView.findViewById(R.id.feedPopUpMenuBtn);
        }
    }
}
