/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quorum.vm.implementation;

import java.util.Stack;
import org.objectweb.asm.Label;

/**
 * The check-detect descriptor contains information related specifically to
 * bytecode generation of these constructs. Primarily,  it contains:
 * 
 * 1. The start and end label of all detect statements.
 * 2. The start and end label of the check statement.
 * 3. The start and end label of the always statement.
 * 4. The physical Quorum Bytecode position of the start of the always block.
 * 
 * @author jeff
 */
public class CheckDetectDescriptor {
    private Label checkStart = new Label();
    private Label checkEnd = new Label();
    private Label alwaysStart = new Label();
    private Label alwaysEnd = new Label();
    private Label constructEnd = new Label();
    private int alwaysStartPosition = -1;
    private Stack<Label> detectStarts = new Stack<Label>();
    private Stack<Label> detectEnds = new Stack<Label>();
    private boolean hasAlways = false;
    
    public Label pushDetectStartLabel() {
        Label l = new Label();
        detectStarts.push(l);
        
        return l;
    }
    
    public Label pushDetectEndLabel() {
        Label l = new Label();
        detectEnds.push(l);
        
        return l;
    }
    
    public Label getNextDetectStartLabel() {
        return detectStarts.pop();
    }
    
    public Label getNextDetectEndLabel() {
        return detectEnds.pop();
    }
    
    /**
     * @return the checkStart
     */
    public Label getCheckStart() {
        return checkStart;
    }

    /**
     * @param checkStart the checkStart to set
     */
    public void setCheckStart(Label checkStart) {
        this.checkStart = checkStart;
    }

    /**
     * @return the checkEnd
     */
    public Label getCheckEnd() {
        return checkEnd;
    }

    /**
     * @param checkEnd the checkEnd to set
     */
    public void setCheckEnd(Label checkEnd) {
        this.checkEnd = checkEnd;
    }

    /**
     * @return the alwaysStart
     */
    public Label getAlwaysStart() {
        return alwaysStart;
    }

    /**
     * @param alwaysStart the alwaysStart to set
     */
    public void setAlwaysStart(Label alwaysStart) {
        this.alwaysStart = alwaysStart;
    }

    /**
     * @return the alwaysEnd
     */
    public Label getAlwaysEnd() {
        return alwaysEnd;
    }

    /**
     * @param alwaysEnd the alwaysEnd to set
     */
    public void setAlwaysEnd(Label alwaysEnd) {
        this.alwaysEnd = alwaysEnd;
    }

    /**
     * @return the hasAlways
     */
    public boolean isHasAlways() {
        return hasAlways;
    }

    /**
     * @param hasAlways the hasAlways to set
     */
    public void setHasAlways(boolean hasAlways) {
        this.hasAlways = hasAlways;
    }

    /**
     * @return the constructEnd
     */
    public Label getConstructEnd() {
        return constructEnd;
    }

    /**
     * @param constructEnd the constructEnd to set
     */
    public void setConstructEnd(Label constructEnd) {
        this.constructEnd = constructEnd;
    }

    /**
     * @return the alwaysStartPosition
     */
    public int getAlwaysStartPosition() {
        return alwaysStartPosition;
    }

    /**
     * @param alwaysStartPosition the alwaysStartPosition to set
     */
    public void setAlwaysStartPosition(int alwaysStartPosition) {
        this.alwaysStartPosition = alwaysStartPosition;
    }
}
