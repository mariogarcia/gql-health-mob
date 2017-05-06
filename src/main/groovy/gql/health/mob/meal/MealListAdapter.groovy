package gql.health.mob.meal

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import gql.health.mob.R
import gql.health.mob.ui.Activities
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import groovy.transform.TupleConstructor

@CompileStatic
@TupleConstructor
class MealListAdapter extends RecyclerView.Adapter<MealListAdapter.ViewHolder> {

    List<Meal> meals
    Context context

    @Override
    ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.meal_list_item, parent, false)

        return new ViewHolder(v)
    }

    @Override
    void onBindViewHolder(ViewHolder holder, int position) {
        Meal meal = meals[position]

        holder.date.text = meal.date
        holder.type.text = meal.type
        holder.summary.text = meal.entries.description.collect().join(" + ")

        holder.itemView.onClickListener = { View view ->
            Activities.startActivityWithExtra((Activity)view.context, MealNewActivity, "meal", meal)
        }
    }

    @Override
    int getItemCount() {
        return meals.size()
    }

    @InheritConstructors
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date
        TextView type
        TextView summary

        ViewHolder(View rowView) {
            super(rowView)

            date = rowView.findViewById(R.id.meal_list_item_when) as TextView
            type = rowView.findViewById(R.id.meal_list_item_type) as TextView
            summary = rowView.findViewById(R.id.meal_list_item_summary) as TextView
        }
    }
}