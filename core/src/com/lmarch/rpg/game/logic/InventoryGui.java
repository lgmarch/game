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
    private Inventory inventory;
    private Label labelWeapon;

    public List<String> getItemList() {
        return itemList;
    }

    public Label getLabelWeapon() {
        return labelWeapon;
    }

    private final int INV_WIDTH = 450;
    private final int INV_HEIGHT = 260;

    public void switchVisible() {
        setVisible(!isVisible());
    }

    public InventoryGui(Stage stage, Skin skin, Inventory inv) {
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
        labelWeapon.setText("1 weapon");
        addActor(labelWeapon);

        stage.addActor(this);

        itemList.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getTapCount() == 2) {
                    //Номер выбранной строки
                    int index = itemList.getSelectedIndex();
                    //Выбрали строку из рюкзака, из которой надо будет сделать оружие
                    String string = inventory.getRucksack().get(index);
                    //Сохраним оружие, что в руках в стринге
                    String string1 = inventory.getOwner().weapon.copyWeaponToString();

                    //На место этой строки (в рюкзак) вставляем оружие
                    inventory.getRucksack().set(index, string1);
                    //itemList.setSelectedIndex(index);
                    //Из строки делаем оружие
                    inventory.getOwner().weapon.makeWeaponFromString(string);
                }
            }
        });

        stage.setDebugAll(true);

        this.inventory = inv;
        this.inventory.injectGui(this);
    }

    public void update() {
        itemList.setItems(inventory.getRucksackInfo());

        labelWeapon.setText(inventory.getOwner().weapon.getTitle() + " "
                + inventory.getOwner().weapon.getMinDamage() + "-" + inventory.getOwner().weapon.getMaxDamage());
    }
}
