package drinkshop.validator;

public interface Validator<T> {
    void validate(T entity);
}
