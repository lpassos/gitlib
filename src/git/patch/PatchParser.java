/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.patch;

import gsd.utils.collection.ListUtils;
import java.util.LinkedList;
import java.util.List;

/**
 * A parser to process patches.
 * 
 * @author leonardo
 */
public class PatchParser {    
    
    /*
     * From git help log: http://linux.die.net/man/1/git-log
     * 
     * What the -p option produces is slightly different from the traditional diff format.

        1. It is preceded with a "git diff" header, that looks like this:
        diff --git a/file1 b/file2
        The a/ and b/ filenames are the same unless rename/copy is involved. Especially, even for a creation or a deletion, /dev/null is not used in place of a/ or b/ filenames.
        When rename/copy is involved, file1 and file2 show the name of the source file of the rename/copy and the name of the file that rename/copy produces, respectively.

        2. It is followed by one or more extended header lines:
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
    
    private static enum ParseState {
       PROCESS_DIFF_HEADER_ENTRY,
       PROCESS_DIFF_HEADER_BODY,
       PROCESS_CHUNK_HEADER_STATE,
       PROCESS_CHUNK_BODY_STATE
    }    
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    private static final String DIFF_HEADER_LABEL   = "diff \\-\\-git [^ \\n\\t\\f\\r]+ [^ \\n\\t\\f\\r]+" ;
    
    private static final String OLD_MODE_LABEL = "old mode [0-9]+" ;
    private static final String NEW_MODE_LABEL  = "new mode [0-9]+" ; 
    
    private static final String DELETED_FILE_MODE_LABEL = "deleted file mode [0-9]+" ;
    private static final String NEW_FILE_MODE_LABEL     = "new file mode [0-9]+" ;
    
    private static final String COPY_FROM_LABEL = "copy from [^ \\n\\t\\f\\r]+" ;
    private static final String COPY_TO_LABEL   = "copy to [^ \\n\\t\\f\\r]+" ;
    
    private static final String RENAME_FROM_LABEL = "rename from [^ \\n\\t\\f\\r]+" ;
    private static final String RENAME_TO_LABEL   = "rename to [^ \\n\\t\\f\\r]+" ;
    
    private static final String SIMILARITY_LABEL  = "similarity index [0-9]+%" ;
    private static final String DISSIMILARITY_LABEL  = "dissimilarity index [0-9]+%" ;
    
    private static final String INDEX_LABEL = "index [0-9a-z]+\\.\\.[0-9a-z]+( [0-9]*)?" ;

    private static final String FILE_A_LABEL = "\\-\\-\\- [a-zA-Z0-9/\\.\\-_]++" ;
    private static final String FILE_B_LABEL = "\\+\\+\\+ [a-zA-Z0-9/\\.\\-_]++" ;
    
    private static final String CHUNK_HEADER_LABEL = "@@ \\-[0-9]+(,[0-9]+)? \\+[0-9]+(,[0-9]+)? @@.*" ;
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    private List<String> patchLines ;
    private ParseState state ;
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public PatchParser(List<String> patchLines) {
        this.patchLines = new LinkedList<>(patchLines) ;
        this.state = ParseState.PROCESS_DIFF_HEADER_ENTRY ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public List<Diff> parse() throws Exception {

        List<Diff> patch = new LinkedList<>() ;
        
        Diff diff = null ;                                 
        Chunk chunk = null ;
        
        while(! patchLines.isEmpty()) {
            
            String line = ListUtils.removeFirst(patchLines).trim() ;
            
            if (line.isEmpty())
                continue ;
            
            switch(state) {
                case PROCESS_DIFF_HEADER_ENTRY:
                    diff = processDiffHeaderEntry(line, patch) ;
                    break ;
                
                case PROCESS_DIFF_HEADER_BODY:
                    diff = processDiffHeaderBody(line, diff) ;
                    break ;
                                         
                case PROCESS_CHUNK_HEADER_STATE:
                    chunk = processChunkHeader(line, diff) ;
                    break ;
                    
                case PROCESS_CHUNK_BODY_STATE:
                    processChunk(patchLines, line, chunk) ;
                    break ;
            }
            
        }
        return patch ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public void reset(List<String> patchLines) {
        this.patchLines = new LinkedList<>(patchLines) ;
        this.state = ParseState.PROCESS_DIFF_HEADER_ENTRY ;        
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    protected static void expectingError(String expected, String found) throws Exception {
        throw new Exception("Expecting "+ expected + ", but \"" + found + "\" was found") ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    private Diff processDiffHeaderEntry(String line, List<Diff> patch) throws Exception {
        
        // Header format: diff --git a/filename b/filename
        // Example: diff --git a/builtin-http-fetch.c b/http-fetch.c
        
        if (! line.matches(DIFF_HEADER_LABEL)) 
            expectingError("diff entry", line) ;
                        
        Diff diff = new Diff() ;
       
        String[] headerFields = line.split("( |\\t)") ;
        
        diff.fileA = headerFields[2] ;     
        diff.fileB = headerFields[3] ;
        
        diff.type = DiffType.MODIFIED ;
        
        patch.add(diff) ;
        
        state = ParseState.PROCESS_DIFF_HEADER_BODY ;
        
        return diff ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    private boolean checkIfDiffHeaderEntry(String line) {
        if (line.matches(DIFF_HEADER_LABEL)) {
            state = ParseState.PROCESS_DIFF_HEADER_ENTRY ;
            patchLines.add(0, line);
            return true ;
        }
        
        return false ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    private Diff processDiffHeaderBody(String line, Diff diff) throws Exception {
        if (checkIfDiffHeaderEntry(line))
            return diff;        
        
        if (line.matches(OLD_MODE_LABEL)) {
            
            diff.oldMode = line.split("( |\\t)")[2] ;
            
            while(! patchLines.isEmpty() && (line = ListUtils.removeFirst(patchLines)).isEmpty()) ;
            
            if (! line.matches(NEW_MODE_LABEL))
                expectingError("new mode label", line) ;
            
            diff.newMode = line.split("( |\\t)")[2] ;
        }
        
        else if (line.matches(DELETED_FILE_MODE_LABEL)) {
            diff.deletedFileMode = line.split("( |\\t)")[3] ;
            diff.type = DiffType.DELETED ;
        }
        
        else if (line.matches(NEW_FILE_MODE_LABEL)) {
            diff.newFileMode = line.split("( |\\t)")[3] ;
            diff.type = DiffType.ADDED ;
        }      
        
        else if (line.matches(COPY_FROM_LABEL)) {
            
            String copyFrom = "a/" + line.split("( |\\t)")[2] ;
            assert(copyFrom.equals(diff.fileA)) ;
            
            while(! patchLines.isEmpty() && (line = ListUtils.removeFirst(patchLines)).isEmpty()) ;
            
            if (! line.matches(COPY_TO_LABEL))
                expectingError("copy to label", line) ;
            
            String copyTo = "b/" +line.split("( |\\t)")[2] ;
            assert(copyTo.equals(diff.fileB)) ;
            
            diff.type = DiffType.COPIED ;
        }
        
        else if (line.matches(RENAME_FROM_LABEL)) {
            
            String renameFrom = "a/" + line.split("( |\\t)")[2] ;
            assert(renameFrom.equals(diff.fileA)) ;
            
            while(! patchLines.isEmpty() && (line = ListUtils.removeFirst(patchLines)).isEmpty()) ;
            
            if (! line.matches(RENAME_TO_LABEL))
                expectingError("rename to label", line) ;
            
            String renameTo = "b/" + line.split("( |\\t)")[2] ;
            assert(renameTo.equals(diff.fileB)) ;
            
            diff.type = DiffType.RENAMED ;
        }
        
        else if (line.matches(SIMILARITY_LABEL)) {
            diff.similarity = Integer.parseInt(line.split("( |\\t|%)")[2]) ;
        }
        
        else if (line.matches(DISSIMILARITY_LABEL)) {
            diff.similarity = 100 - Integer.parseInt(line.split("( |\\t|%)")[2]) ;
        }
                        
        else if (line.matches(INDEX_LABEL)) {
            String[] indexFields = line.split("\\.\\.| ") ;
            diff.preImageHash = indexFields[1] ;
            diff.postImageHash = indexFields[2] ; 
            
            if (indexFields.length > 3)
                diff.indexMode = indexFields[3] ;
        }
        
        else if (line.matches(FILE_A_LABEL)) {
            String f = line.split("( |\\t)")[1] ;
            if (f.equals("/dev/null")) {
                if (diff.type != null) {
                    if (! diff.type.equals(DiffType.ADDED))
                        throw new Exception("Inconsistent information: file " + f + " was supposed to be added!") ;
                }
                else
                    diff.type = DiffType.ADDED ; 
            }               
        }
        
        else if (line.matches(FILE_B_LABEL)) {
            String f = line.split("( |\\t)")[1] ;
            if (f.equals("/dev/null")) {
                if (diff.type != null) {
                    if (! diff.type.equals(DiffType.DELETED))
                        throw new Exception("Inconsistent information: file " + f + " was supposed to be deleted!") ;
                }
                else
                    diff.type = DiffType.DELETED ; 
            }               
        }
                
        else if (line.matches(CHUNK_HEADER_LABEL)) {  
            patchLines.add(0, line) ;
            state = ParseState.PROCESS_CHUNK_HEADER_STATE ;
        }
        
        /* Else ignore: irrelevant content */
            
        
        
