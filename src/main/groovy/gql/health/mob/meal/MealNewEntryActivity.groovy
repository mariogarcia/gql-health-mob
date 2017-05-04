package gql.health.mob.meal

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick
import gql.health.mob.R
import gql.health.mob.ui.Activities
import groovy.transform.CompileStatic

@CompileStatic
class MealNewEntryActivity extends Activity {

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_entry_new)
        SwissKnife.inject(this)
    }

    @OnBackground
    @OnClick(R.id.entry_item_button_save)
    void saveEntry(View view) {
        Meal meal = Activities.getExtraSerializable(this, Meal, "meal")

        TextView description = this.findViewById(R.id.meal_item_description) as TextView
        TextView howMany = this.findViewById(R.id.meal_item_how_many) as TextView

        MealEntry entry = new MealEntry(
                id: UUID.randomUUID(),
                description: description.text,
                quantity: howMany.text.toDouble())

        MealService.INSTANCE.addEntry(meal.id, entry)
        this.finish()
    }

    @OnBackground
    @OnClick(R.id.entry_item_button_cancel)
    void cancelEntry(View view) {
        this.finish()
    }
}