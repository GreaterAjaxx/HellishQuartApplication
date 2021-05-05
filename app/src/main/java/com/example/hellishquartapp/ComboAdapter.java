package com.example.hellishquartapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ComboAdapter extends FirestoreRecyclerAdapter<Combo, ComboAdapter.ComboHolder> {
    private OnItemClickListener listener;
    public ComboAdapter(@NonNull FirestoreRecyclerOptions<Combo> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ComboHolder holder, int position, @NonNull Combo model) {
        holder.textViewTitle.setText(model.getTitle());
        holder.textViewInputs.setText(model.getInputs());
        if(model.getLiked()) {
            holder.imageViewLiked.setImageResource(R.drawable.ic_thumb_up);
        }
        else {
            holder.imageViewLiked.setImageResource(R.drawable.ic_thumb_down);
        }
    }

    @NonNull
    @Override
    public ComboHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.combo_item, parent, false);
        return new ComboHolder(v);
    }

    class ComboHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewInputs;
        ImageView imageViewLiked;

        public ComboHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewInputs = itemView.findViewById(R.id.text_view_inputs);
            imageViewLiked = itemView.findViewById(R.id.image_view_liked);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(getSnapshots().getSnapshot(position), position);
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }
}
