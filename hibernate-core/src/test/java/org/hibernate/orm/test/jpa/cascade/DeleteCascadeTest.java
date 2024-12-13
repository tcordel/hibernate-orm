package org.hibernate.orm.test.jpa.cascade;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.testing.orm.junit.DomainModel;
import org.hibernate.testing.orm.junit.SessionFactory;
import org.hibernate.testing.orm.junit.SessionFactoryScope;
import org.junit.jupiter.api.Test;

import java.util.List;

@DomainModel(
	annotatedClasses = {
		DeleteCascadeTest.A.class,
		DeleteCascadeTest.B.class,
		DeleteCascadeTest.C.class,
	}
)
@SessionFactory
class DeleteCascadeTest {

	@Entity
	public static class A {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}
	}

	@Entity
	public static class B {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;
		@ManyToOne
		@JoinColumn(name = "a_id", nullable = false)
		@OnDelete(action = OnDeleteAction.CASCADE)
		private A a;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public A getA() {
			return a;
		}

		public void setA(A a) {
			this.a = a;
		}

	}

	@Entity
	public static class C {
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;
		@ManyToOne
		@JoinColumn(name = "b_id", nullable = false)
		@OnDelete(action = OnDeleteAction.CASCADE)
		private B b;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}

	}


	@Test
	void testRemove(SessionFactoryScope scope) {
		scope.inTransaction(
			session -> {
				A a = new A();
				session.persist(a);

				B bb = new B();
				bb.setA(a);
				session.persist(bb);

				C c = new C();
				c.setB(bb);
				session.persist(c);
			});
		scope.inTransaction(
			session -> {

				HibernateCriteriaBuilder cb = session.getCriteriaBuilder();
				JpaCriteriaQuery<C> cq = cb.createQuery(C.class);
				cq.from(C.class);
				JpaCriteriaQuery<B> bq = cb.createQuery(B.class);
				bq.from(B.class);

				// This is how spring-data-jpa manage delete* Named queries
				session.createQuery(cq).getResultList();
				List<B> bs = session.createQuery(bq).getResultList();
				for (B b1 : bs) {
					session.remove(b1);
				}
			});
	}
}

