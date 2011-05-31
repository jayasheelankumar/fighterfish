/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 2011 Oracle and/or its affiliates. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 * 
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package org.glassfish.fighterfish.sample.osgihttp.helloworld;

import javax.servlet.ServletException;

import org.osgi.service.component.ComponentContext;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

/**
 * This class is an SCR component. It registers servlets and resources in activate() method
 * and unregisters them in deactivate() method. It consumes a service of type HttpService. The 
 * service reference is bound and unbound in setHttp() and unsetHttp() method respectively as
 * specified in scr.xml file.
 * 
 * @author sanjeeb.sahoo@oracle.com
 *
 */
public class Main {
	private HttpService http; // Set and unset by setHttp() and unsetHttp() methods that are called by SCR. See scr.xml

	protected void activate(ComponentContext ctx) throws ServletException, NamespaceException {
		// Create an HttpContext and use it for all web resource registration so that they all
		// share the same ServletContext. Note: one HttpContext maps o one ServletContext.
		HttpContext httpCtx = http.createDefaultHttpContext();
		http.registerServlet("/hello", new HelloWorldServlet(), null, httpCtx);
		// add another instance of the servlet as /hello2 to demonstrate that ServletContext is indeed
		// shared by all the servlet instances.
		http.registerServlet("/hello2", new HelloWorldServlet(), null, httpCtx);
		// Map index.jsp to foo.jsp
		http.registerResources("/index.jsp", "/foo.jsp", httpCtx);
	}

	protected void deactivate(ComponentContext ctx) {
		try {
			http.unregister("/hello");
			http.unregister("/hello2");
			http.unregister("/index.jsp");
		} catch (Exception e) {
			// This can happen if the HttpService has been undpeloyed in which case as part of its undepoyment,
			// it would have unregistered all aliases. So, we should protect against such a case.
			System.out.println(e);
		}
	}
	
	protected void setHttp(HttpService http) {
		this.http = http;
	}
	
	protected void unsetHttp(HttpService http) {
		this.http = null;
	}
}
