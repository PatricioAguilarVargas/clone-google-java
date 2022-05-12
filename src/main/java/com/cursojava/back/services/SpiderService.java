package com.cursojava.back.services;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hibernate.internal.util.StringHelper.isBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cursojava.back.entities.WebPage;

@Service
public class SpiderService {
	@Autowired
	private SearchService searchService;

	/*OBTIENE LOS LINKS QUE ESTEN SIN INDEXAR
	 * la descripcion y titulo en null
	 * */
	public void indexWebPages() {
		List<WebPage> linksToIndex = searchService.getLinksToIndex();
		linksToIndex.stream().parallel().forEach(webPage -> {
			try {
				System.out.println("Indexing");
				indexWebPage(webPage);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		});
	}

	/* PROCESA CADA LINK SIN INDEXAR, BUSCA EL TITULO Y LA DESCRICION Y LO GUARDA */
	private void indexWebPage(WebPage webPage) throws Exception {
		String url = webPage.getUrl();
		System.out.println(url);
		String content = getWebContent(url);
		if (isBlank(content)) {
			return;
		}

		indexAndSaveWebPage(webPage, content);

		System.out.println("Domain: " + getDomain(url));
		saveLinks(getDomain(url), content);
	}

	/* OBTIENE LA URL PRINCIPLA*/
	private String getDomain(String url) {
		String[] aux = url.split("/");
		return aux[0] + "//" + aux[2];
	}

	/*BUSCA LOS LINK NUEVOS Y LOS GUARDA SIN TITULO Y DESCRIPCION*/
	private void saveLinks(String domain, String content) {
		System.out.println("Save Links");
		List<String> links = getLinks(domain, content);
		System.out.println("Links: " + links);
		links.stream()
			.filter(link -> {
				System.out.println("link: " + link);
				return !searchService.exist(link);
			})
			.map(link -> {
				System.out.println("link: " + link);
				return new WebPage(link);
			})
			.forEach(webPage -> {
				System.out.println("WebPage: " + webPage);
				searchService.save(webPage);
			});

	}

	
	/*OBTIENE LOS LINK DE LOS SITIOS BUSCADOS*/
	public List<String> getLinks(String domain, String content) {
		List<String> links = new ArrayList<>();

		String[] splitHref = content.split("href=\"");
		List<String> listHref = Arrays.asList(splitHref);

		listHref.forEach(strHref -> {
			String[] aux = strHref.split("\"");
			links.add(aux[0]);
		});
		return cleanLinks(domain, links);
	}

	/*SACA LOS LINK QUE NO SEAN PAGINAS WEB*/
	private List<String> cleanLinks(String domain, List<String> links) {
		String[] excludedExtensions = new String[] { "css", "js", "json", "jpg", "png", "woff2", "ico"  };

		List<String> resultLinks = links.stream()
				.filter(link -> Arrays.stream(excludedExtensions).noneMatch(link::endsWith))
				.map(link -> link.startsWith("/") ? domain + link : link).filter(link -> link.startsWith("http"))
				.collect(Collectors.toList());

		List<String> uniqueLinks = new ArrayList<>();
		uniqueLinks.addAll(new HashSet<>(resultLinks));

		return uniqueLinks;
	}

	/*BUSCA LOS LINKS QUE NO TENGAN TITULO Y DESCRIPCION
	 * SE LOS ASIGNA Y GUARDA EN LA BASE DE DATOS*/
	private void indexAndSaveWebPage(WebPage webPage, String content) {
		String title = getTitle(content);
		String description = getDescription(content);

		webPage.setDescription(description);
		webPage.setTitle(title);

		System.out.println("Save: " + webPage);
		searchService.save(webPage);
	}

	/*OBTIENE EL TITULO*/
	public String getTitle(String content) {
		String[] aux = content.split("<title>");
		String[] aux2 = aux[1].split("</title>");
		return aux2[0];
	}

	/*OBTIENE LA DESCRIPCION*/
	public String getDescription(String content) {
		String[] aux = content.split("<meta name=\"description\" content=\"");
		String[] aux2 = aux[1].split("/>");
		return aux2[0];
	}

	/*OBTIENE EL CONTENIDO DE UNA PAGINA WEB SEGUN SU LINK*/
	private String getWebContent(String link) {
		try {
			URL url = new URL(link);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			String encoding = conn.getContentEncoding();

			InputStream input = conn.getInputStream();

			Stream<String> lines = new BufferedReader(new InputStreamReader(input)).lines();

			return lines.collect(Collectors.joining());
		} catch (IOException e) {
			System.out.print(e.getMessage());
		}
		return "";
	}
}
