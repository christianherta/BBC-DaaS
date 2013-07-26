package de.bbcdaas.uima_components.cas_types.filelinemetadata;

import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

/**
 *
 * @author Robert Illers
 */
public class FileLineMetadata_Type extends TOP_Type {
	
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
      
		@Override
		public FeatureStructure createFS(int addr, CASImpl cas) {
  			 
			if (FileLineMetadata_Type.this.useExistingInstance) {
				
				// Return eq fs instance if already created
				FeatureStructure fs = FileLineMetadata_Type.this.jcas.getJfsFromCaddr(addr);
  		     
				if (null == fs) {
					
					fs = new FileLineMetadata(addr, FileLineMetadata_Type.this);
					FileLineMetadata_Type.this.jcas.putJfsFromCaddr(addr, fs);
					return fs;
				}
				return fs;
			} else {
				return new FileLineMetadata(addr, FileLineMetadata_Type.this);
			}
		}
	};
	
	/**
	 * @generated
	 */
	@SuppressWarnings ("hiding")
	public final static int typeIndexID = FileLineMetadata.typeIndexID;
  
	/**
	 * @generated 
	 * @modifiable
	 */
	@SuppressWarnings ("hiding")
	public final static boolean featOkTst = JCasRegistry.getFeatOkTst(FileLineMetadata.class.getName());
	
	/**
	 * @generated
	 */
	final Feature casFeat_lineNumber;
  
	/** 
	 * @generated
	 */
	final int casFeatCode_lineNumber;
	
	/**
	 * @generated
	 */ 
	public String getLineNumber(int addr) {
        
		if (featOkTst && casFeat_lineNumber == null) {
			jcas.throwFeatMissing("lineNumber", FileLineMetadata.class.getName());
		}
		return ll_cas.ll_getStringValue(addr, casFeatCode_lineNumber);
	}
	
	/**
	 * @generated
	 */    
	public void setLineNumber(int addr, String v) {
		
		if (featOkTst && casFeat_lineNumber == null) {
			jcas.throwFeatMissing("lineNumber", FileLineMetadata.class.getName());
		}
		ll_cas.ll_setStringValue(addr, casFeatCode_lineNumber, v);
	}
	
	/** 
	 * initialize variables to correspond with Cas Type and Features
	 * @generated
	 */
	public FileLineMetadata_Type(JCas jcas, Type casType) {
		
		super(jcas, casType);
		casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

		casFeat_lineNumber = jcas.getRequiredFeatureDE(casType, "lineNumber", "uima.cas.String", featOkTst);
		casFeatCode_lineNumber  = (null == casFeat_lineNumber) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_lineNumber).getCode();
	}
}
