package ru.job4j.cars.model.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;
    private final Session session;


    public UserRepository() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        session = sf.openSession();
    }

    public UserRepository(SessionFactory sf) {
        this.sf = sf;
        session = sf.openSession();
    }

    /**
     * В сессию вынесли в поля объекта для оптимизации
     * закрываем сессию перед удалением объекта
     */
    public void finalize() {
        session.close();
    }


    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        User result = new User((int)session.save(user), user.getLogin(), user.getPassword());
        return result;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        try {
            session.beginTransaction();
            session.createQuery("Update User set login = :fLogin, password = :fPassword WHERE id = :fId", User.class)
                    .setParameter("fLogin", user.getLogin())
                    .setParameter("password", user.getPassword())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        try {
            session.beginTransaction();
            session.createQuery("DELETE User Where id = :fId", User.class)
                    .setParameter("fId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        Query query = session.createQuery("from User order by id", User.class);
        return query.list();
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int id) {
        Query query = session.createQuery("from User where id = :fId", User.class)
                .setParameter("fId", id);
        List<User> listuser = query.list();
        if (listuser.size() > 0) {
            return Optional.of(listuser.get(0));
        }
        return Optional.empty();
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        Query query = session.createQuery("from User where name Like :fKey", User.class)
                .setParameter("fKey", '%' + key + '%');
        return query.list();
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Query query = session.createQuery("from User where name = :login", User.class)
                .setParameter("login", login);
        List<User> listUser = query.list();
        if (listUser.size() > 0) {
            return Optional.of(listUser.get(0));
        }
        return Optional.empty();
    }
}
