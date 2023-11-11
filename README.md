# emoji-parser

本项目仅用于个人自娱自乐。 您要是乐意拿去用，咱虽然不拦着，但不作任何承诺。

## 使用说明

### 构造解析器

```java
EmojiTextParser parser=EmojiTextParser.buildDefaultParser();
```

### 字符串解析

`EmojiTextParser#parse(String)` 方法可以将字符串解析为一个 `ParsedSequence` 流。

```java
parser.parse("有1️⃣说1️⃣，虽然Java是我的🍚工具，但我并不❤️它🤣👉🤡").forEach(System.out::println);
```

输出：

```
NonEmojiSequence[content=有]
EmojiSequence[chars=1️⃣, code=U+0031 U+FE0F U+20E3, name=keycap: 1, group=Symbols, subGroup=keycap]
NonEmojiSequence[content=说]
EmojiSequence[chars=1️⃣, code=U+0031 U+FE0F U+20E3, name=keycap: 1, group=Symbols, subGroup=keycap]
NonEmojiSequence[content=，虽然Java是我的]
EmojiSequence[chars=🍚, code=U+1F35A, name=cooked rice, group=Food & Drink, subGroup=food-asian]
NonEmojiSequence[content=工具，但我并不]
EmojiPresentation[prefix=EmojiSequence[chars=❤, code=U+2764, name=red heart, group=Smileys & Emotion, subGroup=heart]]
NonEmojiSequence[content=它]
EmojiSequence[chars=🤣, code=U+1F923, name=rolling on the floor laughing, group=Smileys & Emotion, subGroup=face-smiling]
EmojiSequence[chars=👉, code=U+1F449, name=backhand index pointing right, group=People & Body, subGroup=hand-single-finger]
EmojiSequence[chars=🤡, code=U+1F921, name=clown face, group=Smileys & Emotion, subGroup=face-costume]
```

这个方法会将原字符串中的每个 emoji 单独解析为一个 `ParsedSequence` 对象，而连续的非 emoji 字符则会被视作一个整体。

通过操作这个流，可以实现各类需求，`EmojiTextParser` 类的其它公有方法实际上都是以此为基础实现的。这里给出几个示例。

直接去除 emoji：

```java
String emojiRemoved=parser.parse("有1️⃣说1️⃣，虽然Java是我的🍚工具，但我并不❤️它🤣👉🤡")
        .filter(sequence->!sequence.getType().emoji)
        .map(ParsedSequence::getContent)
        .collect(Collectors.joining());
        // 有说，虽然Java是我的工具，但我并不它
        System.out.println(emojiRemoved);
```

自定义编码：

```java
String encoded=parser.parse("有1️⃣说1️⃣，虽然Java是我的🍚工具，但我并不❤️它🤣👉🤡")
        .map(sequence->{
        if(!sequence.getType().emoji){
        return sequence.getContent();
        }
        return"[emoji:%s]".formatted(URLEncoder.encode(sequence.getContent(),StandardCharsets.UTF_8));
        })
        .collect(Collectors.joining());
        // 有[emoji:1%EF%B8%8F%E2%83%A3]说[emoji:1%EF%B8%8F%E2%83%A3]，虽然Java是我的[emoji:%F0%9F%8D%9A]工具，但我并不[emoji:%E2%9D%A4%EF%B8%8F]它[emoji:%F0%9F%A4%A3][emoji:%F0%9F%91%89][emoji:%F0%9F%A4%A1]
        System.out.println(encoded);
```

### 长度计算

`EmojiTextParser#getLength(String)` 方法可以用于获取含 emoji 字符串的准确长度，每一个 emoji 会被算作一个字符。

```java
// 8
System.out.println("乐了🤣👉🤡".length());
        // 5
        System.out.println(parser.getLength("乐了🤣👉🤡"));
```

### 字符替换

`EmojiTextParser#replaceEmoji(String, Function<ParsedSequence, String>)` 方法可以简便地将 emoji 转换成其它字符串。

```java
// 你说你*呢，吃*去吧😀
System.out.println(parser.replaceEmoji("你说你🐎呢，吃💩去吧😅",emoji->{
        String content=emoji.getContent();
        if("😅".equals(content)){
        return"😀";
        }
        return"*";
        }));
```

## 待办事项

- 完善 emoji 的属性解析，将分组等属性替换为枚举
- 完善单元测试
- 使用 tsv 文件替代 json 文件
- 去掉不必要的依赖
- 降低 jdk 版本到 java8
- 完善 modifier、presentation 等特殊情况的处理
- 兼容 [emoji-java](https://github.com/vdurmont/emoji-java) 的大部分 api
- 支持解析行为配置

## 外部资源

- [Emoji_15 资源页](https://www.unicode.org/emoji/charts/index.html)
- [Emoji_15 编码标准](https://www.unicode.org/reports/tr51/)
- [emoji-data](https://github.com/iamcal/emoji-data)
