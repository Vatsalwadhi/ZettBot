package home;

import java.io.InputStream;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;

public class DialogflowService {

    private static final String PROJECT_ID = "zettbot-hwct"; // Replace with your Dialogflow project ID
    private static SessionsClient sessionsClient;

    static {
        try {
            // Load the credentials file from the classpath
            InputStream stream = DialogflowService.class.getClassLoader().getResourceAsStream("zettbot-hwct-be4c22d9aff2.json");
            if (stream == null) {
                throw new RuntimeException("Credentials file not found in classpath");
            }

            GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
            SessionsSettings settings = SessionsSettings.newBuilder()
                    .setCredentialsProvider(() -> credentials)
                    .build();
            sessionsClient = SessionsClient.create(settings);
        } catch (Exception e) {
            System.err.println("Exception occurred during initialization: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Dialogflow client", e);
        }
    }

    public static String getResponseFromDialogflow(String text) {
        try {
            String sessionId = "test-session"; // Replace with a unique session ID
            SessionName session = SessionName.of(PROJECT_ID, sessionId);

            TextInput textInput = TextInput.newBuilder()
                    .setText(text)
                    .setLanguageCode("en-US")
                    .build();
            QueryInput queryInput = QueryInput.newBuilder()
                    .setText(textInput)
                    .build();

            DetectIntentRequest request = DetectIntentRequest.newBuilder()
                    .setSession(session.toString())
                    .setQueryInput(queryInput)
                    .build();

            DetectIntentResponse response = sessionsClient.detectIntent(request);
            return response.getQueryResult().getFulfillmentText();
        } catch (Exception e) {
            System.err.println("Exception occurred while making a request: " + e.getMessage());
            e.printStackTrace();
            return "Error getting response from Dialogflow: " + e.getMessage();
        }
    }
}
