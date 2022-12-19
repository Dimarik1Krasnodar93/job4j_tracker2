package ru.job4j.tracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.tracker.model.Item;

import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HbmTracker implements Store, AutoCloseable {

    private SessionFactory sf;

    public HbmTracker() {

        init();
    }

    private void init() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        sf = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public Item add(Item item) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(item);
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        }
        session.close();
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        Session session = sf.openSession();
        Query query = session.createQuery("update item set name = :fName where id = :fId");
        query.setParameter("fName", item.getName());
        query.setParameter("fId", item.getId());
        int countUpdated = query.executeUpdate();
        session.close();
        return countUpdated > 0;
    }

    @Override
    public boolean delete(int id) {
        boolean rsl = false;
        Session session = sf.openSession();
        Query query = session.createQuery("delete Item where id = :fId");
        query.setParameter("fId", id);
        int count = query.executeUpdate();
        session.close();
        return count > 0;
    }

    @Override
    public List<Item> findAll() {
        List<Item> result;
        Session session = sf.openSession();
        Query query = session.createQuery("select from item");
        result = query.list();
        return result;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> rsl = new ArrayList<>();
        Session session = sf.openSession();
        Query query = session.createQuery("select from item i where i.name = :fName");
        query.setParameter("fName", key);
        rsl = query.list();
        return rsl;
    }

    @Override
    public Item findById(int id) {
        Item result;
        Session session = sf.openSession();
        Query query = session.createQuery("select from item i where i.id = :fId");
        query.setParameter("fId", id);
        result =  (Item) query.uniqueResultOptional().get();
        session.close();
        return result;
    }

}
