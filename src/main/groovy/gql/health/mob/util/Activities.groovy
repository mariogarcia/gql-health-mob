package gql.health.mob.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent

class Activities {

    static AlertDialog createOptionsDialog(Activity activity, String title, String[] options, Closure dsl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
        builder.title = title
        builder.setItems(options, { DialogInterface di, int which ->
            di.dismiss()
            dsl(options[which])
        } as DialogInterface.OnClickListener)

        builder.create()
    }

    static void startActivityWithExtra(Context from, Class<? extends Activity> to, String key, Serializable serializable) {
        Intent intent = new Intent(from, to)
        intent.putExtra(key, serializable)

        from.startActivity(intent)
    }

    static <U extends Serializable> U getExtraSerializable(Activity from, Class<U> type, String key) {
        return (U) from.intent.getSerializableExtra(key)
    }
}