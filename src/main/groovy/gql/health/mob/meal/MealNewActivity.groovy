package gql.health.mob.meal

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.View
import android.widget.TextView
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
class MealNewActivity extends AppCompatActivity {

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_entry_list)
        SwissKnife.inject(this)

        getSupportActionBar().setDisplayHomeAsUpEnabled(true)

        recyclerView.hasFixedSize = true
        recyclerView.setLayoutManager(new LinearLayoutManager(this))
        recyclerView.adapter = new MealEntryListAdapter([], this)
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this))

        loadInformation()
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_meal, menu)
        return true
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
        TextView type = this.findViewById(R.id.header_type) as TextView

        type.text = meal.type


        MealEntryListAdapter adapter = recyclerView.adapter as MealEntryListAdapter

        adapter.entries.clear()
        adapter.entries.addAll(meal.entries)
        adapter.notifyDataSetChanged()
    }

    @OnBackground
    @OnClick(R.id.button_meal_entry_new)
    void createNewEntry(View view) {
        Meal savedMeal = Activities.getExtraSerializable(this, Meal, "meal")

        openNewEntry(savedMeal)
    }

    @OnUIThread
    void openNewEntry(Meal savedMeal) {
        Activities.startActivityWithExtra(this,MealNewEntryActivity, "meal", savedMeal)
    }

//    @OnBackground
 //   @OnClick(R.id.footer_button_back_to_meal_list)
 //   void backToList(View view) {
 //       this.finish()
 //   }
}