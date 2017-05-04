package gql.health.mob.meal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import gql.health.mob.R
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

@CompileStatic
@InheritConstructors
class MealListAdapter extends ArrayAdapter<Meal> {

    View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        View rowView = inflater.inflate(R.layout.meal_list_item, parent, false)

        Meal meal = this.getItem(position)

        TextView date = rowView.findViewById(R.id.meal_list_item_when) as TextView
        TextView type = rowView.findViewById(R.id.meal_list_item_type) as TextView
        TextView summary = rowView.findViewById(R.id.meal_list_item_summary) as TextView

        date.text = meal.date
        type.text = meal.type
        summary.text = meal.entries.description.collect().join(" + ")

        return rowView
    }
}