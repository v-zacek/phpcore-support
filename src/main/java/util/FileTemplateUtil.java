package util;

import org.jetbrains.annotations.NotNull;

public class FileTemplateUtil {

    public static String removeTemplateMacros(@NotNull String content) {
        String[] macros = {"${NAME}"};

        for (String macro : macros) {
            content = content.replace(macro, "");
        }

        return content;
    }

}
