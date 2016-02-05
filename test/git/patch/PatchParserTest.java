/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.patch;

import git.common.ChunkUtils;
import git.common.DiffUtils;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import test_utils.TestUtils;

/**
 *
 * @author leonardo
 */
public class PatchParserTest {    
       
    @Test
    public void parseTest1() {
        boolean failed = false ;
        List<Diff> patch = null ;
        try {
            patch = new PatchParser(loadPatch("patch1")).parse() ;
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }
        assertTrue(! failed) ;  
        assertTrue(patch.size() == 1) ;                
        assertTrue(patch.get(0).chunks.size() == 3) ;
        assertTrue(patch.get(0).fileA.equals("a/fs/gfs2/lock_dlm.c")) ;
        assertTrue(patch.get(0).fileB.equals("b/fs/gfs2/lock_dlm.c")) ;   
        assertTrue(patch.get(0).type.equals(DiffType.MODIFIED)) ;
    }
    
    @Test
    public void parseTest2() {
        boolean failed = false ;
        List<Diff> patch = null ; 
        try {
            patch = new PatchParser(loadPatch("patch2")).parse() ;
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }
        assertTrue(! failed) ;       
        assertTrue(patch.size() == 1) ;
        assertTrue(patch.get(0).chunks.size() == 1) ;
        assertTrue(patch.get(0).fileA.equals("a/mm/swap_state.c")) ;
        assertTrue(patch.get(0).fileB.equals("b/mm/swap_state.c")) ; 
        assertTrue(patch.get(0).type.equals(DiffType.MODIFIED)) ;
    }  
    
    @Test
    public void parseTest3() {
        boolean failed = false ;
        List<Diff> patch = null ; 
        try {
            patch = new PatchParser(loadPatch("patch3")).parse() ;
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }
        assertTrue(! failed) ;       
        assertTrue(patch.size() == 1) ;
        assertTrue(patch.get(0).chunks.size() == 1) ;
        assertTrue(patch.get(0).fileA.equals("a/Documentation/devicetree/bindings/ata/calxeda-sata.txt")) ;
        assertTrue(patch.get(0).fileB.equals("b/Documentation/devicetree/bindings/ata/calxeda-sata.txt")) ;  
        assertTrue(patch.get(0).type.equals(DiffType.DELETED)) ;
    }    
    
    @Test
    public void parseTest4() {
        boolean failed = false ;
        List<Diff> patch = null ;
        try {
            patch = new PatchParser(loadPatch("patch4")).parse() ;
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }
        assertTrue(! failed) ;       
        assertTrue(patch.size() == 1) ;
        assertTrue(patch.get(0).chunks.size() == 1) ;
        assertTrue(patch.get(0).fileA.equals("a/Documentation/devicetree/bindings/ata/ahci-platform.txt")) ;
        assertTrue(patch.get(0).fileB.equals("b/Documentation/devicetree/bindings/ata/ahci-platform.txt")) ;  
        assertTrue(patch.get(0).type.equals(DiffType.ADDED)) ;
    } 
    
    @Test
    public void parseTest5() {
        boolean failed = false ;
        List<Diff> patch = null ;
        try {
            patch = new PatchParser(loadPatch("patch5")).parse() ;
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }
        assertTrue(! failed) ;    
        assertTrue(patch.size() == 2) ;
        
        assertTrue(patch.get(0).getType().equals(DiffType.MODIFIED)) ;
        assertTrue(patch.get(0).getFileA().equals("a/drivers/Makefile"));
        assertTrue(patch.get(0).getFileB().equals("b/drivers/Makefile"));
        assertTrue(patch.get(0).getChunks().size() == 1) ;
        assertTrue(patch.get(0).getChunks().get(0).getLines().size() == 8) ;
        
        assertTrue(patch.get(1).getType().equals(DiffType.MODIFIED)) ;
        assertTrue(patch.get(1).getFileA().equals("a/drivers/char/Makefile"));
        assertTrue(patch.get(1).getFileB().equals("b/drivers/char/Makefile"));
        assertTrue(patch.get(1).getChunks().size() == 1) ;
        assertTrue(patch.get(1).getChunks().get(0).getLines().size() == 6) ;
     }   
    
    @Test
    public void parseTest6() {
        boolean failed = false ;
        List<Diff> patch = null ;
        try {
            patch = new PatchParser(loadPatch("patch6")).parse() ;
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }
        assertTrue(patch.size() == 222);
        assertTrue(! failed) ;    
     }  
    
