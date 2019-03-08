package kaczmarek.notatki.ui.pagesList.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import kaczmarek.notatki.R;
import kaczmarek.notatki.di.services.database.models.Page;
import kaczmarek.notatki.ui.pagesList.interfaces.ModifyPageDialog;
import kaczmarek.notatki.ui.pagesList.interfaces.OnPageClickListener;

import java.util.ArrayList;
import java.util.List;

public class PagesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private LayoutInflater inflater;
    private List<Page> pagesList;
    private List<Page> mFilteredList;
    private ModifyPageDialog dialog;
    private OnPageClickListener mOnPageClickListener;

    public PagesRecyclerViewAdapter(Context context, List<Page> list, OnPageClickListener clickListner) {
        this.inflater = LayoutInflater.from(context);
        this.pagesList = list;
        this.mFilteredList = list;
        this.mOnPageClickListener = clickListner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = inflater.inflate(R.layout.page_item, viewGroup,false);
        PagesViewHolder holder = new PagesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        PagesViewHolder mPageViewHolder = (PagesViewHolder) viewHolder;
        mPageViewHolder.titlePage.setText((mFilteredList.get(position).getTitlePage()));
        mPageViewHolder.itemView.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
            menu.add(R.string.item_context_menu_delete_page).setOnMenuItemClickListener(item -> {
                dialog.onShowDeleteDialog(mFilteredList.get(position).getTitlePage());
                return true;
            });
            menu.add(R.string.item_context_menu_edit_page).setOnMenuItemClickListener(item -> {
                dialog.onShowEditDialog(mFilteredList.get(position).getTitlePage(),1);
                return true;
            });
        });
    }


    public void setDialog(ModifyPageDialog dialog) {
        this.dialog = dialog;
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
                if (charString.isEmpty())
                    mFilteredList = pagesList;
                else {
                    ArrayList<Page> filteredList = new ArrayList<>();
                    for (Page page : pagesList)
                        if (page.getTitlePage().toLowerCase().contains(charString))
                            filteredList.add(page);
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredList = (ArrayList<Page>) results.values;
                notifyDataSetChanged();

            }
        };
    }

    public class PagesViewHolder extends RecyclerView.ViewHolder {
        final TextView titlePage;
        PagesViewHolder(View view){
            super(view);
            titlePage = view.findViewById(R.id.item_title_page);
            view.setOnClickListener(v -> {
                Page idPage = mFilteredList.get(getLayoutPosition());
                mOnPageClickListener.onPageClick(idPage);
            });
        }


    }
}