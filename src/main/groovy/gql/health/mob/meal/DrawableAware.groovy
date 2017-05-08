package gql.health.mob.meal

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import groovy.transform.CompileStatic

@CompileStatic
trait DrawableAware {

    Drawable findDrawableById(int id) {
        Context context = this as Context
        ContextCompat.getDrawable(context, id)
    }
}