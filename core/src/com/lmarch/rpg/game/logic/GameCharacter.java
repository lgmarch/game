package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.logic.utils.MapElement;
import com.lmarch.rpg.game.screens.utils.Assets;

public abstract class GameCharacter implements MapElement {
    public enum State {
        IDLE, MOVE, ATTACK, PURSUIT, RETREAT
    }

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
    protected float damageTimer;

    protected float visionRadius; //Дальность просмотра
    protected float speed;
    protected int hp, hpMax;

    protected Weapon weapon;
    protected Treasure treasure;

    public int getCellX(){
        return (int) position.x / Map.CELL_WIDTH;
    }

    public int getCellY(){
        return (int) (position.y) / Map.CELL_HEIGHT;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public float getY() {
        return position.y;
    }

    public void changePosition(float x, float y){
        position.set(x, y);
        checkBounds();
        area.setPosition(x, y);
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void changePosition(Vector2 newPosition){
        changePosition(newPosition.x, newPosition.y);
    }

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
        damageTimer -= dt;
        if (damageTimer < 0.0f) {
            damageTimer = 0.0f;
        }

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
                    tmp.set(target.position).sub(position);
                    gc.getEffectsController().setupSwordSwing(position.x, position.y, tmp.angle());
                    target.takeDamage(this, weapon.generateDamage());
                }
                if (weapon.getType() == Weapon.Type.RANGED && target != null) {
                    gc.getProjectilesController().setup(this, position.x, position.y, target.getPosition().x,
                            target.getPosition().y, weapon.generateDamage());
                }
            }
        }
        slideFromWall(dt);
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
            position.x = Map.MAP_CELLS_WIDTH * 80 - 20;
        }
        if (position.y - 20 < 0.1f) {
            position.y = 20.1f;
        }
        if (position.y > Map.MAP_CELLS_HEIGHT * 80) {
            position.y = Map.MAP_CELLS_HEIGHT * 80;
        }
    }

    public boolean takeDamage(GameCharacter attacker, int amount) {
        lastAttacker = attacker;
        this.hp -= amount;
        damageTimer += 0.4f; //увел. на 0.4 сек.

        if (damageTimer > 1.0f) {
            damageTimer = 1.0f;
        }
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

    public void slideFromWall(float dt) {
        if (!gc.getMap().isGroundPassable(position)) {
            tmp.set(position).sub(getCellX() * Map.CELL_WIDTH + Map.CELL_WIDTH / 2,
                    getCellY() * Map.CELL_HEIGHT + Map.CELL_HEIGHT / 2).nor().scl(60.0f);
            changePosition(position.x + tmp.x * dt, position.y + tmp.y * dt);
        }
    }

    public void onDeath() {
        for (GameCharacter gameCharacter : gc.getAllCharacters()) {
            if (gameCharacter.target == this) {
                gameCharacter.resetAttackState();
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, BitmapFont font) {
        //batch.setColor(1, 0 , 0, 1);
        TextureRegion currentRegion = texture[0][getCurrentFrameIndex()];
        if (dst.x > position.x) {
            if (currentRegion.isFlipX()) {
                currentRegion.flip(true, false);
            }
        }else {
            if (!currentRegion.isFlipX()) {
                currentRegion.flip(true, false);
            }
        }

        batch.setColor(1.0f, 1.0f - damageTimer, 1.0f - damageTimer, 1.0f);
        batch.draw(currentRegion, position.x - 30, position.y - 15, 30, 30,
                60, 60, 1.0f, 1.0f, 0);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        //Полоска жизни
        batch.setColor(0.2f, 0.2f, 0.2f, 1.0f);
        batch.draw(textureHp, position.x - 20, position.y + 45, 50, 8);
        float n = (float) hp / hpMax;
        float shock = damageTimer * 5.0f;
        batch.setColor(1.0f - n, n, 0.0f, 1.0f);
        batch.draw(textureHp, position.x - 20 + MathUtils.random(-shock, shock), position.y + 45 + MathUtils.random(-shock, shock), 50 * ((float) hp / hpMax), 8);

        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(batch, String.valueOf(hp), position.x - 15 + MathUtils.random(-shock, shock), position.y + 55 + MathUtils.random(-shock, shock), 10, 1, false);

        batch.draw(weapon.getTexture(), position.x + 10,position.y + 35, 30, 30);

//        batch.draw(treasure.getTexture(), position.x, position.y + 35, 30, 30);
    }
}
