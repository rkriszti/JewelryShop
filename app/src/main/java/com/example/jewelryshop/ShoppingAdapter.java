package com.example.jewelryshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> implements Filterable {

    private final LayoutInflater mInflater;
    private List<Item> mItems; // Cached copy of words
    private ArrayList<Item> itemList = new ArrayList<>();
    private ArrayList<Item> allItems = new ArrayList<>();
    private Context context;
    private int lastPosition = -1;

    //konstruktor
    ShoppingAdapter(Context context, ArrayList<Item> items) {
        this.itemList = items;
        this.allItems = new ArrayList<>(items);
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    //hozzákapcsoljuk a lista.xml-t az adapterhez
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.lista, parent, false);
        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(ShoppingAdapter.ViewHolder holder, int position) {
        Item currentItem = itemList.get(position);
        holder.bindTo(currentItem); //bindto (Viewholderben)

        //update??
        /*if (holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }*/
    }

    void setItemList(List<Item> items){
        itemList = new ArrayList<>(items);
        allItems = new ArrayList<>(items);  // hogy a filter is frissüljön
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public Filter getFilter() {
        return kereses;
    }

    //filter obj
    private final Filter kereses = new Filter() {
        @Override
        //kereses
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Item> talalatok = new ArrayList<>();
            FilterResults eredmeny = new FilterResults();

            if (constraint == null || constraint.length() == 0) { //ha nincs feltétel
                eredmeny.count = allItems.size();
                eredmeny.values = allItems;
            } else {
                String filterText = constraint.toString().toLowerCase().trim();
                for (Item item : allItems) {
                    if (item.getNev().toLowerCase().contains(filterText)) {
                        talalatok.add(item);
                    }
                }
                eredmeny.count = talalatok.size();
                eredmeny.values = talalatok;
            }

            return eredmeny;
        }

        //eredmeny
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemList = (ArrayList<Item>) results.values;
            notifyDataSetChanged(); //értesíteni kell változásról
        }
    };


    //ahhoz hogy az adapter elérje:
    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final TextView price;
        private final ImageView image;

        ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.itemTitle);
            description = itemView.findViewById(R.id.subTitle);
            image = itemView.findViewById(R.id.itemImage);
            price = itemView.findViewById(R.id.price);

            //update?
            itemView.findViewById(R.id.add_to_cart).setOnClickListener(view ->
                    ((ShopActivity) context).updateAlertIcon()
            );
        }

        void bindTo(Item item) {
            title.setText(item.getNev());
            description.setText(item.getTermekleiras() + "\nTípus: " + item.getTipus().name()); //enum!!!
            price.setText(item.getAr());
            Glide.with(context).load(item.getKep()).into(image); //image glide-dal
        }
    }
}
