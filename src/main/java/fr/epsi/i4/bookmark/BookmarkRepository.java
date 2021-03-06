package fr.epsi.i4.bookmark;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class BookmarkRepository {

	private final Map<String, Bookmark> map = new LinkedHashMap<String, Bookmark>() {
		private static final long serialVersionUID = -4842188442391736903L;

		protected boolean removeEldestEntry(Map.Entry<String,Bookmark> eldest) {
			return this.size() > 100;
		};
	};
	
	public BookmarkRepository() {
		populate();
	}

	public synchronized boolean add(String id, Bookmark bookmark) throws InvalidBookmarkException {
		bookmark.validate();
		bookmark.setLastModification(new Date());
		return map.put(id, bookmark) == null;
	}

	public synchronized String getLatestId() {
		String[] keys = map.keySet().toArray(new String[map.size()]);
		return keys.length == 0 ? null : keys[keys.length -1];
	}

	public synchronized Bookmark get(String id) {
		return map.get(id);
	}

	public synchronized void delete(String id) {
		map.remove(id);
	}

	public synchronized List<Map.Entry<String, Bookmark>> getBookmarkEntries(int startIndex, int itemCount) {
		return map.entrySet().stream().skip(startIndex).limit(itemCount).collect(Collectors.toList());
	}

	private void populate() {
		List<Bookmark> bookmarks = new ArrayList<>();
		bookmarks.add(new Bookmark("HTTP RFC7230", "Hypertext Transfer Protocol (HTTP/1.1): Message Syntax and Routing", "http://tools.ietf.org/html/rfc7230"));
		bookmarks.add(new Bookmark("HTTP RFC7231", "Hypertext Transfer Protocol (HTTP/1.1): Semantics and Content", "http://tools.ietf.org/html/rfc7231"));
		bookmarks.add(new Bookmark("HTTP RFC7232", "Hypertext Transfer Protocol (HTTP/1.1): Conditional Requests", "http://tools.ietf.org/html/rfc7232"));
		bookmarks.add(new Bookmark("HTTP RFC7234", "Hypertext Transfer Protocol (HTTP/1.1): Caching", "http://tools.ietf.org/html/rfc7234"));
		bookmarks.add(new Bookmark("HTTP RFC7235", "Hypertext Transfer Protocol (HTTP/1.1): Authentication", "http://tools.ietf.org/html/rfc7235"));

		bookmarks.add(new Bookmark("REST cookbook", "quelques articles intéressants sur REST", "http://restcookbook.com/"));
		bookmarks.add(new Bookmark("Richardson Maturity Model", "évaluer le niveau RESTful d'une API", "http://martinfowler.com/articles/richardsonMaturityModel.html"));
		bookmarks.add(new Bookmark("freegeoip", "Un exemple de web service RESTful", "http://freegeoip.net/"));
		bookmarks.add(new Bookmark("OpenBeer", "Un exemple de web service RESTful", "http://openbeerdatabase.com/"));
		bookmarks.add(new Bookmark("API WoW", "Un exemple de web service RESTful", "http://blizzard.github.io/api-wow-docs/"));
		bookmarks.add(new Bookmark("Annuaire Web services RESTful", "Pour trouvez des web services publics", "http://www.programmableweb.com/apis/directory"));
		bookmarks.add(new Bookmark("Architectural Styles and the Design of Network-based Software Architectures", "La thèse de doctorat de Roy Fielding", "http://www.ics.uci.edu/~fielding/pubs/dissertation/top.htm"));
		bookmarks.add(new Bookmark("Document Jersey", "La documentation du framework Jersey pour implémenter des services Web", "https://jersey.java.net/documentation/latest/"));
		bookmarks.add(new Bookmark("How to get a cup of coffee", "Starbuck en RESTful", "http://www.infoq.com/articles/webber-rest-workflow"));
		bookmarks.add(new Bookmark("RESTful Web APIs", "Un lien vers le livre de Jim Webber, Savas Parastatidis et Ian Robinson, publié chez O'Reilly Media en 2010", "http://shop.oreilly.com/product/0636920028468.do"));

		try {
			for (Bookmark bookmark : bookmarks) {
				add(UUID.randomUUID().toString(), bookmark);
			}
		} catch (InvalidBookmarkException e) {
		}
	}

}
