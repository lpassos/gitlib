/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git;

import git.common.Params;
import gsd.utils.collection.CollectionsFormatter;
import gsd.utils.ipc.ProcessRunner;
import gsd.utils.processor.ErrorLineProcessor;
import gsd.utils.processor.Processor;
import gsd.utils.processor.ProcessorException;
import gsd.utils.string.Separator;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

/**
 * A class to process common Git commands.
 * @author Leonardo Passos (lpassos@gsd.uwaterloo.ca)
 */
public class Git {

    private File repository ;
    private String[] environment ;

    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/  
    
    private static final String RES_DELIM = ":" ;         
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/        
    /**
     * Creates a new Git repository object.
     * @param repository 
     */            
    public Git(final String repository) 
            throws FileNotFoundException, Exception {        
        this(new File(repository), null) ;
    }   
    
    public Git(final String repository, String dest) 
            throws FileNotFoundException, Exception {        
        this(new File(repository), new File(dest)) ;
    }       
    
    public Git(final File repository, final File dest) 
            throws FileNotFoundException, Exception {
        
        checkIfRepoDirExists(repository);
        checkIfGitMetaDirExists(repository) ;
        
        if (dest != null) {
            runCommand(formatCommand("clone", repository.getAbsolutePath() + 
                    " " + dest.getAbsolutePath())) ;
            this.repository = dest ;
        }
        else
            this.repository = repository ;
                
        /* Sets environment */
        setEnv(this.repository) ;        
    }
    
    public final void checkIfRepoDirExists(final File repository) throws FileNotFoundException {
        if (! repository.exists())
            throw new FileNotFoundException("Repository " + repository + " does not exist") ;                                
    }
    
    public final void checkIfGitMetaDirExists(final File repository) throws FileNotFoundException {
        if (! new File(repository.getAbsolutePath() + File.separator + ".git").exists())
            throw new FileNotFoundException(".git does not exist in " + repository) ;        
    }
    
