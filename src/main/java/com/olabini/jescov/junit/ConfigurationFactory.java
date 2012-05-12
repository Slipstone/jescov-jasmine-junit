/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.olabini.jescov.junit;

import be.klak.junit.jasmine.JasmineSuite;
import be.klak.rhino.RhinoContext;

import com.olabini.jescov.Configuration;
import com.slipstone.FindFiles;

public class ConfigurationFactory {
	public static Configuration getConfiguration(final JasmineSuite suite,
			final JesCovConfig conf) {
		final Configuration config = new Configuration();
		config.setEnabled(Boolean.getBoolean("com.olabini.jescov.enabled"));

		for (final String filename : RhinoContext.BUNDLE_FILES.asMap().values()) {
			if (filename.endsWith(".js")) {
				config.ignore(filename);
			}
		}
		config.ignore("script");

		for (final String spec : FindFiles.findFiles(suite.specDir(),
				suite.specInclude(), suite.specExclude())) {
			config.ignore(suite.specDir() + "/" + spec);
		}

		for (final String mock : FindFiles.findFiles(suite.mockDir(),
				suite.mockInclude(), suite.mockExclude())) {
			config.ignore(suite.mockDir() + "/" + mock);
		}

		final String[] ignoreFiles = System.getProperty(
				"com.olabini.jescov.ignoreFiles", "").split(",");
		for (final String file : ignoreFiles) {
			config.ignore(file.trim());
		}

		if (conf != null) {
			// Ignore additional files from the JesCovConfig annotation - note
			// we include the excludes since we are building the ignore list
			for (final String file : FindFiles.findFiles(suite.sourceDir(),
					conf.sourceExclude(), new String[0])) {
				config.ignore(suite.sourceDir() + "/" + file);
			}
		}

		if (System.getProperty("com.olabini.jescov.jsonOutputFile") != null) {
			config.setJsonOutputFile(System
					.getProperty("com.olabini.jescov.jsonOutputFile"));
		}

		config.setJsonOutputMerge(Boolean
				.getBoolean("com.olabini.jescov.jsonMerge"));

		return config;
	}
}
