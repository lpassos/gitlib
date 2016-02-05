/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.utils;

import git.TestRepo;
import git.common.Filter;
import git.test.utils.GitRepo;
import java.util.LinkedList;
import org.junit.*;
import static org.junit.Assert.*;


/**
 *
 * @author leonardo
 */
public class FilterTest {
   
    @Test
    public void byContainingFileTest1() {
        boolean failed = false ;
        try {
            LinkedList<String> commits = new LinkedList<String>() ;
            commits.add("e49f7a9997c61cf800f2db5decba68d318352ef0") ;
            commits.add("2198edddd8f0245d4c47419310c6cfb0f4e1a197") ;
            commits.add("f0e81fecd4f83de7854262c8a6b3af19dfa99bf9") ;
            assertTrue(Filter.byContainingFilename(GitRepo.get(TestRepo.PATH), commits, "Kconfig").equals(commits)) ;
        } catch (Exception e) {
            e.printStackTrace();
            failed = true ;
        }   
        assertTrue(! failed) ;
    }
    
    @Test
    public void byContainingFileTest2() {
        boolean failed = false ;
        try {
            LinkedList<String> commits = new LinkedList<String>() ; 
            commits.add("aca50bd3b4c4bb5528a1878158ba7abce41de534") ;
            commits.add("98e5272fe70d62e193f70acf9951667beab27aba") ;
            commits.add("44afb3a04391a74309d16180d1e4f8386fdfa745") ;
            assertTrue(Filter.byContainingFilename(GitRepo.get(TestRepo.PATH), commits, "Kconfig").equals(new LinkedList())) ;
        } catch (Exception e) {
            e.printStackTrace();
            failed = true ;
        } 
        assertTrue(! failed) ;
    }
    
    @Test
    public void byContainingFileTest3() {
        boolean failed = false ;
        try {            
            LinkedList<String> withKconfig = new LinkedList<String>() ;            
            withKconfig.add("e49f7a9997c61cf800f2db5decba68d318352ef0") ;
            withKconfig.add("2198edddd8f0245d4c47419310c6cfb0f4e1a197") ;
            withKconfig.add("f0e81fecd4f83de7854262c8a6b3af19dfa99bf9") ;
            
            LinkedList<String> commits = new LinkedList<String>() ; 
            commits.add("aca50bd3b4c4bb5528a1878158ba7abce41de534") ;
            commits.add("98e5272fe70d62e193f70acf9951667beab27aba") ;
            commits.add("44afb3a04391a74309d16180d1e4f8386fdfa745") ;
            commits.addAll(withKconfig) ;
            
            assertTrue(Filter.byContainingFilename(GitRepo.get(TestRepo.PATH), commits, "Kconfig").equals(withKconfig)) ;
        } catch (Exception e) {
            e.printStackTrace();
            failed = true ;
        } 
        assertTrue(! failed) ;
    }  
    
    @Test
    public void byExcludingTagsTest() {
        boolean failed = false ;
        try {                        
            LinkedList<String> commits1 = new LinkedList<String>() ; 
            commits1.add("aca50bd3b4c4bb5528a1878158ba7abce41de534") ;
            commits1.add("98e5272fe70d62e193f70acf9951667beab27aba") ;
            commits1.add("44afb3a04391a74309d16180d1e4f8386fdfa745") ;
            
            LinkedList<String> tags = new LinkedList<String>() ;
            tags.add("521cb40b0c44418a4fd36dc633f575813d59a43d");
            tags.add("c16fa4f2ad19908a47c63d8fa436a1178438c7e7") ;
            
            LinkedList<String> commits2 = new LinkedList<String>() ; 
            commits2.addAll(commits1);
            commits2.addAll(tags);
            
            assertTrue(Filter.byExcludingTags(GitRepo.get(TestRepo.PATH), commits2).equals(commits1)) ;
            
        } catch (Exception e) {
            e.printStackTrace();
            failed = true ;
        }
        assertTrue(! failed) ;
    } 
    
    @Test
    public void byExcludingHashTest() {
        boolean failed = false ;
        try {                        
            LinkedList<String> commits = new LinkedList<String>() ; 
            commits.add("aca50bd3b4c4bb5528a1878158ba7abce41de534") ;
            commits.add("98e5272fe70d62e193f70acf9951667beab27aba") ;
            commits.add("44afb3a04391a74309d16180d1e4f8386fdfa745") ;            
            commits.add("521cb40b0c44418a4fd36dc633f575813d59a43d") ;
            commits.add("c16fa4f2ad19908a47c63d8fa436a1178438c7e7") ;
            
            assertTrue(! Filter.byExcludingHash(GitRepo.get(TestRepo.PATH), commits, "44afb3a04391a74309d16180d1e4f8386fdfa745").contains("44afb3a04391a74309d16180d1e4f8386fdfa745")) ;
            
        } catch (Exception e) {
            e.printStackTrace();
            failed = true ;
        }
        assertTrue(! failed) ;
    }     
}