    public final void setEnv(final File repository) {
        this.environment = new String[2] ;
        environment[0] = String.format("GIT_DIR=%s/.git", repository.getAbsolutePath()) ;
        environment[1] = String.format("GIT_WORK_TREE=%s", repository.getAbsolutePath()) ;               
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/    
    /**
     * Get all tags in the repository.
     * 
     * @return a list of tags
     * @throws Exception 
     */
    public List<String> getTags() throws Exception {
        return getTags(null) ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

    public String getCommitMessage(final String commit) throws Exception {
        return CollectionsFormatter.format(runCommand(formatCommand("log", "-1 --pretty=format:%B " + commit), environment), " ") ;
    }
    
    public String getCommitMessage(final String commit, final String delim) throws Exception {
        return CollectionsFormatter.format(runCommand(formatCommand("log", "-1 --pretty=format:%B " + commit), environment), delim) ;
    }    
    
    public String getCommitAuthorName(final String commit) throws Exception {
        return CollectionsFormatter.format(runCommand(formatCommand("log", "-1 --pretty=format:%an " + commit), environment), " ") ;
    }
    
    public String getCommitAuthorEmail(final String commit) throws Exception {
        return CollectionsFormatter.format(runCommand(formatCommand("log", "-1 --pretty=format:%ae " + commit), environment), " ") ;
    }    
    
    public String getCommitAuthorDate(final String commit) throws Exception {
        return CollectionsFormatter.format(runCommand(formatCommand("log", "-1 --pretty=format:%ad " + commit), environment), " ") ;
    }    
    
    public String getCommitAuthorRelativeDate(final String commit) throws Exception {
        return CollectionsFormatter.format(runCommand(formatCommand("log", "-1 --pretty=format:%cr " + commit), environment), " ") ;
    }
    
    public String getCommitCommitterName(final String commit) throws Exception {
        return CollectionsFormatter.format(runCommand(formatCommand("log", "-1 --pretty=format:%cn " + commit), environment), " ") ;
    }
    
    public String getCommitCommitterEmail(final String commit) throws Exception {
        return CollectionsFormatter.format(runCommand(formatCommand("log", "-1 --pretty=format:%ce " + commit), environment), " ") ;
    }
    
    
    public String getCommitCommitterDate(final String commit) throws Exception {
        return CollectionsFormatter.format(runCommand(formatCommand("log", "-1 --pretty=format:%cd " + commit), environment), " ") ;
    }    
    
    public String getCommitCommitterRelativeDate(final String commit) throws Exception {
        return CollectionsFormatter.format(runCommand(formatCommand("log", "-1 --pretty=format:%cr " + commit), environment), " ") ;
    }    
    
    public String getCommitMessageSubject(final String commit) throws Exception {
        return CollectionsFormatter.format(runCommand(formatCommand("log", "-1 --pretty=format:%s " + commit), environment), " ") ;
    } 
    
    public String getCommitMessageBody(final String commit) throws Exception {
        return CollectionsFormatter.format(runCommand(formatCommand("log", "-1 --pretty=format:%b " + commit), environment), " ") ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/    
    
    /**
     * Get all tags respecting the given parameters.
     * 
     * @param params command parameters (may be null)
     * @return a list of tags
     * @throws Exception 
     */
    public List<String> getTags(final String params) throws Exception {
        return runCommand(formatCommand("tag", params), environment) ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public final List<String> runCommand(final String cmd) throws Exception {
        return runCommand(cmd, environment, false) ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/    
    
    public final List<String> runCommand(final String cmd, final String[] envp) throws Exception {
        return runCommand(cmd, envp, false) ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/    
    
    public final List<String> runCommand(final String cmd, final String[] envp, final boolean keepEmptyLines) throws Exception {
        ProcessRunner p = new ProcessRunner(cmd, environment) ;  
        
        final LinkedList<String> lines =  new LinkedList<>() ;
        //ErrorLineProcessor err = new ErrorLineProcessor() ;
        
        // Git by default writes to standard error when doing checkout.
        
        p.run(new Processor<String>() {

            int i = 0 ;
            
            @Override
            public void process(String line) throws ProcessorException {
                if ((line.isEmpty() && keepEmptyLines) || (! line.isEmpty()))
                   lines.add(line) ;
                    
            }
        }) ;
                    
        return lines ;
    }
     
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/    
    /**
     * Formats a given command with the provided parameters.
     * @param command command to be formatted
     * @param params command parameters (may be null)
     * @return a formatted command
     * @throws Exception 
     */
    public static String formatCommand(final String command, final String params) throws Exception {
        if (command == null)
            throw new Exception("Cannot format a null command!") ;               
        
        StringBuilder cmd = new StringBuilder() ;
        cmd.append("git ").append(command).append(params != null ? (" " + params) : "") ;

        return cmd.toString() ;
    }     
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    /**
     * Gets all files that belong to a given commit.
     * @param commit hash code of the associated commit
     * @return a list of files in the commit
     * @throws Exception 
     */
    public List<String> getFiles(final String commit) throws Exception {         
        return runCommand(formatCommand("log", "--pretty=format:  --name-only -n 1 " +  commit), environment);  
    }  
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/            
    /**
     * Gets the commit of an object in the repository.
     * 
     * @param object object (SHA-1 Blob, tag, file, etc.) to which the commit hash should be retrieved
     * @return the associated commit hash
     */
    
    public String getCommitOfObject(final String object) throws Exception {      
        List<String> res = runCommand(formatCommand("log",  "--pretty=format:%H -1 " + object), environment);  
        
        if (res == null || res.isEmpty())
            return null ;
        
        return res.get(0).trim() ;        
    }   
 
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/ 
    
    //public List<String> getDescription(String commit) throws Exception {
    //    return getDescription(commit, true) ;
    //}
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    
    //public List<String> getDescription(String commit, boolean keepEmptyLines) throws Exception {
    //    return runCommand(formatCommand("log", "--pretty=format:%B -1 " + commit), environment, keepEmptyLines) ;
    //}
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public int getNumberOfBranches() throws Exception {
        
        ProcessRunner p = new ProcessRunner(formatCommand("branch", null), environment) ;
        
        ErrorLineProcessor err = new ErrorLineProcessor() ;
        final int[] count = new int[1] ;
        
        p.run(new Processor<String>() {

            @Override
            public void process(String line) throws ProcessorException {
                if (! line.trim().isEmpty())
                    count[0] ++ ;
            }
        }) ;
        
        return count[0] ;       
    }    
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/ 
    
    public String getCurrentBranch() throws Exception {
        
        ProcessRunner p = new ProcessRunner(formatCommand("branch", null), environment) ;               
        
        final String[] branch = new String[1] ;        
        p.run(new Processor<String>() {

            @Override
            public void process(String line) throws ProcessorException {
                if (line.trim().startsWith("*")) {
                    branch[0] = line.replaceFirst("\\*", "").trim() ;
                    stop();
                }
            }
        }) ;
        
        return branch[0] ;       
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/        
    /**
     * Gets all commit hashes in the repository.
     * 
     * @return list of commit hashes
     */
    public List<String> getCommits() throws Exception {
        return getCommitsByParams(null) ;
    }      
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/  

    public List<String> getCommitsFrom(final String start) throws Exception {
        return getCommitsInInterval(start, null, null) ;
    }
      
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public List<String> getCommitsInInterval(final String start, final String end) throws Exception { 
        return getCommitsInInterval(start, end, null) ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public List<String> getCommitsInInterval(final String start, final String end, final String params) throws Exception {              
        
        String endRelease = end ;
        
        if (endRelease == null) {
            endRelease = getLastCommit() ;
        }
        
        List<String> res ;
        if (params != null)
            res = runCommand(formatCommand("log",  "--pretty=format:%H " + params + " " + Params.Log.range(start, endRelease)), environment);  
        else
            res = runCommand(formatCommand("log",  "--pretty=format:%H " +  Params.Log.range(start, endRelease)), environment); 
        
        if (res.size() > 0) {
            if (res.get(0).equals(endRelease)) {
                /* Not reversed. */
                res.add(res.size() - 1, start) ;
            }
            else
                /* Reversed. */
               res.add(0, start);
        }        
        
        
        return res ;
    } 
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/ 
    
    public String getLastCommit() throws Exception {
        List<String> res = runCommand(formatCommand("log",  "--pretty=format:%H -1"), environment);  
        return res.get(0).trim() ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/    
    /**
     * Gets all commit hashes in the repository using the provided parameters.
     * 
     * @param params command parameters (--pretty is ignored; <code>params</code> may be null)
     * @return list of commit hashes formatted as "commiter time:hash code"
     */        
    public List<String> getCommitsByParams(final String params) throws Exception {

        ProcessRunner p ;
        if (params != null)
            p = new ProcessRunner(formatCommand("log",  "--pretty=format:%H " + params), environment);        
        else
            p = new ProcessRunner(formatCommand("log",  "--pretty=format:%H"), environment);             
        
        final LinkedList<String> commits = new LinkedList<String>() ;
        
        p.run(new Processor<String>() {

            int i = 0 ;
            
            @Override
            public void process(String line) throws ProcessorException {
                if (! line.isEmpty()) {
                    commits.add(line.trim()) ;
                }
                    
            }
        });
        
        return commits ;
    }    
        
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public List<String> getPatch(final String commit) throws Exception {
        return getPatch(commit, null) ;
    }
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public List<String> getPatch(final String commit, final String params) throws Exception {
        
        if (params != null)
            return runCommand(formatCommand("log", "--pretty=format: -p -n 1 " + commit + " " + params), environment); 
                
        return runCommand(formatCommand("log", "--pretty=format: -p -n 1 " + commit), environment); 
            
    }    
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/

    public List<String> getFilePatch(final String commit, final String file) throws Exception {
        return getFilePatch(commit, file, null) ;
    }    
              
    /**
     * Gets the patch of a file in a given commit.
     * 
     * @param commit commit hash code
     * @param file the file to which the patch refers to
     * @param params command parameters 
     * @return the string lines that comprise the patch
     * @throws Exception 
     */
    public List<String> getFilePatch(final String commit, final String file, final String params) throws Exception { 
                
        if (params != null)
            return runCommand(formatCommand("log", "--pretty=format: -p -n 1 " + commit + " " + params + " -- " + file), environment); 
                
        return runCommand(formatCommand("log", "--pretty=format: -p -n 1 " + commit + " -- " + file), environment); 
    }
    
 
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    /**
     * Get all commit hashes starting from an initial tag's commit hash.
     * @param initialTag name of the initial tag
     * @return a list of all commit hashes starting from the initial tag
     * @throws Exception 
     */
    public List<String> getCommitsFromTag(final String initialTag) throws Exception {
        return getCommitsFrom(getCommitOfObject(initialTag)) ;
    }  
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/    
    /**
     * Gets all commit hashes from an initial tag's commit hash up to (inclusive) a
     * tag's commit end tag's commit hash.
     * 
     * @param initialTag initialTag name of initial tag
     * @param endTag name of the end tag
     * @return a list of all commit hashes in the tag interval
     * @throws Exception 
     */
    public List<String> getCommitsInTagInterval(final String initialTag, final String endTag) throws Exception {
        return getCommitsInInterval(getCommitOfObject(initialTag) , getCommitOfObject(endTag), null) ; 
    } 
    
    /**
     * Gets all commit hashes from an initial tag's commit hash up to (exclusive) a
     * tag's commit end tag's commit hash using the provided command parameters.
     * 
     * @param initialTag initialTag  name of initial tag
     * @param endTag name of the end tag
     * @param params command parameters (may be null)
     * @return a list of commit hashes in the hash interval, given the provided parameters
     * @throws Exception 
     */
    public List<String> getCommitsInTagInterval(final String initialTag, final String endTag, final String params) throws Exception {        
        return getCommitsInInterval(getCommitOfObject(initialTag), getCommitOfObject(endTag), params) ;  
    }       
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public void checkout(final String branchName) throws Exception {        
        checkout(branchName, true) ;
    }   
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public void checkout(final String branchName, final boolean force) throws Exception {        
        if (force)
                runOfflineCommand(formatCommand("checkout",  "-f " + branchName), environment);
        else
            runOfflineCommand(formatCommand("checkout",  branchName), environment);
    }        
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public void checkout(final String obj, final String branchName) throws Exception {
        checkout(obj, branchName, true) ; 
    }  
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public void checkout(final String obj, final String branchName, final boolean force) throws Exception {
        if (force)
            runOfflineCommand(formatCommand("checkout",  " -f " + obj + " -b " + branchName), environment); 
        else
            runOfflineCommand(formatCommand("checkout",  obj + " -b " + branchName), environment); 
    }      
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public void deleteBranch(final String branchName) throws Exception {
        runOfflineCommand(formatCommand("branch ",  "-D " + branchName), environment);
    } 
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    private void runOfflineCommand(final String cmd, final String[] environment) throws Exception {
        ProcessRunner p = new ProcessRunner(cmd, environment);
        p.run() ;  
    }    
    
    /* . . . . . . . . . . . . . . . . . . . . . . . . . . . .*/
    
    public String[] getEnvironment() {
        return environment;
    }
            
    /* . . . . . . .  . . . . . . . . . . . . . . . . . . . .*/
    public File getRepository() {
        return repository;
    }
    
    /* . . . . . . .  . . . . . . . . . . . . . . . . . . . .*/

    public List<String> getAcceptingTags(final String commit) throws Exception {
        return runCommand(formatCommand("tag",  Params.Tag.CONTAINS + " " + commit), environment);  
    }
    
    /* . . . . . . .  . . . . . . . . . . . . . . . . . . . .*/
 
    public static boolean isInGitRepo(final String f) {
        return isInGitRepo(new File(f)) ;
    }
    
    /* . . . . . . .  . . . . . . . . . . . . . . . . . . . .*/
    
    public static boolean isInGitRepo(final File f) {
        String path = f.getAbsolutePath() ;
        
        return path.startsWith(".git") || 
               path.endsWith(Separator.DIR_PATH + ".git") ||
               path.contains(Separator.DIR_PATH + ".git" + Separator.DIR_PATH) ;               
    }    
}
