package net.forprogrammers.dziennik.util;

import java.util.List;
import javax.persistence.TypedQuery;

public class JPAUtils {
	public static <T> T getSingleResultOrNull(TypedQuery<T> query) {
	    query.setMaxResults(1);
	    List<T> list = query.getResultList();
	    if (list.isEmpty()) {
	        return null;
	    }
	    return list.get(0);
	}
}
