package com.cursojava.back.repositories;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.cursojava.back.entities.WebPage;

@Repository
public class SearchRepositoryImpl implements SearchRepository{

	@PersistenceContext
	EntityManager entityManager;
	
	@Transactional
	@Override
	public List<WebPage> search(String textSearch) {
		
		String query = "FROM WebPage WHERE description LIKE :textSearch";
		return entityManager.createQuery(query).setParameter("textSearch", "%" + textSearch + "%" ).getResultList();
	}
	
	@Transactional
	@Override
	public void save(WebPage webPage) {
		entityManager.merge(webPage);
	}

	@Transactional
	@Override
	public WebPage getByUrl(String url) {
		String query = "FROM WebPage WHERE url = :url";
		List<WebPage> list = entityManager.createQuery(query).setParameter("url", url).getResultList();
		return list.size() == 0 ? null : list.get(0);
	}

	@Override
	public boolean exist(String link) {
		// TODO Auto-generated method stub
		return getByUrl(link) != null;
	}

	@Override
	public List<WebPage> getLinkToIndex() {
		String query = "FROM WebPage WHERE title is null AND description is null";
		return entityManager.createQuery(query).setMaxResults(100).getResultList();
	}

}
