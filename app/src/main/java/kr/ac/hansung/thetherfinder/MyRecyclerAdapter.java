package kr.ac.hansung.thetherfinder;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Jur on 2018-12-05.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.Holder> {

    private Context context;

    private List<CardItem> list = new ArrayList<>(); Bitmap bitmap;
    public MyRecyclerViewClickListener mListener;
    public MyRecyclerAdapter(Context context, List<CardItem> list) {
        this.context = context;
        this.list = list;
    }
    public void setOnClickListener(MyRecyclerViewClickListener listener){
        mListener = listener;
    }
    public interface MyRecyclerViewClickListener{
        void onItemClicked(int potition, String title, String name);
    }
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    /*
     *
     * */
    @Override
    public void onBindViewHolder(Holder holder, int position) {

        int itemposition = position;
        //final String Url = list.get(itemposition).getImage();

        holder.title.setText(list.get(itemposition).getTitle());
        holder.contents.setText(list.get(itemposition).getContents());
        holder.ranks.setText(list.get(itemposition).getRanks());
        holder.audi.setText(list.get(itemposition).getAudiA());
        holder.imageView.setImageBitmap(list.get(itemposition).getBitmap());

        if(mListener != null){
            final int pos = position;
            final String title = list.get(itemposition).getOverview();
            final String name = list.get(itemposition).getContents();
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemClicked(pos, title, name);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size(); // RecyclerView??size return
    }

    public class Holder extends RecyclerView.ViewHolder{
        public TextView title;
        public TextView contents;
        public TextView ranks;
        public TextView audi;
        public ImageView imageView;

        public Holder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.tv_title);
            contents = (TextView) view.findViewById(R.id.tv_contents);
            ranks = (TextView) view.findViewById(R.id.tv_rank);
            audi = (TextView) view.findViewById(R.id.tv_audi);
            imageView = (ImageView) view.findViewById(R.id.iv_poster);
        }
    }
}