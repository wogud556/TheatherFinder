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

    public MyRecyclerAdapter(Context context, List<CardItem> list) {
        this.context = context;
        this.list = list;
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
        
        /*
        String url = "https://image.tmdb.org/t/p/w500" + list.get(position).getImage();
        Glide.with(context)
                .load(url)
                .centerCrop()
                .crossFade()
                .into(holder.imageView);
        */
        /*Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(Url);
                    HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        try {
            thread.join();
            holder.imageView.setImageBitmap(bitmap);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
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