package io.github.yuanbug.emoji.domain.sequence;

/**
 * @author yuanbug
 * @since 2023-10-12
 */
public interface ParsedSequence {
    ParseResultSequenceType getType();

    String getContent();
}
