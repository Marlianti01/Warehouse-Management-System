package com.example.warehousemanagementarifah;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private List<Item> itemList = new ArrayList<>();
    private String userEmail;

    public ItemAdapter(List<Item> itemList, String userEmail) {
        this.itemList = itemList;
        this.userEmail = userEmail;
    }
    DatabaseHelper db;

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    //to display data in list
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.itemBarcode.setText(item.getBarcode());
        holder.itemName.setText(item.getName());
        holder.itemQty.setText(String.valueOf(item.getQuantity()));
        holder.itemLocation.setText(item.getLocation());



        holder.itemView.setOnClickListener( v-> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailsItem.class);
            intent.putExtra("itemBarcode", item.getBarcode());
            intent.putExtra("user_email", userEmail);
            intent.putExtra("imageUri", item.getImageUri());
            //intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            holder.itemView.getContext().startActivity(intent);

        });


        if (item.getImageUri() != null) {
            Glide.with(holder.itemImage.getContext())
                    .load(Uri.parse(item.getImageUri()))
                    .placeholder(R.drawable.ic_upload_image)
                    .into(holder.itemImage);

        }



    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<Item> items) {
        this.itemList = items;
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName;
        ImageView itemImage;
        TextView itemQty;
        TextView itemLocation;
        TextView itemBarcode;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemBarcode = itemView.findViewById(R.id.item_barcode);
            itemName = itemView.findViewById(R.id.item_name);
            itemImage = itemView.findViewById(R.id.item_image);
            itemQty = itemView.findViewById(R.id.item_quantity);
            itemLocation = itemView.findViewById(R.id.item_location);

        }
    }
}