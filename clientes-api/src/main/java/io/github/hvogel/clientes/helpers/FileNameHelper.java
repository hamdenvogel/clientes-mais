package io.github.hvogel.clientes.helpers;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.github.hvogel.clientes.exception.FileNameException;

@Component
public class FileNameHelper {

	private final java.util.Random random = new java.security.SecureRandom();

	public String generateUniqueNumber() {
		int min = 10000;
		int max = 99999;
		int randomInt = random.nextInt((max - min) + 1) + min;
		return String.valueOf(randomInt);
	}

	public String generateFileName(String fileName) {

		// generate random alphabet
		String shortRandomAlphabet = generateUniqueNumber();

		// create date format as string
		String dateStrFormat = DateTime.now().toString("HHmmss_ddMMyyyy");

		// find extension of file
		int indexOfExtension = fileName.indexOf(".");
		String extensionName = fileName.substring(indexOfExtension);

		// return new file name
		return dateStrFormat + "_" + shortRandomAlphabet + extensionName;

	}

	public String generateDisplayName(String orgFileName) {
		String orgCleanPath = StringUtils.cleanPath(orgFileName);

		// Check if the file's name contains invalid characters
		if (orgCleanPath.contains(".."))
			throw new FileNameException("Sorry! Filename contains invalid path sequence " + orgCleanPath);

		// generate new file name
		return generateFileName(orgCleanPath);
	}

}
