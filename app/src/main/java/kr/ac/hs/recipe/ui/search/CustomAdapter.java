package kr.ac.hs.recipe.ui.search;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import kr.ac.hs.recipe.R;

public class CustomAdapter extends BaseAdapter {
    private ArrayList<ListView> itemList = new ArrayList<ListView>() ;

    public CustomAdapter() {

    }

    @Override
    public int getCount() {
        return itemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        View v = convertView;
        ViewHolder holder;

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.searchlist_layout, parent, false);

            holder = new ViewHolder();
            holder.imageView = v.findViewById(R.id.searchlist_img);
            holder.nameView = v.findViewById(R.id.searchlist_name) ;
            holder.aboutView = v.findViewById(R.id.searchlist_about) ;

            v.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        ListView listItem = itemList.get(position);

        //Bitmap img = urlToBitmap(listItem.getBImg());
        //holder.imageView.setImageBitmap(img);
        holder.imageView.setImageBitmap(listItem.getBImg());
        holder.nameView.setText(listItem.getName());
        holder.aboutView.setText(listItem.getAbout());

        return v;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position) ;
    }

    public void addItem(String url, String name, String about) {
        ListView item = new ListView();

        Bitmap img = urlToBitmap(url); // url > bitmap
        //item.setBImg(url);
        item.setBImg(img);
        item.setName(name);
        item.setAbout(about);

        itemList.add(item);
    }

    //URL??? ?????? Bitmap ????????? ??????
    public Bitmap urlToBitmap(final String Url){
        final Bitmap[] bitmap = new Bitmap[1];
        Thread mThread = new Thread(){
            @Override
            public void run(){
                try {
                    URL url = new URL(Url);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap[0] = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mThread.start();

        try{
            //??????????????? ?????? ??? ?????????????????? ????????? ?????????????????? ??????????????? ??? ????????? ?????? ??????
            //?????????????????? ????????? ????????? ????????? ??? ?????? ??????????????? ?????? ?????????
            mThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap[0];
    }

    public void clear() {
        itemList.clear();
    }

    public class ViewHolder
    {
        public ImageView imageView;
        public TextView nameView, aboutView;
    }
}
