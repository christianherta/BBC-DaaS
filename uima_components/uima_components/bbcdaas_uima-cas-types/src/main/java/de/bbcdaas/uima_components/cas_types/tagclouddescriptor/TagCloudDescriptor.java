package de.bbcdaas.uima_components.cas_types.tagclouddescriptor;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.cas.TOP;

/** 
 * Updated by JCasGen Fri Apr 20 10:13:20 CEST 2012
 * Updated by Robert Illers 
 * XML source: ../tagCloudDescriptor.xml
 * @generated 
 */
public class TagCloudDescriptor extends TOP {
  
	/** 
	 * @generated
	 * @ordered 
	 */
	@SuppressWarnings ("hiding")
	public final static int typeIndexID = JCasRegistry.register(TagCloudDescriptor.class);
  
	/** @generated
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
	 * Never called.  Disable default constructor
	 * @generated 
	 */
	protected TagCloudDescriptor() {
		/* intentionally empty block */
	}
    
	/**
	 * Internal - constructor used by generator 
	 * @generated
	 */
	public TagCloudDescriptor(int addr, TOP_Type type) {
		
		super(addr, type);
		readObject();
	}
  
	/**
	 * @generated
	 */
	public TagCloudDescriptor(JCas jcas) {
		
		super(jcas);
		readObject();   
	} 

	/**
	 * <!-- begin-user-doc -->
	 * Write your own initialization here
	 * <!-- end-user-doc -->
	 * @generated modifiable
	 */
	private void readObject() {
		/*default - does nothing empty block */
	}
     
	//*--------------*
	//* Feature: Tags

	/** 
	 * getter for Tags - gets 
	 * @generated
	 */
	public String getTags() {
		
		if (TagCloudDescriptor_Type.featOkTst &&
			((TagCloudDescriptor_Type)jcasType).casFeat_Tags == null) {
			
			jcasType.jcas.throwFeatMissing("Tags", TagCloudDescriptor.class.getName());
		}
		return jcasType.ll_cas.ll_getStringValue(addr, ((TagCloudDescriptor_Type)jcasType).casFeatCode_Tags);
	}
    
	/** 
	 * setter for Tags - sets  
	 * @generated
	 */
	public void setTags(String v) {
    
		if (TagCloudDescriptor_Type.featOkTst &&
			((TagCloudDescriptor_Type)jcasType).casFeat_Tags == null) {
      
			jcasType.jcas.throwFeatMissing("Tags", TagCloudDescriptor.class.getName());
		}
		jcasType.ll_cas.ll_setStringValue(addr, ((TagCloudDescriptor_Type)jcasType).casFeatCode_Tags, v);
	}    
   
	//*--------------*
	//* Feature: CloudName

	/** 
	 * getter for CloudName - gets 
	 * @generated
	 */
	public String getCloudName() {
    
		if (TagCloudDescriptor_Type.featOkTst &&
			((TagCloudDescriptor_Type)jcasType).casFeat_CloudName == null) {
			
			jcasType.jcas.throwFeatMissing("CloudName", TagCloudDescriptor.class.getName());
		}
		return jcasType.ll_cas.ll_getStringValue(addr, ((TagCloudDescriptor_Type)jcasType).casFeatCode_CloudName);
	}
    
	/** setter for CloudName - sets  
	 * @generated
	 */
	public void setCloudName(String v) {
    
		if (TagCloudDescriptor_Type.featOkTst &&
			((TagCloudDescriptor_Type)jcasType).casFeat_CloudName == null) {
      
			jcasType.jcas.throwFeatMissing("CloudName", TagCloudDescriptor.class.getName());
		}
		jcasType.ll_cas.ll_setStringValue(addr, ((TagCloudDescriptor_Type)jcasType).casFeatCode_CloudName, v);
	}    
}    