package labaFarm.farm;

@FunctionalInterface
public interface IActionManager<T, U> {
    String performAction(T entity, U action);
}
