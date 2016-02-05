/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.common;

import java.util.List;

/**
 *
 * @author leonardo
 */
public abstract class  FilterPredicate {
    abstract public boolean holds(List<String> commits, String commit) throws Exception ;    
}
