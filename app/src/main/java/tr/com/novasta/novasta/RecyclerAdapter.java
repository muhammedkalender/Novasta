package tr.com.novasta.novasta;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    List<Item> list;
    ItemListener itemListener;

    public RecyclerAdapter(ArrayList<Item> list, ItemListener ItemListener) {
        this.list = list;
        this.itemListener = ItemListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            final ViewHolder view_holder = new ViewHolder(v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemListener.onItemClick(v, view_holder.getPosition());
                }
            });

            return view_holder;
        } catch (Exception e) {
            clib.err(2591, e);
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.title.setText(list.get(position).title);
            holder.card_view.setTag(list.get(position).id);
            holder.description.setText(list.get(position).description);

            if (list.get(position).date != null && !list.get(position).date.equals("") && !list.get(position).date.equals("null")) {
                holder.catndate.setText(list.get(position).categoryName + " / " + list.get(position).date);
            } else {
                if (list.get(position).categoryName != null && !list.get(position).categoryName.equals("") && !list.get(position).categoryName.equals("null")) {
                    holder.catndate.setText(list.get(position).categoryName);
                } else {
                    holder.catndate.setText("");
                }
            }

            clib.glide(list.get(position).image, holder.image);
        } catch (IllegalArgumentException ex) {
            clib.err(450, ex);
        } catch (Exception ex) {
            clib.err(458, ex);
        }
    }


    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        }

        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        try {
            super.onAttachedToRecyclerView(recyclerView);
        } catch (Exception e) {
            clib.err(256, e);
        }
    }

    interface ItemListener {
        void onItemClick(View v, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView catndate;
        public TextView description;
        public ImageView image;
        public RelativeLayout card_view;

        public ViewHolder(View view) {
            super(view);
            try {
                card_view = view.findViewById(R.id.rlCard);
                title = view.findViewById(R.id.tvTitle);
                image = view.findViewById(R.id.ivImage);
                catndate = view.findViewById(R.id.tvCategoryNDate);
                description = view.findViewById(R.id.tvDescription);
            } catch (Exception e) {
                clib.err(393, e);
            }
        }

    }
}
