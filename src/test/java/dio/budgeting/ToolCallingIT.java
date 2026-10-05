package dio.budgeting;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "OPENROUTER_API_KEY", matches = ".+")
public class ToolCallingIT {

    @Autowired
    OpenAiChatModel chatModel;

    static class MathTool {

        @Tool(description = "soma dois numeros inteiros, a e b")
        public int sum(int a, int b) {
            return a + b;
        }

        @Tool(description = "subtrai dois numeros inteiros, a e b")
        public int diff(int a, int b) {
            return a - b;
        }
    }

    @Test
    void should_executeSum_when_prompted() {

        var chatClient = ChatClient.builder(chatModel)
                .defaultTools(new MathTool())
                .defaultSystem("Você é um matemático").build();

        var response = chatClient
                .prompt("Some 10 mais 20. Depois subtraia 30 do resultado anterior. Exiba apenas o resultado final.")
                .call().content();

        assertThat(response).contains("0");
        System.out.println("Response: " + response);
    }
}