    @Test
    public void parseTest7() {
        boolean failed = false ;
        List<Diff> patch = null ;
        try {
            patch = new PatchParser(loadPatch("patch7")).parse() ;
                        
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }        
        assertTrue(! failed) ; 
        assertTrue(patch.size() == 1);    
        

        int numberOfConfigs = 0 ;
        int others = 0 ;
        for (Diff diff : patch) {
            for(Chunk chunk : diff.getChunks()) {
                
                int i = 0 ;
                
                for(DiffLine line : chunk.getLines()) {
                    
                    if (DiffUtils.addsFeature(line)) {
                        if (DiffUtils.getFeatureType(line).equals(FeatureType.CONFIG)) {
                            numberOfConfigs++ ;
                        }
                        else
                            others++ ;                        
                    }
                    
                    i++ ;

                }
            }
        } 
        
        assertTrue(numberOfConfigs == 13) ;
        assertTrue(others == 1) ;
     }  
    
    @Test
    public void parseTest8() {
        List<Diff> patch = null ;
        
        boolean foundDefaultAsRemoval = false,
                foundDefaultAsAddition = false ;
        
        try {
            patch = new PatchParser(loadPatch("patch8")).parse() ;
            
            for(Diff diff : patch) {
                
                for(Chunk chunk : diff.getChunks()) {                   
                    
                    for(DiffLine diffline : chunk.getLines()) {
                                             
                        
                        if (DiffUtils.addsFeature(diffline) && DiffUtils.getFeatureName(diffline).equals("DEFAULT_AS"))
                            foundDefaultAsAddition = true ;
                        
                        if (DiffUtils.removesFeature(diffline) && DiffUtils.getFeatureName(diffline).equals("DEFAULT_AS"))
                            foundDefaultAsRemoval = true ;
                    }
                }
            }
        } catch(Exception e) {
            fail(e.getMessage()) ;
            e.printStackTrace() ;
        }
        assertTrue(foundDefaultAsRemoval) ;
        assertTrue(! foundDefaultAsAddition);  
     }     
    
    @Test
    public void parseTest9() {
        boolean failed = false ;
        List<Diff> patch = null ;
        try {
            patch = new PatchParser(loadPatch("patch9")).parse() ;
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }
        assertTrue(! failed) ;    
        assertTrue(patch.size() == 1) ;
             
        assertTrue(patch.get(0).getType().equals(DiffType.RENAMED)) ;
        assertTrue(patch.get(0).getSimilarity() == 100) ;
     }  
    
    @Test
    public void parseTest10() {
        boolean failed = false ;
        List<Diff> patch = null ;
        try {
            patch = new PatchParser(loadPatch("patch10")).parse() ;
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }
        assertTrue(! failed) ;    
        assertTrue(patch.size() == 1) ;
             
        assertTrue(patch.get(0).getType().equals(DiffType.COPIED)) ;
        assertTrue(patch.get(0).getSimilarity() == 54) ;
        assertTrue(patch.get(0).getFileA().equals("a/arch/s390/include/asm/pci_insn.h")) ;
        assertTrue(patch.get(0).getFileB().equals("b/arch/s390/pci/pci_insn.c")) ;
     }    
    
    @Test
    public void parseTest11() {
        boolean failed = false ;
        List<Diff> patches = null ;
        try {
            patches = new PatchParser(loadPatch("patch11")).parse() ;
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }
        assertTrue(patches.size() == 9) ;
     }     
    
    @Test
    public void parseTest12() {
        boolean failed = false ;
        List<Diff> patches = null ;
        try {
            patches = new PatchParser(loadPatch("patch12")).parse() ;
        } catch(Exception e) {
            e.printStackTrace() ;
            failed = true ;
        }
        assertTrue(patches.size() == 1) ;
        assertTrue(patches.get(0).getFileA().equals("a/arch/powerpc/kernel/power5+-pmu.c")) ;
        assertTrue(patches.get(0).getFileB().equals("b/arch/powerpc/kernel/power5+-pmu.c")) ;
     }     
    
    private List<String> loadPatch(String fileName) throws Exception {
        String filePath = TestUtils.getFileAt(this.getClass().getPackage().getName(), fileName) ;
        LinkedList<String> lines = new LinkedList<String>() ;
        
        BufferedReader reader = new BufferedReader(new FileReader(filePath)) ;
        String line ;
        while((line = reader.readLine()) != null) {
            lines.add(line) ;
        }
        reader.close() ;
        
        return lines ;
    }
}
