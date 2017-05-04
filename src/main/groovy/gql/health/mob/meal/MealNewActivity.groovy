package gql.health.mob.meal

import android.app.ListActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick
import com.arasthel.swissknife.annotations.OnUIThread
import gql.health.mob.R
import gql.health.mob.ui.Activities
import groovy.transform.CompileStatic
import org.w3c.dom.Text

@CompileStatic
class MealNewActivity extends ListActivity {

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_entry_list)
        SwissKnife.inject(this)
        MealEntryListAdapter entriesAdapter =
                new MealEntryListAdapter(this, R.layout.meal_entry_list_item, [])

        setListAdapter(entriesAdapter)
        loadInformation()
    }

    @Override
    protected void onResume() {
        super.onResume()
        loadInformation()
    }

    @OnBackground
    void loadInformation() {
        Meal meal = Activities.getExtraSerializable(this, Meal, "meal")
        Meal refreshed = MealService.INSTANCE.findMealById(meal.id)

        paintInformation(refreshed)
    }

    @OnUIThread
    void paintInformation(Meal meal) {
        TextView type = this.findViewById(R.id.header_where) as TextView
        TextView date = this.findViewById(R.id.header_when) as TextView
        TextView total = this.findViewById(R.id.header_total) as TextView

        type.text = meal.type
        date.text = meal.date
        total.text = "(${meal.entries.size()})"


        MealEntryListAdapter adapter = listAdapter as MealEntryListAdapter

        adapter.clear()
        adapter.addAll(meal.entries)
        adapter.notifyDataSetChanged()
    }

    @OnBackground
    @OnClick(R.id.footer_button_meal_entry_new)
    void createNewEntry(View view) {
        Meal savedMeal = Activities.getExtraSerializable(this, Meal, "meal")

        openNewEntry(savedMeal)
    }

    @OnUIThread
    void openNewEntry(Meal savedMeal) {
        Activities.startActivityWithExtra(this,MealNewEntryActivity, "meal", savedMeal)
    }

    @OnBackground
    @OnClick(R.id.footer_button_back_to_meal_list)
    void backToList(View view) {
        this.finish()
    }
}