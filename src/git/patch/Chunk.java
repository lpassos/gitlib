/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package git.patch;

import java.util.LinkedList;
import java.util.List;

/**
 * A class to process chunks of a diff.
 * 
 * @author leonardo
 */
public class Chunk {
    protected int lineA ;
    protected int numberOfLinesA ;
    protected int lineB ;
    protected int numberOfLinesB ;    
    protected List<DiffLine> diffs ;

    public Chunk() {
        diffs = new LinkedList<DiffLine>() ;
    }
    
    public Chunk(int lineA, int numberOfLinesA, int lineB, int numberOfLinesB) {
        initChunk(lineA, numberOfLinesA, lineB, numberOfLinesB, new LinkedList<DiffLine>()) ;
    }
    
    public Chunk(int lineA, int numberOfLinesA, int lineB, int numberOfLinesB, List<DiffLine> lines) {
        initChunk(lineA, numberOfLinesA, lineB, numberOfLinesB, lines) ;
    }
    
    private void initChunk(int lineA, int numberOfLinesA, int lineB, int numberOfLinesB, List<DiffLine> lines) {
        this.lineA= lineA;
        this.lineB= lineB;
        this.numberOfLinesA = numberOfLinesA;
        this.numberOfLinesA = numberOfLinesB;  
    }

    public List<DiffLine> getLines() {
        return diffs;
    }

    public int getLineA() {
        return lineA;
    }

    public int getLineB() {
        return lineB;
    }

    public int getNumberOfLinesA() {
        return numberOfLinesA;
    }

    public int getNumberOfLinesB() {
        return numberOfLinesB;
    }   
}
