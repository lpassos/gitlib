/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.common;

/**
 *
 * @author leonardo
 */
public class Params {
    public static final class Log {
        public final static String NO_MERGES = "--no-merges" ;
        public final static String REVERSE   = "--reverse" ;        
        
        public final static String nlogs(int n) {
            return "-" + n ;
        }
        
        public final static String range(String start, String end) {
            return start + ".." + end ;
        }
    }
    
    public static final class Tag {
        public final static String CONTAINS = "--contains" ;
    }
    
}
