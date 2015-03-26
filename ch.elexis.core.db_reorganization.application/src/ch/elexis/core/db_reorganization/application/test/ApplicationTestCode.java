/*******************************************************************************
 * Copyright (c) 2013 MEDEVIT <office@medevit.at>.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     MEDEVIT <office@medevit.at> - initial API and implementation
 ******************************************************************************/
package ch.elexis.core.db_reorganization.application.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class contains a the static method {@link #performApplicationTest()}, which is being called
 * after startup and login. It may contain test code to be executed. Elexis is closed after this
 * method returns.
 */
public class ApplicationTestCode {

	static Logger log = LoggerFactory.getLogger(ApplicationTestCode.class.getName());

	public static void performApplicationTest(){

		Db_extinfo_updater.copy_extinfo();

		// meaningless eventloop, to avoid exit
		for (int i = 0; i < 1; i++) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
