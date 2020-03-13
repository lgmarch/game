package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.screens.utils.Assets;

public abstract class GameCharacter implements MapElement {
    public enum State {
        IDLE, MOVE, ATTACK, PURSUIT, RETREAT
    }

    protected State state;
    protected float stateTimer;
    protected GameCharacter target;

    protected GameController gc;

    protected TextureRegion texture;
    protected TextureRegion textureHp;  //Показатель здоровья

    protected Vector2 position;
    protected Vector2 dst; //Точка, к которой двигаемся
    protected Vector2 tmp;
    protected Vector2 tmp2;

    protected Circle area; //окружности под ногами
    protected float visionRadius; //Дальность просмотра
    private float attackTime;

    protected float lifeTime;
    protected float speed;
    protected int hp, hpMax;

    public int getCellX(){
        return (int) position.x / 80;
    }

    public int getCellY(){
        return (int) (position.y - 20) / 80;
    }

    public GameCharacter(GameController gameController, int hpMax, float speed) {
        this.gc = gameController;
        this.textureHp = Assets.getInstance().getAtlas().findRegion("hp");
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
        this.target = null;
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
        if (state == State.MOVE || (state == State.ATTACK && this.position.dst(target.getPosition()) > 35)) {
            moveToDst(dt);
        }
        //Нанесение урона противнику
        if (state == State.ATTACK && this.position.dst(target.getPosition()) < 40){
            attackTime += dt;
            if (attackTime > 0.3f) {
                attackTime = 0.0f;
                target.takeDamage(1);
            }
        }
        area.setPosition(position.x, position.y - 20);
    }

    public void moveToDst(float dt) {
        tmp.set(dst).sub(position).nor().scl(speed); //вектор скорости
        tmp2.set(position); //Запомнили позицию
        if (position.dst(dst) > speed * dt){
            position.mulAdd(tmp, dt);
        }else {
            position.set(dst); //Добрались до dst
            state = State.IDLE;
        }
        //Обход стен
        if (!gc.getMap().isGroundPassable(getCellX(), getCellY())) {
            position.set(tmp2);
            position.add(tmp.x * dt, 0);
            if (!gc.getMap().isGroundPassable(getCellX(), getCellY())) {
                position.set(tmp2);
                position.add(0, tmp.y * dt);
                if (!gc.getMap().isGroundPassable(getCellX(), getCellY())) {
                    position.set(tmp2);
                }
            }
        }
    }

    public boolean takeDamage(int amount) {
        this.hp -= amount;
        if (hp <= 0) {
            onDeath();
            return true;
        }
        return false;
    }

    public abstract void onDeath();

    public void changePosition(float x, float y){
        position.set(x, y);
        area.setPosition(x, y - 20);
    }

    public void changePosition(Vector2 newPosition){
        changePosition(newPosition.x, newPosition.y);
    }
}
