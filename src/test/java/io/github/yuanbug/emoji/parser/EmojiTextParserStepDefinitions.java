package io.github.yuanbug.emoji.parser;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author yuanbug
 * @since 2023/10/18
 */
public class EmojiTextParserStepDefinitions {

    private static final EmojiTextParser PARSER = buildParser();

    public static void main(String[] args) {
        PARSER.parse("\uD83D\uDC68\uD83C\uDFFF\u200D\uD83E\uDDB1 asd \uD83D\uDC68\uD83C\uDFFB\u200D\uD83C\uDF73").forEach(System.out::println);
//        System.out.println(PARSER.getLength("\uD83D\uD83D\uDE02"));
    }

    private String content;
    private long length;

    @SneakyThrows
    private static EmojiTextParser buildParser() {
        return EmojiTextParser.builder().build();
    }

    @Given("input content {string}")
    public void giveContent(String content) {
        this.content = content;
    }

    @Given("input null")
    public void giveNull() {
        this.content = null;
    }

    @When("get content length")
    public void getContentLength() {
        this.length = PARSER.getLength(this.content);
    }

    @Then("content length should be {long}")
    public void contentLengthShouldBe(long expectedLength) {
        assertEquals(expectedLength, this.length);
    }
}
