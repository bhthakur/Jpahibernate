package modal;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestBidirectionalManyToMany {

	Logger log = Logger.getLogger(this.getClass().getName());

	private EntityManagerFactory emf;

	@Before
	public void init() {
		emf = Persistence.createEntityManagerFactory("my-persistence-unit");
	}

	@After
	public void close() {
		emf.close();
	}

	@Test
	public void testManytoMany(){
		log.info("Testing manay to many" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Book book= new Book();
		book.setTitle("Rishi Test");
		
		Author author= new Author();
		author.setFirstName("Rishi");
		author.setLastName("Thakur");
		
		book.getAuthors().add(author);
		author.getBooks().add(book);
		
		em.persist(book);
		em.persist(author);
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void testManytoManyUsinghelperMethods(){
		log.info("Testing manay to many" );
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		
		Book book= new Book();
		book.setTitle("Rishi Test");
		
		Author author= new Author();
		author.setFirstName("Rishi");
		author.setLastName("Thakur");
		
		// book.getAuthors().add(author);
		// author.getBooks().add(book);
	
		book.addAuthor(author);
		
		em.persist(book);
		em.persist(author);
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void bidirectionalManyToMany() {
		log.info("... bidirectionalManyToMany ...");

		// Add a new Review
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Book b = em.find(Book.class, 1L);
		
		Author a = new Author();
		a.setFirstName("Thorben");
		a.setLastName("Janssen");
		
		a.getBooks().add(b);
		b.getAuthors().add(a);
		
		em.persist(a);
		
		em.getTransaction().commit();
		em.close();
		
		// Get Book entity with Authors
		em = emf.createEntityManager();
		em.getTransaction().begin();

		b = em.find(Book.class, 1L);
		
		List<Author> authors = b.getAuthors();
		Assert.assertTrue(authors.get(0).getBooks().contains(b));
		Assert.assertTrue(authors.contains(a));
		
		em.getTransaction().commit();
		em.close();
	}
	
	@Test
	public void bidirectionalManyToManyWithHelperMethod() {
		log.info("... bidirectionalManyToManyWithHelperMethod ...");

		// Add a new Review
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Book book = em.find(Book.class, 1L);
		
		Author author = new Author();
		author.setFirstName("Thorben");
		author.setLastName("Janssen");
		
		author.addBook(book);
		
		em.persist(author);
		
		em.getTransaction().commit();
		em.close();
		
		// Get Book entity with Authors
		em = emf.createEntityManager();
		em.getTransaction().begin();

		book = em.find(Book.class, 1L);
		
		List<Author> authors = book.getAuthors();
		Assert.assertTrue(authors.get(0).getBooks().contains(book));
		Assert.assertTrue(authors.contains(author));
		
		em.getTransaction().commit();
		em.close();
	}
}
