package com.kehgye.noted.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.kehgye.noted.R;
import com.kehgye.noted.model.Note;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteAdapter extends ListAdapter<Note, NoteAdapter.NoteViewHolder> {

    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;
    private OnPinToggleListener pinToggleListener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
        setHasStableIds(true); // ✅ Helps RecyclerView track changes
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Note>() {
                @Override
                public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                    return oldItem.getTitle().equals(newItem.getTitle()) &&
                            oldItem.getContent().equals(newItem.getContent()) &&
                            oldItem.isPinned() == newItem.isPinned() &&          // ✅ crucial
                            oldItem.isTrashed() == newItem.isTrashed() &&
                            oldItem.getLastEdited() == newItem.getLastEdited();
                }
            };

    @Override
    public long getItemId(int position) {
        return getItem(position).getId(); // ✅ Ensure stable ID
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note currentNote = getItem(position);

        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewContent.setText(currentNote.getContent());

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
        String editedTime = "Edited: " + sdf.format(new Date(currentNote.getLastEdited()));
        holder.textViewLastEdited.setText(editedTime);

        // ✅ Reset visibility each time to avoid reused view issues
        holder.imageViewPin.setVisibility(View.GONE);
        if (currentNote.isPinned()) {
            holder.imageViewPin.setVisibility(View.VISIBLE);
        }
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final TextView textViewContent;
        private final TextView textViewLastEdited;
        private final ImageView imageViewPin;

        public NoteViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textView_title);
            textViewContent = itemView.findViewById(R.id.textView_content);
            textViewLastEdited = itemView.findViewById(R.id.textView_last_edited);
            imageViewPin = itemView.findViewById(R.id.imageView_pin);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(getItem(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (longClickListener != null && position != RecyclerView.NO_POSITION) {
                    longClickListener.onItemLongClick(getItem(position));
                    return true;
                }
                return false;
            });

            imageViewPin.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (pinToggleListener != null && position != RecyclerView.NO_POSITION) {
                    pinToggleListener.onTogglePin(getItem(position));
                }
            });
        }
    }

    // Interfaces for click actions
    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(Note note);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public interface OnPinToggleListener {
        void onTogglePin(Note note);
    }

    public void setOnPinToggleListener(OnPinToggleListener listener) {
        this.pinToggleListener = listener;
    }
}