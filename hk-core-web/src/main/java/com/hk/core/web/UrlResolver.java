package com.hk.core.web;

import javax.servlet.ServletContext;

import com.hk.commons.util.AssertUtils;

/**
 *
 */
public abstract class UrlResolver {

	/**
	 *
	 * @param ctx
	 * @param url
	 * @return
	 */
	public static String resolveUrl(ServletContext ctx, String url) {
		AssertUtils.notNull(ctx, "ctx must not be null.");
		AssertUtils.notNull(url, "url must not be null.");
		if (url.startsWith("/")) {
			return String.format("%s%s", ctx.getContextPath(), url);
		}
		return url;
	}

}
