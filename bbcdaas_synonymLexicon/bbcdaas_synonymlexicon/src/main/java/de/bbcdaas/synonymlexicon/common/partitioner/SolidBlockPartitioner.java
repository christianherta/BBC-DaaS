package de.bbcdaas.synonymlexicon.common.partitioner;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;

/**
 * Gets the partition of an item in a group of part files, grouped in blocks
 * without gaps.
 * Example:
 * part1: 1,2,3,4,5
 * part2: 6,7,8,9,10
 * (HashPartitioner for comparison:
 *  part1: 1,3,5,7,9
 *  part2: 2,4,6,8,10)
 * @author Robert Illers
 */
public class SolidBlockPartitioner extends org.apache.hadoop.mapreduce.Partitioner<LongWritable, Writable>{

	private long numberOfItems;
	
	public SolidBlockPartitioner(long numberOfItems) {
		this.numberOfItems = numberOfItems;
	}
	
	@Override
	public int getPartition(LongWritable key, Writable ignoredValue, int numPartitions) {
		
		long itemsPerBlock = this.numberOfItems / (long)numPartitions;
		// always round up
		if (this.numberOfItems % (long)numPartitions != 0) {
			itemsPerBlock++;
		}
		
		long partition = key.get() / itemsPerBlock;
		// always round up
		if (key.get() % itemsPerBlock != 0) {
			partition++;
		}
		// subtract 1 because of index starting at 0
		return (int)partition-1;
	}
}
