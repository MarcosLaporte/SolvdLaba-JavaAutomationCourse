import entities.Customer;
import entitiesDAO.GenericDAO;
import services.MainMenu;
import services.ReflectionService;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static services.ReflectionService.ClassExclusionPredicate.ANNOTATION;

public class Main {
    public static void main(String[] args) {
        MainMenu.run();
    }

    private static void testDbConnection() {
        List<Class<?>> classList = ReflectionService.getClassesInPackage("entities", ANNOTATION);
        List<? extends GenericDAO<?>> daoList = classList.stream().map(GenericDAO::new).toList();
        for (GenericDAO<?> dao : daoList) {
            System.out.println("\n--------------" + dao.getTableName() + "--------------");
            for (Object entity : dao.getAll()) {
                System.out.println(entity);
            }
        }

        for (GenericDAO<?> dao : daoList) {
            dao.close();
        }
    }

}
