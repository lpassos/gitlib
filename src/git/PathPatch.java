/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class PathPatch {
    
    private String commit ;
    private List<List<String>> diffs ;

    public PathPatch(String commit) {
        this(commit, new LinkedList<List<String>>()) ;
    }
        
    public PathPatch(String commit, List<List<String>> diffs) {
        this.commit = commit;
        this.diffs = diffs;
    }   
    
    public String getCommit() {
        return commit;
    }

    public List<List<String>> getPatches() {
        return diffs;
    }        
}
