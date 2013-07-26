package de.bbcdaas.synonymlexicon.tests;

import de.bbcdaas.synonymlexicon.termlexicon.jobs.WordCountJob.InMapperCombiningWordCountMapper;
import de.bbcdaas.synonymlexicon.termlexicon.jobs.WordCountJob.WordCountInverseReducer;
import de.bbcdaas.synonymlexicon.termlexicon.jobs.WordCountJob.WordCountReducer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.testutil.ExtendedAssert;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Frithjof Schulte
 */
public class LexiconBuilderWordCountTest {

	MapDriver<LongWritable, Text, Text, LongWritable> mapDriver;
	ReduceDriver<Text, LongWritable, LongWritable, Text> reduceDriver;
	MapReduceDriver<LongWritable, Text, Text, LongWritable, LongWritable, Text> mapReduceDriver;

	/**
	 *
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {

		InMapperCombiningWordCountMapper mapper = new InMapperCombiningWordCountMapper();
		WordCountInverseReducer reducer = new WordCountInverseReducer();
		mapDriver = MapDriver.newMapDriver(mapper);
		reduceDriver = ReduceDriver.newReduceDriver(reducer);
		mapReduceDriver = MapReduceDriver.newMapReduceDriver(mapper, reducer);

		// set the test configuration for all drivers
		Configuration.addDefaultResource("test-config.xml");
		Configuration conf = new Configuration(true);
		mapDriver.setConfiguration(conf);
		reduceDriver.setConfiguration(conf);
		mapReduceDriver.setConfiguration(conf);
	}

	/**
	 *
	 * @throws IOException
	 */
	@Test
	public void countSomeWordsInMapper() throws IOException {

		Text value1 = new Text(" gelb, gelb, gruen , rot , rot?   ,  rot,...   .Net , rot , grün");

		mapDriver.withInput(new LongWritable(), value1);
		// expected outputs
		mapDriver.withOutput(new Text("gruen"), new LongWritable(2));
		mapDriver.withOutput(new Text("rot"), new LongWritable(4));
		mapDriver.withOutput(new Text(".net"), new LongWritable(1));
		mapDriver.withOutput(new Text("gelb"), new LongWritable(2));
		List<Pair<Text, LongWritable>> output = mapDriver.run();
		List<Pair<Text, LongWritable>> expected = mapDriver.getExpectedOutputs();

		ExtendedAssert.assertListEquals(expected, output);
	}

	/**
	 *
	 * @throws IOException
	 */
	@Test
	public void countSomeDifficultWordsInMapper() throws IOException {

		Text value2 = new Text("C++: , =====, ! Buchhaltung, java, c++? , JAVA...    ,  \"JavaEE\"  ,   , Exposè");

		mapDriver.withInput(new LongWritable(), value2);
		// expected outputs
		mapDriver.withOutput(new Text("exposè"), new LongWritable(1));
		mapDriver.withOutput(new Text("buchhaltung"), new LongWritable(1));
		mapDriver.withOutput(new Text("c++"), new LongWritable(2));
		mapDriver.withOutput(new Text("javaee"), new LongWritable(1));
		mapDriver.withOutput(new Text("java"), new LongWritable(2));
		List<Pair<Text, LongWritable>> output = mapDriver.run();
		List<Pair<Text, LongWritable>> expected = mapDriver.getExpectedOutputs();
		
		ExtendedAssert.assertListEquals(expected, output);
	}

	/**
	 *
	 * @throws IOException
	 */
	@Test
	public void reduceSomeWordCounts() throws IOException {
		List<LongWritable> values = new ArrayList<LongWritable>();
		values.add(new LongWritable(23));
		values.add(new LongWritable(4));
		values.add(new LongWritable(11));

		reduceDriver.withInput(new Text("java"), values);
		// expected outputs
		reduceDriver.withOutput(new LongWritable(38), new Text("java"));
		List<Pair<LongWritable, Text>> output = reduceDriver.run();
		List<Pair<LongWritable, Text>> expected = reduceDriver.getExpectedOutputs();

		ExtendedAssert.assertListEquals(expected, output);
	}

	/**
	 *
	 * @throws IOException
	 */
	@Test
	public void wordCountMapCombinerReduce() throws IOException {

		Text value1 = new Text(" gelb, gelb, gruen , rot , rot?   ,  rot,...   .Net , (rot) ");
		Text value2 = new Text(" gelb, rot, gruen , gelb , rot    , rot,  ..gruen  ,  , Exposè");
		Text value3 = new Text("C++ , =====, ! Buchhaltung, c++) , gelb...  ,   ,  grün");

		mapReduceDriver.withCombiner(new WordCountReducer());
		mapReduceDriver.withInput(new LongWritable(1), value1);
		mapReduceDriver.withInput(new LongWritable(2), value2);
		mapReduceDriver.withInput(new LongWritable(3), value3);
		// expected outputs
		mapReduceDriver.withOutput(new LongWritable(1), new Text(".net"));
		mapReduceDriver.withOutput(new LongWritable(1), new Text("buchhaltung"));
		mapReduceDriver.withOutput(new LongWritable(2), new Text("c++"));
		mapReduceDriver.withOutput(new LongWritable(1), new Text("exposè"));
		mapReduceDriver.withOutput(new LongWritable(5), new Text("gelb"));
		mapReduceDriver.withOutput(new LongWritable(4), new Text("gruen"));
		mapReduceDriver.withOutput(new LongWritable(7), new Text("rot"));

		List<Pair<LongWritable, Text>> output = mapReduceDriver.run();
		List<Pair<LongWritable, Text>> expected = mapReduceDriver.getExpectedOutputs();
		
		ExtendedAssert.assertListEquals(expected, output);
	}
}