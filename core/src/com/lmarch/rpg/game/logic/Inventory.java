package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.utils.Array;

public class Inventory {
    private Hero owner;
    private GameController gc;
    private InventoryGui guiInventory;
    private Array<String> rucksack;

    public Array<String> getRucksack() {
        return rucksack;
    }

    public Array<String> getRucksackInfo() {
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

    public InventoryGui getGuiInventory() {
        return guiInventory;
    }

    public void injectGui(InventoryGui guiInventory) {
        this.guiInventory = guiInventory;
//        final InventoryGui inGui = this.guiInventory;
//        this.guiInventory.getItemList().addListener(new ClickListener() {
//                @Override
//                public void clicked(InputEvent event, float x, float y) {
//                    if (getTapCount() == 2) {
//                        int index = inGui.getItemList().getSelectedIndex();
//
//                        String string = inGui.getItemList().getSelected();
//                        inGui.addActorAfter(inGui.getLabelWeapon(), new Label(string, new Label.LabelStyle(font12, Color.WHITE)));
//                        //Weapon weaponFromPlayer = gc.getHero().switchWeapon(weapons.get(index));
//                        //weapons.set(index, weaponFromPlayer);
//                        //inGui.update();
//                        //guiInventory.getItemList().getSelected().
//                        System.out.println("88888888 Clicked index: " + index + " " + string);
//                    }
//                }
//            });
        guiInventory.update();
    }

    public Inventory(Hero owner, GameController gc) {
        this.owner = owner;
        this.gc = gc;
        this.rucksack = new Array<>();
    }

    public void update() {
        getGuiInventory().update();
    }
}
