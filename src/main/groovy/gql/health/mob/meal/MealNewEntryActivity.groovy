package gql.health.mob.meal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick
import com.arasthel.swissknife.annotations.OnUIThread
import gql.health.mob.R
import gql.health.mob.util.Activities
import gql.health.mob.util.I18nEnabled
import gql.health.mob.ui.I18nViewAdapter

class MealNewEntryActivity extends AppCompatActivity {

    @InjectView(R.id.meal_item_unit_type)
    Spinner unitType

    @InjectView(R.id.meal_item_food_type)
    Spinner foodType

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
        unitType.adapter = new I18nViewAdapter(
            this,
            R.layout.dropdown_item,
            AuxiliarTypes.QUANTITY_TYPES
        )

        foodType.adapter = new I18nViewAdapter(
            this,
            R.layout.dropdown_item,
            AuxiliarTypes.FOOD_TYPES
        )
    }

    @OnBackground
    @OnClick(R.id.entry_item_button_save)
    void saveEntry(View view) {
        Meal meal = Activities.getExtraSerializable(this, Meal, "meal")
        MealEntry entry = new MealEntry(
            id: UUID.randomUUID(),
            description: description.text,
            quantityType: unitType.selectedItem as I18nEnabled,
            quantity: howMany.text.toDouble(),
            foodType: foodType.selectedItem as I18nEnabled)

        MealService.INSTANCE.addEntry(meal.id, entry)
        this.finish()
    }

    @OnBackground
    @OnClick(R.id.entry_item_button_cancel)
    void cancelEntry(View view) {
        this.finish()
    }
}