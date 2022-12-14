package ru.job4j.cars.model.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ru.job4j.cars.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {

    public static final String QUERY_UPDATE = "UPDATE User SET login = :fLogin, password = :fPassword WHERE id = :fId";
    public static final String QUERY_DELETE = "DELETE User WHERE id = :fId";
    public static final String QUERY_FIND_BY_ORDER_ID = "SELECT u FROM User As u ORDER BY u.id";
    public static final String QUERY_FIND_BY_ID = "SELECT u FROM User As u WHERE u.id = :fId";
    public static final String QUERY_FIND_LIKE_LOGIN = "SELECT u FROM User AS u WHERE u.login LIKE :fKey";
    public static final String QUERY_FIND_BY_LOGIN = "SELECT u FROM User AS u WHERE u.login = :login";

    private final SessionFactory sf;

    /**
     * Сохранить в базе.
     * @param user пользователь.
     * @return пользователь с id.
     */
    public User create(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return user;
    }

    /**
     * Обновить в базе пользователя.
     * @param user пользователь.
     */
    public void update(User user) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(QUERY_UPDATE, User.class)
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
            session.createQuery(QUERY_DELETE, User.class)
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
        List<User> result = new ArrayList<>();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(QUERY_FIND_BY_ORDER_ID, User.class);
            result = query.list();
            session.getTransaction().rollback();
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return result;
    }

    /**
     * Найти пользователя по ID
     * @return пользователь.
     */
    public Optional<User> findById(int id) {
        Optional<User> result = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(QUERY_FIND_BY_ID, User.class)
                    .setParameter("fId", id);
            result = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        }
        session.close();
        return result;
    }

    /**
     * Список пользователей по login LIKE %key%
     * @param key key
     * @return список пользователей.
     */
    public List<User> findByLikeLogin(String key) {
        List<User> result = null;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            Query<User> query = session.createQuery(QUERY_FIND_LIKE_LOGIN, User.class)
                    .setParameter("fKey", '%' + key + '%');
            result = query.list();
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        }
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
            session.beginTransaction();
            Query<User> query = session.createQuery(QUERY_FIND_BY_LOGIN, User.class)
                    .setParameter("login", login);
            result = query.uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            session.getTransaction().rollback();
        }
        session.close();
        return result;
    }
}
