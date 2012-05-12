/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package com.olabini.jescov.junit;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import be.klak.rhino.RhinoContext;

import com.olabini.jescov.Configuration;
import com.olabini.jescov.Coverage;
import com.olabini.jescov.CoverageData;
import com.olabini.jescov.generators.JsonGenerator;
import com.olabini.jescov.generators.JsonIngester;

public class JasmineTestRunner extends be.klak.junit.jasmine.JasmineTestRunner {
	private Configuration configuration;
	private Coverage coverage;

	public JasmineTestRunner(final Class<?> testClass) {
		super(testClass);
	}

	@Override
	protected void pre(final RhinoContext context) {
		this.configuration = ConfigurationFactory.getConfiguration(
				suiteAnnotation, testClass.getAnnotation(JesCovConfig.class));
		this.coverage = Coverage.on(context.getJsContext(),
				context.getJsScope(), configuration);
	}

	@Override
	protected void after() {
		this.coverage.done();
		super.after();
		outputCoverageResults();
	}

	private void outputCoverageResults() {
		if (configuration.isEnabled()) {
			CoverageData data = coverage.getCoverageData();
			final String fileout = configuration.getJsonOutputFile();

			try {
				if (configuration.isJsonOutputMerge()
						&& new File(fileout).exists()) {
					final FileReader fr = new FileReader(fileout);
					final CoverageData moreData = new JsonIngester().ingest(fr);
					fr.close();
					data = moreData.plus(data);
				}

				final FileWriter fw = new FileWriter(fileout);
				new JsonGenerator(fw).generate(data);
				fw.close();
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
