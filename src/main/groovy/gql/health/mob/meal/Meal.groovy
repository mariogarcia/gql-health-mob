package gql.health.mob.meal

import android.net.Uri
import groovy.transform.Immutable

@Immutable(copyWith = true, knownImmutableClasses = [Uri])
class Meal implements Serializable {
    UUID id
    String type
    String date
    Uri image
    List<MealEntry> entries
}