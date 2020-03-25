package com.lmarch.rpg.game.logic.utils;

import com.lmarch.rpg.game.logic.GameCharacter;
import com.lmarch.rpg.game.logic.GameController;

public interface Consumable {
    void consume(GameController gc, GameCharacter gameCharacter);
}
