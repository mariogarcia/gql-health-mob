package gql.health.mob.meal

import gql.health.mob.util.I18nEnabled
import groovy.transform.Immutable

@Immutable(knownImmutableClasses = [I18nEnabled])
class MealEntry implements Serializable {
    UUID id
    String description
    Double quantity
    I18nEnabled quantityType
    I18nEnabled foodType
}