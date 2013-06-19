package org.flowerplatform.web.communication;

public interface IRecordingTestWebCommunicationChannelProvider {

	/**
	 * @return A CC casted to {@link RecordingTestWebCommunicationChannel}, for convenience.
	 */
	public RecordingTestWebCommunicationChannel getRecordingTestWebCommunicationChannel();
}
