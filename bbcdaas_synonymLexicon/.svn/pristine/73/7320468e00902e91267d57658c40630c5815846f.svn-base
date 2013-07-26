package de.bbcdaas.synonymlexicon.common.beans;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Writable;

/**
* Contains the termID and the term occurrences. Used to store this term data
* in a hadoop map or sequence file.
*/
public class IdCountPair implements Writable {

   private long termId = 0;
   private long count = 0;

   /**
	* Sets the values.
	* @param termId
	* @param count
	*/
   public void set(long termId, long count) {
	   this.termId = termId;
	   this.count = count;
   }

   /**
	* Id of the term in the term lexicoc.
	* @return termID
	*/
   public long getTermId() {
	   return termId;
   }

   /**
	* How often does this term occur in the input term clouds
	* @return count
	*/
   public long getCount() {
	   return count;
   }

   /**
	*
	* @param in
	* @throws IOException
	*/
   @Override
   public void readFields(DataInput in) throws IOException {
	   termId = in.readLong() + Long.MIN_VALUE;
	   count = in.readLong() + Long.MIN_VALUE;
   }

   /**
	*
	* @param out
	* @throws IOException
	*/
   @Override
   public void write(DataOutput out) throws IOException {
	   out.writeLong(termId - Long.MIN_VALUE);
	   out.writeLong(count - Long.MIN_VALUE);
   }
}