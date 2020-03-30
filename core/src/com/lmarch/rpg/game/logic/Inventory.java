package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.utils.Array;

public class Inventory {
    private Hero owner;
    private GameController gc;
    private Array<String> rucksack;

    public Array<String> getRucksack() {
        return rucksack;
    }

    public Array<String> getRucksackInfo() {
        System.out.println("getRucksackInfo() ");

        Array<String> stringArray = new Array<>();
        for (String string : rucksack) {
            String[] tokens = string.split(",");
            stringArray.add(tokens[2].trim() + " " + tokens[3].trim() + "-" + tokens[4].trim());
        }
        return stringArray;
    }

    public Hero getOwner() {
        return owner;
    }

    public Inventory(Hero owner, GameController gc) {
        this.owner = owner;
        this.gc = gc;
        this.rucksack = new Array<>();
    }
}
