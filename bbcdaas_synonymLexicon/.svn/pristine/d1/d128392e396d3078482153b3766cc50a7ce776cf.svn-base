package de.bbcdaas.synonymlexicon.common.partitioner;

import de.bbcdaas.synonymlexicon.common.beans.RowNumberWritable;
import org.apache.hadoop.io.ByteWritable;

/**
 * 
 * @author Friso van Vollenhoven
 * @author Robert Illers
 */
public class RowNumberPartitioner extends org.apache.hadoop.mapreduce.Partitioner<ByteWritable, RowNumberWritable> {
	
	@Override
	public int getPartition(ByteWritable key, RowNumberWritable value, int numPartitions) {

		if (key.get() == (byte) RowNumberWritable.COUNTER_MARKER) {
			return value.getPartition();
		} else {
			return RowNumberPartitioner.partitionForValue(value, numPartitions);
		}
	}

	public static int partitionForValue(RowNumberWritable value, int numPartitions) {
		return (value.getValue().hashCode() & Integer.MAX_VALUE) % numPartitions;
	}
}