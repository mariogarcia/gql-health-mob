package gql.health.mob.meal

import groovy.transform.Immutable

@Immutable(copyWith = true)
class Meal implements Serializable {
    UUID id
    String type
    String date
    List<MealEntry> entries
}