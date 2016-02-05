/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.patch;

/**
 * A class to represent a line inside a diff.
 * 
 * @author leonardo
 */
public final class DiffLine {

    private String s ;
    
    public DiffLine(String s) {
        this.s = s ;
    }
    
    public boolean isNewLine() {
        return s.length() > 0 && s.charAt(0) == '+' ;
    }
    
    public boolean isRemovedLine() {
        return s.length() > 0 && s.charAt(0) == '-' ;
    }    
    
    public boolean isUnchangedLine() {
        return (! isNewLine()) && (! isRemovedLine()) ;
    }        
    
    @Override
    public String toString() {
        return s ;
    }    
}
