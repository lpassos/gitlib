/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.common;

import git.patch.Chunk;
import git.patch.DiffLine;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class ChunkUtils {        
    
    public static List<DiffLineGroup> group(Chunk chunk) {
        
        int state = 0 ;
        
        LinkedList<DiffLine> additions = new LinkedList<>();
        LinkedList<DiffLine> removals = new LinkedList<>(); 
        
        LinkedList<DiffLineGroup> groups = new LinkedList<>() ;
        
        /* State machine processing the lines. */
        for(DiffLine diffLine : chunk.getLines()) {
            
            String line = diffLine.toString() ;
            
            switch(state) {
                
                case 0 :
                    if (line.charAt(0) == '+') {
                        additions.add(new DiffLine(line)) ;
                        state = 1 ;
                    }
                    else if (line.charAt(0) == '-') {              
                        removals.add(new DiffLine(line)) ;
                        state = 2 ;                           
                    }
                    break ;
                    
                case 1 :
                    if (line.charAt(0) == '-') {
                        
                        groups.add(new DiffLineGroup(removals, additions)) ;
                        
                        /* Resets removals and additions. */
                        removals = new LinkedList<DiffLine>() ;                        
                        additions = new LinkedList<DiffLine>() ;
                        
                        removals.add(new DiffLine(line)) ;                        
                        
                        state = 2 ;
                    }
                    else if (line.charAt(0) != '+') {
                        
                        groups.add(new DiffLineGroup(removals, additions)) ;
                        
                        /* Resets removals and additions. */
                        removals = new LinkedList<DiffLine>() ;
                        additions = new LinkedList<DiffLine>() ;
                        
                        state = 0 ;                        
                    }
                    else
                        additions.add(new DiffLine(line)) ; 
                    
                    break ;
                    
                case 2:
                    if (line.charAt(0) == '+') {
                        additions.add(new DiffLine(line)) ;
                        state = 3 ;
                    }
                    else if (line.charAt(0) != '-') {
                        groups.add(new DiffLineGroup(removals, additions)) ;
                        
                        /* Resets removals and additions. */
                        removals = new LinkedList<>() ;
                        additions = new LinkedList<>() ;
                        
                        state = 0 ;                  
                    }
                    else
                        removals.add(new DiffLine(line)) ;
                    
                    break ;
                    
                case 3:
                    if (line.charAt(0) == '-') {
                        groups.add(new DiffLineGroup(removals, additions)) ;
                        
                        /* Resets removals and additions. */
                        removals = new LinkedList<>() ;
                        additions = new LinkedList<>() ;
                        
                        removals.add(new DiffLine(line)) ;
                        
                        state = 2 ;
                    }
                    else if (line.charAt(0) != '+') {
                        groups.add(new DiffLineGroup(removals, additions)) ;
                        
                        /* Resets removals and additions. */
                        removals = new LinkedList<>() ;
                        additions = new LinkedList<>() ;                        
                        
                        state = 0 ;
                    }
                    else
                        additions.add(new DiffLine(line)) ;
                    break ;
            }
        }        
        
        if (additions.size() > 0 || additions.size() > 0) {
            groups.add(new DiffLineGroup(removals, additions)) ;
        }
            
        return groups ;
    }
}
