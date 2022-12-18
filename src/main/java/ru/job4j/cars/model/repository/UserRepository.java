package ru.job4j.cars.model.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {
    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        Session session = sf.openSession();
        User result = null;
        try {
            session.beginTransaction();
            result = new User((int) session.save(user), user.getLogin(), user.getPassword());
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return result;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sf.openSession();
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
        session.close();
    }

    /**
     * Удалить пользователя по id.
     * @param userId ID
     */
    public void delete(int userId) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE User Where id = :fId", User.class)
                    .setParameter("fId", userId)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
    }

    /**
     * Список пользователь отсортированных по id.
     * @return список пользователей.
     */
    public List<User> findAllOrderById() {
        List<User> result;
        Session session = sf.openSession();
        Query query = session.createQuery("from User order by id", User.class);
        result = query.list();
        session.close();
        return result;
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int id) {
        Optional<User> result;
        Session session = sf.openSession();
        Query query = session.createQuery("from User where id = :fId", User.class)
                .setParameter("fId", id);
        result = query.uniqueResultOptional();
        session.close();
        return result;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        List<User> result;
        Session session = sf.openSession();
        Query query = session.createQuery("from User where login Like :fKey", User.class)
                .setParameter("fKey", '%' + key + '%');
        result = query.list();
        return result;
    }

    /**
     * Найти пользователя по login.
     * @param login login.
     * @return Optional or user.
     */
    public Optional<User> findByLogin(String login) {
        Optional<User> result = Optional.empty();
        Session session = sf.openSession();
        try {
            Query query = session.createQuery("from User where login = :login", User.class)
                    .setParameter("login", login);
            result = query.uniqueResultOptional();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        session.close();
        return result;
    }
}
