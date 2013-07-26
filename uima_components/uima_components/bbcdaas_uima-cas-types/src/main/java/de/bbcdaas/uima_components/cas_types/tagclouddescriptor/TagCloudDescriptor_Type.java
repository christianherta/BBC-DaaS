/* First created by JCasGen Fri Apr 20 10:13:20 CEST 2012 */
package de.bbcdaas.uima_components.cas_types.tagclouddescriptor;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.cas.TOP_Type;

/** 
 * Updated by JCasGen Fri Apr 20 10:13:20 CEST 2012
 * Updated by Robert Illers
 * @generated 
 */
public class TagCloudDescriptor_Type extends TOP_Type {
 
	/**
	 * @generated
	 */
	@Override
	protected FSGenerator getFSGenerator() {
		return fsGenerator;
	}
  
	/** 
	 * @generated
	 */
	private final FSGenerator fsGenerator = new FSGenerator() {
		
		public FeatureStructure createFS(int addr, CASImpl cas) {
  			 
			if (TagCloudDescriptor_Type.this.useExistingInstance) {
				// Return eq fs instance if already created
				FeatureStructure fs = TagCloudDescriptor_Type.this.jcas.getJfsFromCaddr(addr);
				if (null == fs) {
					
					fs = new TagCloudDescriptor(addr, TagCloudDescriptor_Type.this);
					TagCloudDescriptor_Type.this.jcas.putJfsFromCaddr(addr, fs);
					return fs;
				}
				return fs;
			} else {
				return new TagCloudDescriptor(addr, TagCloudDescriptor_Type.this);
			}
		}
	};
  
	/**
	 * @generated
	 */
	@SuppressWarnings ("hiding")
	public final static int typeIndexID = TagCloudDescriptor.typeIndexID;
  
	/** 
	 * @generated 
	 * @modifiable
	 */
	@SuppressWarnings ("hiding")
	public final static boolean featOkTst = JCasRegistry.getFeatOkTst("de.bbcdaas.taghandler.uima.impl.xing.cas.TagCloudDescriptor");
 
	/**
	 * @generated
	 */
	final Feature casFeat_Tags;
  
	/**
	 * @generated
	 */
	final int casFeatCode_Tags;
  
	/**
	 * @generated
	 */ 
	public String getTags(int addr) {
        
		if (featOkTst && casFeat_Tags == null) {
			jcas.throwFeatMissing("Tags", TagCloudDescriptor.class.getName());
		}
		return ll_cas.ll_getStringValue(addr, casFeatCode_Tags);
	}
	
	/**
	 * @generated
	 */    
	public void setTags(int addr, String v) {
		
		if (featOkTst && casFeat_Tags == null) {
			jcas.throwFeatMissing("Tags", TagCloudDescriptor.class.getName());
		}
		ll_cas.ll_setStringValue(addr, casFeatCode_Tags, v);
	}
    
	/**
	 * @generated
	 */
	final Feature casFeat_CloudName;
	
	/**
	 * @generated
	 */
	final int casFeatCode_CloudName;
 
	/**
	 * @generated
	 */ 
	public String getCloudName(int addr) {
		
		if (featOkTst && casFeat_CloudName == null) {
			jcas.throwFeatMissing("CloudName", TagCloudDescriptor.class.getName());
		}
		return ll_cas.ll_getStringValue(addr, casFeatCode_CloudName);
	}
	
	/**
	 * @generated
	 */    
	public void setCloudName(int addr, String v) {
        
		if (featOkTst && casFeat_CloudName == null) {
			jcas.throwFeatMissing("CloudName", TagCloudDescriptor.class.getName());
		}
		ll_cas.ll_setStringValue(addr, casFeatCode_CloudName, v);
	}

  
	/**
	 * initialize variables to correspond with Cas Type and Features
	 * @generated
	 */
	public TagCloudDescriptor_Type(JCas jcas, Type casType) {
    
		super(jcas, casType);
		casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

		casFeat_Tags = jcas.getRequiredFeatureDE(casType, "Tags", "uima.cas.String", featOkTst);
		casFeatCode_Tags  = (null == casFeat_Tags) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_Tags).getCode();
		
		casFeat_CloudName = jcas.getRequiredFeatureDE(casType, "CloudName", "uima.cas.String", featOkTst);
		casFeatCode_CloudName  = (null == casFeat_CloudName) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_CloudName).getCode();
  }
}