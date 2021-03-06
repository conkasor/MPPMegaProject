package Domain;

import repository.ValidationException;

public interface Validator<E> {
    void validate (E entity) throws ValidationException;
}
