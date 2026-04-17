package com.example.istream;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    public interface OnItemClickListener { void onItemClick(PlaylistItem item); }
    public interface OnDeleteClickListener { void onDeleteClick(PlaylistItem item); }

    private final List<PlaylistItem> items;
    private final OnItemClickListener clickListener;
    private final OnDeleteClickListener deleteListener;

    public PlaylistAdapter(List<PlaylistItem> items, OnItemClickListener clickListener, OnDeleteClickListener deleteListener) {
        this.items = items;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PlaylistItem item = items.get(position);
        holder.tvUrl.setText(item.videoUrl);
        holder.itemView.setOnClickListener(v -> clickListener.onItemClick(item));
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() { return items.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUrl;
        ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUrl = itemView.findViewById(R.id.tv_playlist_url);
            btnDelete = itemView.findViewById(R.id.btn_delete_item);
        }
    }
}
