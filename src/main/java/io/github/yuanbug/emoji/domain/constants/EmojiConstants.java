package io.github.yuanbug.emoji.domain.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @author yuanbug
 * @since 2023-10-11
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmojiConstants {

    /**
     * \x{FE0E}
     */
    public static final char SELECTOR_TEXT_PRESENTATION = 65038;

    /**
     * \x{FE0F}
     */
    public static final char SELECTOR_EMOJI_PRESENTATION = 65039;

    public static boolean isSelector(char ch) {
        return SELECTOR_TEXT_PRESENTATION == ch || SELECTOR_EMOJI_PRESENTATION == ch;
    }

    /**
     * \x{200D}
     */
    public static final char ZERO_WIDTH_JOINER = 8205;

    /**
     * range that emoji char must within, necessary but not sufficient,
     * not contain text prefix chars of keycap sequence (like #、0~9、©、®)
     */
    private static final char[] NECESSARY_EMOJI_CHAR_RANGE = new char[]{ZERO_WIDTH_JOINER, SELECTOR_TEXT_PRESENTATION};
    public static final char MIN_EMOJI_CHAR = NECESSARY_EMOJI_CHAR_RANGE[0];
    public static final char MAX_EMOJI_CHAR = NECESSARY_EMOJI_CHAR_RANGE[1];

}
