package de.bbcdaas.taghandler.reader;

import de.bbcdaas.common.beans.Term;
import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.beans.entity.TermCloudField;
import de.bbcdaas.taghandler.TagHandlerImpl;
import de.bbcdaas.taghandler.exception.ProcessException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * File reader for a specific file format used to store xing crawled user data.
 * @author Christian Herta
 * @author Robert Illers
 */
public final class XingEntityFileReader extends AbstractEntityFileReader {

	private String inputFileName;
	private String ENTITYNAME_TO_FIELDS_SEPARATOR = "::";
    private String FIELDNAME_TO_FIELDS_SEPARATOR;
	private String TERM_TO_TERM_SEPARATOR = ",";
    private String FIELD_TO_FIELD_SEPARATOR = ";";

	/**
	 *
	 * @param inputFileName
	 */
	public void setInputFileName(String inputFileName) {
		this.inputFileName = inputFileName;
		scanner = null;
	}

	/**
	 * injected
	 * @param nameToFieldsSeparator
	 */
	public void setEntityNameToFieldsSeparator(String nameToFieldsSeparator) {
		ENTITYNAME_TO_FIELDS_SEPARATOR = nameToFieldsSeparator;
	}

    /**
     *
     * @return
     */
    public String getEntityNameToFieldsSeparator() {
        return this.ENTITYNAME_TO_FIELDS_SEPARATOR;
    }

	/**
     *
     * @param termToTermSeparator
     */
	public void setTermToTermSeparator(String termToTermSeparator) {
		TERM_TO_TERM_SEPARATOR = termToTermSeparator;
	}

    /**
     *
     * @return
     */
    public String getTermToTermSeparator() {
        return this.TERM_TO_TERM_SEPARATOR;
    }

    /**
     *
     * @param fieldNameToFieldsSeparator
     */
    public void setFieldNameToFieldsSeparator(String fieldNameToFieldsSeparator) {
        this.FIELDNAME_TO_FIELDS_SEPARATOR = fieldNameToFieldsSeparator;
    }

    /**
     *
     * @param fieldToFieldSeparator
     */
    public void setFieldToFieldSeparator(String fieldToFieldSeparator) {
        this.FIELD_TO_FIELD_SEPARATOR = fieldToFieldSeparator;
    }

    /**
     *
     * @return
     */
    public String getFieldToFieldSeparator() {
        return this.FIELD_TO_FIELD_SEPARATOR;
    }

	/**
	 * Gets a list of entities from a file.
	 * @return List<Entity>
	 */
	@Override
	public List<Entity> readEntities() throws ProcessException {

		List<Entity> entities = new ArrayList<Entity>();

		this.openReader(inputFileName);

		long nbOfCurrentReadEntities = 0;
		// read one read-step of entities
		while (scanner.hasNext() && nbOfCurrentReadEntities < ENTITY_READ_STEP) {

			// split line into name and terms of field "i_offer" if possible
			String[] rawEntity = scanner.next().split(ENTITYNAME_TO_FIELDS_SEPARATOR);
			if (rawEntity.length == 2) {
				String name = rawEntity[0];
                String[] rawFields;
                // use fieldNames? ( == FIELDNAME_TO_FIELDS_SEPARATOR set?)
                if (FIELDNAME_TO_FIELDS_SEPARATOR != null) {
                    // yes, get all fields
                    rawFields = rawEntity[1].split(FIELD_TO_FIELD_SEPARATOR);
                } else {
                    // no, get the only one field
                    rawFields = new String[1];
                    rawFields[0] = rawEntity[1];
                }
                List<TermCloudField> fields = new ArrayList<TermCloudField>();
                // iterate through fields
                for (int i = 0; i < rawFields.length; i++) {

                    String fieldType = null;
                    String rawTermsString = null;
                    // if fieldNames used, extract the fieldName
                    if (FIELDNAME_TO_FIELDS_SEPARATOR != null) {
                        String[] rawField = rawFields[i].split(FIELDNAME_TO_FIELDS_SEPARATOR);
                        if (rawField.length == 2) {
                            fieldType = rawField[0];
                            rawTermsString = rawField[1];
                        }
                    } else {
                        // no fieldname -> only get terms
                        rawTermsString = rawFields[i];
                    }

                    // terms found
                    if (rawTermsString != null) {

                        // create a term list object
                        List<String> rawTermValues = new ArrayList<String>(Arrays.
                            asList(rawTermsString.split(TERM_TO_TERM_SEPARATOR)));
                        List<Term> rawTerms = new ArrayList<Term>();
                        for (String value : rawTermValues) {
                            rawTerms.add(new Term(value));
                        }
                        // create a field
                        TermCloudField field = new TermCloudField();
                        if (fieldType != null) {
                            field.setFieldType(Integer.parseInt(fieldType));
                        } else {
                            field = new TermCloudField();
                        }
                        field.setTerms(rawTerms);
                        // add field to list of entities fields
                        fields.add(field);
                    }
                }

                // create the entity and add the fields
                if (!fields.isEmpty()) {

                    Entity entity = new Entity();
                    entity.setName(name);
                    entity.setFields(fields);
                    entities.add(entity);
                    nbOfCurrentReadEntities++;
                }
			}
			numberOfTotalReadLines++;
			percentageRead = TagHandlerImpl.reportProgress(numberOfTotalReadLines,
				numberOfLines, percentageRead);
		}
		return entities;
	}
}
