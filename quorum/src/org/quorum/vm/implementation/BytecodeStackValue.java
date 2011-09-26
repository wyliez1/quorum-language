/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.quorum.vm.implementation;

import org.objectweb.asm.Opcodes;
import org.quorum.execution.ExpressionValue;
import org.quorum.symbols.Result;
import org.quorum.symbols.TypeDescriptor;

/**
 * This class is a wrapper class for containing variables and constants
 * in the conversion between Quorum types and Java bytecode values.
 * 
 * @author Andreas Stefik and Matt Lawson
 */
public class BytecodeStackValue {
    //private Result result;
    private TypeDescriptor type = new TypeDescriptor();
    private int varNumber = 0;
    private boolean isConstant = true; 
    private Result result = new Result();
    private boolean isReturnValue = false;
    private String varName = "";
    
    public Result getResult() {
        return result;
    }
    
    /**
     * Returns the type descriptor for a constant or variable.
     * 
     * @return 
     */
    public TypeDescriptor getType() {
        return type;
    }
    
    public int getVarNumber() {
        return varNumber;
    }
    
    public String getVarName() {
        return varName;
    }
    
    public boolean isReturnValue() {
        return isReturnValue;
    }
    
    /**
     * Determines whether or not the value currently placed is a fixed
     * constant or whether its value must be computed at runtime.
     * 
     * @return 
     */
    public boolean isConstant() {
        return isConstant;
    }
    
    public void setType (TypeDescriptor type) {
        this.type = type;
    }
    
    public void setResult (Result result) {
        this.result = result;
    }
    
    public void setVarNumber (int varNumber) {
        this.varNumber = varNumber;
    }
    
    public void setAsVariable(int varNumber) {
        isConstant = false;
        this.varNumber = varNumber;
    }
    
    public void setName(String name) {
        this.varName = name;
    }
    
    public void setAsConstant() {
        isConstant = true;
    }
    
    public void setAsReturnValue(boolean isReturnValue) {
        this.isReturnValue = isReturnValue;
    }
    
//    temporary
    public Object getValue(){
        if (type.isBoolean())
            return result.boolean_value;
        else if (type.isInteger())
            return result.integer;
        else if (type.isNumber())
            return result.number;
        else if (type.isText())
            return result.text;
        else
            return null;
    }
    
    /**
     * This method returns the correct opcode for loading a particular 
     * value onto the stack. 
     */
    public int getLoadOpcode() {
        return QuorumConverter.getLoadOpcode(type);
    }
    
    public int getStoreOpcode() {
        return QuorumConverter.getStoreOpcode(type);
    }
    
    /**
     * This method returns the raw String representation in java bytecode
     * of a particular Quorum type. For example, a type boolean
     * (primitive), would be Z, while a text (primitive), would be
     * Ljava/lang/String;
     * 
     * @return The string representation of the type in java bytecode.
     */
    public String getByteCodeTypeDescriptor() {
        return QuorumConverter.convertTypeToBytecodeString(type);
    }

    /**
     * This value computes the size of the object being pushed on the stack
     * from its type, to help with computations related to max stack size
     * in methods.
     * 
     * @return 
     */
    public int getSize() {
        return getSize(type);
    }
    
    /**
     * This value computes the size of the object being pushed on the stack
     * from its type, to help with computations related to max stack size
     * in methods.
     * 
     * @param type
     * @return 
     */
    public static int getSize(TypeDescriptor type) {
        if(!type.isPrimitiveType()) {
            return 1;
        }
        
        if(type.isNumber()) {
            return 2;
        }
//        if(type.isText()) {
//            return 2;
//        }
        
        return 1;
    }
}
