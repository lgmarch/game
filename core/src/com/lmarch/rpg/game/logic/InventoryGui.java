package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.lmarch.rpg.game.screens.utils.Assets;

public class InventoryGui extends Group {
    private com.badlogic.gdx.scenes.scene2d.ui.List<String> itemList;
    private Hero hero;
    private Label labelWeapon;

    private final int INV_WIDTH = 450;
    private final int INV_HEIGHT = 260;

    public void switchVisible() {
        setVisible(!isVisible());
    }

    public InventoryGui(Stage stage, Skin skin, Hero played) {
        this.hero = played;

        BitmapFont font12 = Assets.getInstance().getAssetManager().get("fonts/font12.ttf");
        BitmapFont font20 = Assets.getInstance().getAssetManager().get("fonts/font20.ttf");

        setBounds(20, 120, INV_WIDTH, INV_HEIGHT);
        setVisible(false);

        //Background
        Image background = new Image(skin.getDrawable("whiteRect"));
        background.setColor(0.0f, 0.0f, 0.2f, 0.8f);
        background.setSize(INV_WIDTH, INV_HEIGHT);
        addActor(background);

        //Item List - рюкзак
        itemList = new List<>(new List.ListStyle(font12, Color.BLACK, Color.WHITE, skin.getDrawable("shortButton")));
//        ScrollPane scrollPane = new ScrollPane(itemList, new ScrollPane.ScrollPaneStyle(null,
//                skin.getDrawable("emptyPoint"), skin.getDrawable("emptyPoint"), skin.getDrawable("emptyPoint"), skin.getDrawable("emptyPoint")));
        ScrollPane scrollPane = new ScrollPane(itemList, new ScrollPane.ScrollPaneStyle());
        scrollPane.setTransform(true);
        scrollPane.setColor(0.0f, 0.0f, 0.2f, 0.8f);
        scrollPane.setBounds(10, 10, 200, 200);
        addActor(scrollPane);

        //Title
        Label invTitle = new Label("Inventory", new Label.LabelStyle(font20, Color.WHITE));
        invTitle.setPosition(10, 220);
        addActor(invTitle);

        //Hero
        Label invTitleHero = new Label("Equipment", new Label.LabelStyle(font20, Color.WHITE));
        invTitleHero.setPosition(220, 220);
        addActor(invTitleHero);

        //Weapon в действии
        labelWeapon = new Label("", new Label.LabelStyle(font12, Color.WHITE));
        labelWeapon.setWrap(true);
        labelWeapon.setPosition(220, 190);
        //labelWeapon.setText("1 weapon");
        addActor(labelWeapon);

        stage.addActor(this);

        itemList.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getTapCount() == 2) {
                    //Номер выбранной строки
                    int index = itemList.getSelectedIndex();
                    if (index >= 0) {
                        //Выбрали строку из рюкзака, из которой надо будет сделать оружие
                        String string = hero.getInventory().getRucksack().get(index);
                        //Сохраним оружие, что в руках в стринге
                        String string1 = hero.weapon.copyWeaponToString();

                        //На место выбранной строки (в рюкзак) вставляем оружие
                        hero.getInventory().getRucksack().set(index, string1);
                        //Из строки делаем оружие и отдаем Игроку
                        hero.weapon.makeWeaponFromString(string);
                    }
                }
            }
        });
        stage.setDebugAll(true);
    }

    public void update(Hero commander) {
        this.hero = commander;
        itemList.setItems(hero.getInventory().getRucksackInfo());

        labelWeapon.setText(hero.weapon.getTitle() + " "
                + hero.weapon.getMinDamage() + "-" + hero.weapon.getMaxDamage());


    }
}
