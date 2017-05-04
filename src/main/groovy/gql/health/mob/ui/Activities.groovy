package gql.health.mob.ui

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent

class Activities {

    static AlertDialog createOptionsDialog(Activity activity, String title, List<String> options, Closure dsl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
        builder.title = title
        builder.setItems(options as String[], { DialogInterface di, int which ->
            di.dismiss()
            dsl(options.get(which))
        } as DialogInterface.OnClickListener)

        builder.create()
    }

    static void startActivityWithExtra(Activity from, Class<? extends Activity> to, String key, Serializable serializable) {
        Intent intent = new Intent(from, to)
        intent.putExtra(key, serializable)

        from.startActivity(intent)
    }

    static <U extends Serializable> U getExtraSerializable(Activity from, Class<U> type, String key) {
        return (U) from.intent.getSerializableExtra(key)
    }
}