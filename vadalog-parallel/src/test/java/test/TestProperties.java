package test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestProperties extends Properties {

	private static final long serialVersionUID = -8670650951531188183L;

	public TestProperties() {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("pmtx.properties");
		try {
			this.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
