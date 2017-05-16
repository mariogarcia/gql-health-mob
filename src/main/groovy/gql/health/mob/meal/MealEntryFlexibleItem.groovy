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
import gql.health.mob.ui.DrawableAware
import gql.health.mob.ui.FlexibleItem
import gql.health.mob.util.I18nEnabled
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
    I18nEnabled quantityType
    I18nEnabled foodType

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
        String i18nQuantity = holder.contentView.context.getString(quantityType.description)

        holder.description.text = description.capitalize()
        holder.quantity.text = "${quantity} ${i18nQuantity}"
        holder.foodType.imageDrawable = resolveDrawableType(holder.itemView.context, foodType)
    }

    Drawable resolveDrawableType(Context ctx, I18nEnabled type) {
        switch(type.description) {
            case R.string.food_type_bread:   return findDrawableById(ctx, R.mipmap.ic_meal_type_bread)
            case R.string.food_type_meat:    return findDrawableById(ctx, R.mipmap.ic_meal_type_steak)
            case R.string.food_type_milk:    return findDrawableById(ctx, R.mipmap.ic_meal_type_milk)
            case R.string.food_type_fat:     return findDrawableById(ctx, R.mipmap.ic_meal_type_sugar_fat)
            case R.string.food_type_healthy: return findDrawableById(ctx, R.mipmap.ic_meal_type_healthy_drinks)
            case R.string.food_type_fruit:   return findDrawableById(ctx, R.mipmap.ic_meal_type_vegetables)

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