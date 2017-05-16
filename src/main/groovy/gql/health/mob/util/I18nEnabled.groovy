package gql.health.mob.util

import groovy.transform.Immutable

@Immutable
class I18nEnabled implements Serializable {
    int description
    int drawableImage
    String symbol
}