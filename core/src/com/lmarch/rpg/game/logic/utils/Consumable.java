package com.lmarch.rpg.game.logic.utils;

import com.lmarch.rpg.game.logic.GameController;
import com.lmarch.rpg.game.logic.Hero;

public interface Consumable {
    void consume(GameController gc, Hero gameCharacter);
}
