package com.frost.dbrom.components.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.frost.dbrom.R;
import com.frost.dbrom.database.Note;
import com.frost.dbrom.databinding.ItemNoteBinding;

import java.util.List;

/**
 * Created by Gokul Kalagara (Mr. Psycho) on 08-08-2019.
 * <p>
 * Frost
 */
public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Note> list;
    private INotesAdapter iNotesAdapter;

    public NotesAdapter(List<Note> list, INotesAdapter iNotesAdapter) {
        this.list = list;
        this.iNotesAdapter = iNotesAdapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new NoteViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_note,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NoteViewHolder)
        {
            NoteViewHolder noteViewHolder = (NoteViewHolder) holder;
            noteViewHolder.bindData(list.get(position));
            noteViewHolder.binding.imgDelete.setOnClickListener(view -> {
                iNotesAdapter.deleteNote(list.get(position),position);
            });

            noteViewHolder.itemView.setOnClickListener(view -> {
                iNotesAdapter.updateNote(list.get(position),position);
            });

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,0,0,position==list.size()-1?300:0);
            noteViewHolder.itemView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder
    {
        private ItemNoteBinding binding;
        public NoteViewHolder(ItemNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(Note note)
        {
            binding.tvTitle.setText(note.getTitle());
            binding.tvDescription.setText(note.getDescription());
        }
    }
    public void updatedNotes(List<Note> notes)
    {
        this.list = notes;
        notifyDataSetChanged();
    }
}
