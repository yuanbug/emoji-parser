package io.github.yuanbug.emoji;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.yuanbug.emoji.domain.sequence.EmojiSequence;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author yuanbug
 * @since 2023-10-12
 */
public class TempScript {

    private static String removeNewAddSymbol(String name) {
        if (name.startsWith("⊛ ")) {
            return name.substring(2);
        }
        return name;
    }

    @SneakyThrows
    public static void main(String[] args) {
        URL emojiData = Optional.ofNullable(TempScript.class.getClassLoader())
                .map(loader -> loader.getResource("full-emoji-list.html"))
                .orElseThrow(() -> new IllegalArgumentException("fuck"));
        Document document = Jsoup.parse(Path.of(emojiData.toURI()).toFile());
        Elements tableRows = document.getElementsByTag("tr");
        String group = "";
        String subGroup = "";
        List<EmojiSequence> result = new ArrayList<>(1024);
        for (Element tableRow : tableRows) {
            if (!tableRow.getElementsByTag("th").isEmpty()) {
                Elements bigHead = tableRow.select("th.bighead");
                if (!bigHead.isEmpty()) {
                    group = bigHead.get(0).text();
                    subGroup = "";
                }
                Elements mediumHead = tableRow.select("th.mediumHead");
                if (!mediumHead.isEmpty()) {
                    subGroup = mediumHead.get(0).text();
                }
                continue;
            }

            Elements tds = tableRow.getElementsByTag("td");
            String code = tds.get(1).text();
            String chars = tds.get(2).text();
            String name = removeNewAddSymbol(tds.get(tds.size() - 1).text());
            result.add(EmojiSequence.builder().code(code).chars(chars).name(name).group(group).subGroup(subGroup).build());
        }
        try (PrintWriter writer = new PrintWriter("emoji_full_list_15.1.json")) {
            writer.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(result));
        }
    }

}
