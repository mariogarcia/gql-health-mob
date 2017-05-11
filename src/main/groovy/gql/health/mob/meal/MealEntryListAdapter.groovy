package gql.health.mob.meal

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import gql.health.mob.R
import gql.health.mob.ui.MLRoundedImageView
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import groovy.transform.TupleConstructor

@CompileStatic
@TupleConstructor
class MealEntryListAdapter extends RecyclerView.Adapter<MealEntryListAdapter.ViewHolder>
   implements DrawableAware {

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
        holder.quantity.text = "${entry.quantity} ${entry.quantityType}"
        holder.foodType.imageDrawable = resolveDrawableType(entry.foodType)
    }

    Drawable resolveDrawableType(FoodType type) {
        switch(type) {
            case FoodType.BREAD_RICE:           return findDrawableById(context, R.mipmap.ic_meal_type_bread)
            case FoodType.MEAT_FISH:            return findDrawableById(context, R.mipmap.ic_meal_type_steak)
            case FoodType.MILK:                 return findDrawableById(context, R.mipmap.ic_meal_type_milk)
            case FoodType.FAT_AND_SUGAR:        return findDrawableById(context, R.mipmap.ic_meal_type_sugar_fat)
            case FoodType.HEALTHY_DRINKS:       return findDrawableById(context, R.mipmap.ic_meal_type_healthy_drinks)
            case FoodType.FRUIT_AND_VEGETABLES: return findDrawableById(context, R.mipmap.ic_meal_type_vegetables)

            default:
            return findDrawableById(context, R.mipmap.ic_meal_type_default)
        }
    }

    @Override
    int getItemCount() {
        return entries.size()
    }

    @InheritConstructors
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView description
        TextView quantity
        MLRoundedImageView foodType

        ViewHolder(View view) {
            super(view)

            description = view.findViewById(R.id.meal_item_list_item_description) as TextView
            quantity = view.findViewById(R.id.meal_item_list_item_units) as TextView
            foodType = view.findViewById(R.id.food_type) as MLRoundedImageView
        }

    }
}