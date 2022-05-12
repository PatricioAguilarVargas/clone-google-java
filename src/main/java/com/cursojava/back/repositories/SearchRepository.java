package com.cursojava.back.repositories;

import java.util.List;

import com.cursojava.back.entities.WebPage;

public interface SearchRepository {
	
	List<WebPage> search(String tesxtSesarch);
	
	void save(WebPage webPage);
	
	boolean exist(String link);

	WebPage getByUrl(String link);
	
	List<WebPage> getLinkToIndex();
	
}
