package gql.health.mob.ui

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import gql.health.mob.R
import gql.health.mob.util.I18nEnabled
import groovy.transform.InheritConstructors

@InheritConstructors
class I18nViewAdapter extends ArrayAdapter<I18nEnabled>
        implements DrawableAware{

    @Override
    View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        getCustomView(position, convertView, parent)
    }

    @Override
    View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        getCustomView(position, convertView, parent)
    }

    View getCustomView(int position, View convertView, ViewGroup parent) {
        I18nEnabled object = getItem(position)

        String i18nDescription = parent.context.getString(object.description)
        String symbol = object.symbol

        if (object.drawableImage == 0) {
            View dropItemView = convertView ?: LayoutInflater
                .from(context)
                .inflate(R.layout.i18n_spinner_item, parent, false)

            TextView textView = dropItemView.findViewById(R.id.i18n_description) as TextView
            textView.text = "${i18nDescription.capitalize()} ${symbol ? '(' + symbol + ')' : ''}"

            return dropItemView
        } else {
            View dropItemView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.i18n_spinner_item_w_image, parent, false)

            TextView textView = dropItemView.findViewById(R.id.i18n_description) as TextView
            ImageView imageView = dropItemView.findViewById(R.id.i18n_image) as ImageView

            textView.text = "$i18nDescription ${symbol ? '(' + symbol + ')' : ''}"
            imageView.imageDrawable = findDrawableById(context, object.drawableImage)

            return dropItemView
        }
    }
}