package gql.health.mob.meal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick
import com.arasthel.swissknife.annotations.OnUIThread
import gql.health.mob.R
import gql.health.mob.ui.Activities
import gql.health.mob.ui.SimpleDividerItemDecoration
import groovy.transform.CompileStatic

@CompileStatic
class MealListActivity extends AppCompatActivity {

    static final List<String> MEAL_TYPES = ["BREAKFAST", "LUNCH", "DINNER", "IN_BETWEEN"]

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView
      
    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_list)
        SwissKnife.inject(this)

        recyclerView.hasFixedSize = true
        recyclerView.setLayoutManager(new LinearLayoutManager(this))
        recyclerView.adapter = new MealListAdapter([], this)

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
        MealListAdapter adapter = recyclerView.adapter as MealListAdapter

        adapter.meals.clear()
        adapter.meals.addAll(meals)
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
