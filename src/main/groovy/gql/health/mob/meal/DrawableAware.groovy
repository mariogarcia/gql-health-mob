package gql.health.mob.meal

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import gql.health.mob.R
import groovy.transform.CompileStatic

@CompileStatic
trait DrawableAware {

    Drawable findDrawableById(Context context, int id) {
        ContextCompat.getDrawable(context, id)
    }

    Drawable resolveDrawable(Context ctx, String type) {
        switch(type.toLowerCase()) {
            case 'lunch':      return findDrawableById(ctx, R.mipmap.ic_lunch)
            case 'dinner':     return findDrawableById(ctx, R.mipmap.ic_dinner)
            case 'breakfast':  return findDrawableById(ctx, R.mipmap.ic_breakfast)
            case 'in_between': return findDrawableById(ctx, R.mipmap.ic_in_between)
        }
    }
}