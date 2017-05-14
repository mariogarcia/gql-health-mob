package gql.health.mob.meal

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat

trait DrawableAware {

    Drawable findDrawableById(Context context, int id) {
        ContextCompat.getDrawable(context, id)
    }
}