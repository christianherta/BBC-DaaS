package de.bbcdaas.synonymlexicon.tests;

import com.google.common.collect.Lists;
import de.bbcdaas.synonymlexicon.common.mahout.used.RandomAccessSparseVector;
import de.bbcdaas.synonymlexicon.common.mahout.used.Vector;
import de.bbcdaas.synonymlexicon.common.mahout.used.VectorWritable;
import de.bbcdaas.synonymlexicon.cooccurrencematrix.jobs.TermIdEntriesToCooccurrenceJob.TermIdEntriesToCooccurrenceMapper;
import de.bbcdaas.synonymlexicon.cooccurrencematrix.jobs.TermIdEntriesToCooccurrenceJob.TermIdEntriesToCooccurrenceReducer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Frithjof Schulte
 */
public class TermEntriesVectorToCooccurrenceVectorTest {

	MapDriver<LongWritable, VectorWritable, LongWritable, LongWritable> mapDriver;
	ReduceDriver<LongWritable, LongWritable, LongWritable, VectorWritable> reduceDriver;
	MapReduceDriver<LongWritable, VectorWritable, LongWritable, LongWritable, LongWritable, VectorWritable> mapReduceDriver;

	/**
	 *
	 */
	@Before
	public void setUp() {

		TermIdEntriesToCooccurrenceMapper mapper = new TermIdEntriesToCooccurrenceMapper();
		TermIdEntriesToCooccurrenceReducer reducer = new TermIdEntriesToCooccurrenceReducer();
		mapDriver = MapDriver.newMapDriver(mapper);
		reduceDriver = ReduceDriver.newReduceDriver(reducer);
		mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);
	}

	/**
	 *
	 */
	@Test
	public void validTermIdsVectorMapper() {

		Vector value = new RandomAccessSparseVector(Integer.MAX_VALUE, 4);
		value.set(1, 1.0);
		value.set(3, 1.0);
		value.set(62, 1.0);
		value.set(5, 1.0);

		mapDriver.withInput(new LongWritable(), new VectorWritable(value));
		// expected outputs
		mapDriver.withOutput(new LongWritable(62), new LongWritable(5));
		mapDriver.withOutput(new LongWritable(62), new LongWritable(3));
		mapDriver.withOutput(new LongWritable(62), new LongWritable(1));
		mapDriver.withOutput(new LongWritable(5), new LongWritable(62));
		mapDriver.withOutput(new LongWritable(5), new LongWritable(3));
		mapDriver.withOutput(new LongWritable(5), new LongWritable(1));
		mapDriver.withOutput(new LongWritable(3), new LongWritable(62));
		mapDriver.withOutput(new LongWritable(3), new LongWritable(5));
		mapDriver.withOutput(new LongWritable(3), new LongWritable(1));
		mapDriver.withOutput(new LongWritable(1), new LongWritable(62));
		mapDriver.withOutput(new LongWritable(1), new LongWritable(5));
		mapDriver.withOutput(new LongWritable(1), new LongWritable(3));
		mapDriver.runTest();
	}

	/**
	 *
	 */
	@Test
	public void doubleTermIdsVectorMapper() {

		Vector value = new RandomAccessSparseVector(Integer.MAX_VALUE, 4);
		value.set(33, 1.0);
		value.set(2, 1.0);
		value.set(2, 1.0);
		value.set(1, 1.0);

		mapDriver.withInput(new LongWritable(), new VectorWritable(value));
		// expected outputs
		mapDriver.withOutput(new LongWritable(33), new LongWritable(2));
		mapDriver.withOutput(new LongWritable(33), new LongWritable(1));
		mapDriver.withOutput(new LongWritable(2), new LongWritable(33));
		mapDriver.withOutput(new LongWritable(2), new LongWritable(1));
		mapDriver.withOutput(new LongWritable(1), new LongWritable(33));
		mapDriver.withOutput(new LongWritable(1), new LongWritable(2));
		mapDriver.runTest();
	}

	/**
	 *
	 * @throws IOException
	 */
	@Test
	public void countCoOccuredFrequenceReducer() throws IOException {

		List<LongWritable> values = new ArrayList<LongWritable>();
		values.add(new LongWritable(23));
		values.add(new LongWritable(23));
		values.add(new LongWritable(43));
		values.add(new LongWritable(854));
		values.add(new LongWritable(3));
		values.add(new LongWritable(82));

		reduceDriver.withInput(new LongWritable(3), values);
		// expected outputs
		Vector outVec = new RandomAccessSparseVector(Integer.MAX_VALUE, 4);
		outVec.set(23, 2.0);
		outVec.set(43, 2.0);
		outVec.set(82, 1.0);
		outVec.set(854, 1.0);
		reduceDriver.withOutput(new LongWritable(3), new VectorWritable(outVec));
		List<Pair<LongWritable, VectorWritable>> output = reduceDriver.run();
		List<Pair<LongWritable, VectorWritable>> expected = reduceDriver.getExpectedOutputs();

		// compare expected with real output
		assertEquals("Different count of outputs!", expected.size(), output.size());

		// Pair -> Value(VectorWritable) -> Vector
		Vector expectedVec = expected.get(0).getSecond().get();
		Vector outputVec = output.get(0).getSecond().get();

		int expectedSize = expectedVec.getNumNondefaultElements();
		int actualualSize = outputVec.getNumNondefaultElements();
		assertEquals("Expected size is different to actual size!", expectedSize, actualualSize);

		Iterator<Vector.Element> iterExpected = expectedVec.iterateNonZero();
		Iterator<Vector.Element> iterActual = outputVec.iterateNonZero();
		List<Vector.Element> expList = Lists.newArrayList(iterExpected);
		List<Vector.Element> actualList = Lists.newArrayList(iterActual);

		for(int i = 0; i < expList.size(); i++) {
			assertEquals("Key for " + i + " element is different!", expList.get(i).index(), actualList.get(i).index());
			assertEquals("Value for " + i + " element is different!", expList.get(i).get(), actualList.get(i).get(), 0);
		}
	}
}
