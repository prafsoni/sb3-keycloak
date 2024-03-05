package org.pksprojects.sb3configclientoauth2support;

import org.springframework.core.io.Resource;

public interface PlainTextConfigClient {

	Resource getPlainTextResource(String profile, String label, String path);
}
