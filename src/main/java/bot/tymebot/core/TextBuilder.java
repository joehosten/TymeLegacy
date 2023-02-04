package bot.tymebot.core;

public class TextBuilder {

    private final String text;

    public TextBuilder(String text) {
        this.text = text;
    }

    public TextBuilder replace(String key, String value) {
        return new TextBuilder(text.replaceAll(key, value));
    }

    public String build() {
        return text;
    }
}
