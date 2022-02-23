package cg.repository.impl;

import cg.model.Product;
import cg.repository.IProductRepository;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;

@Repository
public class ProductRepositoryImpl implements IProductRepository {

    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;

    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.conf.xml").buildSessionFactory();
            entityManager = sessionFactory.createEntityManager();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Product> findAll() {
        String queryStr = "SELECT p FROM Product AS p";
        TypedQuery<Product> query = entityManager.createQuery(queryStr, Product.class);
        return (ArrayList<Product>) query.getResultList();
    }

    @Override
    public Product findProductById(int id) {
        String queryStr = "SELECT p FROM Product AS p WHERE p.id = :id";
        TypedQuery<Product> query = entityManager.createQuery(queryStr, Product.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public ArrayList<Product> findProductByName(String name) {
        String queryStr = "SELECT p FROM Product AS p WHERE p.name like :name ";
        TypedQuery<Product> query = entityManager.createQuery(queryStr, Product.class);
        query.setParameter("name","%" + name + "%");
        return (ArrayList<Product>) query.getResultList();
    }

    @Override
    public Product saveProduct(Product product) {
        Transaction transaction = null;
        Product origin;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            if (product.getId() != 0) {
                origin = findProductById(product.getId());
                origin.setName(product.getName());
                origin.setPrice(product.getPrice());
                origin.setDescription(product.getDescription());
                origin.setImage(product.getImage());
            } else {
                origin = product;
            }
            session.saveOrUpdate(origin);
            transaction.commit();
            return origin;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }

    @Override
    public Product deleteProduct(int id) {
        Transaction transaction = null;
        Product origin = findProductById(id);
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            if (origin != null) {
                session.delete(origin);
            }
            transaction.commit();
            return origin;
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        return null;
    }
}
