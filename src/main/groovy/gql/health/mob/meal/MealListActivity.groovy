package gql.health.mob.meal

import android.app.ListActivity
import android.os.Bundle
import android.view.View
import android.widget.ListAdapter
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick
import com.arasthel.swissknife.annotations.OnUIThread
import gql.health.mob.R
import gql.health.mob.ui.Activities
import groovy.transform.CompileStatic

@CompileStatic
class MealListActivity extends ListActivity {

    static final List<String> MEAL_TYPES = ["BREAKFAST", "LUNCH", "DINNER", "IN_BETWEEN"]
      
    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_list)
        SwissKnife.inject(this)
        MealListAdapter adapter =
                new MealListAdapter(
                        this,
                        R.layout.meal_list_item,
                        [])

        setListAdapter(adapter)
        loadMeals()
    }

    @Override
    protected void onResume() {
        super.onResume()
        loadMeals()
    }

    @OnBackground
    void loadMeals() {
        paintMealList(MealService.INSTANCE.listAll())
    }

    @OnUIThread
    void paintMealList(List meals) {
        MealListAdapter adapter = listAdapter as MealListAdapter

        adapter.clear()
        adapter.addAll(meals)
        adapter.notifyDataSetChanged()
    }

    @OnBackground
    @OnClick(R.id.button_add_new_meal)
    void createNewMeal(View view) {
        showPrompt()
    }

    @OnUIThread
    void showPrompt() {
        def dialog = Activities.createOptionsDialog(this, "Select meal type:", MEAL_TYPES) { String selected ->
            Meal meal = new Meal(
                    entries: [],
                    id: UUID.randomUUID(),
                    type: selected,
                    date: new Date().format('dd/MM/yyyy'))

            Meal savedMeal = MealService.INSTANCE.addMeal(meal)
            Activities.startActivityWithExtra(this, MealNewActivity, "meal", savedMeal)
        }

        dialog.show()
    }
}
