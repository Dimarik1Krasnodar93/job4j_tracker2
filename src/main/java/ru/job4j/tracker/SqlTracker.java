package ru.job4j.tracker;

import ru.job4j.tracker.model.Item;

import javax.swing.plaf.nimbus.State;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlTracker implements Store, AutoCloseable {

    private Connection cn;

    public SqlTracker() {
        init();
    }

    public SqlTracker(Connection cn) {
        this.cn = cn;
    }

    public void init() {
        try (InputStream in = SqlTracker.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            cn = DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if (cn != null) {
            cn.close();
        }
    }

    @Override
    public Item add(Item item) {
        try (PreparedStatement pst = cn.prepareStatement("insert into items "
                + "(name, created) values (?, ?)");
             Statement st = cn.createStatement();
        ) {
            pst.setString(1, item.getName());
            pst.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
            pst.execute();
            ResultSet res = st.executeQuery("select id from items order by "
                    + "id desc limit 1");
            while (res.next()) {
                item.setId(res.getInt("id"));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return item;
    }

    @Override
    public boolean replace(int id, Item item) {
        boolean rsl = false;
        try (PreparedStatement pst = cn.prepareStatement("update items set id = ? "
                + "created = ? where name = ?")) {
            pst.setInt(1, id);
            pst.setTimestamp(2, Timestamp.valueOf(item.getCreated()));
            pst.setString(3, item.getName());
            rsl = pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rsl;
    }

    @Override
    public boolean delete(int id) {
        boolean rsl = false;
        try (PreparedStatement pst = cn.prepareStatement("delete from items where id = ?")) {
            pst.setInt(1, id);
            rsl = pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rsl;
    }

    @Override
    public List<Item> findAll() {
        List<Item> rsl = new ArrayList<>();
        try (Statement pst = cn.createStatement()) {
            ResultSet rslSet = pst.executeQuery("select * from items");
            while (rslSet.next()) {
                rsl.add(new Item(rslSet.getString(2)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rsl;
    }

    @Override
    public List<Item> findByName(String key) {
        List<Item> rsl = new ArrayList<>();
        try (PreparedStatement pst = cn.prepareStatement("select * from items where name = ?")) {
            pst.setString(1, key);
            ResultSet rslSet = pst.executeQuery();
            while (rslSet.next()) {
                rsl.add(new Item(rslSet.getString(2)));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rsl;
    }

    @Override
    public Item findById(int id) {
        Item rsl = null;
        try (PreparedStatement pst = cn.prepareStatement("select * from items where id = ?")) {
            pst.setInt(1, id);
            ResultSet rslSet = pst.executeQuery();
            if (rslSet.next()) {
                rsl = new Item(rslSet.getString(2));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return rsl;
    }
}
