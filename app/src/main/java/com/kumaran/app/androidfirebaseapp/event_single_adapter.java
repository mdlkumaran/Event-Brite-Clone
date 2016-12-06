package com.kumaran.app.androidfirebaseapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by kumaran on 02-11-2016.
 */

public class event_single_adapter extends BaseAdapter {

    private  Context mContext;
    private List<event_single_for_you_product> mEventList;

    public event_single_adapter(Context mContext, List<event_single_for_you_product> mEventList) {
        this.mContext = mContext;
        this.mEventList = mEventList;
    }

    @Override
    public int getCount() {
        return mEventList.size();
    }

    @Override
    public Object getItem(int position) {
        return mEventList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(mContext,R.layout.eventlist_cardlayout,null);
        ImageView e_image = (ImageView)v.findViewById(R.id.events_image);
        TextView e_date = (TextView)v.findViewById(R.id.event_date_text);
        TextView e_title= (TextView)v.findViewById(R.id.event_title_text);
        TextView e_Event_ID= (TextView)v.findViewById(R.id.event_ID);
        TextView e_City= (TextView)v.findViewById(R.id.e_location);
        TextView e_CategoryName= (TextView)v.findViewById(R.id.category_name_text);
        TextView e_Ticket_available= (TextView)v.findViewById(R.id.tickets_available_text);
        TextView e_ticket_Cost= (TextView)v.findViewById(R.id.ticket_cost_text);

        /*URL url = new URL("http://image10.bizrate-images.com/resize?sq=60&uid=2216744464");
        Picasso.with(convertView.getContext()).load(downloadUri).fit().centerCrop().into(mImageView);
        Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        imageView.setImageBitmap(bmp);*/

        Picasso.with(mContext).load(mEventList.get(position).getImage_url()).fit().centerCrop().into(e_image);
        e_date.setText((mEventList.get(position).getDate()));
        e_title.setText((mEventList.get(position).getTitle()));
        e_Event_ID.setText((mEventList.get(position).getEvent_ID()));
        e_City.setText((mEventList.get(position).getCity()));
        e_CategoryName.setText((mEventList.get(position).getCategoryName()));
        e_Ticket_available.setText(mEventList.get(position).getTicket_available());
        e_ticket_Cost.setText(mEventList.get(position).getTicket_cost());

        v.setTag(mEventList.get(position).getEvent_ID());

        return v;
    }
}
