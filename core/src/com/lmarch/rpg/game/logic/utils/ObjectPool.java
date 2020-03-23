package com.lmarch.rpg.game.logic.utils;

import java.util.ArrayList;
import java.util.List;

public abstract class ObjectPool <T extends Poolable> {
    protected List<T> activeList;
    protected List<T> freeList;

    public ObjectPool() {
        this.activeList = new ArrayList<>();
        this.freeList = new ArrayList<>();
    }

    public  List<T> getActiveList(){
        return activeList;
    }

    public List<T> getFreeList() {
        return freeList;
    }

    //Обход ограничения дженерика
    protected abstract T newObject();

    public void free(int index) {
        freeList.add(activeList.remove(index));
    }

    public T getActiveElement(){
        if (freeList.size() == 0){
            freeList.add(newObject());
        }
        //Пыталась достать тип дженерика (с помощью черной магии...)
//        T temp = freeList.remove(freeList.size() - 1);
//        ParameterizedType t = (ParameterizedType)temp.getClass().getGenericSuperclass();
//        Class<?> cls = (Class<?>) t.getActualTypeArguments()[0];
//        System.out.println("*********** " + cls);

        T temp = freeList.remove(freeList.size() - 1);
        activeList.add(temp);

        return temp;
    }

    public void checkPool() {
        for (int i = activeList.size()-1; i >= 0 ; i--) {
            if (!activeList.get(i).isActive()){
                free(i);
            }
        }
    }
}
