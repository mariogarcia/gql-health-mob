package gql.health.mob.meal

import groovy.transform.Immutable

@Immutable(copyWith = true)
class Meal implements Serializable {
    UUID id
    String type
    Date date
    String imagePath
    List<MealEntry> entries
}