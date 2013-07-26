package de.bbcdaas.uima_components.tools;

import de.bbcdaas.common.util.FileReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.apache.uima.pear.tools.PackageBrowser;
import org.apache.uima.pear.tools.PackageInstaller;
import org.apache.uima.pear.tools.PackageInstallerException;

/**
 * Wrapper class for installing an UIMA pear package into the project.
 * @author Robert Illers
 */
public class PearPackageInstaller {

	private static Logger logger = Logger.getLogger(PearPackageInstaller.class);
	private static final FileReader fileReader = new FileReader();

	public static void main(String[] args) {

		List<String> installDirPaths = new ArrayList<String>();
		List<String> pearPackagePaths = new ArrayList<String>();

		Options options = new Options();
		options.addOption("help", false, "Shows the help");
		options.addOption("installDirPathIp", true, "path where the pear package should be extracted to");
		options.addOption("pearPackageInputPathIp", true, "path to the input pear package");
		options.addOption("propertiesFilePathIp", true, "path to install pear properties file");
		CommandLineParser parser = new PosixParser();
		HelpFormatter helpFormater = new HelpFormatter();

		try {

			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("help")) {
				helpFormater.printHelp("java -jar [fileName]", options);
			}

			if(cmd.hasOption("installDirPathIp") && cmd.hasOption("pearPackageInputPathIp")) {

				installDirPaths.add(cmd.getOptionValue("installDirPathIp"));
				pearPackagePaths.add(cmd.getOptionValue("pearPackageInputPathIp"));
			} else {

				Configuration config;

				if (cmd.hasOption("propertiesFilePathIp")) {

					config = fileReader.readPropertiesConfig(cmd.getOptionValue("propertiesFilePathIp"), FileReader.FILE_OPENING_TYPE.ABSOLUTE,
							FileReader.FILE_OPENING_TYPE.RELATIVE, true);
				} else {
					config = fileReader.readPropertiesConfig(new StringBuilder("properties").
						append(File.separator).append("uimaPearPackages.properties").toString(), FileReader.FILE_OPENING_TYPE.ABSOLUTE,
							FileReader.FILE_OPENING_TYPE.RELATIVE, true);
				}

				if (config != null) {

					int pearPackageIndex = 1;
					String installDirPath;
					while ((installDirPath = config.getString("installDirPath_"+pearPackageIndex)) != null) {

						String pearPackagePath = config.getString("pearPackagePath_"+pearPackageIndex);
						installDirPaths.add(installDirPath);
						pearPackagePaths.add(pearPackagePath);
						pearPackageIndex++;
					}
				}

				// execute the installations
				PearPackageInstaller installer = new PearPackageInstaller();
				int i = 0;
				for (String installDirPath : installDirPaths) {

					installer.install(installDirPath, pearPackagePaths.get(i));
					i++;
				}
			}

		} catch(ParseException ex) {
			logger.error(ex);
		}
	}

	/**
	 * Executes the pear packages instalation process by usinf the UIMA API.
	 * @param installDirPath
	 * @param pearPackagePath
	 * @return true if installation process was successful
	 */
	public boolean install(String installDirPath, String pearPackagePath) {

		File installDir = fileReader.readFile(installDirPath, FileReader.FILE_OPENING_TYPE.ABSOLUTE,
			FileReader.FILE_OPENING_TYPE.RELATIVE);

		File pearPackage = fileReader.readFile(pearPackagePath, FileReader.FILE_OPENING_TYPE.ABSOLUTE,
			FileReader.FILE_OPENING_TYPE.RELATIVE);

		boolean verify = false;
		boolean cleanInstallDir = true;
		boolean installToTopLevelDir = false;

		PackageBrowser packageBrowser;

		// install pear package
		try {

			packageBrowser = PackageInstaller.installPackage(installDir, pearPackage, verify,
				cleanInstallDir, installToTopLevelDir);

		} catch(PackageInstallerException ex) {
			logger.error(ex);
			return false;
		}

		if (packageBrowser != null) {

			logger.info("PEAR package directories:");
			for (File directory : packageBrowser.getAllDirectories()) {
				logger.debug(directory.getPath());
			}

			logger.info("PEAR package files:");
			for (File file : packageBrowser.getAllFiles()) {
				logger.debug(file.getPath());
			}
		}
		return true;
	}
}