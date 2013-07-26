package de.bbcdaas.synonymlexicon.tests;

import de.bbcdaas.synonymlexicon.termlexicon.jobs.WordCountJob;
import de.bbcdaas.synonymlexicon.utils.hadoop.DataAccessUtils;
import java.io.IOException;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Frithjof Schulte
 */
public class LexicountBuilderWordCountDriverTest {

	private Configuration conf;
	private String input;
	private String output;
	private FileSystem fs;

	/**
	 *
	 * @throws IOException
	 */
	@Before
	public void setUp() throws IOException {

		this.conf = new Configuration();
		this.conf.addResource("test-config.xml");
		this.conf.set("fs.defaultFS", "file:///");
		this.conf.set("mapreduce.jobtracker.address", "local");

		this.input = "src/test/resources/wordCountInput";
		this.output = "src/test/resources/wordCountOutput";

		this.fs = FileSystem.getLocal(this.conf);
		this.fs.delete(new Path(this.output), true);
	}

	/**
	 *
	 * @throws IOException
	 */
	@After
	public void cleanUp() throws IOException {
		fs.delete(new Path(this.output), true);
	}

	/**
	 *
	 * @throws Exception
	 */
	@Test
	@Ignore
	public void test() throws Exception {

		WordCountJob wordCount = new WordCountJob(this.conf);

		int exitCode = wordCount.run(new String[] {this.input, this.output});
		assertEquals(0, exitCode);

		validateOuput();
	}

	/**
	 *
	 * @throws IOException
	 */
	private void validateOuput() throws IOException {

		List<SequenceFile.Reader> readers = DataAccessUtils.getSequenceFilePartReaders(new Path(output), conf);
		try {
			
			SequenceFile.Reader reader = readers.get(0);

			Text term = new Text();
			LongWritable count = new LongWritable();

			reader.next(count, term);
			assertEquals("First Pair", ".net", term.toString());
			assertEquals("First Pair", 1, count.get());

			reader.next(count, term);
			assertEquals("Second Pair", "buchhaltung", term.toString());
			assertEquals("Second Pair", 1, count.get());

			reader.next(count, term);
			assertEquals("Third Pair", "c++", term.toString());
			assertEquals("Third Pair", 2, count.get());

			reader.next(count, term);
			assertEquals("Fourth Pair", "expose", term.toString());
			assertEquals("Fourth Pair", 1, count.get());

			reader.next(count, term);
			assertEquals("Fifth Pair", "gelb", term.toString());
			assertEquals("Fifth Pair", 5, count.get());

			reader.next(count, term);
			assertEquals("Sixth Pair", "gruen", term.toString());
			assertEquals("Sixth Pair", 4, count.get());

			reader.next(count, term);
			assertEquals("Seventh Pair", "rot", term.toString());
			assertEquals("Seventh Pair", 7, count.get());

		} catch(Exception e) {
			System.err.println("Exception in validateOutput...");
			System.err.println(e);
		} finally {
			IOUtils.closeStream(readers.get(0));
		}
	}
}
