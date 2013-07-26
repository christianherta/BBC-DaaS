package de.bbcdaas.uima_components.cas_types.filelinemetadata;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.cas.TOP_Type;

/**
 *
 * @author Robert Illers
 */
public class FileLineMetadata extends TOP {
	
	/**
	 * @generated
	 * @ordered
	 */
	@SuppressWarnings ("hiding")
	public final static int typeIndexID = JCasRegistry.register(FileLineMetadata.class);

	/**
	 * @generated
	 * @ordered
	 */
	@SuppressWarnings ("hiding")
	public final static int type = typeIndexID;
	
	/**
	 * @generated
	 */
	@Override
	public int getTypeIndexID() {
		return typeIndexID;
	}

	/**
	 * Never called. Disable default constructor
	 * @generated
	 */
	protected FileLineMetadata() {
		/* intentionally empty block */
	}
	
	/**
	 * Internal - constructor used by generator
	 * @generated
	 */
	public FileLineMetadata(int addr, TOP_Type type) {
		
		super(addr, type);
		readObject();
	}

	/**
	 * @generated
	 */
	public FileLineMetadata(JCas jcas) {
		
		super(jcas);
		readObject();
	}
	
	/** 
	 * <!-- begin-user-doc -->
	 * Write your own initialization here
	 * <!-- end-user-doc -->
	 * @generated
	 * @modifiable 
	 */
	private void readObject() {
		/*default - does nothing empty block */
	}
	
	//*--------------*
	//* Feature: lineNumber

	/** 
	 * getter for lineNumber
	 * @generated
	 */
	public String getLineNumber() {
		
		if (FileLineMetadata_Type.featOkTst && ((FileLineMetadata_Type)jcasType).casFeat_lineNumber == null) {
			jcasType.jcas.throwFeatMissing("lineNumber", FileLineMetadata.class.getName());
		}
	 return jcasType.ll_cas.ll_getStringValue(addr, ((FileLineMetadata_Type)jcasType).casFeatCode_lineNumber);}

	/** 
	 * setter for lineNumber
	 * @generated
	 */
	public void setLineNumber(String v) {
		
		if (FileLineMetadata_Type.featOkTst && ((FileLineMetadata_Type)jcasType).casFeat_lineNumber == null) {
			jcasType.jcas.throwFeatMissing("lineNumber", FileLineMetadata.class.getName());
		}
		jcasType.ll_cas.ll_setStringValue(addr, ((FileLineMetadata_Type)jcasType).casFeatCode_lineNumber, v);
	}
}
