package gql.health.mob.meal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.view.ActionMode
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.arasthel.swissknife.SwissKnife
import com.arasthel.swissknife.annotations.InjectView
import com.arasthel.swissknife.annotations.OnBackground
import com.arasthel.swissknife.annotations.OnClick
import com.arasthel.swissknife.annotations.OnUIThread
import eu.davidea.flexibleadapter.FlexibleAdapter
import gql.health.mob.R
import gql.health.mob.ui.DrawableAware
import gql.health.mob.util.Activities
import gql.health.mob.ui.ImageAware
import gql.health.mob.ui.SimpleDividerItemDecoration

class MealNewActivity extends AppCompatActivity implements
        DrawableAware,
        ImageAware,
        ActionMode.Callback,
        FlexibleAdapter.OnItemLongClickListener {

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView

    @InjectView(R.id.meal_type_image)
    ImageView imageView

    @InjectView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout

    ActionMode selectActionMode

    FlexibleAdapter<MealEntryFlexibleItem> listAdapter

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_entry_list)
        SwissKnife.inject(this)

        loadAdapter()
        loadInformation()
    }

    void loadAdapter() {
        listAdapter = new FlexibleAdapter<MealEntryFlexibleItem>([], this)
        recyclerView.hasFixedSize = true
        recyclerView.setLayoutManager(new LinearLayoutManager(this))
        recyclerView.adapter = listAdapter
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this))
    }

    @Override
    void onResume() {
        super.onResume()
        loadInformation()
    }

    @OnBackground
    void loadInformation() {
        Meal refreshed = MealService.INSTANCE.findMealById(currentMeal.id)

        paintInformation(refreshed)
        paintEntries(refreshed.entries)
    }

    @OnUIThread
    void paintInformation(Meal meal) {
        collapsingToolbarLayout.title = meal.type.toLowerCase().capitalize()

        paintImageOrDrawable(
            this,
            meal.imagePath,
            imageView,
            findDrawableById(this, R.mipmap.ic_meal_default))
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
        Meal savedMeal = MealService
            .INSTANCE
            .findMealById(currentMeal.id)
            .copyWith(imagePath: imageUri.path)

        MealService.INSTANCE.updateMeal(savedMeal)

        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(takePictureIntent, IMAGE_KEY)
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

    @Override
    void onItemLongClick(int position) {
        if (!selectActionMode) {
            selectActionMode = startSupportActionMode(this)
        }

        toggleSelection(position)
    }

    private void toggleSelection(int position) {
        listAdapter.toggleSelection(position)

        int count = listAdapter.selectedItemCount

        if (count == 0) {
            selectActionMode.finish()
        } else {
            setContextTitle(count)
        }
    }

    private void setContextTitle(int count) {
        selectActionMode.title = "$count ${getString(R.string.action_mode_select_entries)}"
    }

    @Override
    boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.menuInflater.inflate(R.menu.entries_selected_mode, menu)
        listAdapter.mode = FlexibleAdapter.MODE_MULTI
        return true
    }

    @Override
    boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false
    }

    @Override
    boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.itemId) {
            case R.id.action_delete_selected:
                deleteSelectedItems()
                selectActionMode.finish()
                return true
            default:
                return false
        }
    }

    @Override
    void onDestroyActionMode(ActionMode mode) {
        listAdapter.mode = FlexibleAdapter.MODE_IDLE
        selectActionMode = null
    }

    void deleteSelectedItems() {
        Meal meal = MealService
            .INSTANCE
            .findMealById(currentMeal.id)

        List<MealEntryFlexibleItem> items = listAdapter
            .getSelectedPositions()
            .collect { Integer i -> listAdapter.getItem(i) }

        List<MealEntry> entries = meal
            .entries
            .findAll { MealEntry entry -> !(entry.id in items.id) }

        MealService.INSTANCE.updateMeal(meal.copyWith(entries: entries))
        paintEntries(entries)
    }

    @OnUIThread
    void paintEntries(List<MealEntry> entries) {
        def adaptedEntries =
                entries.collect(this.&convert) as List<MealEntryFlexibleItem>

        listAdapter.clear()

        if (adaptedEntries) {
            listAdapter.addItems(0, adaptedEntries)
            listAdapter.notifyDataSetChanged()
        }
    }

    MealEntryFlexibleItem convert(MealEntry entry) {
        return new MealEntryFlexibleItem(
            id: entry.id,
            description: entry.description,
            quantity: entry.quantity,
            quantityType: entry.quantityType,
            foodType: entry.foodType
        )
    }
}