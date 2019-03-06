package repository;

import Domain.HasID;
import Domain.Validator;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRepo<ID, T extends HasID<ID>> implements IRepository<ID,T> {
     private Map<ID, T> entities;
     private Validator<T> validator;

    public void AbstractRepository(Validator<T> valid ){
        entities=new HashMap<>();
        validator=valid;
    }
    @Override
    public int size() {
        return entities.size();
    }

    @Override
    public void update(ID id, T entity) {
        try {
            validator.validate(entity);
        }catch(ValidationException ex){
            System.err.println("Entitatea " +entity+ "nu este valida");
            throw  ex;
        }

        if (!(entities.get(id)==null)) {
            if (!id.equals(entity.getID()))
                if (entities.get(entity.getID())!=null)
                    throw new RepositoryException("Id "+entity.getID()+" already exists!!");
            entities.put(entity.getID(), entity);
            System.out.println("Entitate modificata " + entity);
        }else
            throw new RepositoryException("Id "+id +" does not  exists");
    }

    @Override
    public void save(T entity) {
        try {
            validator.validate(entity);
        }catch(ValidationException ex){
            System.err.println("Entitatea " +entity+ "nu este valida");
            throw  ex;
        }

        // setEntityId(entity);
        ID id=entity.getID();
        if (entities.get(id)==null) {
            entities.put(id, entity);
        }else
            throw new RepositoryException("Id already exists"+id);

    }

    @Override
    public void delete(ID id) {
        entities.remove(id);
        System.out.println("Entity deleted "+id);
    }

    @Override
    public T findOne(ID id) {
        T res=entities.get(id);
        if (res!=null)
            return res;
        throw new RepositoryException("Id not found "+id);
    }

    @Override
    public Iterable<T> findAll() {
        return entities.values();
    }

    //protected abstract void setEntityId(T entity);
}

