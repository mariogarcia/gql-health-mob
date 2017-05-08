package gql.health.mob.meal

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick
import com.arasthel.swissknife.annotations.OnUIThread
import gql.health.mob.R
import gql.health.mob.ui.Activities
import gql.health.mob.ui.ImageAware
import gql.health.mob.ui.SimpleDividerItemDecoration
import groovy.transform.CompileStatic

@CompileStatic
class MealNewActivity extends AppCompatActivity implements DrawableAware, ImageAware {

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView

    @InjectView(R.id.meal_type_image)
    ImageView imageView

    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_entry_list)
        SwissKnife.inject(this)

        recyclerView.hasFixedSize = true
        recyclerView.setLayoutManager(new LinearLayoutManager(this))
        recyclerView.adapter = new MealEntryListAdapter([], this)
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this))

        loadInformation()
    }

    @Override
    protected void onResume() {
        super.onResume()
        loadInformation()
    }

    @OnBackground
    void loadInformation() {
        Meal refreshed = MealService.INSTANCE.findMealById(currentMeal.id)

        paintInformation(refreshed)
    }

    @OnUIThread
    void paintInformation(Meal meal) {
        collapsingToolbarLayout.title = meal.type.toLowerCase().capitalize()

        if (!meal.image) {
            imageView.imageDrawable = resolveDrawable(meal.type)
        }

        MealEntryListAdapter adapter = recyclerView.adapter as MealEntryListAdapter

        adapter.entries.clear()
        adapter.entries.addAll(meal.entries)
        adapter.notifyDataSetChanged()
    }

    Drawable resolveDrawable(String type) {
        switch(type.toLowerCase()) {
            case 'lunch':      return findDrawableById(R.mipmap.ic_lunch)
            case 'dinner':     return findDrawableById(R.mipmap.ic_dinner)
            case 'breakfast':  return findDrawableById(R.mipmap.ic_breakfast)
            case 'in_between': return findDrawableById(R.mipmap.ic_in_between)
        }
    }

    @OnBackground
    @OnClick(R.id.button_meal_entry_new)
    void createNewEntry(View view) {
        openNewEntry(currentMeal)
    }

    @OnUIThread
    void openNewEntry(Meal savedMeal) {
        Activities.startActivityWithExtra(this, MealNewEntryActivity, "meal", savedMeal)
    }

    @OnBackground
    @OnClick(R.id.button_meal_cancel)
    void cancelEntry(View view) {
        this.finish()
    }

    @OnBackground
    @OnClick(R.id.button_meal_photo)
    void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = createImageFile().getAbsolutePath() as File
        Meal savedMeal = currentMeal
                .copyWith(image: Uri.fromFile(file))

        MealService.INSTANCE.updateMeal(savedMeal)

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, savedMeal.image);
        startActivityForResult(takePictureIntent, 1);
    }

    @Override
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            Meal meal = MealService.INSTANCE.findMealById(currentMeal.id)
            String imagePath = meal.image.toString() - "file://"

            loadPicFromPathInto(imagePath, imageView)
        }
    }

    Meal getCurrentMeal() {
        return Activities.getExtraSerializable(this, Meal, "meal")
    }
}
