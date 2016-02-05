/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.common;

import git.patch.DiffLine;
import java.util.List;

/**
 *
 * @author leonardo
 */
public class DiffLineGroup {
    private List<DiffLine> removals ;
    private List<DiffLine> additions ;

    public DiffLineGroup(List<DiffLine> removals, List<DiffLine> additions) {
        this.removals = removals;
        this.additions = additions;
    }

    public List<DiffLine> getAdditions() {
        return additions;
    }

    public List<DiffLine> getRemovals() {
        return removals;
    }        
}
