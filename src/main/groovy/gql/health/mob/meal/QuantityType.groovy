package gql.health.mob.meal

import groovy.transform.TupleConstructor

@TupleConstructor
enum QuantityType {

    GRAMS('grams', 'g'),
    LITER('liter', 'l'),
    MILLILITER('milliliter', 'ml'),
    UNITS('units', 'u')

    String description
    String symbol

    String toString() {
        "$description ($symbol)"
    }
}