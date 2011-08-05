package com.jdkcn.openexplorer;

import openexplorer.util.IOUtils;

/**
 * @author <a href="mailto:rory.cn@gmail.com">Rory</a>
 * @version $Id$
 */
public class BrowserDetachTest {

    public static void main(String[] args) throws Exception {
        Process process = Runtime.getRuntime().exec("which nautilus");
        String nautilusPath = IOUtils.toString(process.getInputStream());
        System.out.println("nautilus path:[" + nautilusPath + "]");

        process = Runtime.getRuntime().exec("which pcmanfm");
        String pcmanfmPath = IOUtils.toString(process.getInputStream());
        System.out.println("pcmanfm path:[" + pcmanfmPath + "]");

        process = Runtime.getRuntime().exec("which thunar");
        String thunarPath = IOUtils.toString(process.getInputStream());
        System.out.println("thunar path:[" + thunarPath + "]");
    }

}
