package com.tessera.intercept.login.cookie;

import java.util.*;

import javax.servlet.http.*;

import com.tessera.dispatch.*;
import com.tessera.intercept.*;
import com.tessera.intercept.login.*;

/**
 * 
 * @author crawford
 *
 */

public class SetBCookieInterceptor
	extends SetCookieInterceptorSupport<BCookieGenerator>
{
//	private static final Logger logger = Logger.getLogger (SetBCookieInterceptor.class); 
	
	public
	SetBCookieInterceptor (final Map<String, String> props)
	{
		super (props); 
		return; 
	}

	protected
	String getCookie (CookieManager cm) 
	{
		return cm.getBCookieName (); 
	}

	public
	Alteration intercept (final HttpServletRequest req, final HttpServletResponse res, final DispatchContext dc)
	    throws Exception
	{
		// Retrieve their login from the login manager
		
		final LoginManager<?> lm = LoginManagerUtil.getLoginManager (req); 
		if (! lm.isLoggedIn (req)) { 
			logger.warn ("Not logged in."); 
			return NO_ALTERATION; 
		}
		
		// Otherwise, use the generator to generate a new B cookie and set it on the response object
		
		final LoginSession lsess = lm.getLoginSession (req); 
		final CookieManager cm = getCookieManager (req); 
		final String value = cm.generateBCookie (req, lsess);
		
		if (value != null) { 
			final String name = getCookie (cm);
			final Cookie cookie = new Cookie (name, value); 
			cookie.setMaxAge (maxAge); 
			final String domain = cm.getDomain (); 
			if (domain != null) { 
				cookie.setDomain (domain); 
			}		
			final String path = cm.getPath (); 
			if (path != null) { 
				cookie.setPath (path);
			}
			res.addCookie (cookie); 
		}
		return NO_ALTERATION; 
	}
	
//	public static
//	void setBCookie (final HttpServletRequest req, final HttpServletResponse res) 
//	{ 
//		//TODO: Move the above code into something that can be shared. 
//		return; 
//	}
}

// EOF