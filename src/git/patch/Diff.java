/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.patch;

import java.util.LinkedList;
import java.util.List;

/**
 * A class to represent diffs. A diff is basically a set of added and removed lines.
 * 
 * @author leonardo
 */

/* Attributes of a diff (as stated in git help log:
 
    old mode <mode>
    new mode <mode>
    deleted file mode <mode>
    new file mode <mode>
    copy from <path>
    copy to <path>
    rename from <path>
    rename to <path>
    similarity index <number>
    dissimilarity index <number>
    index <hash>..<hash> <mode> 
*/ 

public class Diff {
    
    protected String indexMode ; 
    
    protected String oldMode ;
    protected String newMode ;
    
    protected String deletedFileMode ;
    protected String newFileMode ;    
    
    protected String fileA ;
    protected String fileB ;
    
    protected String preImageHash ;
    protected String postImageHash ;
    
    protected DiffType type ;
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */    
    
    protected int similarity = -1 ;
    protected int dissimilarity = -1;
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */    
     
  
    protected List<Chunk> chunks = new LinkedList<Chunk>() ;
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */

    public String getFileA() {
        return fileA;
    }

    public String getFileB() {
        return fileB;
    }

    public String getPostImageHash() {
        return postImageHash;
    }

    public String getPreImageHash() {
        return preImageHash;
    }

    public int getSimilarity() {
        return similarity;
    }

    public int getDissimilarity() {
        return dissimilarity;
    }        

    public DiffType getType() {
        return type;
    }  
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . */
    
    
    public List<Chunk> getChunks() {
        return chunks;
    }

    public String getOldMode() {
        return oldMode;
    }

    public String getNewMode() {
        return newMode;
    }

    public String getDeletedFileMode() {
        return deletedFileMode;
    }

    public String getNewFileMode() {
        return newFileMode;
    }

    public String getIndexMode() {
        return indexMode;
    }        
}
