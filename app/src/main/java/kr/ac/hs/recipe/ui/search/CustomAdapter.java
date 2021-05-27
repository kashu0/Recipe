package kr.ac.hs.recipe.ui.search;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.hs.recipe.R;

public class CustomAdapter extends BaseAdapter {

    ImageView imageView;
    TextView nameView, aboutView;
    private ArrayList<ListView> itemList = new ArrayList<ListView>() ;

    public CustomAdapter() {

    }

    @Override
    public int getCount() {
        return itemList.size() ;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.searchlist_layout, parent, false);
        }

        imageView = v.findViewById(R.id.searchlist_img) ;
        nameView = v.findViewById(R.id.searchlist_name) ;
        aboutView = v.findViewById(R.id.searchlist_about) ;

        ListView listItem = itemList.get(position);

        imageView.setImageDrawable(listItem.getImg());
        nameView.setText(listItem.getName());
        aboutView.setText(listItem.getAbout());

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

    public void addItem(Drawable img, String name, String about) {
        ListView item = new ListView();

        item.setImg(img);
        item.setName(name);
        item.setAbout(about);

        itemList.add(item);
    }
}
