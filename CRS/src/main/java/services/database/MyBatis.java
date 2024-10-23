package services.database;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyBatis<T> implements IDao<T> {
    private final SqlSessionFactory sessionFactory;
    public final Class<T> clazz;

    public MyBatis(Class<T> clazz) throws IOException {
        Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
        this.sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        this.clazz = clazz;
    }

    @Override
    public List<T> get(Map<String, Object> columnCondition) {
        try (SqlSession session = sessionFactory.openSession()) {
            return session.selectList(clazz.getName() + ".get", columnCondition);
        }
    }

    @Override
    public int create(T t) {
        try (SqlSession session = sessionFactory.openSession()) {
            int rows = session.insert(clazz.getName() + ".create", t);
            session.commit();
            return rows;
        }
    }

    @Override
    public int update(Map<String, Object> newValues, Map<String, Object> columnCondition) {
        try (SqlSession session = sessionFactory.openSession()) {
            Map<String, Map<String, Object>> paramsMap = new HashMap<>();
            paramsMap.put("values", newValues);
            paramsMap.put("filters", columnCondition);

            int rows = session.update(clazz.getName() + ".update", paramsMap);
            session.commit();
            return rows;
        }
    }

    @Override
    public int delete(Map<String, Object> columnCondition) {
        try (SqlSession session = sessionFactory.openSession()) {
            int rows = session.delete(clazz.getName() + ".delete", columnCondition);
            session.commit();
            return rows;
        }
    }
}
