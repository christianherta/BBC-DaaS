package de.bbcdaas.taghandler.writer;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.beans.entity.TermCloudField;
import de.bbcdaas.common.util.FileWriter;
import java.util.List;
/**
 * Implementation of the EntityWriter Interface. This writer creates a file that
 * contains all read entities with the following pattern:
 * EntityName::FieldName:term1,term2;FieldName2:term1,term2;
 * @author Christian Herta
 * @author Robert Illers
 */
public final class SimpleEntityFileWriter implements EntityWriter {
	
	private String ENTITYNAME_TO_FIELDS_SEPARATOR = "::";
    private String FIELDNAME_TO_TERMS_SEPARATOR = ":";
    private String TERM_TO_TERM_SEPARATOR = ",";
    private String FIELD_TO_FIELD_SEPARATOR = ";";
	private String outputFileName = null;
	private boolean append = false;
    private boolean useSingleTermLexicon = true;

    /**
     * 
     * @param useSingleTermLexicon 
     */
    @Override
    public void setUseSingleTermLexicon(boolean useSingleTermLexicon) {
        this.useSingleTermLexicon = useSingleTermLexicon;
    }
    
	/**
     * Setter for the separator between the entity name and the list of fields
     * @param entityNameToFieldsSeparator 
     */
	public void setEntityNameToFieldsSeparator(String entityNameToFieldsSeparator) {
		ENTITYNAME_TO_FIELDS_SEPARATOR = entityNameToFieldsSeparator;
	}
	
    /**
     * Setter for the separator between the field names and the list of field terms
     * @param fieldNameToTermsSeparator 
     */
	public void setFieldNameToTermsSeparator(String fieldNameToTermsSeparator) {
		FIELDNAME_TO_TERMS_SEPARATOR = fieldNameToTermsSeparator;
	}
    
    /**
     * Setter for the separator between the field terms
     * @param termToTermSeparator 
     */
	public void setTermToTermSeparator(String termToTermSeparator) {
		TERM_TO_TERM_SEPARATOR = termToTermSeparator;
	}
    
    /**
     * Setter for the separator between the fields
     * @param fieldToFieldSeparator 
     */
	public void setFieldToFieldSeparator(String fieldToFieldSeparator) {
		FIELD_TO_FIELD_SEPARATOR = fieldToFieldSeparator;
	}
    
	/**
	 * Setter for the name of the file the output should be written
	 * @param outputFileName 
	 */
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	
	/**
	 * Setter of the number of entities that should be written. If this value is
	 * set to 0, all entities will be written.
	 * @param setNbOfTotalReadEntities 
	 */
	@Override
	public void setNbOfTotalReadEntities(float setNbOfTotalReadEntities) {
		append = setNbOfTotalReadEntities != 0;
	}
	
	/**
	 * Starts the write process. 
	 * @param entities 
	 */
	@Override
	public void writeEntities(List<Entity> entities) {
		
		FileWriter writer = new FileWriter();
		writer.openFile(outputFileName, append);
		
        for (Entity entity : entities) {
			
            StringBuilder line = new StringBuilder();
            // write entity name
			line.append(entity.getName()).append(ENTITYNAME_TO_FIELDS_SEPARATOR);
            
            for (TermCloudField field : entity.getFields()) {
                // write field type
                line.append(field.getFieldType()).append(FIELDNAME_TO_TERMS_SEPARATOR);
                // write the terms of the field
                for (Term term : field.getTerms()) {
                    line.append(term.getValue()).append(TERM_TO_TERM_SEPARATOR);
                }
                line.deleteCharAt(line.length()-1).append(FIELD_TO_FIELD_SEPARATOR);
            }
			writer.println(line.toString());
		}
        
		writer.closeFile();
	}
}
