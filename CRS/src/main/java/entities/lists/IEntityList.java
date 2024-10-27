package entities.lists;

import entities.Entity;

import java.util.List;

public interface IEntityList<T extends Entity> {
    List<T> getList();
    void setList(List<T> list);
}
