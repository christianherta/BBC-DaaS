package de.bbcdaas.synonymlexicon.utils.hadoop;

import org.apache.hadoop.fs.Path;

/**
 * Builds a String or Path from folder names by adding hdfs separator "/".
 * @author Robert Illers
 */
public final class PathBuilder {

	public static String buildAsString(String... parts) {

		StringBuilder path = new StringBuilder();
		int partCount = parts.length;
		int i = 1;
		for (String part : parts) {

			path.append(part);
			if (i < partCount) {
				path.append("/");
			}
			i++;
		}
		return path.toString();
	}

	public static Path buildAsPath(String... parts) {
		return new Path(buildAsString(parts));
	}
}
