package gql.health.mob.meal

import gql.health.mob.R
import gql.health.mob.ui.I18nEnabled

class AuxiliarTypes {

    static I18nEnabled[] FOOD_TYPES = [
        [description: R.string.food_type_meat, symbol: 'meat'],
        [description: R.string.food_type_fruit, symbol: 'fruit'],
        [description: R.string.food_type_bread, symbol: 'bread'],
        [description: R.string.food_type_milk, symbol: 'milk'],
        [description: R.string.food_type_fat, symbol: 'fat'],
        [description: R.string.food_type_healthy, symbol: 'healthy'],
    ] as I18nEnabled[]

    static I18nEnabled[] QUANTITY_TYPES = [
        [description: R.string.quantity_type_grams, symbol: "g"],
        [description: R.string.quantity_type_liter, symbol: "l"],
        [description: R.string.quantity_type_milliliter, symbol: "ml"],
        [description: R.string.quantity_unit_, symbol: "u"],
    ] as I18nEnabled[]
}