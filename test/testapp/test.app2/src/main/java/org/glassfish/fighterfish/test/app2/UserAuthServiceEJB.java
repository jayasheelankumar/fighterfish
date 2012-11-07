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


package org.glassfish.fighterfish.test.app2;

import javax.annotation.PostConstruct;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class UserAuthServiceEJB
 */
@Stateless
@LocalBean
public class UserAuthServiceEJB 
{

    @PersistenceContext
    EntityManager em;

    @PostConstruct
    public void postConstruct() {
        System.out.println("UserAuthServiceEJB.postConstruct");
    }

    public boolean login(String name, String password)
    {
        System.out.println("UserAuthServiceEJBuser: logging in " + name);
        UserCredential uc = em.find(UserCredential.class, name);
        return (uc != null && uc.isMatchingPassword(password));
    }

    public boolean register(String name, String password)
    {
        System.out.println("UserAuthServiceEJB: registering " + name);
        UserCredential uc = new UserCredential(name, password);
        try {
         em.persist(uc);
        } catch (RuntimeException e) {
         org.glassfish.fighterfish.test.util.JStack jstack = new org.glassfish.fighterfish.test.util.JStack();
         jstack.printStackTrace();
         System.out.println(jstack);
         throw  e;
        }
        return true;
    }
}
