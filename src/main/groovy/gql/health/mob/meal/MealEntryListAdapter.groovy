package gql.health.mob.meal

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import gql.health.mob.R
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import groovy.transform.TupleConstructor

@CompileStatic
@TupleConstructor
class MealEntryListAdapter extends RecyclerView.Adapter<MealEntryListAdapter.ViewHolder> {

    List<MealEntry> entries = []
    Context context

    @Override
    ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.meal_entry_list_item, parent, false)

        ViewHolder holder = new ViewHolder(v)

        return holder
    }

    @Override
    void onBindViewHolder(ViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        MealEntry entry = entries[position]

        holder.description.text = entry.description.capitalize()
        holder.quantity.text = "${entry.quantity} ${entry.unit}"

        //holder.imageView.setImageResource(list.get(position).imageId);
    }

    @Override
    int getItemCount() {
        return entries.size()
    }

    @InheritConstructors
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView description
        TextView quantity

        ViewHolder(View view) {
            super(view)

            description = view.findViewById(R.id.meal_item_list_item_description) as TextView
            quantity = view.findViewById(R.id.meal_item_list_item_units) as TextView
        }

    }
}