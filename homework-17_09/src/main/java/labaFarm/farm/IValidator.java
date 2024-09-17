package labaFarm.farm;

@FunctionalInterface
public interface IValidator<T> {
    boolean validate(T t);
}
