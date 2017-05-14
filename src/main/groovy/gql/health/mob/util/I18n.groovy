package gql.health.mob.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build

class I18n {

    @SuppressWarnings('deprecation')
    static void changeLocale(Context context, Locale overrideLocale) {
        Resources resources = context.getApplicationContext().getResources()
        Configuration overrideConfiguration = resources.getConfiguration()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            overrideConfiguration.setLocale(overrideLocale)
        } else {
            overrideConfiguration.locale = overrideLocale
        }

        if (Build.VERSION.SDK_INT >= 24) { // N
            context.getApplicationContext().createConfigurationContext(overrideConfiguration)
        } else {
            resources.updateConfiguration(overrideConfiguration, null)
        }
    }
}