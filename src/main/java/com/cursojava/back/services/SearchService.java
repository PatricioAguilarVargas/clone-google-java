package com.cursojava.back.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursojava.back.entities.WebPage;
import com.cursojava.back.repositories.SearchRepository;

@Service
public class SearchService {
	
	@Autowired
	private SearchRepository searchRepository;
	
	public List<WebPage> search(String textSearch) {
		return searchRepository.search(textSearch);
	}
	
	public void save(WebPage webPage) {
		searchRepository.save(webPage);
	}
	
	public boolean exist(String link) {
		return searchRepository.exist(link);
	}
	
	public List<WebPage> getLinksToIndex() {
		return searchRepository.getLinkToIndex();
	}
	
}
