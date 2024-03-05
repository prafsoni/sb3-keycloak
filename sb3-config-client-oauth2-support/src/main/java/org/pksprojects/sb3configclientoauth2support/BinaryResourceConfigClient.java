package org.pksprojects.sb3configclientoauth2support;

import org.springframework.core.io.Resource;
import org.springframework.web.client.HttpClientErrorException;

public interface BinaryResourceConfigClient {

	/**
	 * Retrieves a binary config file using the defaults profiles and labels.
	 * @param path config file path relative to spring application folder
	 * @return plain text file retrieved from config server
	 * @throws IllegalArgumentException when application name or Config Server url is
	 * undefined.
	 * @throws HttpClientErrorException when a config file is not found.
	 */
	Resource getBinaryResource(String path);

	/**
	 * Retrieves a binary config file.
	 * @param profile profile name (can be a comma-separated list of profiles)
	 * @param label git label
	 * @param path config file path relative to spring application folder
	 * @return plain text file retrieved from config server
	 * @throws IllegalArgumentException when application name or Config Server url is
	 * undefined.
	 * @throws HttpClientErrorException when a config file is not found.
	 */
	Resource getBinaryResource(String profile, String label, String path);

}
