package io.github.yuanbug.emoji.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yuanbug.emoji.domain.constants.EmojiConstants;
import io.github.yuanbug.emoji.domain.parse.EmojiParseConfig;
import io.github.yuanbug.emoji.domain.parse.EmojiTailTreeNode;
import io.github.yuanbug.emoji.domain.sequence.*;
import lombok.Builder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.yuanbug.emoji.domain.constants.EmojiConstants.SELECTOR_TEXT_PRESENTATION;
import static io.github.yuanbug.emoji.domain.constants.EmojiConstants.isSelector;

/**
 * @author yuanbug
 * @since 2023/10/17
 */
public class EmojiTextParser {

    private static final String FILE_NAME_EMOJI_LIST = "emoji_full_list_15.1.json";
    private static final TypeReference<EmojiSequence> EMOJI_SEQUENCE_TYPE_REFERENCE = new TypeReference<>() {};
    private static final AtomicBoolean TREE_INIT = new AtomicBoolean(false);


    /**
     * \x{FE0F}
     */
    public static final char SELECTOR_EMOJI_PRESENTATION = 65039;

    private static final EmojiTailTreeNode root = new EmojiTailTreeNode();

    private static class ParseContext {
        private final String content;
        private int cursor;

        private ParseContext(String content) {
            this.content = content;
            this.cursor = 0;
        }
    }

    private final EmojiParseConfig config;

    @Builder
    private EmojiTextParser(EmojiParseConfig config) throws IOException {
        initTailTree();
        this.config = config;
    }

    private static void initTailTree() throws IOException {
        if (TREE_INIT.get()) {
            return;
        }
        synchronized (TREE_INIT) {
            if (TREE_INIT.get()) {
                return;
            }
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
            try (InputStream emojiListStream = getEmojiListStream()) {
                JsonNode emojiList = objectMapper.readTree(emojiListStream);
                for (JsonNode emojiNode : emojiList) {
                    root.addSequence(objectMapper.convertValue(emojiNode, EMOJI_SEQUENCE_TYPE_REFERENCE));
                }
            }
            TREE_INIT.set(true);
        }
    }

    private static InputStream getEmojiListStream() {
        return Optional.ofNullable(EmojiTextParser.class.getClassLoader())
                .map(loader -> loader.getResourceAsStream(FILE_NAME_EMOJI_LIST))
                .orElseThrow(() -> new IllegalArgumentException("can not load emoji list resource"));
    }

    /**
     * TODO replace with state machine
     */
    private ParsedSequence next(ParseContext context) {
        EmojiTailTreeNode cursor = root;
        EmojiSequence preData = null;
        StringBuilder builder = new StringBuilder();
        boolean selecting = false;
        while (context.cursor < context.content.length()) {
            char ch = context.content.charAt(context.cursor);
            EmojiTailTreeNode next = cursor.goDown(ch);
            if (null == next && selecting) {
                return buildPresentation(builder, preData);
            }
            preData = cursor.getData();
            selecting = !selecting && null != preData && isSelector(ch);
            if (null == next) {
                EmojiSequence data = cursor.getData();
                if (null != data) {
                    return data;
                }
                if (cursor != root) {
                    return new NonEmojiSequence(builder.toString());
                }
                builder.append(ch);
                context.cursor++;
                continue;
            }
            if (cursor == root && !builder.isEmpty()) {
                return new NonEmojiSequence(builder.toString());
            }
            builder.append(ch);
            context.cursor++;
            cursor = next;
        }
        if (cursor.getData() != null) {
            return cursor.getData();
        }
        if (builder.isEmpty()) {
            return null;
        }
        if (selecting) {
            return buildPresentation(builder, preData);
        }
        return new NonEmojiSequence(builder.toString());
    }

    public ParsedSequence buildPresentation(StringBuilder builder, EmojiSequence preData) {
        if (builder.isEmpty()) {
            return null;
        }
        char selector = builder.charAt(builder.length() - 1);
        if (SELECTOR_EMOJI_PRESENTATION == selector) {
            return new EmojiPresentation(preData);
        }
        return new TextPresentation(preData);
    }

    public Stream<ParsedSequence> parse(String content) {
        if (null == content || content.isEmpty()) {
            return Stream.empty();
        }
        ParseContext context = new ParseContext(content);
        return Stream.generate(() -> next(context)).takeWhile(Objects::nonNull);
    }

    public long getLength(String content) {
        return parse(content)
                .mapToLong(sequence -> sequence.getType().emoji ? 1 : sequence.getContent().length())
                .sum();
    }

    public String removeEmoji(String content) {
        return parse(content)
                .filter(sequence -> !sequence.getType().emoji)
                .map(ParsedSequence::getContent)
                .collect(Collectors.joining());
    }

}
