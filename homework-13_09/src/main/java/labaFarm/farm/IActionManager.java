package labaFarm.farm;

public interface IActionManager<T, U> {
    String performAction(T entity, U action);
}
