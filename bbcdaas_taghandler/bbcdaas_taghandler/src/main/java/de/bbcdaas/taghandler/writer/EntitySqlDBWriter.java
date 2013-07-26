package de.bbcdaas.taghandler.writer;

import de.bbcdaas.common.beans.entity.Entity;
import de.bbcdaas.common.dao.exceptions.DaoException;
import de.bbcdaas.taghandler.dao.TagHandlerDao;
import de.bbcdaas.taghandler.dao.TermLexiconDao;
import de.bbcdaas.taghandler.exception.ProcessException;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Implementation of the EntityWriter Interface that writes Entities read by an
 * EntityReader in an SQL Database.
 * @author Robert Illers
 */
public class EntitySqlDBWriter implements EntityWriter {

	private TagHandlerDao taghandlerDao;
	private TermLexiconDao termLexiconDao;
	private Logger logger = Logger.getLogger(this.getClass());
    private boolean useSingleTermLexicon = true;

	/**
	 * Setter for the Data Access Object that persists the taghandler data.
	 * @param taghandlerDao
	 */
	public void setTaghandlerDao(TagHandlerDao taghandlerDao) {
		this.taghandlerDao = taghandlerDao;
	}

	/**
	 * Setter for the Data Access Object that persists the termLexicon data.
	 * @param termLexiconDao
	 */
	public void setTermLexiconDao(TermLexiconDao termLexiconDao) {
		this.termLexiconDao = termLexiconDao;
	}

    /**
     *
     * @param useSingleTermLexicon
     */
    @Override
    public void setUseSingleTermLexicon(boolean useSingleTermLexicon) {
        this.useSingleTermLexicon = useSingleTermLexicon;
    }

	/**
	 * Sets the number of entities that should be read from the source. If set to
	 * 0, all entities will be written.
	 * @param setNbOfTotalReadEntities
	 */
	@Override
	public void setNbOfTotalReadEntities(float setNbOfTotalReadEntities) {}

	/**
	 * Starts the write process.
	 * @param entities
	 */
	@Override
	public void writeEntities(List<Entity> entities) throws ProcessException {

		try {
			termLexiconDao.insertEntityTerms(entities);
			taghandlerDao.insertEntities(entities);
		} catch (DaoException e) {
			throw new ProcessException(e.getCompleteMessage(), ProcessException.ERROR_CODE_READ_ENTITIES);
		}
	}

}
