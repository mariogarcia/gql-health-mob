package gql.health.mob.meal

import groovy.transform.Immutable

@Immutable
class MealEntry implements Serializable {
    UUID id
    String description
    Double quantity
    String unit
}