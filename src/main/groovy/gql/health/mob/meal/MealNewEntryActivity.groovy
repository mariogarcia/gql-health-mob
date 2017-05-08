package gql.health.mob.meal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick
import com.arasthel.swissknife.annotations.OnUIThread
import gql.health.mob.R
import gql.health.mob.ui.Activities
import groovy.transform.CompileStatic

@CompileStatic
class MealNewEntryActivity extends AppCompatActivity {

    @InjectView(R.id.meal_item_unit_type)
    Spinner spinner

    @InjectView(R.id.meal_item_description)
    TextView description

    @InjectView(R.id.meal_item_how_many)
    TextView howMany

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_entry_new)
        SwissKnife.inject(this)
        loadEntryTypes()
    }

    @OnUIThread
    void loadEntryTypes() {
        ArrayAdapter<String> spinnerAdapter =
            new ArrayAdapter<>(
                this,
                R.layout.dropdown_item,
                ['GRAM', 'UNIT'] as String[]
            )

        spinner.adapter = spinnerAdapter
    }

    @OnBackground
    @OnClick(R.id.entry_item_button_save)
    void saveEntry(View view) {
        Meal meal = Activities.getExtraSerializable(this, Meal, "meal")
        MealEntry entry = new MealEntry(
                id: UUID.randomUUID(),
                description: description.text,
                unit: spinner.selectedItem.toString(),
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