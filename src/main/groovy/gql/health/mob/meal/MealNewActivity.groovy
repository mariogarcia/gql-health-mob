package gql.health.mob.meal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
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
    void onResume() {
        super.onResume()
        loadInformation()
    }

    @OnBackground
    void loadInformation() {
        Log.i("Background", "loadInformation")

        Meal refreshed = MealService.INSTANCE.findMealById(currentMeal.id)
        paintInformation(refreshed)
    }

    @OnUIThread
    void paintInformation(Meal meal) {
        Log.i("UIThread", "paintInformation [$meal]")

        collapsingToolbarLayout.title = meal.type.toLowerCase().capitalize()

        if (meal.imagePath) {
            loadPicFromPathInto(meal.imagePath, imageView)
        } else {
            imageView.imageDrawable = findDrawableById(this, R.mipmap.ic_meal_default)
        }

        MealEntryListAdapter adapter = recyclerView.adapter as MealEntryListAdapter

        adapter.entries.clear()
        adapter.entries.addAll(meal.entries)
        adapter.notifyDataSetChanged()
    }

    @OnBackground
    @OnClick(R.id.button_meal_entry_new)
    void createNewEntry(View view) {
        Activities.startActivityWithExtra(this, MealNewEntryActivity, "meal", currentMeal)
    }

    @OnBackground
    @OnClick(R.id.button_meal_cancel)
    void cancelEntry(View view) {
        this.finish()
    }

    @OnBackground
    @OnClick(R.id.button_meal_photo)
    void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        Uri imageUri = Uri.fromFile(createImageFile())
        Meal savedMeal = currentMeal.copyWith(imagePath: imageUri.path)

        MealService.INSTANCE.updateMeal(savedMeal)

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(takePictureIntent, IMAGE_KEY);
    }

    @Override
    void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_KEY && resultCode == 0) {
            def updatedMeal = MealService
                .INSTANCE
                .findMealById(currentMeal.id)
                .copyWith(imagePath: null)

            MealService.INSTANCE.updateMeal(updatedMeal)
        }
    }

    Meal getCurrentMeal() {
        return Activities.getExtraSerializable(this, Meal, "meal")
    }
}