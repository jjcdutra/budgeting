package dio.budgeting;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENROUTER_API_KEY", matches = ".+")
public class OpenRouterTranscriptionModelIT {

    @Autowired
    OpenAiAudioTranscriptionModel openAiAudioTranscriptionModel;

    @ParameterizedTest
    @CsvSource({
            "recording-1.m4a, 40 reais",
            "recording-2.m4a, 80 reais"
    })
    public void should_contain_expectedKeywords_when_audioFilesProcessed(String fileName, String expectedKeyword) {

        var recording = audioResource(fileName);

        var response = openAiAudioTranscriptionModel.call(recording);

        assertThat(response).contains(expectedKeyword);
        System.out.println(response);
    }

    private static Resource audioResource(String fileName) {
        var classPathResource = new ClassPathResource("audio/" + fileName);
        if (classPathResource.exists()) {
            return classPathResource;
        }
        var relativePath = Path.of("src", "test", "resources", "audio", fileName);
        var dir = Path.of("").toAbsolutePath();
        for (int i = 0; i < 4 && dir != null; i++) {
            var candidate = dir.resolve(relativePath);
            if (Files.exists(candidate)) {
                return new FileSystemResource(candidate);
            }
            dir = dir.getParent();
        }
        return new FileSystemResource(relativePath);
    }
}
