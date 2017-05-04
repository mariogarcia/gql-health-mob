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
class MealEntryListAdapter extends ArrayAdapter<MealEntry> {

    View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        View rowView = inflater.inflate(R.layout.meal_entry_list_item, parent, false)

        TextView description = rowView.findViewById(R.id.meal_item_list_item_description) as TextView
        TextView quantityText = rowView.findViewById(R.id.meal_item_list_item_units) as TextView

        MealEntry entry = this.getItem(position)

        description.text = entry.description
        quantityText.text = "${entry.quantity} ${entry.unit}"

        return rowView
    }
}