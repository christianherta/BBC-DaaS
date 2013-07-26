package de.bbcdaas.synonymlexicon.tests;

import de.bbcdaas.synonymlexicon.common.mahout.used.SparseRowMatrix;
import de.bbcdaas.synonymlexicon.common.mahout.used.VectorWritable;
import de.bbcdaas.synonymlexicon.cosinesimilarity.jobs.CosineSimilarityVectorNormJob.CosineSimilarityVectorNormMapper;
import java.io.IOException;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.testutil.ExtendedAssert;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Frithjof Schulte
 */
public class CosineSimilarityVectorNormTest {

	MapReduceDriver<LongWritable, VectorWritable, LongWritable, DoubleWritable, LongWritable, DoubleWritable> mapReduceDriver;

	@Before
	public void setUp() {

		CosineSimilarityVectorNormMapper mapper = new CosineSimilarityVectorNormMapper();
		Reducer reducer = new Reducer<LongWritable, DoubleWritable, LongWritable, DoubleWritable>(); // identity reducer
		mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);

		// set the test configuration for all drivers
		Configuration.addDefaultResource("test-config.xml");
		Configuration conf = new Configuration(true);
		mapReduceDriver.setConfiguration(conf);
	}

	/**
	 *
	 * @throws IOException
	 */
	@Test
	public void calculateSomeVectorNorms() throws IOException {

		double[] v1 = {0.0,2.347,0.0,0.0,7.547,4.843,0.0};		// =  9,269	gerundet
		double[] v4 = {2.348,0.0,8.433,3.568,0.0,0.0,0.0};		// =  9,453
		double[] v17 = {0.0,8.244,0.0,0.0,0.0,6.542,2.023};		// = 10,717
		double[] v18 = {0.0,3.945,0.0,1.185,5.350,1.602,8.094};	// = 10,662
		double[] v22 = {7.937,0.0,0.0,5.493,0.0,0.0,0.0};		// =  9,652
		double[] v24 = {4.283,0.0,6.922,1.064,0.0,0.0,1.592};	// =  8,362
		double[] v30 = {0.0,0.0,2.835,8.733,0.0,1.897,0.0};		// =  9,376

		SparseRowMatrix helperMatrix = new SparseRowMatrix(7, 7);
		helperMatrix.set(0, v1);
		mapReduceDriver.withInput(new LongWritable(1), new VectorWritable(helperMatrix.viewRow(0)));
		helperMatrix.set(1, v4);
		mapReduceDriver.withInput(new LongWritable(4), new VectorWritable(helperMatrix.viewRow(1)));
		helperMatrix.set(2, v17);
		mapReduceDriver.withInput(new LongWritable(17), new VectorWritable(helperMatrix.viewRow(2)));
		helperMatrix.set(3, v18);
		mapReduceDriver.withInput(new LongWritable(18), new VectorWritable(helperMatrix.viewRow(3)));
		helperMatrix.set(4, v22);
		mapReduceDriver.withInput(new LongWritable(22), new VectorWritable(helperMatrix.viewRow(4)));
		helperMatrix.set(5, v24);
		mapReduceDriver.withInput(new LongWritable(24), new VectorWritable(helperMatrix.viewRow(5)));
		helperMatrix.set(6, v30);
		mapReduceDriver.withInput(new LongWritable(30), new VectorWritable(helperMatrix.viewRow(6)));

		// expected outputs
		mapReduceDriver.withOutput(new LongWritable(1), new DoubleWritable(9.269));
		mapReduceDriver.withOutput(new LongWritable(4), new DoubleWritable(9.453));
		mapReduceDriver.withOutput(new LongWritable(17), new DoubleWritable(10.717));
		mapReduceDriver.withOutput(new LongWritable(18), new DoubleWritable(10.662));
		mapReduceDriver.withOutput(new LongWritable(22), new DoubleWritable(9.652));
		mapReduceDriver.withOutput(new LongWritable(24), new DoubleWritable(8.362));
		mapReduceDriver.withOutput(new LongWritable(30), new DoubleWritable(9.376));

		List<Pair<LongWritable, DoubleWritable>> output = mapReduceDriver.run();
		List<Pair<LongWritable, DoubleWritable>> expected = mapReduceDriver.getExpectedOutputs();

		ExtendedAssert.assertListEquals(expected, output);
	}


}
