/**
 * 
 */
package com.hk.core.query.jpa;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import com.hk.commons.util.StringUtils;

/**
 * @author huangkai
 * @date 2017年12月20日下午12:52:03
 */
public class PathUtils {

	public static <X> Path<X> getPath(Root<X> root, String propertyName) {
		Path<X> path;
		if (StringUtils.contains(propertyName, ".")) {
			String[] names = StringUtils.split(propertyName, ",");
			path = root.get(names[0]);
			for (String name : names) {
				path = path.get(name);
			}
		} else {
			path = root.get(propertyName);
		}
		return path;

	}
}
