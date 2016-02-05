package git;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import git.test.utils.GitRepo;
import git.common.Params;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author leonardo
 */
public class GitTest {                
    
    @Test
    public void getTagsTest1() {        
        List<String> tags = null ;
        try {
            
            Git git = GitRepo.get(TestRepo.PATH) ;
            tags  = git.getTags() ;
            
            assertTrue(tags != null) ;
            assertTrue(tags.size() == 323) ;        
            
        } catch(Exception e) {   
            e.printStackTrace();
        }
        
        assertTrue(tags != null) ;
    } 
    
    @Test
    public void getTagsTest2() {
        Exception err = null ;
        try {
            
            Git git = GitRepo.get("xxx") ;        
            
        } catch(Exception e) {            
            err = e ;
        }            
        
        assertTrue(err != null) ;
    } 
    
    @Test
    public void getTagsTest3() {
        
        List<String> tags = null ;
        
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;
            tags = git.getTags("-l") ;
                        
            assertTrue(tags.size() == 323) ;             
            
        } catch(Exception e) {
            /* 
             * No exception should occur.
             * If it happens, force test error.
             */                        
            e.printStackTrace() ;                       
       }                
        
       assertTrue(tags != null) ;
    }  
    
    @Test
    public void getTagsTest4() {        
        
        List<String> tags = null;
        try {            
           
           Git git = GitRepo.get(TestRepo.PATH) ;
           tags = git.getTags(null) ;
           assertTrue(tags.size() == 323) ; 
           
        } catch(Exception e) {
            /* 
             * No exception should occur.
             * If it happens, force test error.
             */                        
            e.printStackTrace() ;           
        }                
        
        assertTrue(tags != null) ;
    }         
    
    @Test
    public void getFilesTest1() {
        List<String> files = null ;
        
        try {
            
            Git git = GitRepo.get(TestRepo.PATH) ;            
            files = git.getFiles("57f27cca7ab59cec05adc85cef97e9b4f7d28d78") ;
            
        } catch (Exception e) {
            e.printStackTrace();
        }        
        
        assertTrue(files != null) ;       
        assertTrue(files.size() == 1) ;        
        assertTrue(files.get(0).equals("arch/hexagon/kernel/smp.c")) ;                        
    } 
    
    @Test
    public void getFilesTest2() {
        List<String> files = null ;
        
        try {
            
            Git git = GitRepo.get(TestRepo.PATH) ;            
            files = git.getFiles("6be5ceb02e98eaf6cfc4f8b12a896d04023f340d") ;
            
        } catch (Exception e) {
            e.printStackTrace() ;
        }
        
        assertTrue(files != null) ;       
        assertTrue(files.size() == 15) ;                               
    }  
    
    @Test
    public void getFilesTest3() {
        List<String> files = null ;
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;            
            files = git.getFiles("xxx") ;
        } catch (Exception e) {
            /* Do nothing. */
        }
        assertTrue(files == null) ;       
    } 
    
    @Test
    public void getCommitsTest() {        
        List<String> commits = null ;
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;
            commits = git.getCommits() ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(commits != null) ;
        assertTrue(commits.size() == 370139) ;
    }
    
    @Test
    public void getCommitsByParamsTest() {        
        List<String> commits = null ;
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;
            commits = git.getCommitsByParams(Params.Log.NO_MERGES) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(commits != null) ;
        assertTrue(commits.size() == 345253) ;
    }  
    
    @Test
    public void getCommitTest() {        
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;
            assertTrue(git.getCommitOfObject("v2.6.26").equals("bce7f793daec3e65ec5c5705d2457b81fe7b5725")) ;
        } catch(Exception e) {
            e.printStackTrace(); 
        }
    }
    
    @Test
    public void getCurrentBranchTest1() {        
        String branch = null ;
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;
            branch = git.getCurrentBranch() ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(branch != null) ;
        assertTrue(branch.equals("master")) ;
    }  
    
    @Test
    public void getCheckoutAndDeleteBranchTest() {
        final String branchName = "____TESTING_TMP_v2.6.33" ;
        boolean failed = false ;
        
        Git git = null ;
        
        try {
            git = GitRepo.get(TestRepo.PATH) ;
        } catch(Exception e) {
            e.printStackTrace() ;
        }
        
        assertTrue(git != null) ;
        
        try {            
            git.checkout("v2.6.33", branchName);
            assertTrue(git.getNumberOfBranches() == 2) ;

        } catch (Exception e) {
            e.printStackTrace();
            failed = true ;
        } finally {
            try {
                git.checkout("master") ;
                git.deleteBranch(branchName);
                assertTrue(git.getNumberOfBranches() == 1) ;            
            } catch (Exception e) {
                e.printStackTrace();
                failed = true ;
            }
            
        }
        assertTrue(! failed) ;
    }     
    
    @Test
    public void getCommitsFromTag() {
        boolean failed = false ;
        try {
            
            Git git = GitRepo.get(TestRepo.PATH) ;
            assertTrue(git.getCommitsFromTag("v3.4-rc6").size() == 70051) ;
 
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }
        assertTrue(! failed) ;
    }

    
    @Test
    public void getCommitsInTagIntervalTest1() {
        boolean failed = false ;
        try {
           Git git = GitRepo.get(TestRepo.PATH) ;
           assertTrue(git.getCommitsInTagInterval("v3.4-rc5", "v3.4-rc6").size() == 244) ; 
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }  
        assertTrue(! failed) ;
    }  
    
    @Test
    public void getCommitsInTagIntervalTest2() {        
        boolean failed = false ;
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;
           assertTrue(git.getCommitsInTagInterval("v3.4-rc5", "v3.4-rc6", Params.Log.NO_MERGES).size() == 206) ; 
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }   
        assertTrue(! failed) ;
    }     
    
    
    @Test
    public void getPatchTest() {
        List<String> pathLines = null ;        
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;
            pathLines = git.getFilePatch("6be5ceb02e98eaf6cfc4f8b12a896d04023f340d", "arch/tile/kernel/single_step.c") ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue(pathLines != null) ;       
        assertTrue(pathLines.size() > 1) ; 
    }   
    
    @Test
    public void getLastCommit() {        
        try {
            Git git = GitRepo.get(TestRepo.PATH) ;
            git.getLastCommit().equals("789505b05752239d957dbfa84b183e0d7a641952") ;
        } catch(Exception e) {
            
        }
    }
    
    @Test
    public void getAcceptingTagsTest() {        
        try {
            Git git  = GitRepo.get(TestRepo.PATH) ;
            List<String> tags = git.getAcceptingTags("350f8f5631922c7848ec4b530c111cb8c2ff7caa") ;
            assertTrue(tags.size() == 144) ;
            assertTrue(tags.get(0).equals("v2.6.33")) ;
            assertTrue(tags.get(tags.size() - 1).equals("v3.9-rc8")) ;            
        } catch(Exception e) {
            
        }
    }
}