        return diff ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    private Chunk processChunkHeader(String line, Diff diff) {            
        
        if (checkIfDiffHeaderEntry(line))
            return null ;
        
        Chunk chunk = null ;
        if (line.matches(CHUNK_HEADER_LABEL)) {
        
            // chunk format: @@ -[:line number:],[:number of lines:] , +[:line number:],[:number of lines:] @@ [header]        
            // Example 1: @@ -1,8 +1,9 @@
            // Example 2: @@ -18,6 +19,8 @@ int cmd_http_fetch(int argc, const char **argv, ...       


            int fstAtAt = line.indexOf("@@") ;
            int sndAtAt = line.indexOf( "@@", fstAtAt + 2) ;

            String[] intervals = line.substring(fstAtAt + 2, sndAtAt).replaceAll("\\+", "").trim().split("( |\\t)") ;

            chunk = new Chunk() ;

            String[] intervalsA = intervals[0].split(",") ;
            chunk.lineA = Integer.parseInt(intervalsA[0]) ;

            if (intervalsA.length > 1) {
                chunk.numberOfLinesA = Integer.parseInt(intervalsA[1]) ;  
            }

            String[] intervalsB = intervals[1].split(",") ;
            chunk.lineB = Integer.parseInt(intervalsB[0]) ;

            if (intervalsB.length > 1) {
                chunk.numberOfLinesB = Integer.parseInt(intervalsB[1]) ;  
            }      

            String headDiffline = line.substring(sndAtAt + 2) ;
            
            if (! headDiffline.isEmpty())
                chunk.diffs.add(new DiffLine(headDiffline)); 
            
            diff.chunks.add(chunk) ;
            
            state = ParseState.PROCESS_CHUNK_BODY_STATE ;
        }
        else
            state = ParseState.PROCESS_DIFF_HEADER_ENTRY ;
        
        
        return chunk ;
    } 
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    private void processChunk(List<String> patchLines, String line, Chunk chunk) {
        
        if (checkIfDiffHeaderEntry(line))
            return ;
        
        if (line.matches(CHUNK_HEADER_LABEL)) {  
            patchLines.add(0, line) ;
            state = ParseState.PROCESS_CHUNK_HEADER_STATE ;
        }
        else        
            chunk.diffs.add(new DiffLine(line)) ;                          
    } 
}
