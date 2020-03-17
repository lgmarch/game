package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.logic.utils.MapElement;
import com.lmarch.rpg.game.screens.utils.Assets;

public abstract class GameCharacter implements MapElement {
    public enum State {
        IDLE, MOVE, ATTACK, PURSUIT, RETREAT
    }

    static final int WIDTH = 60;
    static final int HEIGHT = 60;

    protected GameController gc;

    protected TextureRegion[][] texture;
    protected TextureRegion textureHp;  //Показатель здоровья
    protected StringBuilder strBuilder;

    protected State state;
    protected float stateTimer;

    protected GameCharacter lastAttacker;
    protected GameCharacter target;

    protected Vector2 position;
    protected Vector2 dst; //Точка, к которой двигаемся
    protected Vector2 tmp;
    protected Vector2 tmp2;

    protected Circle area; //окружности под ногами

    protected float lifeTime;
    protected float attackTime;
    protected float walkTime;
    protected float timePerFrame;

    protected float visionRadius; //Дальность просмотра
    protected float speed;
    protected int hp, hpMax;

    protected Weapon weapon;

    public int getCellX(){
        return (int) position.x / 80;
    }

    public int getCellY(){
        return (int) (position.y - 20) / 80;
    }

    public void changePosition(float x, float y){
        position.set(x, y);
        checkBounds();
        area.setPosition(x, y - 20);
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void changePosition(Vector2 newPosition){
        changePosition(newPosition.x, newPosition.y);
    }

//    public boolean isAlive() {
//        return hp > 0;
//    }

    public GameCharacter(GameController gameController, int hpMax, float speed) {
        this.gc = gameController;
        this.textureHp = Assets.getInstance().getAtlas().findRegion("hp");
        this.strBuilder = new StringBuilder();
        this.tmp = new Vector2(0.0f, 0.0f);
        this.tmp2 = new Vector2(0.0f, 0.0f);
        this.dst = new Vector2(0.0f, 0.0f);
        this.position = new Vector2(0.0f, 0.0f);
        this.area = new Circle(0.0f, 0.0f, 15);
        this.hpMax = hpMax;
        this.hp = this.hpMax;
        this.speed = speed;
        this.state = State.IDLE;
        this.stateTimer = 1.0f;
        this.timePerFrame = 0.2f; //8 кадров меняются каждые 0.2 сек.
        this.target = null;
    }

    public int getCurrentFrameIndex() {
        // ТекущееВремя / ВремяОдного кадра % КоличествоКадров
        return (int)(walkTime / timePerFrame) % texture[0].length;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getArea() {
        return area;
    }

    public void update(float dt) {
        lifeTime += dt;

        if (state == State.ATTACK) {
            dst.set(target.getPosition());
        }
        if (state == State.MOVE || state == State.RETREAT ||
                (state == State.ATTACK && this.position.dst(target.getPosition()) > weapon.getRange() - 10)) {
            moveToDst(dt);
        }
        //Нанесение урона противнику
        if (state == State.ATTACK && this.position.dst(target.getPosition()) < weapon.getRange()) {
            attackTime += dt;
            if (attackTime > weapon.getSpeed()) {
                attackTime = 0.0f;
                if (weapon.getType() == Weapon.Type.MELEE) {
                    target.takeDamage(this, weapon.generateDamage());
                }
                if (weapon.getType() == Weapon.Type.RANGED && target != null) {
                    gc.getProjectilesController().setup(this, position.x, position.y, target.getPosition().x,
                            target.getPosition().y, weapon.generateDamage());
                }
            }
        }
    }

    public void moveToDst(float dt) {
        tmp.set(dst).sub(position).nor().scl(speed); //вектор скорости
        tmp2.set(position); //Запомнили позицию
        walkTime += dt;
        if (position.dst(dst) > speed * dt){
            changePosition(position.x + tmp.x * dt, position.y + tmp.y * dt);
        }else {
            changePosition(dst); //Добрались до dst
            state = State.IDLE;
        }
        //Обход стен
        if (!gc.getMap().isGroundPassable(getCellX(), getCellY())) {
            changePosition(tmp2.x + tmp.x * dt, tmp2.y);
            if (!gc.getMap().isGroundPassable(getCellX(), getCellY())) {
                changePosition(tmp2.x, tmp2.y + tmp.y * dt);
                if (!gc.getMap().isGroundPassable(getCellX(), getCellY())) {
                    changePosition(tmp2);
                }
            }
        }
    }

    public void checkBounds() {
        //Чтобы персонаж не убежал за экран
        if (position.x - 20 < 0.1f) {
            position.x = 20.1f;
        }
        if (position.x> Map.MAP_CELLS_WIDTH * 80 - 20) {
            position.x = Map.MAP_CELLS_WIDTH * 80 - 1 - 20;
        }
        if (position.y - 20 < 0.1f) {
            position.y = 20.1f;
        }
        if (position.y > Map.MAP_CELLS_HEIGHT * 80 - 20) {
            position.y = Map.MAP_CELLS_HEIGHT * 80 - 1 - 20;
        }
    }

    public boolean takeDamage(GameCharacter attacker, int amount) {
        lastAttacker = attacker;

        this.hp -= amount;
        if (hp <= 0) {
            onDeath();
            return true;
        }
        return false;
    }

    public void resetAttackState() {
        dst.set(position);
        state = State.IDLE;
        target = null;
    }


    public void renderHills(SpriteBatch batch, BitmapFont font) {
        strBuilder.setLength(0); //Очистка
        strBuilder.append(this.hp);
        font.draw(batch, strBuilder, this.position.x -20, this.position.y + 50);
    }

    public void onDeath() {
        for (GameCharacter gameCharacter : gc.getAllCharacters()) {
            if (gameCharacter.target == this) {
                gameCharacter.resetAttackState();
            }
        }
    }
}
