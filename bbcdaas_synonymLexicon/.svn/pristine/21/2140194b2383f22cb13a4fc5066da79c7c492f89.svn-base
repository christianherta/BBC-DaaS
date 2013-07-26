package de.bbcdaas.synonymlexicon.common.beans;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
*
*/
public final class TermCountPair implements Writable {

   private Text term = null;
   private long count = 0;

   /**
	*
	*/
   public TermCountPair() {
	   set(new Text(), 0);
   }

   /**
	* Set the left and right values.
	*
	* @param term
	* @param count
	*/
   public void set(Text term, long count) {

	   this.term = term;
	   this.count = count;
   }

   /**
	*
	* @return
	*/
   public Text getTerm() {
	   return this.term;
   }

   /**
	*
	* @return
	*/
   public long getCount() {
	   return this.count;
   }

   /**
	*
	* @param in
	* @throws IOException
	*/
   @Override
   public void readFields(DataInput in) throws IOException {

	   this.term.readFields(in);
	   this.count = in.readLong() + Long.MIN_VALUE;
   }

   /**
	*
	* @param out
	* @throws IOException
	*/
   @Override
   public void write(DataOutput out) throws IOException {

	   this.term.write(out);
	   out.writeLong(this.count - Long.MIN_VALUE);
   }
}