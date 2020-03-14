package com.lmarch.rpg.game.logic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.lmarch.rpg.game.screens.utils.Assets;

public abstract class GameCharacter implements MapElement {

    public enum State {
        IDLE, MOVE, ATTACK, PURSUIT, RETREAT
    }

    public enum Type {
        MELEE, RANGED
    }

    protected GameController gc;
    protected WeaponsController wc;

    protected TextureRegion texture;
    protected TextureRegion textureHp;  //картинка здоровья
    protected TextureRegion textureWeapon; //рисунок оружия

    protected Type type;
    protected State state;
    protected float stateTimer;

    protected WeaponAction weaponAction; //Ссылка на интерфейс
    protected float attackRadius; //Радиус атаки оружия
    protected float attackTime; //Период атаки
    protected float damage; //Урон, наносимый оружием

    protected GameCharacter lastAttacker; //последний атакующий
    protected GameCharacter target;

    protected Vector2 position;
    protected Vector2 dst; //Точка, к которой двигаемся
    protected Vector2 tmp;
    protected Vector2 tmp2;

    protected Circle area; //окружности под ногами

    protected float lifeTime;
    protected float visionRadius; //Дальность просмотра
    protected float speed;
    protected int hp, hpMax;

    public int getCellX(){
        return (int) position.x / 80;
    }

    public int getCellY(){
        return (int) (position.y - 20) / 80;
    }

    public void changePosition(float x, float y){
        position.set(x, y);
        area.setPosition(x, y - 20);
    }

    public void changePosition(Vector2 newPosition){
        changePosition(newPosition.x, newPosition.y);
    }

    public GameCharacter(GameController gameController, WeaponsController weaponsController, int hpMax, float speed) {
        this.gc = gameController;
        this.wc = weaponsController;
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

    public void initWeapon(WeaponAction wc){
        //Создаем оружие
        if (wc.getTypeWeapon() == Weapons.TypeWeapon.ARROW) {
            this.weaponAction = new ArrowAction();
        }
        if (wc.getTypeWeapon() == Weapons.TypeWeapon.SWORD) {
            this.weaponAction = new SwordAction();
        }
        if (wc.getTypeWeapon() == Weapons.TypeWeapon.AXE) {
            this.weaponAction = new AxeAction();
        }
        setWeaponProperty(wc);
    }

    public void changeWeapon(Treasure treasure) {
        this.weaponAction.setActive(false); //Старое оружие в пул...
        this.weaponAction = treasure.getWeaponActions(); //Ссылке новое оружие
        setWeaponProperty(this.weaponAction);
    }

    public void setWeaponProperty(WeaponAction wc){
        //Устанавливаем его свойства
        this.attackTime = weaponAction.getAttackTime();
        this.attackRadius = weaponAction.getAttackRadius();
        this.damage = weaponAction.getDamage();
        //установка типа поведения Объекта по типу оружия
        if (weaponAction.getTypeWeapon() == Weapons.TypeWeapon.ARROW) {
            this.type = Type.RANGED;}
        else {this.type = Type.MELEE;}
        //Выбираем текстуру для прорисовки оружия
        this.textureWeapon = weaponAction.getTextureWeapon();
        this.weaponAction.setActive(true);
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
                (state == State.ATTACK && this.position.dst(target.getPosition()) > attackRadius - 5)) {
            moveToDst(dt);
        }
        //Нанесение урона противнику
        if (state == State.ATTACK && this.position.dst(target.getPosition()) < attackRadius) {
            attackTime -= dt;
            if (attackTime <= 0.0f) {
                attackTime = weaponAction.getAttackTime();
                if (type == Type.MELEE) {
                    target.takeDamage(this);
                }
                if (type == Type.RANGED) {
                    gc.getProjectilesController().setup(this, position.x, position.y, target.getPosition().x, target.getPosition().y);
                }
            }
        }
        area.setPosition(position.x, position.y - 20);

        //changeWeapons();
        checkBounds();
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

    public void checkBounds() {
        //Чтобы персонаж не убежал за экран
        if (position.x < 0.1f) {
            position.x = 0.1f;
        }
        if (position.x > 1279.0f) {
            position.x = 1279.0f;
        }
        if (position.y < 0.1f) {
            position.y = 0.1f;
        }
        if (position.y > 720.0f) {
            position.y = 720.0f;
        }
    }

    public boolean takeDamage(GameCharacter attacker) {
        lastAttacker = attacker;

        this.hp -= attacker.damage;
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

    public void onDeath() {
        for (GameCharacter gameCharacter : gc.getAllCharacters()) {
            if (gameCharacter.target == this) {
                gameCharacter.resetAttackState();
            }
        }
    }
}
