package gql.health.mob.ui

import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import groovy.transform.InheritConstructors

@InheritConstructors
class I18nViewAdapter extends ArrayAdapter<I18nEnabled> {

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

        TextView dropItemView = convertView as TextView ?: LayoutInflater
                .from(getContext())
                .inflate(android.R.layout.simple_spinner_item, parent, false) as TextView

        dropItemView.text = "$i18nDescription ${symbol ? '(' + symbol + ')' : ''}"

        return dropItemView
    }
}