package io.github.yuanbug.emoji.domain.sequence;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yuanbug
 * @since 2023-10-12
 */
@Getter
@AllArgsConstructor
public enum ParseResultSequenceType {

    NON_EMOJI_TEXT(false),

    SIMPLE_STICKER(true),
    KEYCAP(true),
    TEXT_PRESENTATION(true),
    EMOJI_PRESENTATION(true),
    COMBINE_STICKER(true),
    INVALID(true),
    ;

    public final boolean emoji;

}
