package gql.health.mob.meal

import groovy.transform.TupleConstructor

@TupleConstructor
enum FoodType {

    MEAT_FISH('Meat/fish/eggs/beans'),
    FRUIT_AND_VEGETABLES('Fruit and vegetables'),
    BREAD_RICE('Bread/rice/potatoes/pasta'),
    MILK('Milk and dairy food'),
    FAT_AND_SUGAR('Foods and drinks high in fat and/or sugar'),
    HEALTHY_DRINKS('Drinks low in fat and sugar')

    String description

    String toString() {
        description
    }
}