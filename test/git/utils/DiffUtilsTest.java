/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.utils;

import git.common.DiffUtils;
import git.patch.ChangeUnitType;
import git.patch.DiffLine;
import git.patch.FeatureType;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author leonardo
 */
public class DiffUtilsTest {

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void addsFeatureTest1() {
        DiffLine diffLine1 = new DiffLine("+menuconfig DRM");
        assertTrue(DiffUtils.addsFeature(diffLine1, "DRM"));
        DiffLine diffLine2 = new DiffLine("+	menuconfig DEFAULT_AS");
        assertTrue(DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void addsFeatureTest2() {
        DiffLine diffLine1 = new DiffLine("-menuconfig DRM");
        assertTrue(!DiffUtils.addsFeature(diffLine1, "DRM"));
        DiffLine diffLine2 = new DiffLine("-	menuconfig DEFAULT_AS");
        assertTrue(!DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void addsFeatureTest3() {
        DiffLine diffLine1 = new DiffLine("menuconfig DRM");
        assertTrue(!DiffUtils.addsFeature(diffLine1, "DRM"));
        DiffLine diffLine2 = new DiffLine("	menuconfig DEFAULT_AS");
        assertTrue(!DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void addsFeatureTest4() {
        DiffLine diffLine1 = new DiffLine("+menu \"This is a prompt message\"");
        assertTrue(DiffUtils.addsFeature(diffLine1, "This is a prompt message"));
        DiffLine diffLine2 = new DiffLine("+	menu \"DEFAULT_AS\"");
        assertTrue(DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void addsFeatureTest5() {
        DiffLine diffLine1 = new DiffLine("-menu \"This is a prompt message\"");
        assertTrue(!DiffUtils.addsFeature(diffLine1, "This is a prompt message"));
        DiffLine diffLine2 = new DiffLine("-	menu DEFAULT_AS");
        assertTrue(!DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void addsFeatureTest6() {
        DiffLine diffLine1 = new DiffLine("menu \"This is a prompt message\"");
        assertTrue(!DiffUtils.addsFeature(diffLine1, "This is a prompt message"));
        DiffLine diffLine2 = new DiffLine("	menuconfig DEFAULT_AS");
        assertTrue(!DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void addsFeatureTest7() {
        DiffLine diffLine1 = new DiffLine("+config DRM");
        assertTrue(DiffUtils.addsFeature(diffLine1, "DRM"));
        DiffLine diffLine2 = new DiffLine("+	config DEFAULT_AS");
        assertTrue(DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void addsFeatureTest8() {
        DiffLine diffLine1 = new DiffLine("-config DRM");
        assertTrue(!DiffUtils.addsFeature(diffLine1, "DRM"));
        DiffLine diffLine2 = new DiffLine("-	config DEFAULT_AS");
        assertTrue(!DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void addsFeatureTest9() {
        DiffLine diffLine1 = new DiffLine("config DRM");
        assertTrue(!DiffUtils.addsFeature(diffLine1, "DRM"));
        DiffLine diffLine2 = new DiffLine("	config DEFAULT_AS");
        assertTrue(!DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void addsFeatureTest10() {
        DiffLine diffLine1 = new DiffLine("+choice DRM");
        assertTrue(DiffUtils.addsFeature(diffLine1, "DRM"));
        DiffLine diffLine2 = new DiffLine("+	choice DEFAULT_AS");
        assertTrue(DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void addsFeatureTest11() {
        DiffLine diffLine1 = new DiffLine("-choice DRM");
        assertTrue(!DiffUtils.addsFeature(diffLine1, "DRM"));
        DiffLine diffLine2 = new DiffLine("-	choice DEFAULT_AS");
        assertTrue(!DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void addsFeatureTest12() {
        DiffLine diffLine1 = new DiffLine("choice DRM");
        assertTrue(!DiffUtils.addsFeature(diffLine1, "DRM"));
        DiffLine diffLine2 = new DiffLine("	choice DEFAULT_AS");
        assertTrue(!DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void addsFeatureTest13() {
        DiffLine diffLine1 = new DiffLine("+mainmenu \"This is a prompt message\"");
        assertTrue(DiffUtils.addsFeature(diffLine1, "This is a prompt message"));
        DiffLine diffLine2 = new DiffLine("+	mainmenu \"DEFAULT_AS\"");
        assertTrue(DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void addsFeatureTest14() {
        DiffLine diffLine1 = new DiffLine("-mainmenu \"This is a prompt message\"");
        assertTrue(!DiffUtils.addsFeature(diffLine1, "This is a prompt message"));
        DiffLine diffLine2 = new DiffLine("-	mainmenu \"DEFAULT_AS\"");
        assertTrue(!DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void addsFeatureTest15() {
        DiffLine diffLine1 = new DiffLine("mainmenu \"This is a prompt message\"");
        assertTrue(!DiffUtils.addsFeature(diffLine1, "This is a prompt message"));
        DiffLine diffLine2 = new DiffLine("+	mainmenu DEFAULT_AS");
        assertTrue(!DiffUtils.addsFeature(diffLine2, "DEFAULT_AS"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void addsFeatureTest16() {
        DiffLine diffLine = new DiffLine("+option env=\"KERNELVERSION\"");
        assertTrue(DiffUtils.addsFeature(diffLine, "env"));
    }

    @Test
    public void addsFeatureTest17() {
        DiffLine diffLine = new DiffLine("-option env=\"KERNELVERSION\"");
        assertTrue(!DiffUtils.addsFeature(diffLine, "env"));
    }

    @Test
    public void addsFeatureTest18() {
        DiffLine diffLine = new DiffLine("option env=\"KERNELVERSION\"");
        assertTrue(!DiffUtils.addsFeature(diffLine, "env"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void removesFeatureTest25() {
        DiffLine diffLine1 = new DiffLine("-menuconfig DRM");
        assertTrue(DiffUtils.removesFeature(diffLine1, "DRM"));
        DiffLine diffLine2 = new DiffLine("-	menuconfig DEFAULT_AS");
        assertTrue(DiffUtils.removesFeature(diffLine2, "DEFAULT_AS"));

    }

    @Test
    public void removesFeatureTest26() {
        DiffLine diffLine1 = new DiffLine("+menuconfig DRM");
        assertTrue(!DiffUtils.removesFeature(diffLine1, "DRM"));
        DiffLine diffLine2 = new DiffLine("+	menuconfig DEFAULT_AS");
        assertTrue(!DiffUtils.removesFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void removesFeatureTest27() {
        DiffLine diffLine1 = new DiffLine("menuconfig DRM");
        assertTrue(!DiffUtils.removesFeature(diffLine1));
        DiffLine diffLine2 = new DiffLine("	menuconfig DEFAULT_AS");
        assertTrue(!DiffUtils.removesFeature(diffLine2, "DEFAULT_AS"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void addsFeatureTes28() {
        DiffLine diffLine1 = new DiffLine("-menu \"This is a prompt message\"");
        assertTrue(DiffUtils.removesFeature(diffLine1, "This is a prompt message"));
        DiffLine diffLine2 = new DiffLine("-	menu \"DEFAULT_AS\"");
        assertTrue(DiffUtils.removesFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void removesFeatureTest29() {
        DiffLine diffLine1 = new DiffLine("+menu \"This is a prompt message\"");
        assertTrue(!DiffUtils.removesFeature(diffLine1, "This is a prompt message"));
        DiffLine diffLine2 = new DiffLine("+	menu \"DEFAULT_AS\"");
        assertTrue(!DiffUtils.removesFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void removesFeatureTest30() {
        DiffLine diffLine1 = new DiffLine("menu \"This is a prompt message\"");
        assertTrue(!DiffUtils.removesFeature(diffLine1, "This is a prompt message"));
        DiffLine diffLine2 = new DiffLine("	menu \"DEFAULT_AS\"");
        assertTrue(!DiffUtils.removesFeature(diffLine2, "DEFAULT_AS"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void removesFeatureTest31() {
        DiffLine diffLine1 = new DiffLine("-config DRM");
        assertTrue(DiffUtils.removesFeature(diffLine1));
        DiffLine diffLine2 = new DiffLine("-	config DEFAULT_AS");
        assertTrue(DiffUtils.removesFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void removesFeatureTest32() {
        DiffLine diffLine = new DiffLine("+config DRM");
        assertTrue(!DiffUtils.removesFeature(diffLine));
        DiffLine diffLine2 = new DiffLine("+	config DEFAULT_AS");
        assertTrue(!DiffUtils.removesFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void removesFeatureTest33() {
        DiffLine diffLine1 = new DiffLine("config DRM");
        assertTrue(!DiffUtils.removesFeature(diffLine1));
        DiffLine diffLine2 = new DiffLine("	config DEFAULT_AS");
        assertTrue(!DiffUtils.removesFeature(diffLine2, "DEFAULT_AS"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void removesFeatureTest34() {
        DiffLine diffLine1 = new DiffLine("-choice DRM");
        assertTrue(DiffUtils.removesFeature(diffLine1));
        DiffLine diffLine2 = new DiffLine("-	choice DEFAULT_AS");
        assertTrue(DiffUtils.removesFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void removesFeatureTest35() {
        DiffLine diffLine1 = new DiffLine("+choice DRM");
        assertTrue(!DiffUtils.removesFeature(diffLine1));
        DiffLine diffLine2 = new DiffLine("	choice DEFAULT_AS");
        assertTrue(!DiffUtils.removesFeature(diffLine2, "DEFAULT_AS"));
    }

    @Test
    public void removesFeatureTest36() {
        DiffLine diffLine = new DiffLine("choice DRM");
        assertTrue(!DiffUtils.removesFeature(diffLine));
        DiffLine diffLine2 = new DiffLine("	choice DEFAULT_AS");
        assertTrue(!DiffUtils.removesFeature(diffLine2, "DEFAULT_AS"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void removesFeatureTest40() {
        DiffLine diffLine = new DiffLine("-mainmenu \"This is a prompt message\"");
        assertTrue(DiffUtils.removesFeature(diffLine, "This is a prompt message"));
    }

    @Test
    public void removesFeatureTest41() {
        DiffLine diffLine = new DiffLine("+mainmenu \"This is a prompt message\"");
        assertTrue(!DiffUtils.removesFeature(diffLine, "This is a prompt message"));
    }

    @Test
    public void removesFeatureTest42() {
        DiffLine diffLine = new DiffLine("mainmenu \"This is a prompt message\"");
        assertTrue(!DiffUtils.removesFeature(diffLine, "This is a prompt message"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void removesFeatureTest43() {
        DiffLine diffLine = new DiffLine("-option env=\"KERNELVERSION\"");
        assertTrue(DiffUtils.removesFeature(diffLine, "env"));
    }

    @Test
    public void removesFeatureTest44() {
        DiffLine diffLine = new DiffLine("+option env=\"KERNELVERSION\"");
        assertTrue(!DiffUtils.removesFeature(diffLine, "env"));
    }

    @Test
    public void removesFeatureTest45() {
        DiffLine diffLine = new DiffLine("option env=\"KERNELVERSION\"");
        assertTrue(!DiffUtils.removesFeature(diffLine, "env"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    /*
     * @Test public void removesFeatureTest46() { DiffLine diffLine = new
     * DiffLine("-prompt \"This is a prompt message\"") ;
     * assertTrue(DiffUtils.removesFeature(diffLine, "This is a prompt
     * message")) ; }      *
     * @Test public void removesFeatureTest47() { DiffLine diffLine = new
     * DiffLine("+prompt \"This is a prompt message\"") ; assertTrue(!
     * DiffUtils.removesFeature(diffLine, "This is a prompt message")) ; }      *
     * @Test public void removesFeatureTest48() { DiffLine diffLine = new
     * DiffLine("prompt \"This is a prompt message\"") ; assertTrue(!
     * DiffUtils.removesFeature(diffLine, "This is a prompt message")) ; }
     */
    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void getChangeUnitTypeTest1() {
        assertTrue(DiffUtils.getChangeUnitType(new DiffLine("+ This line adds something")).equals(ChangeUnitType.ADDED));
    }

    @Test
    public void getChangeUnitTypeTest2() {
        assertTrue(DiffUtils.getChangeUnitType(new DiffLine("- This line adds something")).equals(ChangeUnitType.DELETED));
    }

    @Test
    public void getChangeUnitTypeTest3() {
        boolean failed = false;
        try {
            DiffUtils.getChangeUnitType(new DiffLine(" This line adds something?!")).equals(ChangeUnitType.DELETED);
        } catch (Exception e) {
            failed = true;
        }
        assertTrue(failed);
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void getFeatureNameTest1() {
        DiffLine diffLine = new DiffLine("+menuconfig DRM");
        assertTrue(DiffUtils.getFeatureName(diffLine).equals("DRM"));
    }

    @Test
    public void getFeatureNameTest2() {
        DiffLine diffLine = new DiffLine("-menuconfig DRM");
        assertTrue(DiffUtils.getFeatureName(diffLine).equals("DRM"));
    }


    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void getFeatureNameTest3() {
        DiffLine diffLine = new DiffLine("+config DRM");
        assertTrue(DiffUtils.getFeatureName(diffLine).equals("DRM"));
    }

    @Test
    public void getFeatureNameTest4() {
        DiffLine diffLine = new DiffLine("-config DRM");
        assertTrue(DiffUtils.getFeatureName(diffLine).equals("DRM"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    /*
     * @Test public void getFeatureNameTest5() { DiffLine diffLine = new
     * DiffLine("+prompt \"This is a prompt message\"") ;
     * assertTrue(DiffUtils.getFeatureName(diffLine).equals("This is a prompt
     * message")) ; }      *
     * @Test public void getFeatureNameTest6() { DiffLine diffLine = new
     * DiffLine("+prompt \"This is a prompt message\"") ;
     * assertTrue(DiffUtils.getFeatureName(diffLine).equals("This is a prompt
     * message")) ;
    }
     */
    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void getFeatureNameTest7() {
        DiffLine diffLine = new DiffLine("+choice DRM");
        assertTrue(DiffUtils.getFeatureName(diffLine).equals("DRM"));
    }

    @Test
    public void getFeatureNameTest8() {
        DiffLine diffLine = new DiffLine("-choice DRM");
        assertTrue(DiffUtils.getFeatureName(diffLine).equals("DRM"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void getFeatureNameTest9() {
        DiffLine diffLine = new DiffLine("+choice");
        assertTrue(DiffUtils.getFeatureName(diffLine) == null);
    }

    @Test
    public void getFeatureNameTest10() {
        DiffLine diffLine = new DiffLine("-choice");
        assertTrue(DiffUtils.getFeatureName(diffLine) == null);
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void getFeatureNameTest11() {
        DiffLine diffLine = new DiffLine("+menu \"This is a prompt message\"");
        assertTrue(DiffUtils.getFeatureName(diffLine).equals("This is a prompt message"));
    }

    @Test
    public void getFeatureNameTest12() {
        DiffLine diffLine = new DiffLine("+menu \"This is a prompt message\"");
        assertTrue(DiffUtils.getFeatureName(diffLine).equals("This is a prompt message"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void getFeatureNameTest13() {
        DiffLine diffLine = new DiffLine("+mainmenu \"This is a prompt message\"");
        assertTrue(DiffUtils.getFeatureName(diffLine).equals("This is a prompt message"));
    }

    @Test
    public void getFeatureNameTest14() {
        DiffLine diffLine = new DiffLine("+mainmenu \"This is a prompt message\"");
        assertTrue(DiffUtils.getFeatureName(diffLine).equals("This is a prompt message"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
    @Test
    public void getFeatureNameTest15() {
        DiffLine diffLine = new DiffLine("+option env=\"KERNELVERSION\"");
        assertTrue(DiffUtils.getFeatureName(diffLine).equals("env"));
    }

    @Test
    public void getFeatureNameTest16() {
        DiffLine diffLine = new DiffLine("+option env=\"KERNELVERSION\"");
        assertTrue(DiffUtils.getFeatureName(diffLine).equals("env"));
    }

    /*
     * . . . . . . . . . . . . . . . . . . . . . . . . . . . . .
     */
            
    @Test
    public void getFeatureTypeTest1() {
        DiffLine diffLine = new DiffLine("-	choice DEFAULT_AS") ;
        assertTrue(DiffUtils.getFeatureType(diffLine).equals(FeatureType.CHOICE)) ;
    }
    
    @Test
    public void getFeatureTypeTest2() {
        DiffLine diffLine = new DiffLine("choice DEFAULT_AS") ;
        assertTrue(DiffUtils.getFeatureType(diffLine).equals(FeatureType.CHOICE)) ;
    }    
    
    @Test
    public void getFeatureTypeTest3() {
        DiffLine diffLine = new DiffLine("-choice DEFAULT_AS") ;
        assertTrue(DiffUtils.getFeatureType(diffLine).equals(FeatureType.CHOICE)) ;
    }     
    
    @Test
    public void getFeatureTypeTest4() {
        DiffLine diffLine = new DiffLine("-	menu \"DEFAULT_AS\"") ;
        assertTrue(DiffUtils.getFeatureType(diffLine).equals(FeatureType.MENU)) ;
    }
    
    @Test
    public void getFeatureTypeTest5() {
        DiffLine diffLine = new DiffLine("menu \"DEFAULT_AS\"") ;
        assertTrue(DiffUtils.getFeatureType(diffLine).equals(FeatureType.MENU)) ;
    }    
    
    @Test
    public void getFeatureTypeTest6() {
        DiffLine diffLine = new DiffLine("-menu \"DEFAULT_AS\"") ;
        assertTrue(DiffUtils.getFeatureType(diffLine).equals(FeatureType.MENU)) ;
    }     
    
    @Test
    public void getFeatureTypeTest7() {
        DiffLine diffLine = new DiffLine("-	menuconfig \"DEFAULT_AS\"") ;
        assertTrue(DiffUtils.getFeatureType(diffLine).equals(FeatureType.MENUCONFIG)) ;
    }
    
    @Test
    public void getFeatureTypeTest8() {
        DiffLine diffLine = new DiffLine("menuconfig \"DEFAULT_AS\"") ;
        assertTrue(DiffUtils.getFeatureType(diffLine).equals(FeatureType.MENUCONFIG)) ;
    }    
    
    @Test
    public void getFeatureTypeTest9() {
        DiffLine diffLine = new DiffLine("-menuconfig \"DEFAULT_AS\"") ;
        assertTrue(DiffUtils.getFeatureType(diffLine).equals(FeatureType.MENUCONFIG)) ;
    }       
}
