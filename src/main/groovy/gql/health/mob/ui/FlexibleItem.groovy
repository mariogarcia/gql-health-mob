package gql.health.mob.ui

import android.support.v7.widget.RecyclerView
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

abstract class FlexibleItem<VH extends RecyclerView.ViewHolder> extends AbstractFlexibleItem<VH> {

    boolean equals(Object o) {
        return false
    }
}