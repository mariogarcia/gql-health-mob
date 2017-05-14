package gql.health.mob.meal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick
import com.arasthel.swissknife.annotations.OnUIThread
import gql.health.mob.R
import gql.health.mob.ui.Activities

class MealListActivity extends AppCompatActivity {

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView

    @InjectView(R.id.empty_list_message)
    TextView textView
      
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

        if (meals) {
            textView.visible(false)
        }
    }

    @OnBackground
    @OnClick(R.id.button_add_new_meal)
    void createNewMeal(View view) {
        showPrompt()
    }

    @OnUIThread
    void showPrompt() {
        def mealTypes = resources.getStringArray(R.array.meal_types)
        def dialog = Activities.createOptionsDialog(this, "Select meal type:", mealTypes) { String selected ->
            Meal meal = new Meal(
                    entries: [],
                    id: UUID.randomUUID(),
                    type: selected,
                    date: new Date())

            Meal savedMeal = MealService.INSTANCE.addMeal(meal)
            Activities.startActivityWithExtra(this, MealNewActivity, "meal", savedMeal)
        }

        dialog.show()
    }
}
