package gql.health.mob.meal

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder
import gql.health.mob.R
import gql.health.mob.ui.FlexibleItem
import gql.health.mob.ui.MLRoundedImageView
import groovy.transform.Canonical
import groovy.transform.InheritConstructors
import groovy.transform.TupleConstructor

@Canonical
@TupleConstructor
class MealEntryFlexibleItem extends FlexibleItem<ViewHolder> implements DrawableAware {

    UUID id
    String description
    Double quantity
    QuantityType quantityType
    FoodType foodType

    @Override
    int getLayoutRes() {
        return R.layout.meal_entry_list_item
    }

    @Override
    ViewHolder createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        View v = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.meal_entry_list_item, parent, false)

        return new ViewHolder(v, adapter)
    }

    @Override
    void bindViewHolder(FlexibleAdapter adapter, ViewHolder holder, int position, List payloads) {
        holder.description.text = description.capitalize()
        holder.quantity.text = "${quantity} ${quantityType}"
        holder.foodType.imageDrawable = resolveDrawableType(holder.itemView.context, foodType)
    }

    Drawable resolveDrawableType(Context ctx, FoodType type) {
        switch(type) {
            case FoodType.BREAD_RICE:           return findDrawableById(ctx, R.mipmap.ic_meal_type_bread)
            case FoodType.MEAT_FISH:            return findDrawableById(ctx, R.mipmap.ic_meal_type_steak)
            case FoodType.MILK:                 return findDrawableById(ctx, R.mipmap.ic_meal_type_milk)
            case FoodType.FAT_AND_SUGAR:        return findDrawableById(ctx, R.mipmap.ic_meal_type_sugar_fat)
            case FoodType.HEALTHY_DRINKS:       return findDrawableById(ctx, R.mipmap.ic_meal_type_healthy_drinks)
            case FoodType.FRUIT_AND_VEGETABLES: return findDrawableById(ctx, R.mipmap.ic_meal_type_vegetables)

            default:
                return findDrawableById(ctx, R.mipmap.ic_meal_type_default)
        }
    }

    @InheritConstructors
    static class ViewHolder extends FlexibleViewHolder {
        TextView description
        TextView quantity
        MLRoundedImageView foodType

        ViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter)

            description = view.findViewById(R.id.meal_item_list_item_description) as TextView
            quantity = view.findViewById(R.id.meal_item_list_item_units) as TextView
            foodType = view.findViewById(R.id.food_type) as MLRoundedImageView
        }
    }
}