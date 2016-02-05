/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.test.utils;

import git.Git;
import git.TestRepo;
import git.common.Filter;
import git.common.FilterPredicate;
import git.common.Params;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author leonardo
 */
public class ParamsTest {
    @Test
    public void noMergesTest1() {        
        boolean failed = false ;
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;
            List<String> commits = git.getCommitsByParams(Params.Log.NO_MERGES) ;
            
            List<String> filteredCommits =  Filter.byPredicate(git, commits, new FilterPredicate() {

                @Override
                public boolean holds(List<String> cs, String commit) throws Exception {
                    return commit.equals("e7a7c9ab415874f4ad78a0352ca0ec6711092017") ;
                }
            }) ;
            assertTrue(filteredCommits.size() == 0) ;
        } catch (Exception e) {
            e.printStackTrace();
            failed = true ;
        }
        assertTrue(! failed) ;       
    }
    
    @Test
    public void noMergesTest2() {        
        boolean failed = false ;
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;
            List<String> commits = Filter.byPredicate(git, git.getCommits(), new FilterPredicate() {

                @Override
                public boolean holds(List<String> commits, String commit) throws Exception {                   
                    return commit.equals("e7a7c9ab415874f4ad78a0352ca0ec6711092017") ;
                }
            }) ;
            assertTrue(commits.size() == 1) ;
        } catch (Exception e) {
            e.printStackTrace();
            failed = true ;
        }
        assertTrue(! failed) ;       
    } 
    
    @Test
    public void reverseTest() {        
        boolean failed = false ;
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;
            List<String> commits1 = git.getCommitsByParams(Params.Log.REVERSE) ;
            List<String> commits2 = git.getCommits() ;
            assertTrue(commits1.size() == commits2.size()) ;
            
            Collections.reverse(commits2) ;            
            assertTrue(commits1.equals(commits2)) ;
            
        } catch (Exception e) {
            e.printStackTrace();
            failed = true ;
        }
        assertTrue(! failed) ;       
    }  
    
    @Test
    public void reverseWithNoMergesTest() {        
        boolean failed = false ;
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;
            List<String> commits1 = git.getCommitsByParams(Params.Log.REVERSE + " " + Params.Log.NO_MERGES) ;
            List<String> commits2 = git.getCommitsByParams(Params.Log.NO_MERGES) ;
            assertTrue(commits1.size() == commits2.size()) ;
            
            Collections.reverse(commits2) ;            
            assertTrue(commits1.equals(commits2)) ;
            
        } catch (Exception e) {
            e.printStackTrace();
            failed = true ;
        }
        assertTrue(! failed) ;       
    }      
}
