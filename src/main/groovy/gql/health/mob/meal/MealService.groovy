package gql.health.mob.meal

class MealService {

    static final MealService INSTANCE = new MealService()

    private static final List<Meal> meals = []

    List<Meal> listAll() {
        return meals
    }

    Meal addMeal(Meal meal) {
        meals << meal
        return meal
    }

    Meal removeMeal(UUID uuid) {
        Meal meal = findMealById(uuid)

        meals.remove(meal)
        return meal
    }

    Meal findMealById(UUID uuid) {
        meals.find { Meal meal -> meal.id == uuid }
    }

    Meal addEntry(UUID mealId, MealEntry mealEntry) {
        Meal meal = findMealById(mealId)
        Meal copiedMeal = meal.copyWith(entries: [mealEntry] + meal.entries)

        removeMeal(copiedMeal.id)
        addMeal(copiedMeal)

        copiedMeal
    }

    Meal updateMeal(Meal meal) {
        removeMeal(meal.id)

        return addMeal(meal)
    }

    Meal removeEntryById(UUID mealId, UUID entryId) {
        Meal meal = findMealById(mealId)
        MealEntry entry = meal
                .entries
                .find { MealEntry entry -> entry.id == entryId }

        meal.entries.remove(entry)

        return meal
    }
}