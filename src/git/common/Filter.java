/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.common;

import git.Git;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * A class that performs different filtering operations.
 * 
 * @author leonardo
 */
public class Filter {
    
    /**
     * A filter to remove commit, given a predicate.
     * 
     * @param git the git repository object
     * @param cms the list of commit to be filtered
     * @param predicate the predicate stating which commits should be filtered
     * @return a new list of commits for which the predicate hold
     * @throws Exception 
     */
    static public List<String> byPredicate(Git git, List<String> cms, FilterPredicate predicate) throws Exception {
        LinkedList<String> res = new LinkedList<String>() ;
        for(String c: cms) {            
            if (predicate.holds(cms, c))
                res.add(c) ;
        } 
        return res ;        
    }
    
    
    /**
     * A filter to remove commits that do not include a file name.
     * 
     * @param git the git repository object
     * @param cms the list of commit to be filtered
     * @param file a file name to be used as filter; commits that do not contain  <code> file </file>, will be removed
     * @return a filtered list of commits that contain <code> file </code> in their set of changed files
     * @throws Exception 
     */
    static public List<String> byContainingFilename(final Git git, final List<String> cms, final String f) throws Exception {        
        return byPredicate(git, cms, new FilterPredicate() {

            @Override
            public boolean holds(List<String> commits, String commitHash) throws Exception {
                for (String file : git.getFiles(commitHash))
                    if (file.endsWith(f))
                        return true ;
                return false ;                
            }
        }) ;        
    }
    
    /**
     * A filter to remove tag commits.
     * 
     * @param git the git repository object
     * @param commits the list of commit to be filtered
     * @return a filtered list of commits that do not include tag commits
     * @throws Exception 
     */
    static public List<String> byExcludingTags(Git git, List<String> commits) throws Exception {        
        for(String tag : git.getTags()) {
            String c = git.getCommitOfObject(tag) ;
            if (c != null)
                commits.remove(c);
        }   
        return commits ;   
    }
    
    /**
     * A filter to remove a given hash.
     * 
     * @param git the git repository object
     * @param cms the list of commit to be filtered
     * @param h a hash to be excluded from the list of commits
     * @return a filtered list of commits that do not include the given <code> hash </code>
     * @throws Exception 
     */
    static public List<String> byExcludingHash(final  Git git, List<String> cms, final String h) throws Exception {
        return byPredicate(git, cms, new FilterPredicate() {

            @Override
            public boolean holds(List<String> commits, String hash) throws Exception {
                // Only keep different hashes. 
                return (! hash.equals(h) );
            }
        }) ;
    }
    
    static public List<String> sortByHash(Git git, List<String> cms) {
        Collections.sort(cms, new Comparator<String>() {

            @Override
            public int compare(String t1, String t2) {
                return t1.compareTo(t2);
            }
        });
        return cms ;
    }    
}
