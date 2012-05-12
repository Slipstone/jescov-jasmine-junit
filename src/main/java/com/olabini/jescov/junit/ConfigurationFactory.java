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

		final String jsDir = suite.specDir();
		for (final String filename : RhinoContext.BUNDLE_FILES.asMap().values()) {
			if (filename.endsWith(".js")) {
				config.ignore(filename);
			}
		}
		config.ignore("script");

		for (final String spec : FindFiles.findFiles(jsDir,
				suite.specInclude(), suite.specExclude())) {
			config.ignore(jsDir + "/" + spec);
		}

		final String[] ignoreFiles = System.getProperty(
				"com.olabini.jescov.ignoreFiles", "").split(",");
		for (final String file : ignoreFiles) {
			config.ignore(file.trim());
		}

		if (conf != null) {
			// Ignore additional files from the JesCovConfig annotation - note
			// we include the excludes since we are building the ignore list
			final String[] files = FindFiles.findFiles(suite.sourceDir(),
					conf.sourceExclude(), new String[0]);
			for (final String file : files) {
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
