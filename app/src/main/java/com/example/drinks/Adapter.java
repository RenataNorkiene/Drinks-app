package com.example.drinks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<Drinks> data;

    // create constructor to initialize context and data sent from SearchActivity
    public Adapter(Context context, List<Drinks> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.container_drinks, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get current position of item in RecyclerView to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;

		Drinks current = data.get(position);
		//uzpildo kortele duomenimis is saraso
        myHolder.textName.setText("Name: " + current.getName());
        myHolder.textTags.setText("Tag: " + current.getTags());
        myHolder.textCategory.setText("Category: " + current.getCategory());
        myHolder.textGlass.setText("Glass: " + current.getGlass());
    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textName;
        TextView textTags;
        TextView textCategory;
        TextView textGlass;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.textName);
            textTags= (TextView) itemView.findViewById(R.id.textTags);
            textCategory = (TextView) itemView.findViewById(R.id.textCategory);
            textGlass = (TextView) itemView.findViewById(R.id.textGlass);
            itemView.setOnClickListener(this);
        }

        // Click event for all items
        @Override
        public void onClick(View v) {
            Toast.makeText(context, "You clicked an item", Toast.LENGTH_SHORT).show();
        }
    }
}
