/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.common;

import git.patch.ChangeUnitType;
import git.patch.DiffLine;
import git.patch.FeatureType;
import java.util.regex.Pattern;

/**
 *
 * @author leonardo
 */

public class DiffUtils {
    
    private static final String addsFeatureRE    = "\\++( |\\t)*((config|menuconfig)( |\\t)+[0-9a-zA-Z_\\-]+|choice(( |\\t)+[0-9a-zA-Z_\\-]+)?|(menu|mainmenu)( |\\t)+\"[^\"]+\"|option( |\\t)+[0-9a-zA-Z_\\-]+=\"[^\"]+\")" ;
    private static final String removesFeatureRE = "\\-+( |\\t)*((config|menuconfig)( |\\t)+[0-9a-zA-Z_\\-]+|choice(( |\\t)+[0-9a-zA-Z_\\-]+)?|(menu|mainmenu)( |\\t)+\"[^\"]+\"|option( |\\t)+[0-9a-zA-Z_\\-]+=\"[^\"]+\")" ;
        
    private static Pattern addsFeaturePattern ;
    private static Pattern removesFeaturePattern ;
    
    static {
        addsFeaturePattern    = Pattern.compile(addsFeatureRE) ;
        removesFeaturePattern = Pattern.compile(removesFeatureRE) ;
    }
    
    public static boolean addsFeature(DiffLine diffLine, String feature) {
        if (addsFeaturePattern.matcher(diffLine.toString()).matches()) {
            String s1 = getFeatureName(diffLine) ;
            return (s1 != null) ? s1.equals(feature) : false ;
        }
        
        return false ;                
    }
    
    public static boolean addsFeature(DiffLine diffLine) {
        return addsFeaturePattern.matcher(diffLine.toString()).matches() ;
    }
    
    public static boolean removesFeature(DiffLine diffLine, String feature) {
        if (removesFeaturePattern.matcher(diffLine.toString()).matches()) {
            String s1 = getFeatureName(diffLine) ;
            return (s1 != null) ? s1.equals(feature) : false ;
        }
        
        return false ;   
    }
    
    public static boolean removesFeature(DiffLine diffLine) {
        return removesFeaturePattern.matcher(diffLine.toString()).matches() ;
    } 
    
    public static String getFeatureName(DiffLine diffLine) {
        
        String line = diffLine.toString() ;
        
        int i = 0 ;
        
        // Consumes the prefix (+, -)
        while(i < line.length()) {
            char c = line.charAt(i) ;
            if (c == '+' || c == '-' || Character.isWhitespace(c))
                i++ ;
            else
                break ;
        }
        
        // Consumes the feature type string (menu, config, etc.)
        while(i < line.length()) {
            if (!Character.isWhitespace(line.charAt(i)))
                i++ ;
            else
                break ;
        }                
        
        // Consumes whitespaces
        while(i < line.length()) {
            if (Character.isWhitespace(line.charAt(i)))
                i++ ;
            else
                break ;
        }        

        if (i < line.length()) {
            
            // Retrives the feature name.        
            StringBuilder name = new StringBuilder() ;            
            int state = 0 ;
            
            while(state != 3 && i < line.length()) {
                
                char c = line.charAt(i) ;
                
                switch(state) {
                    case 0:
                        if (c == '"' || c == '\'') 
                            state = 1 ;
                        else {
                            name.append(c) ;
                            state = 2 ;
                        }
                        break ;
                        
                    case 1:
                        if (c == '"' || c == '\'')
                            state = 3 ;
                        else
                            name.append(c) ;
                        break ;
                        
                    case 2:
                        if (c == '=') 
                            state = 3 ;
                        else
                            name.append(c) ;
                        break ;
                }
                
                i++ ;
            }
             
            String s = name.toString() ;
            return s.length() > 0 ? s : null ;
        }

        return null ;
            
    }
    
    public static FeatureType getFeatureType(DiffLine diffLine) {        
                        
        String[] fields = new String(diffLine.toString()).replaceFirst("(\\++|\\-+)", "").trim().split("( |\\t)") ;
        if (fields.length > 0) {                                     

            if (fields[0].equals("config"))
                return FeatureType.CONFIG ;

            if (fields[0].equals("menu"))
                return FeatureType.MENU ;

            if (fields[0].equals("menuconfig"))
                return FeatureType.MENUCONFIG ;

            if (fields[0].equals("option"))
                return FeatureType.OPTION ;

            if (fields[0].equals("choice"))
                return FeatureType.CHOICE ;
            
            if (fields[0].equals("mainmenu"))
                return FeatureType.MAINMENU ;            
        }
        
        throw new RuntimeException("About to return null... Unmapped feature type: " + fields[0]) ;              
    }
    
    public static ChangeUnitType getChangeUnitType(DiffLine line) {
        if (line.isNewLine())
            return ChangeUnitType.ADDED ;
        
        else if (line.isRemovedLine())
            return ChangeUnitType.DELETED ;
        
        throw new RuntimeException("Unmodified diff lines do not have a change type!") ;
    }
    
    public static String dropDiffFileNamePrefix(String fileName) {
        return fileName.trim().substring(1) ;
    }   
}
