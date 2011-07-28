package com.jdkcn.openexplorer;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:rory.cn@gmail.com">Rory</a>
 * @version $Id$
 */
public class BrowserDetechTest {

	public static void main(String[] args) throws Exception {
		Process process = Runtime.getRuntime().exec("which nautilus");
		String nautilusPath = IOUtils.toString(process.getInputStream());
		nautilusPath = StringUtils.remove(StringUtils.trim(nautilusPath), "\n");
		System.out.println("nautilus path:[" + nautilusPath + "]");
		
		process = Runtime.getRuntime().exec("which pcmanfm");
		String pcmanfmPath = IOUtils.toString(process.getInputStream());
		pcmanfmPath = StringUtils.remove(StringUtils.trim(pcmanfmPath), "\n");
		System.out.println("pcmanfm path:[" + pcmanfmPath + "]");
		
		process = Runtime.getRuntime().exec("which thunar");
		String thunarPath = IOUtils.toString(process.getInputStream());
		thunarPath = StringUtils.remove(StringUtils.trim(thunarPath), "\n");
		System.out.println("thunar path:[" + thunarPath + "]");
	}
}
