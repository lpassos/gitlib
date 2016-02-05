/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.test.utils;

import git.Git;

/**
 *
 * @author leonardo
 */
public class GitRepo {
    public static Git get(String repo) throws Exception {       
        return new Git(repo); 
    }   
}
