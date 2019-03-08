package kaczmarek.notatki.ui.sectionsList.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kaczmarek.notatki.R;
import kaczmarek.notatki.di.services.database.models.Section;
import kaczmarek.notatki.ui.sectionsList.interfaces.ModifyDialog;
import kaczmarek.notatki.ui.sectionsList.interfaces.OnSectionClickListner;

public class SectionsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private LayoutInflater inflater;
    private List<Section> sectionList;
    private List<Section> mFilteredList;
    private ModifyDialog mModifyDialog;
    private OnSectionClickListner mOnSectionClickListner;

    public SectionsRecyclerViewAdapter(Context context, List<Section> sectionList, OnSectionClickListner clickListner) {
        this.inflater = LayoutInflater.from(context);
        this.sectionList = sectionList;
        this.mFilteredList = sectionList;
        this.mOnSectionClickListner = clickListner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.selection_item, viewGroup,false);
        SectionViewHolder holder = new SectionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        SectionViewHolder mSectionViewHolder = (SectionViewHolder) viewHolder;
        final String titleSection = mFilteredList.get(position).getTitleSection();
        final String colorSection = mFilteredList.get(position).getColorSection();
        mSectionViewHolder.title.setText(titleSection);
        mSectionViewHolder.mark.setColorFilter(Color.parseColor(colorSection));
        mSectionViewHolder.itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
            menu.add(R.string.item_context_menu_delete_section).setOnMenuItemClickListener(item -> {
                mModifyDialog.openDeleteDialog(titleSection);
                return true;
            });
            menu.add(R.string.item_context_menu_edit_section).setOnMenuItemClickListener(item -> {
                mModifyDialog.openEditDialog(titleSection,colorSection, 1);
                return true;
            });
        });
    }

    public void setDialog(ModifyDialog dialog) {
        this.mModifyDialog = dialog;
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()){
                    mFilteredList = sectionList;
                } else {
                    ArrayList<Section> filteredList = new ArrayList<>();
                    for (Section section : sectionList)
                        if (section.getTitleSection().toLowerCase().contains(charString))
                            filteredList.add(section);
                    mFilteredList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                    mFilteredList = (ArrayList<Section>) results.values;
                    notifyDataSetChanged();
            }
        };
    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final ImageView mark;
        SectionViewHolder(View view){
            super(view);
            title = view.findViewById(R.id.section_title);
            mark = view.findViewById(R.id.imageViewMark);
            view.setOnClickListener(v -> {
                Section currentID = mFilteredList.get(getLayoutPosition());
                mOnSectionClickListner.onSectionClick(currentID);
            });
        }


    }
}