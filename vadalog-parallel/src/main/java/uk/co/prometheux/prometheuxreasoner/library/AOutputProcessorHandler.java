package uk.co.prometheux.prometheuxreasoner.library;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.prometheux.prometheuxreasoner.model.types.VadaValue;

public abstract class AOutputProcessorHandler {

	public static class MetadataValues {
		public MetadataValues(List<String> termNames) {
			this.termNames = termNames;
		}

		private final List<String> termNames;

		public List<String> getTermNames() {
			return termNames;
		}
	}

	private final Map<String, MetadataValues> metadata;

	public Map<String, MetadataValues> getMetadata() {
		return metadata;
	}

	public void addMetadataValues(String atomName, MetadataValues vals) {
		metadata.put(atomName, vals);
	}

	public AOutputProcessorHandler() {
		this(new HashMap<>());
	}

	private AOutputProcessorHandler(Map<String, MetadataValues> metadata) {
		this.metadata = metadata;
	}

	public abstract void process(int factId, String atomName, Iterable<VadaValue<?>> terms);

}